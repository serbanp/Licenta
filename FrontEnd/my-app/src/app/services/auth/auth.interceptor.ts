import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap, catchError } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';



@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}
intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
if (AuthService.getToken()!=null) {
    req = req.clone({
      setHeaders: {
        'Content-Type' : 'application/json; charset=utf-8',
        'Accept'       : 'application/json',
        'Authorization': `Bearer ${AuthService.getToken()}`,
      },
    });
}

return next.handle(req)
    .pipe(catchError((err: any) => {

        if (err instanceof HttpErrorResponse) {
            if (err.status === 401) {
                this.router.navigate(['/login']);
            }
            if (err.status === 404) {
              throw new Error(err.error);
            }
        }
      return new Observable<HttpEvent<any>>();
    }));
  }


}