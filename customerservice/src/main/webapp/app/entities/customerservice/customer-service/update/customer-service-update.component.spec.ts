import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CustomerServiceFormService } from './customer-service-form.service';
import { CustomerServiceService } from '../service/customer-service.service';
import { ICustomerService } from '../customer-service.model';
import { IFAQs } from 'app/entities/customerservice/fa-qs/fa-qs.model';
import { FAQsService } from 'app/entities/customerservice/fa-qs/service/fa-qs.service';
import { ICustomerServiceEntity } from 'app/entities/customerservice/customer-service-entity/customer-service-entity.model';
import { CustomerServiceEntityService } from 'app/entities/customerservice/customer-service-entity/service/customer-service-entity.service';
import { ICustomerServiceUser } from 'app/entities/customerservice/customer-service-user/customer-service-user.model';
import { CustomerServiceUserService } from 'app/entities/customerservice/customer-service-user/service/customer-service-user.service';

import { CustomerServiceUpdateComponent } from './customer-service-update.component';

describe('CustomerService Management Update Component', () => {
  let comp: CustomerServiceUpdateComponent;
  let fixture: ComponentFixture<CustomerServiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerServiceFormService: CustomerServiceFormService;
  let customerServiceService: CustomerServiceService;
  let fAQsService: FAQsService;
  let customerServiceEntityService: CustomerServiceEntityService;
  let customerServiceUserService: CustomerServiceUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CustomerServiceUpdateComponent],
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
      .overrideTemplate(CustomerServiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerServiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerServiceFormService = TestBed.inject(CustomerServiceFormService);
    customerServiceService = TestBed.inject(CustomerServiceService);
    fAQsService = TestBed.inject(FAQsService);
    customerServiceEntityService = TestBed.inject(CustomerServiceEntityService);
    customerServiceUserService = TestBed.inject(CustomerServiceUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call faqs query and add missing value', () => {
      const customerService: ICustomerService = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const faqs: IFAQs = { id: '9fa12dfd-e768-4439-a2fc-9000c58860a2' };
      customerService.faqs = faqs;

      const faqsCollection: IFAQs[] = [{ id: 'bc6a0f9f-152e-464a-bec0-3442e4ed4f99' }];
      jest.spyOn(fAQsService, 'query').mockReturnValue(of(new HttpResponse({ body: faqsCollection })));
      const expectedCollection: IFAQs[] = [faqs, ...faqsCollection];
      jest.spyOn(fAQsService, 'addFAQsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customerService });
      comp.ngOnInit();

      expect(fAQsService.query).toHaveBeenCalled();
      expect(fAQsService.addFAQsToCollectionIfMissing).toHaveBeenCalledWith(faqsCollection, faqs);
      expect(comp.faqsCollection).toEqual(expectedCollection);
    });

    it('Should call customerServiceEntity query and add missing value', () => {
      const customerService: ICustomerService = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const customerServiceEntity: ICustomerServiceEntity = { id: 'a64f8135-e5f8-4e4f-9dba-39f71f1c7a46' };
      customerService.customerServiceEntity = customerServiceEntity;

      const customerServiceEntityCollection: ICustomerServiceEntity[] = [{ id: '4ca64544-51b6-4a18-b2e6-16077edc65b3' }];
      jest.spyOn(customerServiceEntityService, 'query').mockReturnValue(of(new HttpResponse({ body: customerServiceEntityCollection })));
      const expectedCollection: ICustomerServiceEntity[] = [customerServiceEntity, ...customerServiceEntityCollection];
      jest.spyOn(customerServiceEntityService, 'addCustomerServiceEntityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customerService });
      comp.ngOnInit();

      expect(customerServiceEntityService.query).toHaveBeenCalled();
      expect(customerServiceEntityService.addCustomerServiceEntityToCollectionIfMissing).toHaveBeenCalledWith(
        customerServiceEntityCollection,
        customerServiceEntity
      );
      expect(comp.customerServiceEntitiesCollection).toEqual(expectedCollection);
    });

    it('Should call customerServiceUser query and add missing value', () => {
      const customerService: ICustomerService = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const customerServiceUser: ICustomerServiceUser = { id: '859e3125-ca59-4bf6-bd00-88f5cf7f69dd' };
      customerService.customerServiceUser = customerServiceUser;

      const customerServiceUserCollection: ICustomerServiceUser[] = [{ id: '1bc57bdb-da63-4976-9b89-63fc6cff5a4f' }];
      jest.spyOn(customerServiceUserService, 'query').mockReturnValue(of(new HttpResponse({ body: customerServiceUserCollection })));
      const expectedCollection: ICustomerServiceUser[] = [customerServiceUser, ...customerServiceUserCollection];
      jest.spyOn(customerServiceUserService, 'addCustomerServiceUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customerService });
      comp.ngOnInit();

      expect(customerServiceUserService.query).toHaveBeenCalled();
      expect(customerServiceUserService.addCustomerServiceUserToCollectionIfMissing).toHaveBeenCalledWith(
        customerServiceUserCollection,
        customerServiceUser
      );
      expect(comp.customerServiceUsersCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customerService: ICustomerService = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const faqs: IFAQs = { id: '84e61861-fc22-446e-9094-e82890131c16' };
      customerService.faqs = faqs;
      const customerServiceEntity: ICustomerServiceEntity = { id: 'd0b3571e-cf47-4ce2-a301-11b9272ae97e' };
      customerService.customerServiceEntity = customerServiceEntity;
      const customerServiceUser: ICustomerServiceUser = { id: '2fe3597d-2dbf-453d-9afa-777b839c0ad3' };
      customerService.customerServiceUser = customerServiceUser;

      activatedRoute.data = of({ customerService });
      comp.ngOnInit();

      expect(comp.faqsCollection).toContain(faqs);
      expect(comp.customerServiceEntitiesCollection).toContain(customerServiceEntity);
      expect(comp.customerServiceUsersCollection).toContain(customerServiceUser);
      expect(comp.customerService).toEqual(customerService);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerService>>();
      const customerService = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerServiceFormService, 'getCustomerService').mockReturnValue(customerService);
      jest.spyOn(customerServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerService }));
      saveSubject.complete();

      // THEN
      expect(customerServiceFormService.getCustomerService).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerServiceService.update).toHaveBeenCalledWith(expect.objectContaining(customerService));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerService>>();
      const customerService = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerServiceFormService, 'getCustomerService').mockReturnValue({ id: null });
      jest.spyOn(customerServiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerService: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerService }));
      saveSubject.complete();

      // THEN
      expect(customerServiceFormService.getCustomerService).toHaveBeenCalled();
      expect(customerServiceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerService>>();
      const customerService = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerServiceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFAQs', () => {
      it('Should forward to fAQsService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(fAQsService, 'compareFAQs');
        comp.compareFAQs(entity, entity2);
        expect(fAQsService.compareFAQs).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomerServiceEntity', () => {
      it('Should forward to customerServiceEntityService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(customerServiceEntityService, 'compareCustomerServiceEntity');
        comp.compareCustomerServiceEntity(entity, entity2);
        expect(customerServiceEntityService.compareCustomerServiceEntity).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomerServiceUser', () => {
      it('Should forward to customerServiceUserService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(customerServiceUserService, 'compareCustomerServiceUser');
        comp.compareCustomerServiceUser(entity, entity2);
        expect(customerServiceUserService.compareCustomerServiceUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
