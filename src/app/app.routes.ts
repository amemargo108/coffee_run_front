import { Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { AdminGuard } from './admin.guard'
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard';
import { MyOrderComponent } from './components/my-order/my-order';
import { RunnerComponent } from './components/runner/runner';
import { AdminEmployeesComponent } from './components/admin-employees/admin-employees';
import { AdminCoffeeShopsComponent } from './components/admin-coffee-shops/admin-coffee-shops';
import { ReportsComponent } from './components/reports/reports';


export const routes: Routes = [
    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: 'login', component: LoginComponent},
    {path:'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
    {path: 'my-order', component: MyOrderComponent, canActivate: [AuthGuard]},
    {path: 'runner', component: RunnerComponent, canActivate: [AuthGuard]},
    {path: 'admin/employees', component: AdminEmployeesComponent, canActivate: [AuthGuard, AdminGuard]},
    {path: 'admin/coffee-shops', component: AdminCoffeeShopsComponent, canActivate: [AuthGuard, AdminGuard]},
    {path: 'reports', component: ReportsComponent, canActivate: [AuthGuard, AdminGuard]},
    {path: '**', redirectTo: 'login'}
];
