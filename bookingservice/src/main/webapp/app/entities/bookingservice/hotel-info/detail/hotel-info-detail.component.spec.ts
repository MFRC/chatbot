import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HotelInfoDetailComponent } from './hotel-info-detail.component';

describe('HotelInfo Management Detail Component', () => {
  let comp: HotelInfoDetailComponent;
  let fixture: ComponentFixture<HotelInfoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HotelInfoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ hotelInfo: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(HotelInfoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HotelInfoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load hotelInfo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.hotelInfo).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
