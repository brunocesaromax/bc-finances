import { Injectable } from '@angular/core';
import { HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { City, Person, State } from '../core/model';
import { BcFinancesHttp } from '../security/lauch-http.service';
import { environment } from '../../environments/environment';

export class PersonFilter {
  name = null;
  page = 0;
  pageSize = 2;
}

@Injectable()
export class PersonService {

  personsUrl: string;
  statesUrl: string;
  citiesUrl: string;

  constructor(private httpClient: BcFinancesHttp) {
    this.personsUrl = `${environment.apiUrl}/persons`;
    this.statesUrl = `${environment.apiUrl}/states`;
    this.citiesUrl = `${environment.apiUrl}/cities`;
  }

  search(filter: PersonFilter): Observable<any> {
    let params = new HttpParams();

    params = params.set('page', filter.page.toString());
    params = params.set('size', filter.pageSize.toString());

    if (filter.name) {
      params = params.append('name', filter.name);
    }

    return this.httpClient.get(`${this.personsUrl}?pagination`, {params});
  }

  findAll(): Observable<any> {
    return this.httpClient.get(`${this.personsUrl}`);
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete(`${this.personsUrl}/${id}`);
  }

  changeStatus(id: number, currentStatus: boolean): Observable<any> {
    // Necessário acrescentar o content type json explicitamente
    const headers = new HttpHeaders().append('Content-Type', 'application/json');
    return this.httpClient.put(`${this.personsUrl}/${id}/active`, !currentStatus, {headers});
  }

  save(person: Person): Observable<any> {
    return this.httpClient.post(this.personsUrl, person);
  }

  update(person: Person): Observable<any> {
    return this.httpClient.put(`${this.personsUrl}/${person.id}`, person);
  }

  findById(id: number): Observable<any> {
    return this.httpClient.get(`${this.personsUrl}/${id}`);
  }

  listStates(): Observable<State[]> {
    return this.httpClient.get(this.statesUrl);
  }

  listCities(stateId: number): Observable<City[]> {
    let params = new HttpParams();
    params = params.set('stateId', stateId.toString());

    return this.httpClient.get(this.citiesUrl, {params});
  }
}
