import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LoyaltyProgramFormService } from './loyalty-program-form.service';
import { LoyaltyProgramService } from '../service/loyalty-program.service';
import { ILoyaltyProgram } from '../loyalty-program.model';

import { LoyaltyProgramUpdateComponent } from './loyalty-program-update.component';

describe('LoyaltyProgram Management Update Component', () => {
  let comp: LoyaltyProgramUpdateComponent;
  let fixture: ComponentFixture<LoyaltyProgramUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let loyaltyProgramFormService: LoyaltyProgramFormService;
  let loyaltyProgramService: LoyaltyProgramService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LoyaltyProgramUpdateComponent],
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
      .overrideTemplate(LoyaltyProgramUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoyaltyProgramUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    loyaltyProgramFormService = TestBed.inject(LoyaltyProgramFormService);
    loyaltyProgramService = TestBed.inject(LoyaltyProgramService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const loyaltyProgram: ILoyaltyProgram = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ loyaltyProgram });
      comp.ngOnInit();

      expect(comp.loyaltyProgram).toEqual(loyaltyProgram);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILoyaltyProgram>>();
      const loyaltyProgram = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(loyaltyProgramFormService, 'getLoyaltyProgram').mockReturnValue(loyaltyProgram);
      jest.spyOn(loyaltyProgramService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loyaltyProgram });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loyaltyProgram }));
      saveSubject.complete();

      // THEN
      expect(loyaltyProgramFormService.getLoyaltyProgram).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(loyaltyProgramService.update).toHaveBeenCalledWith(expect.objectContaining(loyaltyProgram));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILoyaltyProgram>>();
      const loyaltyProgram = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(loyaltyProgramFormService, 'getLoyaltyProgram').mockReturnValue({ id: null });
      jest.spyOn(loyaltyProgramService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loyaltyProgram: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: loyaltyProgram }));
      saveSubject.complete();

      // THEN
      expect(loyaltyProgramFormService.getLoyaltyProgram).toHaveBeenCalled();
      expect(loyaltyProgramService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILoyaltyProgram>>();
      const loyaltyProgram = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(loyaltyProgramService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ loyaltyProgram });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(loyaltyProgramService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
