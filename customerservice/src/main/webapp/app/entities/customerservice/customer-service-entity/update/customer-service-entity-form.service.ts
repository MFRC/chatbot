import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomerServiceEntity, NewCustomerServiceEntity } from '../customer-service-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomerServiceEntity for edit and NewCustomerServiceEntityFormGroupInput for create.
 */
type CustomerServiceEntityFormGroupInput = ICustomerServiceEntity | PartialWithRequiredKeyOf<NewCustomerServiceEntity>;

type CustomerServiceEntityFormDefaults = Pick<NewCustomerServiceEntity, 'id'>;

type CustomerServiceEntityFormGroupContent = {
  id: FormControl<ICustomerServiceEntity['id'] | NewCustomerServiceEntity['id']>;
  reservationNumber: FormControl<ICustomerServiceEntity['reservationNumber']>;
  roomNumber: FormControl<ICustomerServiceEntity['roomNumber']>;
  services: FormControl<ICustomerServiceEntity['services']>;
  prices: FormControl<ICustomerServiceEntity['prices']>;
  amenities: FormControl<ICustomerServiceEntity['amenities']>;
  conversation: FormControl<ICustomerServiceEntity['conversation']>;
};

export type CustomerServiceEntityFormGroup = FormGroup<CustomerServiceEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerServiceEntityFormService {
  createCustomerServiceEntityFormGroup(
    customerServiceEntity: CustomerServiceEntityFormGroupInput = { id: null }
  ): CustomerServiceEntityFormGroup {
    const customerServiceEntityRawValue = {
      ...this.getFormDefaults(),
      ...customerServiceEntity,
    };
    return new FormGroup<CustomerServiceEntityFormGroupContent>({
      id: new FormControl(
        { value: customerServiceEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      reservationNumber: new FormControl(customerServiceEntityRawValue.reservationNumber),
      roomNumber: new FormControl(customerServiceEntityRawValue.roomNumber),
      services: new FormControl(customerServiceEntityRawValue.services),
      prices: new FormControl(customerServiceEntityRawValue.prices),
      amenities: new FormControl(customerServiceEntityRawValue.amenities),
      conversation: new FormControl(customerServiceEntityRawValue.conversation),
    });
  }

  getCustomerServiceEntity(form: CustomerServiceEntityFormGroup): ICustomerServiceEntity | NewCustomerServiceEntity {
    return form.getRawValue() as ICustomerServiceEntity | NewCustomerServiceEntity;
  }

  resetForm(form: CustomerServiceEntityFormGroup, customerServiceEntity: CustomerServiceEntityFormGroupInput): void {
    const customerServiceEntityRawValue = { ...this.getFormDefaults(), ...customerServiceEntity };
    form.reset(
      {
        ...customerServiceEntityRawValue,
        id: { value: customerServiceEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerServiceEntityFormDefaults {
    return {
      id: null,
    };
  }
}
