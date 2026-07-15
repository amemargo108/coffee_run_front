import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CoffeeShop, MenuOption } from '../models/CoffeeShopModel';
import { environment } from '../../environments/environment';


@Injectable({
    providedIn: 'root'
})

export class CoffeeShopService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) {}

    getAll(): Observable<CoffeeShop[]> {
        return this.http.get<CoffeeShop[]>(`${this.apiUrl}/coffee-shops`);
    }

    getMenuOptions(coffeeShopId: string): Observable<MenuOption[]> {
        return this.http.get<MenuOption[]>(`${this.apiUrl}/coffee-shops/${coffeeShopId}/menu`);
    }

    addMenuOption(coffeeShopId: string, option: any): Observable<MenuOption> {
        return this.http.post<MenuOption>(`${this.apiUrl}/coffee-shops/${coffeeShopId}/menu`, option);
    }
    update(id: string, data:any):Observable<CoffeeShop> {
        return this.http.put<CoffeeShop>(`${this.apiUrl}/coffee-shops/${id}`, data)};

    create(shop: any): Observable<CoffeeShop> {
        return this.http.post<CoffeeShop>(`${this.apiUrl}/coffee-shops`, shop);
    }

    delete(shopId: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/coffee-shops/${shopId}`)
    }

    deleteMenuOption(optionId: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/coffee-shops/menu/${optionId}`);
    }
}