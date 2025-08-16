import {Component, OnInit, ViewChild} from '@angular/core';
import {PersonFilter, PersonService} from '../person.service';
import {LazyLoadEvent} from 'primeng/api';
import {Person} from '../Person';
import {ErrorHandlerService} from '../../core/error-handler.service';
import {ToastyService} from 'ng2-toasty';
import {ConfirmationService} from 'primeng/api';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-persons-search',
  templateUrl: './persons-search.component.html',
  styleUrls: ['./persons-search.component.css']
})
export class PersonsSearchComponent implements OnInit {

  totalElements = 0;
  filter = new PersonFilter();
  persons: Person[] = [];
  headers = ['nome', 'cidade', 'estado', 'status', 'Ações'];
  currentPage = 0;

  @ViewChild('table', {static: true}) table;

  constructor(private personService: PersonService,
              private errorHandlerService: ErrorHandlerService,
              private toastyService: ToastyService,
              private confirmationService: ConfirmationService,
              private title: Title) {
  }

  ngOnInit() {
    this.title.setTitle('Pesquisa de pessoas');
  }

  search(page = 0) {
    this.filter.page = page;

    this.personService.search(this.filter)
      .subscribe(resp => {
        this.totalElements = resp.totalElements;
        this.persons = resp.content.map(person => Object.assign(new Person(), person));
      });
  }

  changePage(event: LazyLoadEvent) {
    const page = event.first / event.rows;
    this.search(page);
    this.currentPage = page;
  }

  deleteConfirm(person: any) {
    this.confirmationService.confirm({
      message: 'Tem certeza que deseja excluir?',
      accept: () => {
        this.delete(person);
      }
    });
  }

  delete(person: any) {
    this.personService.delete(person.id)
      .subscribe(() => {
          this.search();
          this.toastyService.success('Pessoa excluída com sucesso!');
        },
        error => this.errorHandlerService.handle(error)
      );
  }

  changeStatus(person: any) {
    this.personService.changeStatus(person.id, person.active)
      .subscribe(() => {
          this.search(this.currentPage);
          this.toastyService.success(person.active ? 'Pessoa inativada com sucesso!' : 'Pessoa ativada com sucesso!');
        },
        error => this.errorHandlerService.handle(error)
      );
  }
}
