import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ChatSessionFormService } from './chat-session-form.service';
import { ChatSessionService } from '../service/chat-session.service';
import { IChatSession } from '../chat-session.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ChatSessionUpdateComponent } from './chat-session-update.component';

describe('ChatSession Management Update Component', () => {
  let comp: ChatSessionUpdateComponent;
  let fixture: ComponentFixture<ChatSessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chatSessionFormService: ChatSessionFormService;
  let chatSessionService: ChatSessionService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ChatSessionUpdateComponent],
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
      .overrideTemplate(ChatSessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChatSessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chatSessionFormService = TestBed.inject(ChatSessionFormService);
    chatSessionService = TestBed.inject(ChatSessionService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const chatSession: IChatSession = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const user: IUser = { id: '946a5124-4200-412c-a294-d9609279dd45' };
      chatSession.user = user;

      const userCollection: IUser[] = [{ id: '16bd2bca-d1ed-4f25-8b06-fb5291d4c25a' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chatSession });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const chatSession: IChatSession = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const user: IUser = { id: '2cf9ccee-e684-4a53-b48d-915b620f3672' };
      chatSession.user = user;

      activatedRoute.data = of({ chatSession });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.chatSession).toEqual(chatSession);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatSession>>();
      const chatSession = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(chatSessionFormService, 'getChatSession').mockReturnValue(chatSession);
      jest.spyOn(chatSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chatSession }));
      saveSubject.complete();

      // THEN
      expect(chatSessionFormService.getChatSession).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(chatSessionService.update).toHaveBeenCalledWith(expect.objectContaining(chatSession));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatSession>>();
      const chatSession = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(chatSessionFormService, 'getChatSession').mockReturnValue({ id: null });
      jest.spyOn(chatSessionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatSession: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chatSession }));
      saveSubject.complete();

      // THEN
      expect(chatSessionFormService.getChatSession).toHaveBeenCalled();
      expect(chatSessionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChatSession>>();
      const chatSession = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(chatSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chatSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(chatSessionService.update).toHaveBeenCalled();
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
  });
});
