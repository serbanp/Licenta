import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppComponent } from 'src/app/app.component';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  public getUsers() : Observable<Object> {
    return this.http.get(AppComponent.serverUrl+"users");
  }

  public getUserByUsername(username:string) : Observable<Object> {
    return this.http.get(AppComponent.serverUrl+"users/"+username);
  }
  
}
