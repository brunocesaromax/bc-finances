import {Component, OnInit, ViewChild} from '@angular/core';
import {TransactionFilter, TransactionService} from '../transaction.service';
import {LazyLoadEvent} from 'primeng/api';
import {ToastyService} from 'ng2-toasty';
import {ConfirmationService} from 'primeng/api';
import 'rxjs/add/operator/catch';
import {ErrorHandlerService} from '../../core/error-handler.service';
import {Title} from '@angular/platform-browser';
import {AuthService} from '../../security/auth.service';

@Component({
  selector: 'app-transactions-search',
  templateUrl: './transactions-search.component.html',
  styleUrls: ['./transactions-search.component.css']
})

export class TransactionsSearchComponent implements OnInit {

  totalElements = 0;
  filter = new TransactionFilter();
  transactions = [];
  headers = ['Pessoa', 'Descrição', 'Vencimento', 'Pagamento', 'Valor', 'Ações'];

  @ViewChild('table', {static: true}) table;

  constructor(private transactionService: TransactionService,
              private errorHandlerService: ErrorHandlerService,
              private toastyService: ToastyService,
              private confirmationService: ConfirmationService,
              private auth: AuthService,
              private title: Title) {
  }

  ngOnInit() {
    this.title.setTitle('Pesquisa de lançamentos');
    this.search();
  }

  search(page = 0) {
    this.filter.page = page;

    this.transactionService.search(this.filter)
      .subscribe(resp => {
          this.totalElements = resp.totalElements;
          this.transactions = resp.content;
        },
        error => this.errorHandlerService.handle(error)
      );
  }

  changePage(event: LazyLoadEvent) {
    const page = event.first / event.rows;
    this.search(page);
  }

  deleteConfirm(transaction: any) {
    this.confirmationService.confirm({
      message: 'Tem certeza que deseja excluir?',
      accept: () => {
        this.delete(transaction);
      }
    });
  }

  delete(transaction: any) {
    this.transactionService.delete(transaction.id)
      .subscribe(() => {
          this.table.first = 0;
          this.search();
          this.toastyService.success('Lançamento excluído com sucesso!');
        },
        error => this.errorHandlerService.handle(error)
      );
  }
}
