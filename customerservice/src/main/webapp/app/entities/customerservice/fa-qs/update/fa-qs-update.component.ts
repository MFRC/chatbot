import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FAQsFormService, FAQsFormGroup } from './fa-qs-form.service';
import { IFAQs } from '../fa-qs.model';
import { FAQsService } from '../service/fa-qs.service';

@Component({
  selector: 'jhi-fa-qs-update',
  templateUrl: './fa-qs-update.component.html',
})
export class FAQsUpdateComponent implements OnInit {
  isSaving = false;
  fAQs: IFAQs | null = null;

  editForm: FAQsFormGroup = this.fAQsFormService.createFAQsFormGroup();

  constructor(protected fAQsService: FAQsService, protected fAQsFormService: FAQsFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fAQs }) => {
      this.fAQs = fAQs;
      if (fAQs) {
        this.updateForm(fAQs);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fAQs = this.fAQsFormService.getFAQs(this.editForm);
    if (fAQs.id !== null) {
      this.subscribeToSaveResponse(this.fAQsService.update(fAQs));
    } else {
      this.subscribeToSaveResponse(this.fAQsService.create(fAQs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFAQs>>): void {
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

  protected updateForm(fAQs: IFAQs): void {
    this.fAQs = fAQs;
    this.fAQsFormService.resetForm(this.editForm, fAQs);
  }
}
