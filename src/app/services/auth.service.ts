import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

@Injectable({
    providedIn: 'root'
})

export class AuthService {

    private apiUrl = 'http://localhost:8080';
    private tokenKey = 'jwt_token';

    constructor(private http: HttpClient, private router: Router) {}

    login(email: string, password: string): Observable<string> {
        return this.http.post('${this.apiUrl}/auth/login', { email, password }, { responseType: 'text' })
        .pipe(tap(token => {localStorage.setItem(this.tokenKey, token);

        })
    );
    }


    logout(): void {
        localStorage.removeItem(this.tokenKey);
        this.router.navigate(['/login']);
    }

    getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    isLoggedIn(): boolean {
        const token = this.getToken();
        if (!token) return false;
        return !this.isTokenExpired(token);
    }

    isAdmin(): boolean {
        const token = this.getToken();
        if(!token) return false;
        const payload = this.decodeToken(token);
        return payload?.isAdmin === true;
    }

    getEmployeeId(): string | null {
        const token = this.getToken();
        if (!token) return null;
        const payload = this.decodeToken(token);
        return payload?.employeeId ?? null;
    }

    getDepartmentCode(): string | null {
        const token = this.getToken();
        if (!token) return null;
        const payload = this.decodeToken(token);
        return payload?.departmentCode ?? null;
    }

    private decodeToken(token: string): any {
        try{
            const payload = token.split('.')[1];
            return JSON.parse(atob(payload));
        } catch {
            return null;
        }
    }

    private isTokenExpired(token: string): boolean {
        const payload = this.decodeToken(token);
        if (!payload?.exp) return true;
        return Date.now() >= payload.exp * 1000;
    }
}