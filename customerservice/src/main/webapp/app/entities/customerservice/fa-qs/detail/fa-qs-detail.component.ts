import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFAQs } from '../fa-qs.model';

@Component({
  selector: 'jhi-fa-qs-detail',
  templateUrl: './fa-qs-detail.component.html',
})
export class FAQsDetailComponent implements OnInit {
  fAQs: IFAQs | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fAQs }) => {
      this.fAQs = fAQs;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
