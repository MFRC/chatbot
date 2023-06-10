import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomerService, NewCustomerService } from '../customer-service.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomerService for edit and NewCustomerServiceFormGroupInput for create.
 */
type CustomerServiceFormGroupInput = ICustomerService | PartialWithRequiredKeyOf<NewCustomerService>;

type CustomerServiceFormDefaults = Pick<NewCustomerService, 'id'>;

type CustomerServiceFormGroupContent = {
  id: FormControl<ICustomerService['id'] | NewCustomerService['id']>;
};

export type CustomerServiceFormGroup = FormGroup<CustomerServiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerServiceFormService {
  createCustomerServiceFormGroup(customerService: CustomerServiceFormGroupInput = { id: null }): CustomerServiceFormGroup {
    const customerServiceRawValue = {
      ...this.getFormDefaults(),
      ...customerService,
    };
    return new FormGroup<CustomerServiceFormGroupContent>({
      id: new FormControl(
        { value: customerServiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getCustomerService(form: CustomerServiceFormGroup): ICustomerService | NewCustomerService {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as ICustomerService | NewCustomerService;
  }

  resetForm(form: CustomerServiceFormGroup, customerService: CustomerServiceFormGroupInput): void {
    const customerServiceRawValue = { ...this.getFormDefaults(), ...customerService };
    form.reset(
      {
        ...customerServiceRawValue,
        id: { value: customerServiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerServiceFormDefaults {
    return {
      id: null,
    };
  }
}
