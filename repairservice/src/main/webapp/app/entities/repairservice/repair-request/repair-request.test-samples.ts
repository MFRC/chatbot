import dayjs from 'dayjs/esm';

import { RepairStatus } from 'app/entities/enumerations/repair-status.model';

import { IRepairRequest, NewRepairRequest } from './repair-request.model';

export const sampleWithRequiredData: IRepairRequest = {
  id: 'd16066fd-57b8-4adb-8457-1b6880dbbcb7',
};

export const sampleWithPartialData: IRepairRequest = {
  id: '977d2544-7943-4180-9d50-a4911ab6fdcd',
  roomNumber: 'Supervisor',
  dateCreated: dayjs('2023-07-05T10:30'),
};

export const sampleWithFullData: IRepairRequest = {
  id: '344ce313-b4c3-4ba2-949e-2311b13d7469',
  repairRequestId: '802dead4-1f6b-43b9-9567-b46b6f95c0ea',
  roomNumber: 'Coordinator clear-thinking',
  description: 'hacking 24/7 International',
  status: RepairStatus['IN_PROGRESS'],
  dateCreated: dayjs('2023-07-04T18:24'),
  dateUpdated: dayjs('2023-07-04T18:16'),
};

export const sampleWithNewData: NewRepairRequest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
