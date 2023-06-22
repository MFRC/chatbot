import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomerInfo, NewCustomerInfo } from '../customer-info.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomerInfo for edit and NewCustomerInfoFormGroupInput for create.
 */
type CustomerInfoFormGroupInput = ICustomerInfo | PartialWithRequiredKeyOf<NewCustomerInfo>;

type CustomerInfoFormDefaults = Pick<NewCustomerInfo, 'id'>;

type CustomerInfoFormGroupContent = {
  id: FormControl<ICustomerInfo['id'] | NewCustomerInfo['id']>;
  customerID: FormControl<ICustomerInfo['customerID']>;
  firstName: FormControl<ICustomerInfo['firstName']>;
  lastName: FormControl<ICustomerInfo['lastName']>;
  address: FormControl<ICustomerInfo['address']>;
};

export type CustomerInfoFormGroup = FormGroup<CustomerInfoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerInfoFormService {
  createCustomerInfoFormGroup(customerInfo: CustomerInfoFormGroupInput = { id: null }): CustomerInfoFormGroup {
    const customerInfoRawValue = {
      ...this.getFormDefaults(),
      ...customerInfo,
    };
    return new FormGroup<CustomerInfoFormGroupContent>({
      id: new FormControl(
        { value: customerInfoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      customerID: new FormControl(customerInfoRawValue.customerID),
      firstName: new FormControl(customerInfoRawValue.firstName),
      lastName: new FormControl(customerInfoRawValue.lastName),
      address: new FormControl(customerInfoRawValue.address),
    });
  }

  getCustomerInfo(form: CustomerInfoFormGroup): ICustomerInfo | NewCustomerInfo {
    return form.getRawValue() as ICustomerInfo | NewCustomerInfo;
  }

  resetForm(form: CustomerInfoFormGroup, customerInfo: CustomerInfoFormGroupInput): void {
    const customerInfoRawValue = { ...this.getFormDefaults(), ...customerInfo };
    form.reset(
      {
        ...customerInfoRawValue,
        id: { value: customerInfoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerInfoFormDefaults {
    return {
      id: null,
    };
  }
}
