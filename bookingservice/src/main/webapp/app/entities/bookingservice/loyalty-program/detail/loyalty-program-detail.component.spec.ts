import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LoyaltyProgramDetailComponent } from './loyalty-program-detail.component';

describe('LoyaltyProgram Management Detail Component', () => {
  let comp: LoyaltyProgramDetailComponent;
  let fixture: ComponentFixture<LoyaltyProgramDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoyaltyProgramDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ loyaltyProgram: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(LoyaltyProgramDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LoyaltyProgramDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load loyaltyProgram on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.loyaltyProgram).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
