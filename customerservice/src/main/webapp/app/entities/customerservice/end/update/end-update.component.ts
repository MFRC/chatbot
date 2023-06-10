import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EndFormService, EndFormGroup } from './end-form.service';
import { IEnd } from '../end.model';
import { EndService } from '../service/end.service';

@Component({
  selector: 'jhi-end-update',
  templateUrl: './end-update.component.html',
})
export class EndUpdateComponent implements OnInit {
  isSaving = false;
  end: IEnd | null = null;

  editForm: EndFormGroup = this.endFormService.createEndFormGroup();

  constructor(protected endService: EndService, protected endFormService: EndFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ end }) => {
      this.end = end;
      if (end) {
        this.updateForm(end);
      }
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
  }
}
