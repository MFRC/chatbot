import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FAQsFormService } from './fa-qs-form.service';
import { FAQsService } from '../service/fa-qs.service';
import { IFAQs } from '../fa-qs.model';

import { FAQsUpdateComponent } from './fa-qs-update.component';

describe('FAQs Management Update Component', () => {
  let comp: FAQsUpdateComponent;
  let fixture: ComponentFixture<FAQsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fAQsFormService: FAQsFormService;
  let fAQsService: FAQsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FAQsUpdateComponent],
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
      .overrideTemplate(FAQsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FAQsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fAQsFormService = TestBed.inject(FAQsFormService);
    fAQsService = TestBed.inject(FAQsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fAQs: IFAQs = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ fAQs });
      comp.ngOnInit();

      expect(comp.fAQs).toEqual(fAQs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFAQs>>();
      const fAQs = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(fAQsFormService, 'getFAQs').mockReturnValue(fAQs);
      jest.spyOn(fAQsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fAQs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fAQs }));
      saveSubject.complete();

      // THEN
      expect(fAQsFormService.getFAQs).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fAQsService.update).toHaveBeenCalledWith(expect.objectContaining(fAQs));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFAQs>>();
      const fAQs = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(fAQsFormService, 'getFAQs').mockReturnValue({ id: null });
      jest.spyOn(fAQsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fAQs: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fAQs }));
      saveSubject.complete();

      // THEN
      expect(fAQsFormService.getFAQs).toHaveBeenCalled();
      expect(fAQsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFAQs>>();
      const fAQs = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(fAQsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fAQs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fAQsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
