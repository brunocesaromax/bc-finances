import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from './auth.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private auth: AuthService,
              private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.auth.isAccessTokenInvalid()) {
      console.log('Token inválido ou expirado. Redirecionando para login.');
      this.router.navigate(['/login']);
      return false;
    } else if (route.data.roles && !this.auth.hasAnyPermission(route.data.roles)) {
      this.router.navigate(['/not-authorized']);
      return false;
    }

    return true;
  }

}
