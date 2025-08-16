import {Component, OnInit} from '@angular/core';
import {AuthService} from '../auth.service';
import {ErrorHandlerService} from '../../core/error-handler.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {
  username: string;
  password: string;

  constructor(private authService: AuthService,
              private errorHandlerService: ErrorHandlerService,
              private router: Router) {
  }

  ngOnInit() {
  }

  login() {
    this.authService.login(this.username, this.password)
      .subscribe(() => {
        this.router.navigate(['/dashboard']);
      }, error => {
        this.password = '';
        this.errorHandlerService.handle(error);
      });
  }
}
