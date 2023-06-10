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

import { CustomerServiceUpdateComponent } from './customer-service-update.component';

describe('CustomerService Management Update Component', () => {
  let comp: CustomerServiceUpdateComponent;
  let fixture: ComponentFixture<CustomerServiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerServiceFormService: CustomerServiceFormService;
  let customerServiceService: CustomerServiceService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const customerService: ICustomerService = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ customerService });
      comp.ngOnInit();

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
});
