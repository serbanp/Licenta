import { Injectable } from '@angular/core';
import { User } from 'src/app/models/user';
import { Observable } from 'rxjs';
import { AppComponent } from 'src/app/app.component';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
    
  private static token:String;
  private static username:String;

  constructor(private http:HttpClient) { }

  public login(user:User): Observable<Object> {
    return this.http.post(AppComponent.serverUrl+"login",user);
  }

  public register(userForm:User): Observable<Object> {
    return this.http.post(AppComponent.serverUrl+"register",userForm);
  }

  public static getToken() {
    return localStorage.getItem("token");
  }
  public static setToken(token:string) {
    localStorage.setItem("token",token);
  }
  public static getUsername() {
    return localStorage.getItem("username");
  }
  public static setUsername(username:string) {
      localStorage.setItem("username",username);
  }
}
