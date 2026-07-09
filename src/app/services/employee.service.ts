import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee } from '../models/employee.model';

@Injectable({
    providedIn: 'root'
})
export class EmployeeService {
    private apiUrl = 'http://localhost:8080';
    constructor(private http: HttpClient) {}

    getByDepartment(departmentCode: string): Observable<Employee[]> {
        return this.http.get<Employee[]>('${this.apiUrl/employees/department/${departmentCode}');   
    }

    create(employee: any): Observable<Employee> {
        return this.http.post<Employee>('${this.apiUrl}/employees', employee);
    }

    update(id: string, employee: any): Observable<Employee> {
        return this.http.put<Employee>('${this.apiUrl}/employees/${id}', employee);
    }

    delete(id: string): Observable<void> {
        return this.http.delete<void>('${this.apiUrl}/employees/${id}');
    }
 }