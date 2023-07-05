import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AddressDetailComponent } from './address-detail.component';

describe('Address Management Detail Component', () => {
  let comp: AddressDetailComponent;
  let fixture: ComponentFixture<AddressDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddressDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ address: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(AddressDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AddressDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load address on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.address).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
