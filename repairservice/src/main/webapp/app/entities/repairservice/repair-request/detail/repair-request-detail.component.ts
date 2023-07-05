import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRepairRequest } from '../repair-request.model';

@Component({
  selector: 'jhi-repair-request-detail',
  templateUrl: './repair-request-detail.component.html',
})
export class RepairRequestDetailComponent implements OnInit {
  repairRequest: IRepairRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ repairRequest }) => {
      this.repairRequest = repairRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
