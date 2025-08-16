import {Injectable} from '@angular/core';
import {BcFinancesHttp} from './lauch-http.service';
import {AuthService} from './auth.service';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LogoutService {

  tokensRevokeUrl: string;

  constructor(private http: BcFinancesHttp,
              private auth: AuthService) {
    this.tokensRevokeUrl = `${environment.apiUrl}/tokens/revoke`;
  }

  // Remoção do cookie que guarda o refresh token e em seguida remoção do access token
  logout() {
    return this.http.delete(this.tokensRevokeUrl, {withCredentials: true})
      .toPromise()
      .then(() => {
        this.auth.clearAccessToken();
      });
  }
}
