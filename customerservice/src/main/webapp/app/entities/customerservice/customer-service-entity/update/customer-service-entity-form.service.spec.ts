import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../customer-service-entity.test-samples';

import { CustomerServiceEntityFormService } from './customer-service-entity-form.service';

describe('CustomerServiceEntity Form Service', () => {
  let service: CustomerServiceEntityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerServiceEntityFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerServiceEntityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerServiceEntityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing ICustomerServiceEntity should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerServiceEntityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getCustomerServiceEntity', () => {
      it('should return NewCustomerServiceEntity for default CustomerServiceEntity initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCustomerServiceEntityFormGroup(sampleWithNewData);

        const customerServiceEntity = service.getCustomerServiceEntity(formGroup) as any;

        expect(customerServiceEntity).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomerServiceEntity for empty CustomerServiceEntity initial value', () => {
        const formGroup = service.createCustomerServiceEntityFormGroup();

        const customerServiceEntity = service.getCustomerServiceEntity(formGroup) as any;

        expect(customerServiceEntity).toMatchObject({});
      });

      it('should return ICustomerServiceEntity', () => {
        const formGroup = service.createCustomerServiceEntityFormGroup(sampleWithRequiredData);

        const customerServiceEntity = service.getCustomerServiceEntity(formGroup) as any;

        expect(customerServiceEntity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomerServiceEntity should not enable id FormControl', () => {
        const formGroup = service.createCustomerServiceEntityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomerServiceEntity should disable id FormControl', () => {
        const formGroup = service.createCustomerServiceEntityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
