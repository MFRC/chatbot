import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../customer-info.test-samples';

import { CustomerInfoFormService } from './customer-info-form.service';

describe('CustomerInfo Form Service', () => {
  let service: CustomerInfoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerInfoFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerInfoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerInfoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            customerID: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            address: expect.any(Object),
          })
        );
      });

      it('passing ICustomerInfo should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerInfoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            customerID: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            address: expect.any(Object),
          })
        );
      });
    });

    describe('getCustomerInfo', () => {
      it('should return NewCustomerInfo for default CustomerInfo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCustomerInfoFormGroup(sampleWithNewData);

        const customerInfo = service.getCustomerInfo(formGroup) as any;

        expect(customerInfo).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomerInfo for empty CustomerInfo initial value', () => {
        const formGroup = service.createCustomerInfoFormGroup();

        const customerInfo = service.getCustomerInfo(formGroup) as any;

        expect(customerInfo).toMatchObject({});
      });

      it('should return ICustomerInfo', () => {
        const formGroup = service.createCustomerInfoFormGroup(sampleWithRequiredData);

        const customerInfo = service.getCustomerInfo(formGroup) as any;

        expect(customerInfo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomerInfo should not enable id FormControl', () => {
        const formGroup = service.createCustomerInfoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomerInfo should disable id FormControl', () => {
        const formGroup = service.createCustomerInfoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
