import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../hotel-info.test-samples';

import { HotelInfoFormService } from './hotel-info-form.service';

describe('HotelInfo Form Service', () => {
  let service: HotelInfoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HotelInfoFormService);
  });

  describe('Service methods', () => {
    describe('createHotelInfoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHotelInfoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            hotelName: expect.any(Object),
            loyaltyProgram: expect.any(Object),
            address: expect.any(Object),
          })
        );
      });

      it('passing IHotelInfo should create a new form with FormGroup', () => {
        const formGroup = service.createHotelInfoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            hotelName: expect.any(Object),
            loyaltyProgram: expect.any(Object),
            address: expect.any(Object),
          })
        );
      });
    });

    describe('getHotelInfo', () => {
      it('should return NewHotelInfo for default HotelInfo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createHotelInfoFormGroup(sampleWithNewData);

        const hotelInfo = service.getHotelInfo(formGroup) as any;

        expect(hotelInfo).toMatchObject(sampleWithNewData);
      });

      it('should return NewHotelInfo for empty HotelInfo initial value', () => {
        const formGroup = service.createHotelInfoFormGroup();

        const hotelInfo = service.getHotelInfo(formGroup) as any;

        expect(hotelInfo).toMatchObject({});
      });

      it('should return IHotelInfo', () => {
        const formGroup = service.createHotelInfoFormGroup(sampleWithRequiredData);

        const hotelInfo = service.getHotelInfo(formGroup) as any;

        expect(hotelInfo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHotelInfo should not enable id FormControl', () => {
        const formGroup = service.createHotelInfoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHotelInfo should disable id FormControl', () => {
        const formGroup = service.createHotelInfoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
