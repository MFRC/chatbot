import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EndFormService, EndFormGroup } from './end-form.service';
import { IEnd } from '../end.model';
import { EndService } from '../service/end.service';
import { IReport } from 'app/entities/customerservice/report/report.model';
import { ReportService } from 'app/entities/customerservice/report/service/report.service';

@Component({
  selector: 'jhi-end-update',
  templateUrl: './end-update.component.html',
})
export class EndUpdateComponent implements OnInit {
  isSaving = false;
  end: IEnd | null = null;

  reportsCollection: IReport[] = [];

  editForm: EndFormGroup = this.endFormService.createEndFormGroup();

  constructor(
    protected endService: EndService,
    protected endFormService: EndFormService,
    protected reportService: ReportService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareReport = (o1: IReport | null, o2: IReport | null): boolean => this.reportService.compareReport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ end }) => {
      this.end = end;
      if (end) {
        this.updateForm(end);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const end = this.endFormService.getEnd(this.editForm);
    if (end.id !== null) {
      this.subscribeToSaveResponse(this.endService.update(end));
    } else {
      this.subscribeToSaveResponse(this.endService.create(end));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnd>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(end: IEnd): void {
    this.end = end;
    this.endFormService.resetForm(this.editForm, end);

    this.reportsCollection = this.reportService.addReportToCollectionIfMissing<IReport>(this.reportsCollection, end.report);
  }

  protected loadRelationshipsOptions(): void {
    this.reportService
      .query({ 'endId.specified': 'false' })
      .pipe(map((res: HttpResponse<IReport[]>) => res.body ?? []))
      .pipe(map((reports: IReport[]) => this.reportService.addReportToCollectionIfMissing<IReport>(reports, this.end?.report)))
      .subscribe((reports: IReport[]) => (this.reportsCollection = reports));
  }
}
