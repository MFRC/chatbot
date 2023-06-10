import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/repairservice/customer/customer.model';
import { RepairStatus } from 'app/entities/enumerations/repair-status.model';

export interface IRepairRequest {
  id: string;
  repairRequestId?: string | null;
  customerId?: string | null;
  roomNumber?: string | null;
  description?: string | null;
  status?: RepairStatus | null;
  dateCreated?: dayjs.Dayjs | null;
  dateUpdated?: dayjs.Dayjs | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewRepairRequest = Omit<IRepairRequest, 'id'> & { id: null };
