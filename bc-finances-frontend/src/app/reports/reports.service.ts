import { Injectable } from '@angular/core';
import { BcFinancesHttp } from '../security/lauch-http.service';
import { environment } from '../../environments/environment';
import { HttpParams } from '@angular/common/http';

import * as moment from 'moment';

@Injectable()
export class ReportsService {

  transactionsUrl: string;

  constructor(private httpClient: BcFinancesHttp) {
    this.transactionsUrl = `${environment.apiUrl}/transactions`;
  }

  reportTransactionsByPerson(start: Date, end: Date) {
    let params = new HttpParams();

    params = params.append('start', moment(start).format('YYYY-MM-DD'));
    params = params.append('end', moment(end).format('YYYY-MM-DD'));

    return this.httpClient.get(`${this.transactionsUrl}/reports/person`,
      {params, responseType: 'blob'})
      .toPromise();
  }
}
