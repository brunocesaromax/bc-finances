import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {BcFinancesHttp} from '../security/lauch-http.service';
import {environment} from '../../environments/environment';

@Injectable()
export class CategoryService {

  categoriesUrl: string;

  constructor(private httpClient: BcFinancesHttp) {
    this.categoriesUrl = `${environment.apiUrl}/categories`;
  }

  findAll(): Observable<any> {
    return this.httpClient.get(`${this.categoriesUrl}?pagination`);
  }
}
