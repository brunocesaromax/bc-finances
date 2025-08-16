import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { Transaction } from '../core/model';
import { BcFinancesHttp } from '../security/lauch-http.service';
import { environment } from '../../environments/environment';

export class TransactionFilter {
  description: string;
  dueDayStart: Date;
  dueDayEnd: Date;
  page = 0;
  pageSize = 4;
}

@Injectable()
export class TransactionService {

  transactionsUrl: string;

  constructor(private httpClient: BcFinancesHttp) {
    this.transactionsUrl = `${environment.apiUrl}/transactions`;
  }

  search(filter: TransactionFilter): Observable<any> {
    let params = new HttpParams({
      fromObject: {
        page: filter.page.toString(),
        size: filter.pageSize.toString()
      }
    });

    if (filter.description) {
      params = params.append('description', filter.description);
    }

    if (filter.dueDayStart) {
      params = params.append('dueDayStart',
        moment(filter.dueDayStart).format('YYYY-MM-DD'));
    }

    if (filter.dueDayEnd) {
      params = params.append('dueDayEnd',
        moment(filter.dueDayEnd).format('YYYY-MM-DD'));
    }

    return this.httpClient.get(`${this.transactionsUrl}?summary`, {params});
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete(`${this.transactionsUrl}/${id}`);
  }

  save(transaction: Transaction): Observable<any> {
    return this.httpClient.post(this.transactionsUrl, transaction);
  }

  update(transaction: Transaction): Observable<any> {
    return this.httpClient.put(`${this.transactionsUrl}/${transaction.id}`, transaction);
  }

  findById(id: number): Observable<any> {
    return this.httpClient.get(`${this.transactionsUrl}/${id}`);
  }

  stringsToDates(transactions: any[]): void {
    transactions.forEach(transaction => {
      if (transaction.payday) {
        transaction.payday = moment(transaction.payday).toDate();
      }

      if (transaction.dueDay) {
        transaction.dueDay = moment(transaction.dueDay).toDate();
      }
    });
  }

  urlUploadAttachment(): string {
    return `${this.transactionsUrl}/attachment`;
  }
}
