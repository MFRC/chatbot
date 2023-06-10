import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../end.test-samples';

import { EndFormService } from './end-form.service';

describe('End Form Service', () => {
  let service: EndFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EndFormService);
  });

  describe('Service methods', () => {
    describe('createEndFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEndFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IEnd should create a new form with FormGroup', () => {
        const formGroup = service.createEndFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getEnd', () => {
      it('should return NewEnd for default End initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEndFormGroup(sampleWithNewData);

        const end = service.getEnd(formGroup) as any;

        expect(end).toMatchObject(sampleWithNewData);
      });

      it('should return NewEnd for empty End initial value', () => {
        const formGroup = service.createEndFormGroup();

        const end = service.getEnd(formGroup) as any;

        expect(end).toMatchObject({});
      });

      it('should return IEnd', () => {
        const formGroup = service.createEndFormGroup(sampleWithRequiredData);

        const end = service.getEnd(formGroup) as any;

        expect(end).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEnd should not enable id FormControl', () => {
        const formGroup = service.createEndFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEnd should disable id FormControl', () => {
        const formGroup = service.createEndFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
