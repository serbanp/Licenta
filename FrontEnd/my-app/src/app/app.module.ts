import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { AllMoviesComponent } from './pages/all-movies/all-movies.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {NgbPaginationModule,NgbRatingModule, NgbButtonsModule, NgbRadioGroup, NgbRadio, NgbCheckBox, NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthInterceptor } from './services/auth/auth.interceptor';
import { RegisterComponent } from './register/register.component';
import { Movie } from './models/movie';


@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    AllMoviesComponent,
    LoginComponent,
    RegisterComponent,
  ],
  imports: [
    NgbPaginationModule,
    NgbButtonsModule,
    NgbRatingModule,
    NgbModalModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule, 
    ReactiveFormsModule
  ],
  providers: [
    {
      provide : HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi   : true,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
