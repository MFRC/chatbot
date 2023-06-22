import { Component, OnInit, Compiler, Injector, NgModuleFactory, Type } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;
  entitiesNavbarItems: any[] = [];
  bookingserviceEntityNavbarItems: any[] = [];
  chatbotserviceEntityNavbarItems: any[] = [];
  repairserviceEntityNavbarItems: any[] = [];
  customerserviceEntityNavbarItems: any[] = [];

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private compiler: Compiler,
    private injector: Injector,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      import('bookingservice/entity-navbar-items').then(
        async ({ EntityNavbarItems: BookingserviceEntityNavbarItems }) => {
          this.bookingserviceEntityNavbarItems = BookingserviceEntityNavbarItems;
          const { LazyTranslationModule } = await import('bookingservice/translation-module');
          this.loadModule(LazyTranslationModule as Type<any>);
        },
        error => {
          // eslint-disable-next-line no-console
          console.log('Error loading bookingservice entities', error);
        }
      );
      import('chatbotservice/entity-navbar-items').then(
        async ({ EntityNavbarItems: ChatbotserviceEntityNavbarItems }) => {
          this.chatbotserviceEntityNavbarItems = ChatbotserviceEntityNavbarItems;
          const { LazyTranslationModule } = await import('chatbotservice/translation-module');
          this.loadModule(LazyTranslationModule as Type<any>);
        },
        error => {
          // eslint-disable-next-line no-console
          console.log('Error loading chatbotservice entities', error);
        }
      );
      import('repairservice/entity-navbar-items').then(
        async ({ EntityNavbarItems: RepairserviceEntityNavbarItems }) => {
          this.repairserviceEntityNavbarItems = RepairserviceEntityNavbarItems;
          const { LazyTranslationModule } = await import('repairservice/translation-module');
          this.loadModule(LazyTranslationModule as Type<any>);
        },
        error => {
          // eslint-disable-next-line no-console
          console.log('Error loading repairservice entities', error);
        }
      );
      import('customerservice/entity-navbar-items').then(
        async ({ EntityNavbarItems: CustomerserviceEntityNavbarItems }) => {
          this.customerserviceEntityNavbarItems = CustomerserviceEntityNavbarItems;
          const { LazyTranslationModule } = await import('customerservice/translation-module');
          this.loadModule(LazyTranslationModule as Type<any>);
        },
        error => {
          // eslint-disable-next-line no-console
          console.log('Error loading customerservice entities', error);
        }
      );
    });
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.loginService.login();
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  private loadModule(moduleType: Type<any>): void {
    const moduleFactory = this.compiler.compileModuleAndAllComponentsSync(moduleType);
    moduleFactory.ngModuleFactory.create(this.injector);
  }
}
