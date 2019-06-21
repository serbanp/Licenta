import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppComponent } from 'src/app/app.component';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  constructor(private http:HttpClient) { }
  
  public getMovies(pageNr:number,pageSize:number,filter:string,sortField:string,sortOrder:string): Observable<Object> {
    console.log(AppComponent.serverUrl+"movies"+filter+"?"+sortField+sortOrder+"page="+pageNr+"&size="+pageSize);
    return this.http.get(AppComponent.serverUrl+"movies"+filter+"?"+sortField+sortOrder+"page="+pageNr+"&size="+pageSize);
  }
  
  public getMoviesByTitleContaining(title:string) {
    return this.http.get(AppComponent.serverUrl+"movies/title-contains/"+title);
  }

  public getRecommendedMovies():Observable<Object> {
    return this.http.get(AppComponent.serverUrl+"recommend/"+AuthService.getUsername());
  }

  public getMovieById(movieId:number): Observable<Object> {
    return this.http.get(AppComponent.serverUrl+"movies/"+movieId);
  }


}
