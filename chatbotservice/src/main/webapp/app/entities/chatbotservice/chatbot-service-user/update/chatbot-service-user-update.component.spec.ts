import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ChatbotServiceUserFormService } from './chatbot-service-user-form.service';
import { ChatbotServiceUserService } from '../service/chatbot-service-user.service';
import { IChatbotServiceUser } from '../chatbot-service-user.model';

import { ChatbotServiceUserUpdateComponent } from './chatbot-service-user-update.component';

describe('ChatbotServiceUser Management Update Component', () => {
  let comp: ChatbotServiceUserUpdateComponent;
  let fixture: ComponentFixture<ChatbotServiceUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chatbotServiceUserFormService: ChatbotServiceUserFormService;
  let chatbotServiceUserService: ChatbotServiceUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ChatbotServiceUserUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ChatbotServiceUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChatbotServiceUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chatbotServiceUserFormService = TestBed.inject(ChatbotServiceUserFormService);
    chatbotServiceUserService = TestBed.inject(ChatbotServiceUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const chatbotServiceUser: IChatbotServiceUser = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ chatbotServiceUser });
      comp.ngOnInit();

      expect(comp.chatbotServiceUser).toEqual(chatbotServiceUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatbotServiceUser>>();
      const chatbotServiceUser = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(chatbotServiceUserFormService, 'getChatbotServiceUser').mockReturnValue(chatbotServiceUser);
      jest.spyOn(chatbotServiceUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatbotServiceUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chatbotServiceUser }));
      saveSubject.complete();

      // THEN
      expect(chatbotServiceUserFormService.getChatbotServiceUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(chatbotServiceUserService.update).toHaveBeenCalledWith(expect.objectContaining(chatbotServiceUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatbotServiceUser>>();
      const chatbotServiceUser = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(chatbotServiceUserFormService, 'getChatbotServiceUser').mockReturnValue({ id: null });
      jest.spyOn(chatbotServiceUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatbotServiceUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chatbotServiceUser }));
      saveSubject.complete();

      // THEN
      expect(chatbotServiceUserFormService.getChatbotServiceUser).toHaveBeenCalled();
      expect(chatbotServiceUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatbotServiceUser>>();
      const chatbotServiceUser = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(chatbotServiceUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatbotServiceUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(chatbotServiceUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
