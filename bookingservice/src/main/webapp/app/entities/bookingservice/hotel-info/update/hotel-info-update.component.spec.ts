import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HotelInfoFormService } from './hotel-info-form.service';
import { HotelInfoService } from '../service/hotel-info.service';
import { IHotelInfo } from '../hotel-info.model';
import { ILoyaltyProgram } from 'app/entities/bookingservice/loyalty-program/loyalty-program.model';
import { LoyaltyProgramService } from 'app/entities/bookingservice/loyalty-program/service/loyalty-program.service';
import { IAddress } from 'app/entities/bookingservice/address/address.model';
import { AddressService } from 'app/entities/bookingservice/address/service/address.service';

import { HotelInfoUpdateComponent } from './hotel-info-update.component';

describe('HotelInfo Management Update Component', () => {
  let comp: HotelInfoUpdateComponent;
  let fixture: ComponentFixture<HotelInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hotelInfoFormService: HotelInfoFormService;
  let hotelInfoService: HotelInfoService;
  let loyaltyProgramService: LoyaltyProgramService;
  let addressService: AddressService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HotelInfoUpdateComponent],
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
      .overrideTemplate(HotelInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HotelInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hotelInfoFormService = TestBed.inject(HotelInfoFormService);
    hotelInfoService = TestBed.inject(HotelInfoService);
    loyaltyProgramService = TestBed.inject(LoyaltyProgramService);
    addressService = TestBed.inject(AddressService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call loyaltyProgram query and add missing value', () => {
      const hotelInfo: IHotelInfo = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const loyaltyProgram: ILoyaltyProgram = { id: 'bc800958-385d-4aec-b6a7-df2d6d332ae0' };
      hotelInfo.loyaltyProgram = loyaltyProgram;

      const loyaltyProgramCollection: ILoyaltyProgram[] = [{ id: '6405907f-4e21-45d9-835a-5744aaea7e35' }];
      jest.spyOn(loyaltyProgramService, 'query').mockReturnValue(of(new HttpResponse({ body: loyaltyProgramCollection })));
      const expectedCollection: ILoyaltyProgram[] = [loyaltyProgram, ...loyaltyProgramCollection];
      jest.spyOn(loyaltyProgramService, 'addLoyaltyProgramToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hotelInfo });
      comp.ngOnInit();

      expect(loyaltyProgramService.query).toHaveBeenCalled();
      expect(loyaltyProgramService.addLoyaltyProgramToCollectionIfMissing).toHaveBeenCalledWith(loyaltyProgramCollection, loyaltyProgram);
      expect(comp.loyaltyProgramsCollection).toEqual(expectedCollection);
    });

    it('Should call Address query and add missing value', () => {
      const hotelInfo: IHotelInfo = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const address: IAddress = { id: '89f97ff1-33ce-4b36-90f6-259409f0f7ad' };
      hotelInfo.address = address;

      const addressCollection: IAddress[] = [{ id: 'fba79569-1acc-4314-8b35-90bbfa6b1ddf' }];
      jest.spyOn(addressService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const additionalAddresses = [address];
      const expectedCollection: IAddress[] = [...additionalAddresses, ...addressCollection];
      jest.spyOn(addressService, 'addAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hotelInfo });
      comp.ngOnInit();

      expect(addressService.query).toHaveBeenCalled();
      expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(
        addressCollection,
        ...additionalAddresses.map(expect.objectContaining)
      );
      expect(comp.addressesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hotelInfo: IHotelInfo = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const loyaltyProgram: ILoyaltyProgram = { id: '7d9ca86c-3bf0-44b0-8819-4e7e49020ee6' };
      hotelInfo.loyaltyProgram = loyaltyProgram;
      const address: IAddress = { id: '069db3be-6ffc-47e0-a53e-606553e824f3' };
      hotelInfo.address = address;

      activatedRoute.data = of({ hotelInfo });
      comp.ngOnInit();

      expect(comp.loyaltyProgramsCollection).toContain(loyaltyProgram);
      expect(comp.addressesSharedCollection).toContain(address);
      expect(comp.hotelInfo).toEqual(hotelInfo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHotelInfo>>();
      const hotelInfo = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(hotelInfoFormService, 'getHotelInfo').mockReturnValue(hotelInfo);
      jest.spyOn(hotelInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hotelInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hotelInfo }));
      saveSubject.complete();

      // THEN
      expect(hotelInfoFormService.getHotelInfo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(hotelInfoService.update).toHaveBeenCalledWith(expect.objectContaining(hotelInfo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHotelInfo>>();
      const hotelInfo = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(hotelInfoFormService, 'getHotelInfo').mockReturnValue({ id: null });
      jest.spyOn(hotelInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hotelInfo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hotelInfo }));
      saveSubject.complete();

      // THEN
      expect(hotelInfoFormService.getHotelInfo).toHaveBeenCalled();
      expect(hotelInfoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHotelInfo>>();
      const hotelInfo = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(hotelInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hotelInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hotelInfoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLoyaltyProgram', () => {
      it('Should forward to loyaltyProgramService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(loyaltyProgramService, 'compareLoyaltyProgram');
        comp.compareLoyaltyProgram(entity, entity2);
        expect(loyaltyProgramService.compareLoyaltyProgram).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAddress', () => {
      it('Should forward to addressService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(addressService, 'compareAddress');
        comp.compareAddress(entity, entity2);
        expect(addressService.compareAddress).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
