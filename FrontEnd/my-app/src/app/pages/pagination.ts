import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
  })

export class Pagination {
    private static orderType:string='';
    private static orderBy:string='';
    private static filterBy:string='';
    private static moviesPerPage:number=10;

    constructor() {
    }

    setOrderBy(orderBy:string) {
    if (this.getOrderType()!='') {  
        localStorage.setItem("orderBy",orderBy+"&");
        if (this.getOrderType().indexOf('&')>-1)
            localStorage.setItem("orderType",this.getOrderType().slice(0,-1));
        }
    }

    getOrderBy() {
       return localStorage.getItem("orderBy");
    }

    setOrderType(orderType:string) {
        if (orderType!='') {
            localStorage.setItem("orderType",orderType+'&');
            localStorage.setItem("orderBy",'');
        }
        else 
            localStorage.setItem("orderType",orderType);
    }

    getOrderType() {
        return localStorage.getItem("orderType");
    }

    setFilterBy(filterBy:string) {
        localStorage.setItem("filterBy",filterBy);
    }

    getFilterBy() {
        return localStorage.getItem("filterBy");
    }




}