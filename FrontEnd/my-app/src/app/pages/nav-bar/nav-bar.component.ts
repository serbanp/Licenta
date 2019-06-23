import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {


  private username:string;
  constructor(
    private router:Router
  ) {
      this.username=AuthService.getUsername();
   }

  logout() {
    AuthService.setUsername("");
    AuthService.setToken("");
    this.router.navigate(["/login"]);
  }

  ngOnInit() {
  }

}
