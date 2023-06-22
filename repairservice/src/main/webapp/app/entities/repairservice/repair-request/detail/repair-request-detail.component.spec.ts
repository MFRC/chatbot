import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RepairRequestDetailComponent } from './repair-request-detail.component';

describe('RepairRequest Management Detail Component', () => {
  let comp: RepairRequestDetailComponent;
  let fixture: ComponentFixture<RepairRequestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RepairRequestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ repairRequest: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(RepairRequestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RepairRequestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load repairRequest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.repairRequest).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
