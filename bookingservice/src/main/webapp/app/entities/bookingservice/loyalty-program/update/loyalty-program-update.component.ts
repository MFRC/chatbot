import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { LoyaltyProgramFormService, LoyaltyProgramFormGroup } from './loyalty-program-form.service';
import { ILoyaltyProgram } from '../loyalty-program.model';
import { LoyaltyProgramService } from '../service/loyalty-program.service';

@Component({
  selector: 'jhi-loyalty-program-update',
  templateUrl: './loyalty-program-update.component.html',
})
export class LoyaltyProgramUpdateComponent implements OnInit {
  isSaving = false;
  loyaltyProgram: ILoyaltyProgram | null = null;

  editForm: LoyaltyProgramFormGroup = this.loyaltyProgramFormService.createLoyaltyProgramFormGroup();

  constructor(
    protected loyaltyProgramService: LoyaltyProgramService,
    protected loyaltyProgramFormService: LoyaltyProgramFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loyaltyProgram }) => {
      this.loyaltyProgram = loyaltyProgram;
      if (loyaltyProgram) {
        this.updateForm(loyaltyProgram);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const loyaltyProgram = this.loyaltyProgramFormService.getLoyaltyProgram(this.editForm);
    if (loyaltyProgram.id !== null) {
      this.subscribeToSaveResponse(this.loyaltyProgramService.update(loyaltyProgram));
    } else {
      this.subscribeToSaveResponse(this.loyaltyProgramService.create(loyaltyProgram));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILoyaltyProgram>>): void {
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

  protected updateForm(loyaltyProgram: ILoyaltyProgram): void {
    this.loyaltyProgram = loyaltyProgram;
    this.loyaltyProgramFormService.resetForm(this.editForm, loyaltyProgram);
  }
}
