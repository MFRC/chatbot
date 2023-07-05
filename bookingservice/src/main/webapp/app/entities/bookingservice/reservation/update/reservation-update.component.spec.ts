import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReservationFormService } from './reservation-form.service';
import { ReservationService } from '../service/reservation.service';
import { IReservation } from '../reservation.model';
import { IHotelInfo } from 'app/entities/bookingservice/hotel-info/hotel-info.model';
import { HotelInfoService } from 'app/entities/bookingservice/hotel-info/service/hotel-info.service';

import { ReservationUpdateComponent } from './reservation-update.component';

describe('Reservation Management Update Component', () => {
  let comp: ReservationUpdateComponent;
  let fixture: ComponentFixture<ReservationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reservationFormService: ReservationFormService;
  let reservationService: ReservationService;
  let hotelInfoService: HotelInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReservationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ReservationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReservationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reservationFormService = TestBed.inject(ReservationFormService);
    reservationService = TestBed.inject(ReservationService);
    hotelInfoService = TestBed.inject(HotelInfoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call HotelInfo query and add missing value', () => {
      const reservation: IReservation = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const hotel: IHotelInfo = { id: '0902c159-0405-4e0b-b241-882e404be1de' };
      reservation.hotel = hotel;

      const hotelInfoCollection: IHotelInfo[] = [{ id: 'd4b3e95e-bd32-4aa3-b692-7334b7f40767' }];
      jest.spyOn(hotelInfoService, 'query').mockReturnValue(of(new HttpResponse({ body: hotelInfoCollection })));
      const additionalHotelInfos = [hotel];
      const expectedCollection: IHotelInfo[] = [...additionalHotelInfos, ...hotelInfoCollection];
      jest.spyOn(hotelInfoService, 'addHotelInfoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reservation });
      comp.ngOnInit();

      expect(hotelInfoService.query).toHaveBeenCalled();
      expect(hotelInfoService.addHotelInfoToCollectionIfMissing).toHaveBeenCalledWith(
        hotelInfoCollection,
        ...additionalHotelInfos.map(expect.objectContaining)
      );
      expect(comp.hotelInfosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reservation: IReservation = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const hotel: IHotelInfo = { id: '6ed09ebc-5713-463d-9384-9b515e208225' };
      reservation.hotel = hotel;

      activatedRoute.data = of({ reservation });
      comp.ngOnInit();

      expect(comp.hotelInfosSharedCollection).toContain(hotel);
      expect(comp.reservation).toEqual(reservation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReservation>>();
      const reservation = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(reservationFormService, 'getReservation').mockReturnValue(reservation);
      jest.spyOn(reservationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reservation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reservation }));
      saveSubject.complete();

      // THEN
      expect(reservationFormService.getReservation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reservationService.update).toHaveBeenCalledWith(expect.objectContaining(reservation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReservation>>();
      const reservation = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(reservationFormService, 'getReservation').mockReturnValue({ id: null });
      jest.spyOn(reservationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reservation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reservation }));
      saveSubject.complete();

      // THEN
      expect(reservationFormService.getReservation).toHaveBeenCalled();
      expect(reservationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReservation>>();
      const reservation = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(reservationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reservation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reservationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareHotelInfo', () => {
      it('Should forward to hotelInfoService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(hotelInfoService, 'compareHotelInfo');
        comp.compareHotelInfo(entity, entity2);
        expect(hotelInfoService.compareHotelInfo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
