import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EndDetailComponent } from './end-detail.component';

describe('End Management Detail Component', () => {
  let comp: EndDetailComponent;
  let fixture: ComponentFixture<EndDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EndDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ end: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(EndDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EndDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load end on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.end).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
