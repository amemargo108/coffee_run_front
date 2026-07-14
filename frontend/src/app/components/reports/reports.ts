import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { ReportService } from '../../services/ReportService';
import { DepartmentService } from '../../services/DepartmentService';
import { EmployeeService } from '../../services/employee.service';
import { OrderRun } from '../../models/order-run.model';
import { Department } from '../../models/DepartmentModel';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reports.html'
})
export class ReportsComponent implements OnInit {

  runs: OrderRun[] = [];
  selectedRun: OrderRun | null = null;
  filterForm: FormGroup;
  departments: Department[] = [];
  employees: any[] = [];
  errorMessage = '';
  isLoading = false;

  constructor(
    private reportService: ReportService,
    private departmentService: DepartmentService,
    private employeeService: EmployeeService,
    private fb: FormBuilder,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.filterForm = this.fb.group({
      filterType: ['department'],
      departmentCode: [''],
      runnerId: [''],
      startDate: [''],
      endDate: ['']
    });
  }

  ngOnInit(): void {
    this.departmentService.getAll().subscribe({
      next: (depts) => {
        this.departments = depts;
        this.cdr.detectChanges();
      },
      error: () => this.errorMessage = 'Departments could not be loaded.'
    });

    this.employeeService.getAll().subscribe({
      next: (emps) => {
        this.employees = emps;
        this.cdr.detectChanges();
      },
      error: () => this.errorMessage = 'Employees could not be loaded.'
    });
  }

  search(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.runs = [];
    this.selectedRun = null;

    const { filterType, departmentCode, runnerId, startDate, endDate } = this.filterForm.value;

    if (filterType === 'department' && departmentCode) {
      this.reportService.getByDepartment(departmentCode).subscribe({
        next: (runs) => { this.runs = runs; this.isLoading = false; this.cdr.detectChanges(); },
        error: () => { this.errorMessage = 'Reports could not be loaded.'; this.isLoading = false; }
      });
    } else if (filterType === 'runner' && runnerId) {
      this.reportService.getByRunner(runnerId).subscribe({
        next: (runs) => { this.runs = runs; this.isLoading = false; this.cdr.detectChanges(); },
        error: () => { this.errorMessage = 'Reports could not be loaded.'; this.isLoading = false; }
      });
    } else if (filterType === 'dateRange' && startDate && endDate) {
      const start = new Date(startDate).toISOString();
      const end = new Date(endDate).toISOString();
      this.reportService.getByDateRange(start, end).subscribe({
        next: (runs) => { this.runs = runs; this.isLoading = false; this.cdr.detectChanges(); },
        error: () => { this.errorMessage = 'Reports could not be loaded.'; this.isLoading = false; }
      });
    } else {
      this.errorMessage = 'Please select a value to search by.';
      this.isLoading = false;
    }
  }

  selectRun(run: OrderRun): void {
    this.selectedRun = run;
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleString();
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}
