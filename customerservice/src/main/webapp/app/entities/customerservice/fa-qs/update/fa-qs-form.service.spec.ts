import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fa-qs.test-samples';

import { FAQsFormService } from './fa-qs-form.service';

describe('FAQs Form Service', () => {
  let service: FAQsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FAQsFormService);
  });

  describe('Service methods', () => {
    describe('createFAQsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFAQsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IFAQs should create a new form with FormGroup', () => {
        const formGroup = service.createFAQsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getFAQs', () => {
      it('should return NewFAQs for default FAQs initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFAQsFormGroup(sampleWithNewData);

        const fAQs = service.getFAQs(formGroup) as any;

        expect(fAQs).toMatchObject(sampleWithNewData);
      });

      it('should return NewFAQs for empty FAQs initial value', () => {
        const formGroup = service.createFAQsFormGroup();

        const fAQs = service.getFAQs(formGroup) as any;

        expect(fAQs).toMatchObject({});
      });

      it('should return IFAQs', () => {
        const formGroup = service.createFAQsFormGroup(sampleWithRequiredData);

        const fAQs = service.getFAQs(formGroup) as any;

        expect(fAQs).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFAQs should not enable id FormControl', () => {
        const formGroup = service.createFAQsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFAQs should disable id FormControl', () => {
        const formGroup = service.createFAQsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
