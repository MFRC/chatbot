import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../customer-service.test-samples';

import { CustomerServiceFormService } from './customer-service-form.service';

describe('CustomerService Form Service', () => {
  let service: CustomerServiceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerServiceFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerServiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerServiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            reportNumber: expect.any(Object),
            faqs: expect.any(Object),
            customerServiceEntity: expect.any(Object),
            customerServiceUser: expect.any(Object),
          })
        );
      });

      it('passing ICustomerService should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerServiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            reportNumber: expect.any(Object),
            faqs: expect.any(Object),
            customerServiceEntity: expect.any(Object),
            customerServiceUser: expect.any(Object),
          })
        );
      });
    });

    describe('getCustomerService', () => {
      it('should return NewCustomerService for default CustomerService initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCustomerServiceFormGroup(sampleWithNewData);

        const customerService = service.getCustomerService(formGroup) as any;

        expect(customerService).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomerService for empty CustomerService initial value', () => {
        const formGroup = service.createCustomerServiceFormGroup();

        const customerService = service.getCustomerService(formGroup) as any;

        expect(customerService).toMatchObject({});
      });

      it('should return ICustomerService', () => {
        const formGroup = service.createCustomerServiceFormGroup(sampleWithRequiredData);

        const customerService = service.getCustomerService(formGroup) as any;

        expect(customerService).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomerService should not enable id FormControl', () => {
        const formGroup = service.createCustomerServiceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomerService should disable id FormControl', () => {
        const formGroup = service.createCustomerServiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
