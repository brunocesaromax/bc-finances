import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../security/auth.service';
import {LogoutService} from '../../security/logout.service';
import {ErrorHandlerService} from '../error-handler.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  showMenu = false;

  constructor(public auth: AuthService,
              private logoutService: LogoutService,
              private errorHandlerService: ErrorHandlerService,
              private router: Router) {
  }

  ngOnInit() {
  }

  logout() {
    this.logoutService.logout()
      .then(() => {
        this.router.navigate(['/login']);
      })
      .catch(error => this.errorHandlerService.handle(error));
  }
}
