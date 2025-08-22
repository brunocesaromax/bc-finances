import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export const TOKEN_NAME = 'token';

@Injectable()
export class AuthService {

  authLoginUrl: string;
  jwtPayload: any;

  constructor(private httpClient: HttpClient,
              private jwtHelperService: JwtHelperService) {
    this.authLoginUrl = `${environment.apiUrl}/auth/login`;
    this.loadToken();
  }

  login(email: string, password: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    
    const body = {
      email: email,
      password: password
    };

    // Limpando o token atual para acrescentar um novo
    localStorage.clear();

    return this.httpClient.post(this.authLoginUrl, body, {headers})
      .pipe(
        tap((response: any) => this.storeToken(response.accessToken)),
        catchError(exception => {
          if (exception.status === 401) {
            return throwError('Usuário ou senha inválidos!');
          }
          return throwError(exception);
        })
      );
  }


  isAccessTokenInvalid() {
    const token = localStorage.getItem(TOKEN_NAME);
    return !token || this.jwtHelperService.isTokenExpired(token);
  }

  hasPermission(permission: string) {
    return this.jwtPayload && this.jwtPayload.authorities.includes(permission);
  }

  hasAnyPermission(roles: string[]) {
    let result = false;

    roles.forEach(role => {
      if (this.hasPermission(role)) {
        result = true;
      }
    });

    return result;
  }

  clearAccessToken() {
    localStorage.removeItem(TOKEN_NAME);
    this.jwtPayload = null;
  }

  private storeToken(token: string) {
    if (!token) {
      return;
    }
    
    try {
      this.jwtPayload = this.jwtHelperService.decodeToken(token);
      localStorage.setItem(TOKEN_NAME, token);
    } catch (error) {
      console.error('Error decoding JWT token:', error);
    }
  }

  private loadToken() {
    const token = localStorage.getItem(TOKEN_NAME);

    if (token && token !== 'null' && token !== 'undefined') {
      this.storeToken(token);
    }
  }
}
