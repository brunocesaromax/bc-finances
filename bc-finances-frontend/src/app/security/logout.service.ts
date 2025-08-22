import {Injectable} from '@angular/core';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class LogoutService {

  constructor(private auth: AuthService) {
  }

  // Logout client-side para JWT stateless - apenas remove token do localStorage
  logout() {
    this.auth.clearAccessToken();
    return Promise.resolve();
  }
}
