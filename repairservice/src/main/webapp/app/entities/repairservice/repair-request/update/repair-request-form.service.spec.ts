import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../repair-request.test-samples';

import { RepairRequestFormService } from './repair-request-form.service';

describe('RepairRequest Form Service', () => {
  let service: RepairRequestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RepairRequestFormService);
  });

  describe('Service methods', () => {
    describe('createRepairRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRepairRequestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            repairRequestId: expect.any(Object),
            roomNumber: expect.any(Object),
            description: expect.any(Object),
            status: expect.any(Object),
            dateCreated: expect.any(Object),
            dateUpdated: expect.any(Object),
            customer: expect.any(Object),
          })
        );
      });

      it('passing IRepairRequest should create a new form with FormGroup', () => {
        const formGroup = service.createRepairRequestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            repairRequestId: expect.any(Object),
            roomNumber: expect.any(Object),
            description: expect.any(Object),
            status: expect.any(Object),
            dateCreated: expect.any(Object),
            dateUpdated: expect.any(Object),
            customer: expect.any(Object),
          })
        );
      });
    });

    describe('getRepairRequest', () => {
      it('should return NewRepairRequest for default RepairRequest initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRepairRequestFormGroup(sampleWithNewData);

        const repairRequest = service.getRepairRequest(formGroup) as any;

        expect(repairRequest).toMatchObject(sampleWithNewData);
      });

      it('should return NewRepairRequest for empty RepairRequest initial value', () => {
        const formGroup = service.createRepairRequestFormGroup();

        const repairRequest = service.getRepairRequest(formGroup) as any;

        expect(repairRequest).toMatchObject({});
      });

      it('should return IRepairRequest', () => {
        const formGroup = service.createRepairRequestFormGroup(sampleWithRequiredData);

        const repairRequest = service.getRepairRequest(formGroup) as any;

        expect(repairRequest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRepairRequest should not enable id FormControl', () => {
        const formGroup = service.createRepairRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRepairRequest should disable id FormControl', () => {
        const formGroup = service.createRepairRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
