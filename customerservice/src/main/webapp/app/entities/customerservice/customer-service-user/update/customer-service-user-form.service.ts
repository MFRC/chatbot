import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomerServiceUser, NewCustomerServiceUser } from '../customer-service-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomerServiceUser for edit and NewCustomerServiceUserFormGroupInput for create.
 */
type CustomerServiceUserFormGroupInput = ICustomerServiceUser | PartialWithRequiredKeyOf<NewCustomerServiceUser>;

type CustomerServiceUserFormDefaults = Pick<NewCustomerServiceUser, 'id'>;

type CustomerServiceUserFormGroupContent = {
  id: FormControl<ICustomerServiceUser['id'] | NewCustomerServiceUser['id']>;
};

export type CustomerServiceUserFormGroup = FormGroup<CustomerServiceUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerServiceUserFormService {
  createCustomerServiceUserFormGroup(customerServiceUser: CustomerServiceUserFormGroupInput = { id: null }): CustomerServiceUserFormGroup {
    const customerServiceUserRawValue = {
      ...this.getFormDefaults(),
      ...customerServiceUser,
    };
    return new FormGroup<CustomerServiceUserFormGroupContent>({
      id: new FormControl(
        { value: customerServiceUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getCustomerServiceUser(form: CustomerServiceUserFormGroup): ICustomerServiceUser | NewCustomerServiceUser {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as ICustomerServiceUser | NewCustomerServiceUser;
  }

  resetForm(form: CustomerServiceUserFormGroup, customerServiceUser: CustomerServiceUserFormGroupInput): void {
    const customerServiceUserRawValue = { ...this.getFormDefaults(), ...customerServiceUser };
    form.reset(
      {
        ...customerServiceUserRawValue,
        id: { value: customerServiceUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerServiceUserFormDefaults {
    return {
      id: null,
    };
  }
}
