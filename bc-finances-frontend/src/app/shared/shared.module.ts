import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MessageComponent} from './message/message.component';
import { PCalendarLocalDirective } from '../directives/pcalendar-local.directive';

@NgModule({
  declarations: [
    MessageComponent,
    PCalendarLocalDirective
  ],
  imports: [
    CommonModule
  ],
  exports: [
    MessageComponent,
    PCalendarLocalDirective
  ]
})
export class SharedModule {
}
