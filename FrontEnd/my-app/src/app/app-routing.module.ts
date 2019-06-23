import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AllMoviesComponent } from './pages/all-movies/all-movies.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';

const routes: Routes = [
  { path: '', redirectTo: '/movies' , pathMatch: 'full'},
  { path: 'movies', component: AllMoviesComponent },
  { path: 'login', component:LoginComponent} ,
  { path: 'register',component:RegisterComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
