import { Injectable } from '@angular/core';
import { BcFinancesHttp } from '../security/lauch-http.service';
import { environment } from '../../environments/environment';
import * as moment from 'moment';

@Injectable()
export class DashboardService {

  transactionsUrl: string;

  constructor(private httpClient: BcFinancesHttp) {
    this.transactionsUrl = `${environment.apiUrl}/transactions`;
  }

  transactionsByCategory(): Promise<Array<any>> {
    return this.httpClient.get<Array<any>>(`${this.transactionsUrl}/statistics/category`)
      .toPromise();
  }

  transactionsByDay(): Promise<Array<any>> {
    return this.httpClient.get<Array<any>>(`${this.transactionsUrl}/statistics/day`)
      .toPromise()
      .then(response => {
        this.convertStringsToDates(response);
        return response;
      });
  }

  private convertStringsToDates(data: Array<any>) {
    for (const d of data) {
      d.day = moment(d.day, 'YYYY-MM-DD').toDate();
    }
  }
}
