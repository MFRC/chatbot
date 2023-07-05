import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChatbotServiceUserDetailComponent } from './chatbot-service-user-detail.component';

describe('ChatbotServiceUser Management Detail Component', () => {
  let comp: ChatbotServiceUserDetailComponent;
  let fixture: ComponentFixture<ChatbotServiceUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChatbotServiceUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ chatbotServiceUser: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(ChatbotServiceUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ChatbotServiceUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load chatbotServiceUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.chatbotServiceUser).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
