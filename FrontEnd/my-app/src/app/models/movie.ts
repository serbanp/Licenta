import { Genre } from './genre';
import { Actor } from './actor';
import { Director } from './director';

export class Movie {
    id:number;
    imgSrc:string;
    title:string;
    bio:string;
    genres:Genre[];
    director:Director;
    actors:Actor[];
    createdAt:Date;
    updatedAt:Date;
    rating:number;
    averageRating:number;
    releaseDate:string;
}