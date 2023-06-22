import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMessage } from '../message.model';

@Component({
  selector: 'jhi-message-detail',
  templateUrl: './message-detail.component.html',
})
export class MessageDetailComponent implements OnInit {
  message: IMessage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ message }) => {
      this.message = message;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
