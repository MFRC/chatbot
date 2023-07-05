import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomerServiceDetailComponent } from './customer-service-detail.component';

describe('CustomerService Management Detail Component', () => {
  let comp: CustomerServiceDetailComponent;
  let fixture: ComponentFixture<CustomerServiceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerServiceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ customerService: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(CustomerServiceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CustomerServiceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load customerService on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.customerService).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
