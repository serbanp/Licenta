import { Component, OnInit } from '@angular/core';
import { MovieService } from 'src/app/services/movie/movie.service';
import { Movie } from 'src/app/models/movie';
import { UserService } from 'src/app/services/user/user.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { RatingService } from 'src/app/services/rating/rating.service';
import { AppComponent } from 'src/app/app.component';
import { UserMovieRating } from 'src/app/models/userMovieRating';
import { take } from 'rxjs/operators';
import { Pagination } from '../pagination';
import { Genre } from 'src/app/models/genre';
import { GenreService } from 'src/app/services/genre/genre.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { modelGroupProvider } from '@angular/forms/src/directives/ng_model_group';


@Component({
  selector: 'app-all-movies',
  templateUrl: './all-movies.component.html',
  styleUrls: ['./all-movies.component.css']
})
export class AllMoviesComponent implements OnInit {
   movies :  Movie[]=[];
   genres: Genre[]=[];
   movieRequested:Movie;
   movieRatings:Map<number,number>;
   imagesLoaded:boolean;
   currentPage:number;
   noOfTotalMovies:number;
   moviesPerPage:number;
   currentRate:number;
   userId:number;
   sortingTypes:string[]=[];
   selectedGenre:string;
   selectedSortingType:string;
   moviesSearchResult: Movie[]=[];
   foundMovies:boolean;
   modalMovie:Movie;
   recommendedMovies:Movie[]=[];

  constructor(
    private movieService:MovieService,
    private ratingService:RatingService,
    private userService:UserService,
    private genreService:GenreService,
    private pagination:Pagination,
    private modalService: NgbModal
  ) { 
    document.addEventListener('click', this.offClickHandler.bind(this));
    this.currentPage=1;
    this.moviesPerPage=10;
    this.setSelectedGenre();
    this.getGenres();
    this.setSortingTypes();
    this.setSelectedSortingType();
    this.getMoviesForCurrentPage(this.currentPage);
    this.getGenres();
    this.movieRatings=new Map<number,number>();
    this.getRecommendedMovies();
  }

  ngOnInit() {
  }
  
  getCurrentMovies() {
    return this.movies;
  }

  getActorsNames(movie : Movie) {
        let actorNames=[];
        for (let actor of movie.actors) {
         actorNames.push(actor.fullName);
        }
        return actorNames;
  }

  getGenres() {
    this.genreService.getGenres().pipe(take(1)).subscribe(response => {
        this.genres=response as Genre[];
        this.genres.sort((a,b)=>a.title.localeCompare(b.title));
    });
  }
  
  getGenreTitles(movie:Movie) {
      let genreNames=[];
      for (let genre of movie.genres) {
        genreNames.push(genre.title);
      }
      return genreNames;
  }

  getMoviesForCurrentPage(currentPage) {
    this.movieService.getMovies(this.currentPage-1,this.moviesPerPage,this.pagination.getFilterBy(),this.pagination.getOrderType(),this.pagination.getOrderBy()).pipe(take(1)).subscribe(response => {
      this.getCurrentUserId().pipe(take(1)).subscribe(user => {
          this.userId=user['id'];
          this.mapUserRatings().pipe(take(1)).subscribe(data => {
          this.movies=(response['content'] as Movie[]);
          this.noOfTotalMovies=response['totalElements'];
          for (let movieUserRating of data as UserMovieRating[]) {
              this.movieRatings.set(movieUserRating.movieId,movieUserRating.rating);
            }
          this.setMoviesRatings();
          });
      });

    });
  }

  onPageChange(currentPage) {
    this.getMoviesForCurrentPage(currentPage);
  }

  mapUserRatings() {
    return this.ratingService.getUserRatings(AuthService.getUsername());
  }

  setMoviesRatings() {
    for (let movie of this.movies) {
      movie.rating=this.getUserRating(movie.id);
    }
  }

  getUserRating(movieId:number) {
    return this.movieRatings.get(movieId);
  }

  getCurrentUserId() {
    return this.userService.getUserByUsername(AuthService.getUsername());
  }

  editUserRating(movie:Movie) {
      this.ratingService.createUserRating(this.userId,movie.id,movie.rating).subscribe(response => {
        let movieInPage=this.movies.find(movieFound=> movieFound.id===movie.id);
        movieInPage.rating=movie.rating;
      })
    }

  getDirectorFullName(movie:Movie) {
      return movie.director.fullName;
  }

  getCurrentPage() {
    return this.currentPage;
  }

  getReleaseYear(date:string) {
    if (date!=undefined)
    return date.split("-")[0];
  }

  setSelectedGenre() {
    if (this.pagination.getFilterBy()=='') {
      this.selectedGenre="All movies";
    }
    else this.selectedGenre=this.pagination.getFilterBy().split('/')[2];
  }

  changeGenreView(genreTitle:string) {
    if (genreTitle=='')
      this.pagination.setFilterBy('');
    else
      this.pagination.setFilterBy("/genres/"+genreTitle);
    this.setSelectedGenre();
    this.getMoviesForCurrentPage(1);
  }

  setSortingTypes() {
    this.sortingTypes.push("Default");
    this.sortingTypes.push("Release Date");
    this.sortingTypes.push("Rating");
    this.sortingTypes.push("Title")
  }

  changeSortingType(sortingType:string) {
    if (sortingType=='Default') {
      this.pagination.setOrderType('');
    }
    else if (sortingType=='Release Date') {
      this.pagination.setOrderType('sort=releaseDate')
    }
    else if (sortingType=="Rating") {
      this.pagination.setOrderType('sort=averageRating')
    }
    else if (sortingType=='Title') {
      this.pagination.setOrderType('sort=title');
    }
    this.setSelectedSortingType();
    this.getMoviesForCurrentPage(1);
  }

  changeSortingOrder(sortingOrder:string) {
    if (sortingOrder=='asc') {
      this.pagination.setOrderBy(',asc');
    }
    else if (sortingOrder=='desc') {
        this.pagination.setOrderBy(',desc');
    }
    this.getMoviesForCurrentPage(1);
  }


  
  setSelectedSortingType() {
    if(this.pagination.getOrderType()=='')
      this.selectedSortingType='Default';
    else if (this.pagination.getOrderType().startsWith('sort=releaseDate'))
      this.selectedSortingType='Release date';
    else if (this.pagination.getOrderType().startsWith('sort=averageRating'))
      this.selectedSortingType='Rating';
    else if (this.pagination.getOrderType().startsWith('sort=title'))
      this.selectedSortingType='Title';
  }

  onSearchChange(searchValue : string ) { 
    if (searchValue.length>2) {
      this.movieService.getMoviesByTitleContaining(searchValue).pipe(take(1)).subscribe(response => {
        this.foundMovies=true;
        this.moviesSearchResult=response as Movie[];
      })
    }
    else this.foundMovies=false;
  }

  openModal(movie:Movie) {  
    var modal = document.getElementById("myModal");
    this.ratingService.getRatingForMovie(AuthService.getUsername(),movie.id).subscribe(response => {
      console.log("TOT AICEA INTRU");
      let movieUserRating=response as UserMovieRating;
      movie.rating=movieUserRating.rating;
      modal.style.display = "block";
      this.foundMovies=false;
      this.modalMovie=movie;
    },(error) => {
      modal.style.display = "block";
      this.foundMovies=false;
      this.modalMovie=movie;
    });
  }


  offClickHandler(event:any) {
    var modal = document.getElementById("myModal");
    if (event.target==modal) {
      console.log("HAHAHADDADA");
      modal.style.display="none";
    }
  }   

  getRecommendedMovies() {
    console.log("intru");
    this.movieService.getRecommendedMovies().pipe(take(1)).subscribe(response => {
      let recommendations=response as object[];
      for (let rec of recommendations) {
          this.movieService.getMovieById(rec['itemID']).pipe(take(1)).subscribe(movie => {
            this.recommendedMovies.push(movie as Movie);
          });      
    }
  })  
}


}
