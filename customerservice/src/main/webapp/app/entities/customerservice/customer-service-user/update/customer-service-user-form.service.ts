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
  firstName: FormControl<ICustomerServiceUser['firstName']>;
  lastName: FormControl<ICustomerServiceUser['lastName']>;
  email: FormControl<ICustomerServiceUser['email']>;
  phoneNumber: FormControl<ICustomerServiceUser['phoneNumber']>;
  reservationNumber: FormControl<ICustomerServiceUser['reservationNumber']>;
  roomNumber: FormControl<ICustomerServiceUser['roomNumber']>;
  conversation: FormControl<ICustomerServiceUser['conversation']>;
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
      firstName: new FormControl(customerServiceUserRawValue.firstName),
      lastName: new FormControl(customerServiceUserRawValue.lastName),
      email: new FormControl(customerServiceUserRawValue.email),
      phoneNumber: new FormControl(customerServiceUserRawValue.phoneNumber),
      reservationNumber: new FormControl(customerServiceUserRawValue.reservationNumber),
      roomNumber: new FormControl(customerServiceUserRawValue.roomNumber),
      conversation: new FormControl(customerServiceUserRawValue.conversation),
    });
  }

  getCustomerServiceUser(form: CustomerServiceUserFormGroup): ICustomerServiceUser | NewCustomerServiceUser {
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
