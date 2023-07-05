import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoyaltyProgram } from '../loyalty-program.model';

@Component({
  selector: 'jhi-loyalty-program-detail',
  templateUrl: './loyalty-program-detail.component.html',
})
export class LoyaltyProgramDetailComponent implements OnInit {
  loyaltyProgram: ILoyaltyProgram | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loyaltyProgram }) => {
      this.loyaltyProgram = loyaltyProgram;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
