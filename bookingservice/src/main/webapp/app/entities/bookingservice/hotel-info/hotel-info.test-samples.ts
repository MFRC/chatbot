import { IHotelInfo, NewHotelInfo } from './hotel-info.model';

export const sampleWithRequiredData: IHotelInfo = {
  id: '4a3d65cb-edde-4e73-9fd7-a97cd00e132d',
};

export const sampleWithPartialData: IHotelInfo = {
  id: '588385c0-5d8a-45b0-876f-70a6671d989d',
};

export const sampleWithFullData: IHotelInfo = {
  id: 'cdf3c009-cc00-4ffd-b419-e152985d63b7',
  hotelName: 'mobile',
};

export const sampleWithNewData: NewHotelInfo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
