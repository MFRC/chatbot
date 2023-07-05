import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReportFormService } from './report-form.service';
import { ReportService } from '../service/report.service';
import { IReport } from '../report.model';

import { ReportUpdateComponent } from './report-update.component';

describe('Report Management Update Component', () => {
  let comp: ReportUpdateComponent;
  let fixture: ComponentFixture<ReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportFormService: ReportFormService;
  let reportService: ReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReportUpdateComponent],
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
      .overrideTemplate(ReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportFormService = TestBed.inject(ReportFormService);
    reportService = TestBed.inject(ReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const report: IReport = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(comp.report).toEqual(report);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(reportFormService, 'getReport').mockReturnValue(report);
      jest.spyOn(reportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: report }));
      saveSubject.complete();

      // THEN
      expect(reportFormService.getReport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportService.update).toHaveBeenCalledWith(expect.objectContaining(report));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(reportFormService, 'getReport').mockReturnValue({ id: null });
      jest.spyOn(reportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: report }));
      saveSubject.complete();

      // THEN
      expect(reportFormService.getReport).toHaveBeenCalled();
      expect(reportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(reportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
