import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MessageFormService } from './message-form.service';
import { MessageService } from '../service/message.service';
import { IMessage } from '../message.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IChatSession } from 'app/entities/chatbotservice/chat-session/chat-session.model';
import { ChatSessionService } from 'app/entities/chatbotservice/chat-session/service/chat-session.service';

import { MessageUpdateComponent } from './message-update.component';

describe('Message Management Update Component', () => {
  let comp: MessageUpdateComponent;
  let fixture: ComponentFixture<MessageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let messageFormService: MessageFormService;
  let messageService: MessageService;
  let userService: UserService;
  let chatSessionService: ChatSessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MessageUpdateComponent],
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
      .overrideTemplate(MessageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MessageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    messageFormService = TestBed.inject(MessageFormService);
    messageService = TestBed.inject(MessageService);
    userService = TestBed.inject(UserService);
    chatSessionService = TestBed.inject(ChatSessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const message: IMessage = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const user: IUser = { id: '4cc9e4d5-801e-4247-b7e9-c853cea5faec' };
      message.user = user;

      const userCollection: IUser[] = [{ id: '6d32a279-567f-4647-ada6-a4f99b13ad54' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ message });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ChatSession query and add missing value', () => {
      const message: IMessage = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const chatSession: IChatSession = { id: 'ab93ac41-2e67-43ae-8546-78a0d12eb50e' };
      message.chatSession = chatSession;

      const chatSessionCollection: IChatSession[] = [{ id: '2ced43d7-6fdf-4a1b-97c8-5eb546d8ac3e' }];
      jest.spyOn(chatSessionService, 'query').mockReturnValue(of(new HttpResponse({ body: chatSessionCollection })));
      const additionalChatSessions = [chatSession];
      const expectedCollection: IChatSession[] = [...additionalChatSessions, ...chatSessionCollection];
      jest.spyOn(chatSessionService, 'addChatSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ message });
      comp.ngOnInit();

      expect(chatSessionService.query).toHaveBeenCalled();
      expect(chatSessionService.addChatSessionToCollectionIfMissing).toHaveBeenCalledWith(
        chatSessionCollection,
        ...additionalChatSessions.map(expect.objectContaining)
      );
      expect(comp.chatSessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const message: IMessage = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const user: IUser = { id: '4fcb9eb0-18b3-4267-ba17-2e0df2f15da8' };
      message.user = user;
      const chatSession: IChatSession = { id: '292d2897-7544-4b84-afed-2785b0bbccd5' };
      message.chatSession = chatSession;

      activatedRoute.data = of({ message });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.chatSessionsSharedCollection).toContain(chatSession);
      expect(comp.message).toEqual(message);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessage>>();
      const message = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(messageFormService, 'getMessage').mockReturnValue(message);
      jest.spyOn(messageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ message });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: message }));
      saveSubject.complete();

      // THEN
      expect(messageFormService.getMessage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(messageService.update).toHaveBeenCalledWith(expect.objectContaining(message));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessage>>();
      const message = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(messageFormService, 'getMessage').mockReturnValue({ id: null });
      jest.spyOn(messageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ message: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: message }));
      saveSubject.complete();

      // THEN
      expect(messageFormService.getMessage).toHaveBeenCalled();
      expect(messageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessage>>();
      const message = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(messageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ message });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(messageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareChatSession', () => {
      it('Should forward to chatSessionService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(chatSessionService, 'compareChatSession');
        comp.compareChatSession(entity, entity2);
        expect(chatSessionService.compareChatSession).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
