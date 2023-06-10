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

import { CustomerServiceUserUpdateComponent } from './customer-service-user-update.component';

describe('CustomerServiceUser Management Update Component', () => {
  let comp: CustomerServiceUserUpdateComponent;
  let fixture: ComponentFixture<CustomerServiceUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerServiceUserFormService: CustomerServiceUserFormService;
  let customerServiceUserService: CustomerServiceUserService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const customerServiceUser: ICustomerServiceUser = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ customerServiceUser });
      comp.ngOnInit();

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
});
