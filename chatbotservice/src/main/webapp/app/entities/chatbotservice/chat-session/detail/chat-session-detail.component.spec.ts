import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChatSessionDetailComponent } from './chat-session-detail.component';

describe('ChatSession Management Detail Component', () => {
  let comp: ChatSessionDetailComponent;
  let fixture: ComponentFixture<ChatSessionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChatSessionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ chatSession: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(ChatSessionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ChatSessionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load chatSession on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.chatSession).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
