import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEnd } from '../end.model';

@Component({
  selector: 'jhi-end-detail',
  templateUrl: './end-detail.component.html',
})
export class EndDetailComponent implements OnInit {
  end: IEnd | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ end }) => {
      this.end = end;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
