import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'MVRC-fe';
  static serverUrl = "http://localhost:8080/";

  //To be changed with live Url
  static clientUrl = "http://localhost:4200/";
}
