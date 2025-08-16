import { Component, OnInit } from '@angular/core';
import { Person, State } from '../../core/model';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { PersonService } from '../person.service';
import { ToastyService } from 'ng2-toasty';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-person-form',
  templateUrl: './person-form.component.html',
  styleUrls: ['./person-form.component.css']
})
export class PersonFormComponent implements OnInit {

  person = new Person();
  states: any[];
  cities: any[];
  stateSelected: number;

  constructor(private errorHandlerService: ErrorHandlerService,
              private personService: PersonService,
              private toastyService: ToastyService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private title: Title) {
  }

  get isEdit() {
    return Boolean(this.person.id);
  }

  ngOnInit() {
    this.title.setTitle('Nova pessoa');

    const personId = this.activatedRoute.snapshot.params.id;

    this.loadStates();

    if (personId) {
      this.loadPerson(personId);
    }
  }

  loadStates() {
    this.personService.listStates()
      .subscribe(listStates => {
        this.states = listStates.map(uf => ({label: uf.name, value: uf.id}));
      }, error => {
        this.errorHandlerService.handle(error);
      });
  }

  loadCities() {
    this.personService.listCities(this.stateSelected)
      .subscribe(listCities => {
        this.cities = listCities.map(city => ({label: city.name, value: city.id}));
      }, error => {
        this.errorHandlerService.handle(error);
      });
  }

  save(transactionForm: NgForm) {
    if (this.isEdit) {
      this.update(transactionForm);
    } else {
      this.add(transactionForm);
    }
  }

  update(personForm: NgForm) {
    this.personService.update(this.person)
      .subscribe(personUpdated => {
          this.person = personUpdated;
          this.updateEditTitle();
          this.toastyService.success('Pessoa atualizada com sucesso!');
        },
        error => this.errorHandlerService.handle(error)
      );
  }

  add(personForm: NgForm) {
    this.personService.save(this.person)
      .subscribe(() => {
          this.toastyService.success('Pessoa adicionanda com sucesso!');
          personForm.reset();
          this.person = new Person();
        },
        error => this.errorHandlerService.handle(error)
      );
  }

  new(personForm: NgForm) {
    personForm.reset();
    this.router.navigate(['/persons/new']);
  }

  private loadPerson(id: number) {
    this.personService.findById(id)
      .subscribe(person => {
          this.person = person;

          this.stateSelected = (this.person.address.city) ?
            this.person.address.city.state.id : null;

          if (this.stateSelected) {
            this.loadCities();
          }

          this.updateEditTitle();
        },
        error => this.errorHandlerService.handle(error));
  }

  private updateEditTitle() {
    this.title.setTitle(`Edição da pessoa de nome: ${this.person.name}`);
  }
}
