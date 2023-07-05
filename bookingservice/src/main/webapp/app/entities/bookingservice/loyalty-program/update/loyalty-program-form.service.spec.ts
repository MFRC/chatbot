import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../loyalty-program.test-samples';

import { LoyaltyProgramFormService } from './loyalty-program-form.service';

describe('LoyaltyProgram Form Service', () => {
  let service: LoyaltyProgramFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoyaltyProgramFormService);
  });

  describe('Service methods', () => {
    describe('createLoyaltyProgramFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLoyaltyProgramFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            loyaltyProgramName: expect.any(Object),
            loyaltyProgramMember: expect.any(Object),
            loyaltyProgramNumber: expect.any(Object),
            loyaltyProgramTier: expect.any(Object),
          })
        );
      });

      it('passing ILoyaltyProgram should create a new form with FormGroup', () => {
        const formGroup = service.createLoyaltyProgramFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            loyaltyProgramName: expect.any(Object),
            loyaltyProgramMember: expect.any(Object),
            loyaltyProgramNumber: expect.any(Object),
            loyaltyProgramTier: expect.any(Object),
          })
        );
      });
    });

    describe('getLoyaltyProgram', () => {
      it('should return NewLoyaltyProgram for default LoyaltyProgram initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLoyaltyProgramFormGroup(sampleWithNewData);

        const loyaltyProgram = service.getLoyaltyProgram(formGroup) as any;

        expect(loyaltyProgram).toMatchObject(sampleWithNewData);
      });

      it('should return NewLoyaltyProgram for empty LoyaltyProgram initial value', () => {
        const formGroup = service.createLoyaltyProgramFormGroup();

        const loyaltyProgram = service.getLoyaltyProgram(formGroup) as any;

        expect(loyaltyProgram).toMatchObject({});
      });

      it('should return ILoyaltyProgram', () => {
        const formGroup = service.createLoyaltyProgramFormGroup(sampleWithRequiredData);

        const loyaltyProgram = service.getLoyaltyProgram(formGroup) as any;

        expect(loyaltyProgram).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILoyaltyProgram should not enable id FormControl', () => {
        const formGroup = service.createLoyaltyProgramFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLoyaltyProgram should disable id FormControl', () => {
        const formGroup = service.createLoyaltyProgramFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
