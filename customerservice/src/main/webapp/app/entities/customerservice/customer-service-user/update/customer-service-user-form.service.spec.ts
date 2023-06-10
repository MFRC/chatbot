import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../customer-service-user.test-samples';

import { CustomerServiceUserFormService } from './customer-service-user-form.service';

describe('CustomerServiceUser Form Service', () => {
  let service: CustomerServiceUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerServiceUserFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerServiceUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerServiceUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing ICustomerServiceUser should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerServiceUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getCustomerServiceUser', () => {
      it('should return NewCustomerServiceUser for default CustomerServiceUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCustomerServiceUserFormGroup(sampleWithNewData);

        const customerServiceUser = service.getCustomerServiceUser(formGroup) as any;

        expect(customerServiceUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomerServiceUser for empty CustomerServiceUser initial value', () => {
        const formGroup = service.createCustomerServiceUserFormGroup();

        const customerServiceUser = service.getCustomerServiceUser(formGroup) as any;

        expect(customerServiceUser).toMatchObject({});
      });

      it('should return ICustomerServiceUser', () => {
        const formGroup = service.createCustomerServiceUserFormGroup(sampleWithRequiredData);

        const customerServiceUser = service.getCustomerServiceUser(formGroup) as any;

        expect(customerServiceUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomerServiceUser should not enable id FormControl', () => {
        const formGroup = service.createCustomerServiceUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomerServiceUser should disable id FormControl', () => {
        const formGroup = service.createCustomerServiceUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
