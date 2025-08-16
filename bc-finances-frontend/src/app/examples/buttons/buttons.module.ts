import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ButtonBigComponent} from './button-big/button-big.component';
import {ButtonModule} from 'primeng/button';

@NgModule({
  declarations: [ButtonBigComponent],
  exports: [
    ButtonBigComponent
  ],
  imports: [
    CommonModule,
    ButtonModule
  ]
})
export class ButtonsModule { }
