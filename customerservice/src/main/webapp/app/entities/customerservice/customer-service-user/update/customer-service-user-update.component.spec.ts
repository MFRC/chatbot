import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CustomerServiceUserFormService } from './customer-service-user-form.service';
import { CustomerServiceUserService } from '../service/customer-service-user.service';
import { ICustomerServiceUser } from '../customer-service-user.model';
import { IConversation } from 'app/entities/customerservice/conversation/conversation.model';
import { ConversationService } from 'app/entities/customerservice/conversation/service/conversation.service';

import { CustomerServiceUserUpdateComponent } from './customer-service-user-update.component';

describe('CustomerServiceUser Management Update Component', () => {
  let comp: CustomerServiceUserUpdateComponent;
  let fixture: ComponentFixture<CustomerServiceUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerServiceUserFormService: CustomerServiceUserFormService;
  let customerServiceUserService: CustomerServiceUserService;
  let conversationService: ConversationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CustomerServiceUserUpdateComponent],
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
      .overrideTemplate(CustomerServiceUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerServiceUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerServiceUserFormService = TestBed.inject(CustomerServiceUserFormService);
    customerServiceUserService = TestBed.inject(CustomerServiceUserService);
    conversationService = TestBed.inject(ConversationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call conversation query and add missing value', () => {
      const customerServiceUser: ICustomerServiceUser = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const conversation: IConversation = { id: '57bfad11-63d3-46c6-b0cf-4a53304aa903' };
      customerServiceUser.conversation = conversation;

      const conversationCollection: IConversation[] = [{ id: '53a2f6fd-4004-4071-8327-853a300cfef9' }];
      jest.spyOn(conversationService, 'query').mockReturnValue(of(new HttpResponse({ body: conversationCollection })));
      const expectedCollection: IConversation[] = [conversation, ...conversationCollection];
      jest.spyOn(conversationService, 'addConversationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customerServiceUser });
      comp.ngOnInit();

      expect(conversationService.query).toHaveBeenCalled();
      expect(conversationService.addConversationToCollectionIfMissing).toHaveBeenCalledWith(conversationCollection, conversation);
      expect(comp.conversationsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customerServiceUser: ICustomerServiceUser = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const conversation: IConversation = { id: 'b1793182-3d10-4e57-b688-32797031785b' };
      customerServiceUser.conversation = conversation;

      activatedRoute.data = of({ customerServiceUser });
      comp.ngOnInit();

      expect(comp.conversationsCollection).toContain(conversation);
      expect(comp.customerServiceUser).toEqual(customerServiceUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerServiceUser>>();
      const customerServiceUser = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerServiceUserFormService, 'getCustomerServiceUser').mockReturnValue(customerServiceUser);
      jest.spyOn(customerServiceUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerServiceUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerServiceUser }));
      saveSubject.complete();

      // THEN
      expect(customerServiceUserFormService.getCustomerServiceUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerServiceUserService.update).toHaveBeenCalledWith(expect.objectContaining(customerServiceUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerServiceUser>>();
      const customerServiceUser = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerServiceUserFormService, 'getCustomerServiceUser').mockReturnValue({ id: null });
      jest.spyOn(customerServiceUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerServiceUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerServiceUser }));
      saveSubject.complete();

      // THEN
      expect(customerServiceUserFormService.getCustomerServiceUser).toHaveBeenCalled();
      expect(customerServiceUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerServiceUser>>();
      const customerServiceUser = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerServiceUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerServiceUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerServiceUserService.update).toHaveBeenCalled();
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
