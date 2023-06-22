import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FAQsDetailComponent } from './fa-qs-detail.component';

describe('FAQs Management Detail Component', () => {
  let comp: FAQsDetailComponent;
  let fixture: ComponentFixture<FAQsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FAQsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fAQs: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(FAQsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FAQsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fAQs on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fAQs).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
