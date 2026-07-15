import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Department } from '../models/DepartmentModel';
import { environment } from '../../environments/environment';


@Injectable({ providedIn: 'root' })
export class DepartmentService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) {}

    getAll(): Observable<Department[]> {
        return this.http.get<Department[]>(`${this.apiUrl}/departments`);
    }
}