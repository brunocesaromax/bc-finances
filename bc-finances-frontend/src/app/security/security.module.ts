import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SecurityRoutingModule } from './security-routing.module';
import { LoginFormComponent } from './login-form/login-form.component';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AuthGuard } from './auth.guard';
import { LogoutService } from './logout.service';

@NgModule({
  declarations: [LoginFormComponent],
  imports: [
    CommonModule,
    SecurityRoutingModule,
    FormsModule,
    ButtonModule,
    InputTextModule
  ],
  providers: [
    AuthGuard,
    LogoutService
  ]
})
export class SecurityModule {
}
