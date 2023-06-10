import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReport, NewReport } from '../report.model';

export type PartialUpdateReport = Partial<IReport> & Pick<IReport, 'id'>;

export type EntityResponseType = HttpResponse<IReport>;
export type EntityArrayResponseType = HttpResponse<IReport[]>;

@Injectable({ providedIn: 'root' })
export class ReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reports', 'customerservice');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(report: NewReport): Observable<EntityResponseType> {
    return this.http.post<IReport>(this.resourceUrl, report, { observe: 'response' });
  }

  update(report: IReport): Observable<EntityResponseType> {
    return this.http.put<IReport>(`${this.resourceUrl}/${this.getReportIdentifier(report)}`, report, { observe: 'response' });
  }

  partialUpdate(report: PartialUpdateReport): Observable<EntityResponseType> {
    return this.http.patch<IReport>(`${this.resourceUrl}/${this.getReportIdentifier(report)}`, report, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReportIdentifier(report: Pick<IReport, 'id'>): string {
    return report.id;
  }

  compareReport(o1: Pick<IReport, 'id'> | null, o2: Pick<IReport, 'id'> | null): boolean {
    return o1 && o2 ? this.getReportIdentifier(o1) === this.getReportIdentifier(o2) : o1 === o2;
  }

  addReportToCollectionIfMissing<Type extends Pick<IReport, 'id'>>(
    reportCollection: Type[],
    ...reportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reports: Type[] = reportsToCheck.filter(isPresent);
    if (reports.length > 0) {
      const reportCollectionIdentifiers = reportCollection.map(reportItem => this.getReportIdentifier(reportItem)!);
      const reportsToAdd = reports.filter(reportItem => {
        const reportIdentifier = this.getReportIdentifier(reportItem);
        if (reportCollectionIdentifiers.includes(reportIdentifier)) {
          return false;
        }
        reportCollectionIdentifiers.push(reportIdentifier);
        return true;
      });
      return [...reportsToAdd, ...reportCollection];
    }
    return reportCollection;
  }
}
