import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConversationFormService } from './conversation-form.service';
import { ConversationService } from '../service/conversation.service';
import { IConversation } from '../conversation.model';
import { IEnd } from 'app/entities/customerservice/end/end.model';
import { EndService } from 'app/entities/customerservice/end/service/end.service';

import { ConversationUpdateComponent } from './conversation-update.component';

describe('Conversation Management Update Component', () => {
  let comp: ConversationUpdateComponent;
  let fixture: ComponentFixture<ConversationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conversationFormService: ConversationFormService;
  let conversationService: ConversationService;
  let endService: EndService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConversationUpdateComponent],
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
      .overrideTemplate(ConversationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConversationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conversationFormService = TestBed.inject(ConversationFormService);
    conversationService = TestBed.inject(ConversationService);
    endService = TestBed.inject(EndService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call end query and add missing value', () => {
      const conversation: IConversation = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const end: IEnd = { id: '311f0ad8-41f2-4059-b36e-41f9a5b37ddb' };
      conversation.end = end;

      const endCollection: IEnd[] = [{ id: 'e7b131b3-4ebe-4969-aaed-e566453649f2' }];
      jest.spyOn(endService, 'query').mockReturnValue(of(new HttpResponse({ body: endCollection })));
      const expectedCollection: IEnd[] = [end, ...endCollection];
      jest.spyOn(endService, 'addEndToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ conversation });
      comp.ngOnInit();

      expect(endService.query).toHaveBeenCalled();
      expect(endService.addEndToCollectionIfMissing).toHaveBeenCalledWith(endCollection, end);
      expect(comp.endsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const conversation: IConversation = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const end: IEnd = { id: '543fdf62-294d-4910-8711-37d7e9de2661' };
      conversation.end = end;

      activatedRoute.data = of({ conversation });
      comp.ngOnInit();

      expect(comp.endsCollection).toContain(end);
      expect(comp.conversation).toEqual(conversation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversation>>();
      const conversation = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(conversationFormService, 'getConversation').mockReturnValue(conversation);
      jest.spyOn(conversationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conversation }));
      saveSubject.complete();

      // THEN
      expect(conversationFormService.getConversation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(conversationService.update).toHaveBeenCalledWith(expect.objectContaining(conversation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversation>>();
      const conversation = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(conversationFormService, 'getConversation').mockReturnValue({ id: null });
      jest.spyOn(conversationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conversation }));
      saveSubject.complete();

      // THEN
      expect(conversationFormService.getConversation).toHaveBeenCalled();
      expect(conversationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversation>>();
      const conversation = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(conversationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conversationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEnd', () => {
      it('Should forward to endService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(endService, 'compareEnd');
        comp.compareEnd(entity, entity2);
        expect(endService.compareEnd).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
