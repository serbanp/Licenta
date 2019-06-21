import { Component, OnInit } from '@angular/core';
import { User } from '../models/user';
import { AuthService } from '../services/auth/auth.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MustMatch } from './utils/must-match.validator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  private userForm:User;
  private confirmPassword:string;
  private errorMessage:string;
  private encounteredError:boolean;
  private registerForm:FormGroup;
  private isSubmitted=false;

  constructor(private authService:AuthService,
    private router:Router,
    private formBuilder: FormBuilder) {
      this.userForm=new User();
      this.encounteredError=false;
     }

  ngOnInit() {
        this.registerForm=this.formBuilder.group({
          username:['',Validators.required],
          email:['',[Validators.required,Validators.email]],
          password:['',[Validators.required,Validators.minLength(6)]],
          confirmPassword:['',Validators.required]
        }, {
          validator: MustMatch('password', 'confirmPassword')
      });
  }

  get f() { return this.registerForm.controls; }


  onSubmitRegister() {
    this.isSubmitted = true;
    console.log(this.isSubmitted);
    console.log(this.f.username.value);

    if (this.registerForm.invalid) {
      console.log("invalid");
      return;
  }
    this.authService.register(this.registerForm.value).subscribe(data => {
      this.router.navigate(['/login']);
   }, error => {
      this.errorMessage = error.error.message;
      this.encounteredError=true;
   });
  }
}
