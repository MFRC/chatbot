import { IFAQs, NewFAQs } from './fa-qs.model';

export const sampleWithRequiredData: IFAQs = {
  id: '4c9d3c44-70c3-4b43-9ef2-66473a795c1d',
};

export const sampleWithPartialData: IFAQs = {
  id: '43f83499-03af-483d-9ab6-2c45be49be9f',
  answers: 'Park Lek copy',
  question: 'Kids Mississippi Yen',
};

export const sampleWithFullData: IFAQs = {
  id: 'b08c962f-f4e1-4c7d-94ff-f0e185666f20',
  answers: 'virtual Borders compressing',
  question: 'redefine Loan',
  keyWords: 'Lodge plug-and-play Sausages',
};

export const sampleWithNewData: NewFAQs = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
