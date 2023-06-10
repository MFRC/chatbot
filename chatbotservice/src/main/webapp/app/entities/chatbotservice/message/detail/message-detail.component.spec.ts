import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MessageDetailComponent } from './message-detail.component';

describe('Message Management Detail Component', () => {
  let comp: MessageDetailComponent;
  let fixture: ComponentFixture<MessageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MessageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ message: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(MessageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MessageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load message on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.message).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
