import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaymentDetailComponent } from './payment-detail.component';

describe('Payment Management Detail Component', () => {
  let comp: PaymentDetailComponent;
  let fixture: ComponentFixture<PaymentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ payment: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(PaymentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaymentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load payment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.payment).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
