import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReportDetailComponent } from './report-detail.component';

describe('Report Management Detail Component', () => {
  let comp: ReportDetailComponent;
  let fixture: ComponentFixture<ReportDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ report: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(ReportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load report on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.report).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
