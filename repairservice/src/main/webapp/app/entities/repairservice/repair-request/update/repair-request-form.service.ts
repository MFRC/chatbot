import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRepairRequest, NewRepairRequest } from '../repair-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRepairRequest for edit and NewRepairRequestFormGroupInput for create.
 */
type RepairRequestFormGroupInput = IRepairRequest | PartialWithRequiredKeyOf<NewRepairRequest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRepairRequest | NewRepairRequest> = Omit<T, 'dateCreated' | 'dateUpdated'> & {
  dateCreated?: string | null;
  dateUpdated?: string | null;
};

type RepairRequestFormRawValue = FormValueOf<IRepairRequest>;

type NewRepairRequestFormRawValue = FormValueOf<NewRepairRequest>;

type RepairRequestFormDefaults = Pick<NewRepairRequest, 'id' | 'dateCreated' | 'dateUpdated'>;

type RepairRequestFormGroupContent = {
  id: FormControl<RepairRequestFormRawValue['id'] | NewRepairRequest['id']>;
  repairRequestId: FormControl<RepairRequestFormRawValue['repairRequestId']>;
  customerId: FormControl<RepairRequestFormRawValue['customerId']>;
  roomNumber: FormControl<RepairRequestFormRawValue['roomNumber']>;
  description: FormControl<RepairRequestFormRawValue['description']>;
  status: FormControl<RepairRequestFormRawValue['status']>;
  dateCreated: FormControl<RepairRequestFormRawValue['dateCreated']>;
  dateUpdated: FormControl<RepairRequestFormRawValue['dateUpdated']>;
  customer: FormControl<RepairRequestFormRawValue['customer']>;
};

export type RepairRequestFormGroup = FormGroup<RepairRequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RepairRequestFormService {
  createRepairRequestFormGroup(repairRequest: RepairRequestFormGroupInput = { id: null }): RepairRequestFormGroup {
    const repairRequestRawValue = this.convertRepairRequestToRepairRequestRawValue({
      ...this.getFormDefaults(),
      ...repairRequest,
    });
    return new FormGroup<RepairRequestFormGroupContent>({
      id: new FormControl(
        { value: repairRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      repairRequestId: new FormControl(repairRequestRawValue.repairRequestId),
      customerId: new FormControl(repairRequestRawValue.customerId),
      roomNumber: new FormControl(repairRequestRawValue.roomNumber),
      description: new FormControl(repairRequestRawValue.description),
      status: new FormControl(repairRequestRawValue.status),
      dateCreated: new FormControl(repairRequestRawValue.dateCreated),
      dateUpdated: new FormControl(repairRequestRawValue.dateUpdated),
      customer: new FormControl(repairRequestRawValue.customer),
    });
  }

  getRepairRequest(form: RepairRequestFormGroup): IRepairRequest | NewRepairRequest {
    return this.convertRepairRequestRawValueToRepairRequest(form.getRawValue() as RepairRequestFormRawValue | NewRepairRequestFormRawValue);
  }

  resetForm(form: RepairRequestFormGroup, repairRequest: RepairRequestFormGroupInput): void {
    const repairRequestRawValue = this.convertRepairRequestToRepairRequestRawValue({ ...this.getFormDefaults(), ...repairRequest });
    form.reset(
      {
        ...repairRequestRawValue,
        id: { value: repairRequestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RepairRequestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      dateUpdated: currentTime,
    };
  }

  private convertRepairRequestRawValueToRepairRequest(
    rawRepairRequest: RepairRequestFormRawValue | NewRepairRequestFormRawValue
  ): IRepairRequest | NewRepairRequest {
    return {
      ...rawRepairRequest,
      dateCreated: dayjs(rawRepairRequest.dateCreated, DATE_TIME_FORMAT),
      dateUpdated: dayjs(rawRepairRequest.dateUpdated, DATE_TIME_FORMAT),
    };
  }

  private convertRepairRequestToRepairRequestRawValue(
    repairRequest: IRepairRequest | (Partial<NewRepairRequest> & RepairRequestFormDefaults)
  ): RepairRequestFormRawValue | PartialWithRequiredKeyOf<NewRepairRequestFormRawValue> {
    return {
      ...repairRequest,
      dateCreated: repairRequest.dateCreated ? repairRequest.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      dateUpdated: repairRequest.dateUpdated ? repairRequest.dateUpdated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
