import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {BcFinancesHttp} from '../../security/lauch-http.service';

@Injectable({
  providedIn: 'root'
})
export class CityService {

  constructor(
    private httpClient: BcFinancesHttp
  ) {
  }

  list(): Observable<any> {
    return this.httpClient.get('http://localhost:3000/cities');
  }

  add(city: any): Observable<any> {
    return this.httpClient.post('http://localhost:3000/cities', city);
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete(`http://localhost:3000/cities/${id}`);
  }

  update(city: any): Observable<any> {
    return this.httpClient.put(`http://localhost:3000/cities/${city.id}`, city);
  }
}
