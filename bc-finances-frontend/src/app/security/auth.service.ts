import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export const TOKEN_NAME = 'token';

@Injectable()
export class AuthService {

  oauthTokenUrl: string;
  jwtPayload: any;

  constructor(private httpClient: HttpClient,
              private jwtHelperService: JwtHelperService) {
    this.oauthTokenUrl = `${environment.apiUrl}/oauth/token`;
    this.loadToken();
  }

  login(username: string, password: string): Observable<any> {
    let headers = new HttpHeaders();
    const body = `username=${username}&password=${password}&grant_type=password`;

    headers = headers.append('Content-Type', 'application/x-www-form-urlencoded');
    // client and secret
    headers = headers.append('Authorization', 'Basic YW5ndWxhcjoxMjM0NTY=');

    // Limpando o token atual para acrescentar um novo
    localStorage.clear();

    // withCredentials = true para requisições cross-site (porta diferente)

    return this.httpClient.post(this.oauthTokenUrl, body, {headers, withCredentials: true})
      .pipe(
        tap((response: any) => this.storeToken(response.access_token)),
        catchError(exception => {
          if (exception.status === 400 && exception.error.error === 'invalid_grant') {
            return throwError('Usuário ou senha inválidos!');
          }
          return throwError(exception);
        })
      );
  }

  getNewAccessToken() {
    // Removendo atual token inválido, para não envia-lo na requisição
    this.clearAccessToken();

    const headers = new HttpHeaders()
      .append('Content-Type', 'application/x-www-form-urlencoded')
      .append('Authorization', 'Basic YW5ndWxhcjoxMjM0NTY=');

    const body = 'grant_type=refresh_token';

    return this.httpClient.post<any>(this.oauthTokenUrl, body, {headers, withCredentials: true})
      .toPromise()
      .then(response => {
        this.storeToken(response.access_token);
        console.log('Novo acess token criado!');
        return Promise.resolve(null);
      }).catch(() => {
        console.log('Erro ao renovar token.');
        return Promise.resolve(null);
      });
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
    this.jwtPayload = this.jwtHelperService.decodeToken(token);

    // LocalStorage permite acessar um objeto de armazenamento que está guardado no navegador do usuário
    localStorage.setItem(TOKEN_NAME, token);
  }

  private loadToken() {
    const token = localStorage.getItem(TOKEN_NAME);

    if (token) {
      this.storeToken(token);
    }
  }
}
