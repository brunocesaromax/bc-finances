import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../dashboard.service';
import { CurrencyPipe } from '@angular/common';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  pieChartData: any;
  lineChartData: any;

  // Para mais detalhes verificar documentação do Chart.js
  options = {
    tooltips: {
      callbacks: {
        // Posição do valor a ser exibido e o dataset
        label: (tooltipItem, data) => {
          const dataset = data.datasets[tooltipItem.datasetIndex];
          const value = dataset.data[tooltipItem.index];
          const label = dataset.label ? (dataset.label + ': ') : '';

          return label + this.currencyPipe.transform(value, 'BRL');
        }
      }
    }
  };

  constructor(private dashboardService: DashboardService,
              private currencyPipe: CurrencyPipe,
              private title: Title) {
  }

  ngOnInit() {
    this.title.setTitle('Dashboard');

    this.configurePieChart();
    this.configureLineChart();
  }

  configurePieChart() {
    this.dashboardService.transactionsByCategory()
      .then(response => {
        this.pieChartData = {
          labels: response.map(d => d.category.name),
          datasets: [
            {
              data: response.map(d => d.total),
              backgroundColor: ['#FF9900', '#109618', '#990099', '#3B3EAC',
                '#0099C6', '#DD4477', '#3366CC', '#DC3912']
            }
          ]
        };
      });
  }

  configureLineChart() {
    this.dashboardService.transactionsByDay()
      .then(response => {
        const daysOfMonth = this.configureDaysOfMonth();
        const recipeTotals = this.totalByDayOfMonth(response.filter(d => d.type === 'RECIPE'), daysOfMonth);
        const expenseTotals = this.totalByDayOfMonth(response.filter(d => d.type === 'EXPENSE'), daysOfMonth);

        this.lineChartData = {
          labels: daysOfMonth,
          datasets: [
            {
              label: 'Receitas',
              data: recipeTotals,
              borderColor: '#3366CC'
            },
            {
              label: 'Despesas',
              data: expenseTotals,
              borderColor: '#D62B00'
            }
          ]
        };
      });
  }

  private configureDaysOfMonth() {
    const referenceMonth = new Date();
    referenceMonth.setMonth(referenceMonth.getMonth() + 1);
    referenceMonth.setDate(0); // Pegando último dia do mês anterior

    const quantityDays = referenceMonth.getDate();
    const days: number[] = [];

    for (let i = 1; i <= quantityDays; i++) {
      days.push(i);
    }

    return days;
  }

  private totalByDayOfMonth(data, daysOfMonth) {
    const totals: number[] = [];
    for (const day of daysOfMonth) {
      let total = 0;

      for (const d of data) {
        if (d.day.getDate() === day) {
          total = d.total;
          break;
        }
      }

      totals.push(total);
    }

    return totals;
  }
}
