import { Component, OnInit } from '@angular/core';
import { User } from '../models/user';
import { AuthService } from '../services/auth/auth.service';
import { Router } from '@angular/router';
import { Pagination } from '../pages/pagination';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  private user:User;
  private status:Object;
  private results:Object;
  private errorMessage:string;
  private encounteredError:boolean;
  private header: any;
  private token:String;
  private headers:Headers;

  constructor(
    private authService:AuthService,
    private router:Router,
    private pagination:Pagination
    ) {
    this.user=new User();
    this.encounteredError=false;
   }

  ngOnInit() {
  }

  onSubmitLogin() {
    this.authService.login(this.user).subscribe(data => {
     AuthService.setToken(data['token']);
     AuthService.setUsername(this.user.username);
     this.pagination.setFilterBy("");
     this.pagination.setOrderBy("");
     this.pagination.setOrderType("");
     this.router.navigate(['/movies']);
  }, error => {
     this.errorMessage = error.error.message;
     this.encounteredError=true;
  });
  }
}
