import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFAQs, NewFAQs } from '../fa-qs.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFAQs for edit and NewFAQsFormGroupInput for create.
 */
type FAQsFormGroupInput = IFAQs | PartialWithRequiredKeyOf<NewFAQs>;

type FAQsFormDefaults = Pick<NewFAQs, 'id'>;

type FAQsFormGroupContent = {
  id: FormControl<IFAQs['id'] | NewFAQs['id']>;
};

export type FAQsFormGroup = FormGroup<FAQsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FAQsFormService {
  createFAQsFormGroup(fAQs: FAQsFormGroupInput = { id: null }): FAQsFormGroup {
    const fAQsRawValue = {
      ...this.getFormDefaults(),
      ...fAQs,
    };
    return new FormGroup<FAQsFormGroupContent>({
      id: new FormControl(
        { value: fAQsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getFAQs(form: FAQsFormGroup): IFAQs | NewFAQs {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IFAQs | NewFAQs;
  }

  resetForm(form: FAQsFormGroup, fAQs: FAQsFormGroupInput): void {
    const fAQsRawValue = { ...this.getFormDefaults(), ...fAQs };
    form.reset(
      {
        ...fAQsRawValue,
        id: { value: fAQsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FAQsFormDefaults {
    return {
      id: null,
    };
  }
}
