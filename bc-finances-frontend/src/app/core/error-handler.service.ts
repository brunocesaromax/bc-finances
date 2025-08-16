import {Injectable} from '@angular/core';
import {ToastyService} from 'ng2-toasty';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable()
export class ErrorHandlerService {

  constructor(private toastyService: ToastyService) {
  }

  handle(errorResponse: any) {
    let msg = 'Erro ao processar serviço remoto. Tente novamente.';

    if (errorResponse instanceof HttpErrorResponse && errorResponse.status >= 400 && errorResponse.status < 500) {
      if (errorResponse.error instanceof Array) {
        msg = errorResponse.error[0].msgUser;

      } else if (errorResponse.status === 403) {
        msg = 'Você não tem permissão para executar essa ação';

      } else {
        console.log('Ocorreu um erro:', errorResponse);
      }

    } else if (typeof errorResponse === 'string') {
      msg = errorResponse;
    } else {
      console.log('Ocorreu um erro:', errorResponse);
    }

    this.toastyService.error(msg);
  }
}
