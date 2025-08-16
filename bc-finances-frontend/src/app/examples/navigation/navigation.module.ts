import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MenuComponent} from './menu/menu.component';
import {ButtonsModule} from '../buttons/buttons.module';
import {LineComponent} from './line/line.component';

@NgModule({
  declarations: [MenuComponent, LineComponent],
  exports: [
    MenuComponent,
    ButtonsModule,
  ],
  imports: [
    CommonModule,
    ButtonsModule
  ]
})
export class NavigationModule { }
