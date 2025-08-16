import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../categories/category.service';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { PersonService } from '../../persons/person.service';
import { Transaction } from '../../core/model';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { TransactionService } from '../transaction.service';
import { ToastyService } from 'ng2-toasty';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { TOKEN_NAME } from '../../security/auth.service';

@Component({
  selector: 'app-transaction-form',
  templateUrl: './transaction-form.component.html',
  styleUrls: ['./transaction-form.component.css']
})
export class TransactionFormComponent implements OnInit {
  types = [
    {label: 'Receita', value: 'RECIPE'},
    {label: 'Despesa', value: 'EXPENSE'}
  ];

  categories = [];
  persons = [];

  form: FormGroup;

  uploadStarted = false;

  constructor(private categoryService: CategoryService,
              private errorHandlerService: ErrorHandlerService,
              private personService: PersonService,
              private transactionService: TransactionService,
              private toastyService: ToastyService,
              private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder,
              private router: Router,
              private title: Title) {
  }

  get isEdit() {
    return Boolean(this.form.get('id').value);
  }

  get urlUploadAttachment() {
    return this.transactionService.urlUploadAttachment();
  }

  get attachmentName() {
    const attachmentName = this.form.get('attachment').value;

    if (attachmentName) {
      return attachmentName.substring(attachmentName.indexOf('_') + 1, attachmentName.length);
    }

    return '';
  }

  ngOnInit() {
    this.configureForm();
    this.title.setTitle('Novo lançamento');

    const transactionId = this.activatedRoute.snapshot.params.id;

    if (transactionId) {
      this.loadTransaction(transactionId);
    }

    this.loadCategories();
    this.loadPersons();
  }

  configureForm() {
    this.form = this.formBuilder.group({
      id: [],
      type: ['RECIPE', Validators.required],
      dueDay: [null, Validators.required],
      payday: [],
      description: [null, [this.requiredValidate, this.minLengthValidate(5)]],
      value: [null, Validators.required],
      person: this.formBuilder.group({
        id: [null, Validators.required],
        name: []
      }),
      category: this.formBuilder.group({
        id: [null, Validators.required],
        name: []
      }),
      observation: [],
      attachment: [],
      urlAttachment: []
    });
  }

  requiredValidate(input: FormControl) {
    // É possível obter outros campos para validar um certo campo conforme abaixo
    // if (input.root.get('type')) {
    //   console.log(input.root.get('type').value);
    // }
    return input.value ? null : {required: true};
  }

  minLengthValidate(value: number) {
    return (input: AbstractControl) => {
      return (!input.value || input.value.length >= value) ? null : {minlength: {length: value}};
    };
  }

  loadTransaction(id: number) {
    this.transactionService.findById(id)
      .subscribe(transaction => {
          this.transactionService.stringsToDates(Array.of(transaction));
          // this.transaction = transaction;
          this.form.patchValue(transaction);
          this.updateEditTitle();
        },
        error => this.errorHandlerService.handle(error));
  }

  loadCategories() {
    return this.categoryService.findAll()
      .subscribe(categories => {
          this.categories = categories.map(c => ({label: c.name, value: c.id}));
        },
        error => this.errorHandlerService.handle(error)
      );
  }

  loadPersons() {
    return this.personService.findAll()
      .subscribe(persons => {
          this.persons = persons.map(p => ({label: p.name, value: p.id}));
        },
        error => this.errorHandlerService.handle(error)
      );
  }

  save() {
    if (this.isEdit) {
      this.update();
    } else {
      this.add();
    }
  }

  add() {
    this.transactionService.save(this.form.value)
      .subscribe(transactionSaved => {
          this.toastyService.success('Lançamento adicionando com sucesso!');
          // Aplicando navegação imperativa
          this.router.navigate(['/transactions', transactionSaved.id]);
        },
        error => this.errorHandlerService.handle(error)
      );
  }

  update() {
    this.transactionService.update(this.form.value)
      .subscribe(transactionUpdated => {
          this.transactionService.stringsToDates(Array.of(transactionUpdated));
          // this.transaction = transactionUpdated;
          this.form.patchValue(transactionUpdated);
          this.updateEditTitle();
          this.toastyService.success('Lançamento atualizado com sucesso!');
        },
        error => this.errorHandlerService.handle(error)
      );
  }

  new() {
    // Poderia apenas utilizar o routerLink nesse caso
    // transactionForm.reset();
    this.form.reset();

    // Função necessária para não perder o tipo do lançamento
    setTimeout(function() {
      this.transaction = new Transaction();
    }.bind(this), 1);

    this.router.navigate(['/transactions/new']);
  }

  updateEditTitle() {
    this.title.setTitle(`Edição de lançamento: ${this.form.get('description').value}`);
  }

  beforeUploadAttachment(event) {
    if (event && event.xhr) {
      event.xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem(TOKEN_NAME));
    }

    this.uploadStarted = true;
  }

  finishUpload(event: any) {
    const attachment = event.originalEvent.body;

    this.form.patchValue({
      attachment: attachment.name,
      urlAttachment: attachment.url
    });

    this.uploadStarted = false;
  }

  validateFileSize(event: any, maxFileSize: number) {
    if (event.files[0].size > maxFileSize) {
      this.toastyService.error('Envie um anexo de no máximo 10MB');
    }

    this.uploadStarted = false;
  }

  errorUpload(event: any) {
    this.toastyService.error('Erro ao tentar enviar anexo');
    this.uploadStarted = false;
  }

  deleteAttachment() {
    this.form.patchValue({
      attachment: null,
      urlAttachment: null
    });
  }
}
