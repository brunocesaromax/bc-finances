import { Component, OnInit } from '@angular/core';
import { ReportsService } from '../reports.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-report-transactions',
  templateUrl: './report-transactions.component.html',
  styleUrls: ['./report-transactions.component.css']
})
export class ReportTransactionsComponent implements OnInit {

  startDate: Date;
  endDate: Date;

  constructor(private reportsService: ReportsService,
              private title: Title) {
  }

  ngOnInit() {
    this.title.setTitle('Relatório');
  }

  generate() {
    this.reportsService.reportTransactionsByPerson(this.startDate, this.endDate)
      .then(report => {
        const url = window.URL.createObjectURL(report);
        window.open(url);
      });
  }
}
