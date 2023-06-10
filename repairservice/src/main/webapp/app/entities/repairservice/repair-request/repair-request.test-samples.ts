import dayjs from 'dayjs/esm';

import { RepairStatus } from 'app/entities/enumerations/repair-status.model';

import { IRepairRequest, NewRepairRequest } from './repair-request.model';

export const sampleWithRequiredData: IRepairRequest = {
  id: 'd16066fd-57b8-4adb-8457-1b6880dbbcb7',
};

export const sampleWithPartialData: IRepairRequest = {
  id: '77d25447-9431-4805-950a-4911ab6fdcd1',
  customerId: 'f074344c-e313-4b4c-bba2-d49e2311b13d',
  status: RepairStatus['IN_PROGRESS'],
  dateUpdated: dayjs('2023-06-10T06:28'),
};

export const sampleWithFullData: IRepairRequest = {
  id: '69802dea-d41f-46b3-b915-67b46b6f95c0',
  repairRequestId: 'ea5f5d42-3ed2-4e40-8eb0-aff3a625cbff',
  customerId: '68e1d317-5821-4115-b7c3-af258102e622',
  roomNumber: 'Dynamic',
  description: 'multi-byte',
  status: RepairStatus['PENDING'],
  dateCreated: dayjs('2023-06-10T01:33'),
  dateUpdated: dayjs('2023-06-10T03:20'),
};

export const sampleWithNewData: NewRepairRequest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
