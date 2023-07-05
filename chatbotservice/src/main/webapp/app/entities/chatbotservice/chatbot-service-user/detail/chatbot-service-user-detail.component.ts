import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChatbotServiceUser } from '../chatbot-service-user.model';

@Component({
  selector: 'jhi-chatbot-service-user-detail',
  templateUrl: './chatbot-service-user-detail.component.html',
})
export class ChatbotServiceUserDetailComponent implements OnInit {
  chatbotServiceUser: IChatbotServiceUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chatbotServiceUser }) => {
      this.chatbotServiceUser = chatbotServiceUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
