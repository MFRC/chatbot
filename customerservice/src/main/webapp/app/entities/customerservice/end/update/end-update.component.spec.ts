import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EndFormService } from './end-form.service';
import { EndService } from '../service/end.service';
import { IEnd } from '../end.model';
import { IReport } from 'app/entities/customerservice/report/report.model';
import { ReportService } from 'app/entities/customerservice/report/service/report.service';

import { EndUpdateComponent } from './end-update.component';

describe('End Management Update Component', () => {
  let comp: EndUpdateComponent;
  let fixture: ComponentFixture<EndUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let endFormService: EndFormService;
  let endService: EndService;
  let reportService: ReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EndUpdateComponent],
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
      .overrideTemplate(EndUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EndUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    endFormService = TestBed.inject(EndFormService);
    endService = TestBed.inject(EndService);
    reportService = TestBed.inject(ReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call report query and add missing value', () => {
      const end: IEnd = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const report: IReport = { id: '6e030ce8-cea0-4f83-bdf4-54fbea9adb60' };
      end.report = report;

      const reportCollection: IReport[] = [{ id: 'fca8b03b-a19d-4d18-94ed-27081cbdaa81' }];
      jest.spyOn(reportService, 'query').mockReturnValue(of(new HttpResponse({ body: reportCollection })));
      const expectedCollection: IReport[] = [report, ...reportCollection];
      jest.spyOn(reportService, 'addReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ end });
      comp.ngOnInit();

      expect(reportService.query).toHaveBeenCalled();
      expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(reportCollection, report);
      expect(comp.reportsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const end: IEnd = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const report: IReport = { id: '9feead02-d230-4d3a-8b92-1851a783604e' };
      end.report = report;

      activatedRoute.data = of({ end });
      comp.ngOnInit();

      expect(comp.reportsCollection).toContain(report);
      expect(comp.end).toEqual(end);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnd>>();
      const end = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(endFormService, 'getEnd').mockReturnValue(end);
      jest.spyOn(endService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ end });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: end }));
      saveSubject.complete();

      // THEN
      expect(endFormService.getEnd).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(endService.update).toHaveBeenCalledWith(expect.objectContaining(end));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnd>>();
      const end = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(endFormService, 'getEnd').mockReturnValue({ id: null });
      jest.spyOn(endService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ end: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: end }));
      saveSubject.complete();

      // THEN
      expect(endFormService.getEnd).toHaveBeenCalled();
      expect(endService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnd>>();
      const end = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(endService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ end });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(endService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReport', () => {
      it('Should forward to reportService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(reportService, 'compareReport');
        comp.compareReport(entity, entity2);
        expect(reportService.compareReport).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
