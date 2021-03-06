import {Component} from '@angular/core';
import {Router} from '@angular/router';

import {AuthenticationService} from './service/authentication.service';
import {JwtTokenModel} from './model/jwt-token.model';

@Component({selector: 'app-root', templateUrl: 'app.component.html'})
export class AppComponent {
  currentLogin: JwtTokenModel;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentLogin.subscribe(x => this.currentLogin = x);
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
}
