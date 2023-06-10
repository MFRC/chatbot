import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomerDetailComponent } from './customer-detail.component';

describe('Customer Management Detail Component', () => {
  let comp: CustomerDetailComponent;
  let fixture: ComponentFixture<CustomerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ customer: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(CustomerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CustomerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load customer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.customer).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
