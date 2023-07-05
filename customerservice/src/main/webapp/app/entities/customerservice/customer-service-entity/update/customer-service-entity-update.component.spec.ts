import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CustomerServiceEntityFormService } from './customer-service-entity-form.service';
import { CustomerServiceEntityService } from '../service/customer-service-entity.service';
import { ICustomerServiceEntity } from '../customer-service-entity.model';
import { IConversation } from 'app/entities/customerservice/conversation/conversation.model';
import { ConversationService } from 'app/entities/customerservice/conversation/service/conversation.service';

import { CustomerServiceEntityUpdateComponent } from './customer-service-entity-update.component';

describe('CustomerServiceEntity Management Update Component', () => {
  let comp: CustomerServiceEntityUpdateComponent;
  let fixture: ComponentFixture<CustomerServiceEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerServiceEntityFormService: CustomerServiceEntityFormService;
  let customerServiceEntityService: CustomerServiceEntityService;
  let conversationService: ConversationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CustomerServiceEntityUpdateComponent],
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
      .overrideTemplate(CustomerServiceEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerServiceEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerServiceEntityFormService = TestBed.inject(CustomerServiceEntityFormService);
    customerServiceEntityService = TestBed.inject(CustomerServiceEntityService);
    conversationService = TestBed.inject(ConversationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call conversation query and add missing value', () => {
      const customerServiceEntity: ICustomerServiceEntity = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const conversation: IConversation = { id: 'c9ddff6b-34c8-4074-be16-77ce31699a47' };
      customerServiceEntity.conversation = conversation;

      const conversationCollection: IConversation[] = [{ id: 'df09d6ca-0384-44ad-952a-9e5958fbeccb' }];
      jest.spyOn(conversationService, 'query').mockReturnValue(of(new HttpResponse({ body: conversationCollection })));
      const expectedCollection: IConversation[] = [conversation, ...conversationCollection];
      jest.spyOn(conversationService, 'addConversationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customerServiceEntity });
      comp.ngOnInit();

      expect(conversationService.query).toHaveBeenCalled();
      expect(conversationService.addConversationToCollectionIfMissing).toHaveBeenCalledWith(conversationCollection, conversation);
      expect(comp.conversationsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customerServiceEntity: ICustomerServiceEntity = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const conversation: IConversation = { id: '0e24ca66-0e31-442f-b6d1-173a03e29e48' };
      customerServiceEntity.conversation = conversation;

      activatedRoute.data = of({ customerServiceEntity });
      comp.ngOnInit();

      expect(comp.conversationsCollection).toContain(conversation);
      expect(comp.customerServiceEntity).toEqual(customerServiceEntity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerServiceEntity>>();
      const customerServiceEntity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerServiceEntityFormService, 'getCustomerServiceEntity').mockReturnValue(customerServiceEntity);
      jest.spyOn(customerServiceEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerServiceEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerServiceEntity }));
      saveSubject.complete();

      // THEN
      expect(customerServiceEntityFormService.getCustomerServiceEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerServiceEntityService.update).toHaveBeenCalledWith(expect.objectContaining(customerServiceEntity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerServiceEntity>>();
      const customerServiceEntity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerServiceEntityFormService, 'getCustomerServiceEntity').mockReturnValue({ id: null });
      jest.spyOn(customerServiceEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerServiceEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerServiceEntity }));
      saveSubject.complete();

      // THEN
      expect(customerServiceEntityFormService.getCustomerServiceEntity).toHaveBeenCalled();
      expect(customerServiceEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerServiceEntity>>();
      const customerServiceEntity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerServiceEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerServiceEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerServiceEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareConversation', () => {
      it('Should forward to conversationService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(conversationService, 'compareConversation');
        comp.compareConversation(entity, entity2);
        expect(conversationService.compareConversation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
