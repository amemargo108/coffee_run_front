import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CoffeeShop, MenuOption } from '../models/CoffeeShopModel';

@Injectable({
    providedIn: 'root'
})

export class CoffeeShopService {
    private apiUrl = 'http://localhost:8080';

    constructor(private http: HttpClient) {}

    getAll(): Observable<CoffeeShop[]> {
        return this.http.get<CoffeeShop[]>('${this.apiUrl}/coffee-shops');
    }

    getMenuOptions(coffeeShopId: string): Observable<MenuOption[]> {
        return this.http.get<MenuOption[]>('${this.apiUrl}/coffee-shops/${coffeeShopId}/menu');
    }
}