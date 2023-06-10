import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomerServiceEntityDetailComponent } from './customer-service-entity-detail.component';

describe('CustomerServiceEntity Management Detail Component', () => {
  let comp: CustomerServiceEntityDetailComponent;
  let fixture: ComponentFixture<CustomerServiceEntityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerServiceEntityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ customerServiceEntity: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(CustomerServiceEntityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CustomerServiceEntityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load customerServiceEntity on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.customerServiceEntity).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
