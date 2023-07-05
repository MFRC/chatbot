import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILoyaltyProgram, NewLoyaltyProgram } from '../loyalty-program.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILoyaltyProgram for edit and NewLoyaltyProgramFormGroupInput for create.
 */
type LoyaltyProgramFormGroupInput = ILoyaltyProgram | PartialWithRequiredKeyOf<NewLoyaltyProgram>;

type LoyaltyProgramFormDefaults = Pick<NewLoyaltyProgram, 'id' | 'loyaltyProgramMember'>;

type LoyaltyProgramFormGroupContent = {
  id: FormControl<ILoyaltyProgram['id'] | NewLoyaltyProgram['id']>;
  loyaltyProgramName: FormControl<ILoyaltyProgram['loyaltyProgramName']>;
  loyaltyProgramMember: FormControl<ILoyaltyProgram['loyaltyProgramMember']>;
  loyaltyProgramNumber: FormControl<ILoyaltyProgram['loyaltyProgramNumber']>;
  loyaltyProgramTier: FormControl<ILoyaltyProgram['loyaltyProgramTier']>;
};

export type LoyaltyProgramFormGroup = FormGroup<LoyaltyProgramFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LoyaltyProgramFormService {
  createLoyaltyProgramFormGroup(loyaltyProgram: LoyaltyProgramFormGroupInput = { id: null }): LoyaltyProgramFormGroup {
    const loyaltyProgramRawValue = {
      ...this.getFormDefaults(),
      ...loyaltyProgram,
    };
    return new FormGroup<LoyaltyProgramFormGroupContent>({
      id: new FormControl(
        { value: loyaltyProgramRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      loyaltyProgramName: new FormControl(loyaltyProgramRawValue.loyaltyProgramName),
      loyaltyProgramMember: new FormControl(loyaltyProgramRawValue.loyaltyProgramMember),
      loyaltyProgramNumber: new FormControl(loyaltyProgramRawValue.loyaltyProgramNumber),
      loyaltyProgramTier: new FormControl(loyaltyProgramRawValue.loyaltyProgramTier),
    });
  }

  getLoyaltyProgram(form: LoyaltyProgramFormGroup): ILoyaltyProgram | NewLoyaltyProgram {
    return form.getRawValue() as ILoyaltyProgram | NewLoyaltyProgram;
  }

  resetForm(form: LoyaltyProgramFormGroup, loyaltyProgram: LoyaltyProgramFormGroupInput): void {
    const loyaltyProgramRawValue = { ...this.getFormDefaults(), ...loyaltyProgram };
    form.reset(
      {
        ...loyaltyProgramRawValue,
        id: { value: loyaltyProgramRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LoyaltyProgramFormDefaults {
    return {
      id: null,
      loyaltyProgramMember: false,
    };
  }
}
