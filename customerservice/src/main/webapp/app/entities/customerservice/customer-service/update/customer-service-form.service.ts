import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICustomerService | NewCustomerService> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type CustomerServiceFormRawValue = FormValueOf<ICustomerService>;

type NewCustomerServiceFormRawValue = FormValueOf<NewCustomerService>;

type CustomerServiceFormDefaults = Pick<NewCustomerService, 'id' | 'startDate' | 'endDate'>;

type CustomerServiceFormGroupContent = {
  id: FormControl<CustomerServiceFormRawValue['id'] | NewCustomerService['id']>;
  startDate: FormControl<CustomerServiceFormRawValue['startDate']>;
  endDate: FormControl<CustomerServiceFormRawValue['endDate']>;
  reportNumber: FormControl<CustomerServiceFormRawValue['reportNumber']>;
  faqs: FormControl<CustomerServiceFormRawValue['faqs']>;
  customerServiceEntity: FormControl<CustomerServiceFormRawValue['customerServiceEntity']>;
  customerServiceUser: FormControl<CustomerServiceFormRawValue['customerServiceUser']>;
};

export type CustomerServiceFormGroup = FormGroup<CustomerServiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerServiceFormService {
  createCustomerServiceFormGroup(customerService: CustomerServiceFormGroupInput = { id: null }): CustomerServiceFormGroup {
    const customerServiceRawValue = this.convertCustomerServiceToCustomerServiceRawValue({
      ...this.getFormDefaults(),
      ...customerService,
    });
    return new FormGroup<CustomerServiceFormGroupContent>({
      id: new FormControl(
        { value: customerServiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(customerServiceRawValue.startDate),
      endDate: new FormControl(customerServiceRawValue.endDate),
      reportNumber: new FormControl(customerServiceRawValue.reportNumber),
      faqs: new FormControl(customerServiceRawValue.faqs),
      customerServiceEntity: new FormControl(customerServiceRawValue.customerServiceEntity),
      customerServiceUser: new FormControl(customerServiceRawValue.customerServiceUser),
    });
  }

  getCustomerService(form: CustomerServiceFormGroup): ICustomerService | NewCustomerService {
    return this.convertCustomerServiceRawValueToCustomerService(
      form.getRawValue() as CustomerServiceFormRawValue | NewCustomerServiceFormRawValue
    );
  }

  resetForm(form: CustomerServiceFormGroup, customerService: CustomerServiceFormGroupInput): void {
    const customerServiceRawValue = this.convertCustomerServiceToCustomerServiceRawValue({ ...this.getFormDefaults(), ...customerService });
    form.reset(
      {
        ...customerServiceRawValue,
        id: { value: customerServiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerServiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertCustomerServiceRawValueToCustomerService(
    rawCustomerService: CustomerServiceFormRawValue | NewCustomerServiceFormRawValue
  ): ICustomerService | NewCustomerService {
    return {
      ...rawCustomerService,
      startDate: dayjs(rawCustomerService.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawCustomerService.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertCustomerServiceToCustomerServiceRawValue(
    customerService: ICustomerService | (Partial<NewCustomerService> & CustomerServiceFormDefaults)
  ): CustomerServiceFormRawValue | PartialWithRequiredKeyOf<NewCustomerServiceFormRawValue> {
    return {
      ...customerService,
      startDate: customerService.startDate ? customerService.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: customerService.endDate ? customerService.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
