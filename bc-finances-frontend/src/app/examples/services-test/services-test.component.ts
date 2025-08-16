import {Component, OnInit} from '@angular/core';
import {CityService} from './city.service';

@Component({
  selector: 'app-services-test',
  templateUrl: './services-test.component.html',
  styleUrls: ['./services-test.component.css']
})
export class ServicesTestComponent implements OnInit {

  cities: any[] = [];
  headers = [
    'Código',
    'Nome',
    ''
  ];

  constructor(private cityService: CityService) {
  }

  ngOnInit() {
    this.list();
  }

  add(name: string) {
    this.cityService.add({name}) // O mesmo que name: name
      .subscribe(() => {
        this.list();
      });
  }

  delete(id: number) {
    this.cityService.delete(id)
      .subscribe(() => {
        alert('Cidade Excluida com sucesso');
        this.list();
      });
  }

  update(city: any) {
    this.cityService.update(city)
      .subscribe(() => {
          alert('Cidade Atualizada com sucesso!');
          this.list();
        },
        () => {
          alert('Erro ao atualizar cidade!!');
        });
  }

  list() {
    this.cityService.list()
      .subscribe(data => {
        this.cities = data;
      });
  }
}
