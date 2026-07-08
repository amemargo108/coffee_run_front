import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PreferredOrder } from '../models/PreferredOrderModel';

@Injectable({
    providedIn: 'root'
})
export class PreferredOrderService {
    private apiUrl = 'http://localhost:8080';

    constructor(private http: HttpClient) {}

    getOrdersForDepartmentAndShop(departmentCode: string, coffeeShopId: string): Observable<PreferredOrder[]> {
        return this.http.get<PreferredOrder[]>('${this.apiUrl}/preferred-orders/department/${departmentCode}/shop/${coffeeShopId}');
    }

    saveOrUpdate(employeeId: string, CoffeeShopId: string, MenuOptionIds: string[]): Observable<PreferredOrder> {
        return this.http.post<PreferredOrder>('${this.apiUrl}/preferred-orders/employee/${employeeId}/shop/${coffeeShopId}', MenuOptionIds);
    }

    delete(orderId: string, employeeId: string): Observable<void> {
        return this.http.delete<void>('${this.apiUrl}/preferred-orders/${orderId}/employee/${employeeId}');
    }
}