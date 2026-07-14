import { Inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderRun } from '../models/order-run.model';

@Injectable({
    providedIn: 'root'
})
export class OrderRunService {
    private apiUrl = "http://localhost:8080";
    constructor(private http: HttpClient) {}

    pullOrderList(runnerId: string, coffeeShopId: string, departmentCode: string): Observable<OrderRun> {
        return this.http.post<OrderRun>(
                `${this.apiUrl}/order-runs/runner/${runnerId}/shop/${coffeeShopId}/department/${departmentCode}`,
            {}
        );
    }
    getRunById(id: string): Observable<OrderRun> {
        return this.http.get<OrderRun>(`${this.apiUrl}/order-runs/${id}`);
    }
}