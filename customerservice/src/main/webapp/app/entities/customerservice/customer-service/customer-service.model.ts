import dayjs from 'dayjs/esm';
import { IFAQs } from 'app/entities/customerservice/fa-qs/fa-qs.model';
import { ICustomerServiceEntity } from 'app/entities/customerservice/customer-service-entity/customer-service-entity.model';
import { ICustomerServiceUser } from 'app/entities/customerservice/customer-service-user/customer-service-user.model';

export interface ICustomerService {
  id: string;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  reportNumber?: number | null;
  faqs?: Pick<IFAQs, 'id'> | null;
  customerServiceEntity?: Pick<ICustomerServiceEntity, 'id'> | null;
  customerServiceUser?: Pick<ICustomerServiceUser, 'id'> | null;
}

export type NewCustomerService = Omit<ICustomerService, 'id'> & { id: null };
