import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEnd, NewEnd } from '../end.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEnd for edit and NewEndFormGroupInput for create.
 */
type EndFormGroupInput = IEnd | PartialWithRequiredKeyOf<NewEnd>;

type EndFormDefaults = Pick<NewEnd, 'id'>;

type EndFormGroupContent = {
  id: FormControl<IEnd['id'] | NewEnd['id']>;
};

export type EndFormGroup = FormGroup<EndFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EndFormService {
  createEndFormGroup(end: EndFormGroupInput = { id: null }): EndFormGroup {
    const endRawValue = {
      ...this.getFormDefaults(),
      ...end,
    };
    return new FormGroup<EndFormGroupContent>({
      id: new FormControl(
        { value: endRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getEnd(form: EndFormGroup): IEnd | NewEnd {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IEnd | NewEnd;
  }

  resetForm(form: EndFormGroup, end: EndFormGroupInput): void {
    const endRawValue = { ...this.getFormDefaults(), ...end };
    form.reset(
      {
        ...endRawValue,
        id: { value: endRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EndFormDefaults {
    return {
      id: null,
    };
  }
}
