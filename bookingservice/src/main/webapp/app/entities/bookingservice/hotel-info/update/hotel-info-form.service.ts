import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHotelInfo, NewHotelInfo } from '../hotel-info.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHotelInfo for edit and NewHotelInfoFormGroupInput for create.
 */
type HotelInfoFormGroupInput = IHotelInfo | PartialWithRequiredKeyOf<NewHotelInfo>;

type HotelInfoFormDefaults = Pick<NewHotelInfo, 'id'>;

type HotelInfoFormGroupContent = {
  id: FormControl<IHotelInfo['id'] | NewHotelInfo['id']>;
  hotelName: FormControl<IHotelInfo['hotelName']>;
  loyaltyProgram: FormControl<IHotelInfo['loyaltyProgram']>;
  address: FormControl<IHotelInfo['address']>;
};

export type HotelInfoFormGroup = FormGroup<HotelInfoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HotelInfoFormService {
  createHotelInfoFormGroup(hotelInfo: HotelInfoFormGroupInput = { id: null }): HotelInfoFormGroup {
    const hotelInfoRawValue = {
      ...this.getFormDefaults(),
      ...hotelInfo,
    };
    return new FormGroup<HotelInfoFormGroupContent>({
      id: new FormControl(
        { value: hotelInfoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      hotelName: new FormControl(hotelInfoRawValue.hotelName),
      loyaltyProgram: new FormControl(hotelInfoRawValue.loyaltyProgram),
      address: new FormControl(hotelInfoRawValue.address),
    });
  }

  getHotelInfo(form: HotelInfoFormGroup): IHotelInfo | NewHotelInfo {
    return form.getRawValue() as IHotelInfo | NewHotelInfo;
  }

  resetForm(form: HotelInfoFormGroup, hotelInfo: HotelInfoFormGroupInput): void {
    const hotelInfoRawValue = { ...this.getFormDefaults(), ...hotelInfo };
    form.reset(
      {
        ...hotelInfoRawValue,
        id: { value: hotelInfoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): HotelInfoFormDefaults {
    return {
      id: null,
    };
  }
}
