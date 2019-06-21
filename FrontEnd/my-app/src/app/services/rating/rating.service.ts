import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppComponent } from 'src/app/app.component';
import { AuthService } from '../auth/auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})  
export class RatingService {

  constructor(private http: HttpClient) { }

  public getUserRatings(username:string) : Observable<Object> {
      return this.http.get(AppComponent.serverUrl+"ratings/"+username);
  }

  public createUserRating(userId:number,movieId:number,rating:number) : Observable<Object> {
    console.log(userId+" "+movieId+" "+rating);
    console.log(AppComponent.serverUrl+userId+"/"+movieId+"/"+rating," ");
    return this.http.post(AppComponent.serverUrl+"rate/"+userId+"/"+movieId+"/"+rating," ");
  }

  public getAverageRatingForMovie(movieId:number) : Observable<Object> {
    return this.http.get(AppComponent.serverUrl+"ratings/avg/"+movieId);
  }
  
  public getRatingForMovie(username:string,movieId:number) : Observable<Object> {
    return this.http.get(AppComponent.serverUrl+"ratings/"+username+"/"+movieId);
  }
}
