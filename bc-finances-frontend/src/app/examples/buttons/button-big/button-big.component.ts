import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-button-big',
  templateUrl: './button-big.component.html',
  styleUrls: ['./button-big.component.css']
})
export class ButtonBigComponent implements OnInit {

  @Input() label: string;

  constructor() {
  }

  ngOnInit() {
  }

}
