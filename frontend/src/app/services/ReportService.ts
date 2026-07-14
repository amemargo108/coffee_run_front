import { Inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderRun } from '../models/order-run.model';

@Injectable({
    providedIn: 'root'
})
export class ReportService {
    private apiUrl = 'http://localhost:8080';

    constructor(private http: HttpClient) {}

    getByRunner(runnerId: string): Observable<OrderRun[]> {
        return this.http.get<OrderRun[]>(`${this.apiUrl}/reports/runner/${runnerId}`);
    }

    getByDepartment(departmentCode: string): Observable<OrderRun[]> {
        return this.http.get<OrderRun[]>(`${this.apiUrl}/reports/department/${departmentCode}`);
    }

    getByDateRange(start: string, end: string): Observable<OrderRun[]> {
        return this.http.get<OrderRun[]>(`${this.apiUrl}/reports/date-range?start=${start}&end=${end}`);
  }
}