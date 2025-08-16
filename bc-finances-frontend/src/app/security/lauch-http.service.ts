import {HttpClient, HttpHandler} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {from as observableFromPromise, Observable} from 'rxjs';
import {AuthService} from './auth.service';

@Injectable()
export class BcFinancesHttp extends HttpClient {

  constructor(private authService: AuthService,
              handler: HttpHandler) {
    super(handler);
  }

  public delete<T>(url: string, options?: any): Observable<T> {
    return this.doRequest<T>(() => super.delete<T>(url, options));
  }

  public patch<T>(url: string, body: any, options?: any): Observable<T> {
    return this.doRequest<T>(() => super.patch<T>(url, options));
  }

  public head<T>(url: string, options?: any): Observable<T> {
    return this.doRequest<T>(() => super.head<T>(url, options));
  }

  public options<T>(url: string, options?: any): Observable<T> {
    return this.doRequest<T>(() => super.options<T>(url, options));
  }

  public get<T>(url: string, options?: any): Observable<T> {
    return this.doRequest<T>(() => super.get<T>(url, options));
  }

  public post<T>(url: string, body?: any | null, options?: any): Observable<T> {
    return this.doRequest<T>(() => super.post<T>(url, body, options));
  }

  public put<T>(url: string, body: any | null, options?: any): Observable<T> {
    return this.doRequest<T>(() => super.put<T>(url, body, options));
  }

  // tslint:disable-next-line:ban-types
  private doRequest<T>(fn: Function): Observable<T> {
    if (this.authService.isAccessTokenInvalid()) {
      console.log('Requisição HTTP com access token inválido. Obter novo token');
      localStorage.clear();
      const newAccessTokenCall = this.authService.getNewAccessToken()
        .then(() => {
          return fn().toPromise();
        });
      return observableFromPromise(newAccessTokenCall);
    } else {
      return fn();
    }
  }
}
