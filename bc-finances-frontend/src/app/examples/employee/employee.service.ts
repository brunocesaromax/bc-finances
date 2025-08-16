import {LogService} from '../log.service';
import {Injectable} from '@angular/core';

@Injectable() // Permitir receber serviços injetados dentro da classe
export class EmployeeService {

  employees = [
    {id: 1, name: 'João'}
  ];

  lastId = 1;

  constructor(private logService: LogService) {
  }

  add(value: string) {
    this.logService.log(`Adicionando nome ${value}...`);

    const employee = {
      id: ++this.lastId,
      name: value
    };

    this.employees.push(employee);
    console.log(JSON.stringify(this.employees));
  }

  list() {
    return this.employees;
  }

}
