import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RepairRequestFormService } from './repair-request-form.service';
import { RepairRequestService } from '../service/repair-request.service';
import { IRepairRequest } from '../repair-request.model';
import { ICustomer } from 'app/entities/repairservice/customer/customer.model';
import { CustomerService } from 'app/entities/repairservice/customer/service/customer.service';

import { RepairRequestUpdateComponent } from './repair-request-update.component';

describe('RepairRequest Management Update Component', () => {
  let comp: RepairRequestUpdateComponent;
  let fixture: ComponentFixture<RepairRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let repairRequestFormService: RepairRequestFormService;
  let repairRequestService: RepairRequestService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RepairRequestUpdateComponent],
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
      .overrideTemplate(RepairRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RepairRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    repairRequestFormService = TestBed.inject(RepairRequestFormService);
    repairRequestService = TestBed.inject(RepairRequestService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const repairRequest: IRepairRequest = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const customer: ICustomer = { id: '5f01fcee-5f77-4998-83c4-ee2476553a83' };
      repairRequest.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 'c4a07794-aa6a-42fb-971a-c12d25a479c2' }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ repairRequest });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining)
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const repairRequest: IRepairRequest = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const customer: ICustomer = { id: '6b430e25-dd5a-4bca-9d0f-6645378cef34' };
      repairRequest.customer = customer;

      activatedRoute.data = of({ repairRequest });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.repairRequest).toEqual(repairRequest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepairRequest>>();
      const repairRequest = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(repairRequestFormService, 'getRepairRequest').mockReturnValue(repairRequest);
      jest.spyOn(repairRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repairRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: repairRequest }));
      saveSubject.complete();

      // THEN
      expect(repairRequestFormService.getRepairRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(repairRequestService.update).toHaveBeenCalledWith(expect.objectContaining(repairRequest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepairRequest>>();
      const repairRequest = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(repairRequestFormService, 'getRepairRequest').mockReturnValue({ id: null });
      jest.spyOn(repairRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repairRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: repairRequest }));
      saveSubject.complete();

      // THEN
      expect(repairRequestFormService.getRepairRequest).toHaveBeenCalled();
      expect(repairRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepairRequest>>();
      const repairRequest = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(repairRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repairRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(repairRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
