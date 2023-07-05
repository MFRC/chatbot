import dayjs from 'dayjs/esm';

import { SenderType } from 'app/entities/enumerations/sender-type.model';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: '4f5a3d40-9bf6-4ec2-ae8d-95e9265434c5',
  content: 'Peso Maine EXE',
  timestamp: dayjs('2023-07-05T03:37'),
  senderType: SenderType['USER'],
};

export const sampleWithPartialData: IMessage = {
  id: '72c5be12-6e39-4af7-bf10-bdfc9ee86a88',
  content: 'programming plug-and-play bypass',
  timestamp: dayjs('2023-07-05T05:05'),
  senderType: SenderType['BOT'],
};

export const sampleWithFullData: IMessage = {
  id: '0412e2fa-d5aa-42be-8e9d-8f7d271648fc',
  content: 'capacitor',
  timestamp: dayjs('2023-07-05T06:59'),
  senderType: SenderType['USER'],
};

export const sampleWithNewData: NewMessage = {
  content: 'website',
  timestamp: dayjs('2023-07-05T15:01'),
  senderType: SenderType['BOT'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
