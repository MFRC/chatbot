import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChatSession } from '../chat-session.model';

@Component({
  selector: 'jhi-chat-session-detail',
  templateUrl: './chat-session-detail.component.html',
})
export class ChatSessionDetailComponent implements OnInit {
  chatSession: IChatSession | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chatSession }) => {
      this.chatSession = chatSession;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
