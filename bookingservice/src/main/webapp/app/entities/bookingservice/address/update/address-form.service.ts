import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAddress, NewAddress } from '../address.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAddress for edit and NewAddressFormGroupInput for create.
 */
type AddressFormGroupInput = IAddress | PartialWithRequiredKeyOf<NewAddress>;

type AddressFormDefaults = Pick<NewAddress, 'id'>;

type AddressFormGroupContent = {
  id: FormControl<IAddress['id'] | NewAddress['id']>;
  addressStreet1: FormControl<IAddress['addressStreet1']>;
  addressStreet2: FormControl<IAddress['addressStreet2']>;
  addressCity: FormControl<IAddress['addressCity']>;
  addressStateOrProvince: FormControl<IAddress['addressStateOrProvince']>;
  addressCountry: FormControl<IAddress['addressCountry']>;
  addressZipOrPostalCode: FormControl<IAddress['addressZipOrPostalCode']>;
  addressIsHomeOrBusiness: FormControl<IAddress['addressIsHomeOrBusiness']>;
};

export type AddressFormGroup = FormGroup<AddressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AddressFormService {
  createAddressFormGroup(address: AddressFormGroupInput = { id: null }): AddressFormGroup {
    const addressRawValue = {
      ...this.getFormDefaults(),
      ...address,
    };
    return new FormGroup<AddressFormGroupContent>({
      id: new FormControl(
        { value: addressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      addressStreet1: new FormControl(addressRawValue.addressStreet1),
      addressStreet2: new FormControl(addressRawValue.addressStreet2),
      addressCity: new FormControl(addressRawValue.addressCity),
      addressStateOrProvince: new FormControl(addressRawValue.addressStateOrProvince),
      addressCountry: new FormControl(addressRawValue.addressCountry),
      addressZipOrPostalCode: new FormControl(addressRawValue.addressZipOrPostalCode),
      addressIsHomeOrBusiness: new FormControl(addressRawValue.addressIsHomeOrBusiness),
    });
  }

  getAddress(form: AddressFormGroup): IAddress | NewAddress {
    return form.getRawValue() as IAddress | NewAddress;
  }

  resetForm(form: AddressFormGroup, address: AddressFormGroupInput): void {
    const addressRawValue = { ...this.getFormDefaults(), ...address };
    form.reset(
      {
        ...addressRawValue,
        id: { value: addressRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AddressFormDefaults {
    return {
      id: null,
    };
  }
}
