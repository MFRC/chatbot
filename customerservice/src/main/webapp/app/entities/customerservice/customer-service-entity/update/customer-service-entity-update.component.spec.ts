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

import { CustomerServiceEntityUpdateComponent } from './customer-service-entity-update.component';

describe('CustomerServiceEntity Management Update Component', () => {
  let comp: CustomerServiceEntityUpdateComponent;
  let fixture: ComponentFixture<CustomerServiceEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerServiceEntityFormService: CustomerServiceEntityFormService;
  let customerServiceEntityService: CustomerServiceEntityService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const customerServiceEntity: ICustomerServiceEntity = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ customerServiceEntity });
      comp.ngOnInit();

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
});
