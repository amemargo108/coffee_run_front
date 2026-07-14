import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Department } from '../models/DepartmentModel';

@Injectable({ providedIn: 'root' })
export class DepartmentService {
    private apiUrl = 'http://localhost:8080';

    constructor(private http: HttpClient) {}

    getAll(): Observable<Department[]> {
        return this.http.get<Department[]>(`${this.apiUrl}/departments`);
    }
}