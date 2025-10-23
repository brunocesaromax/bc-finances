import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { of, throwError } from 'rxjs';
import { catchError, mapTo } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LogoutService {

  private readonly logoutUrl = `${environment.apiUrl}/auth/logout`;

  constructor(private httpClient: HttpClient,
              private auth: AuthService) {
  }

  logout() {
    return this.httpClient.delete<void>(this.logoutUrl)
      .pipe(
        mapTo(null),
        catchError(error => {
          if ([401, 403, 404].includes(error?.status)) {
            return of(null);
          }
          return throwError(error);
        })
      )
      .toPromise()
      .finally(() => this.auth.clearAccessToken());
  }
}
