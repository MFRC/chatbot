import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomerServiceUserDetailComponent } from './customer-service-user-detail.component';

describe('CustomerServiceUser Management Detail Component', () => {
  let comp: CustomerServiceUserDetailComponent;
  let fixture: ComponentFixture<CustomerServiceUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerServiceUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ customerServiceUser: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(CustomerServiceUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CustomerServiceUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load customerServiceUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.customerServiceUser).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
