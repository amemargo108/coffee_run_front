import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { Employee } from '../../models/employee.model';
import { Department } from '../../models/DepartmentModel';
import { EmployeeService } from '../../services/employee.service';
import { DepartmentService } from '../../services/DepartmentService';

@Component({
  selector: 'app-admin-employees',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './admin-employees.html'
})
export class AdminEmployeesComponent implements OnInit {

  employees: Employee[] = [];
  departments: Department[] = [];
  employeeForm: FormGroup;
  selectedEmployee: Employee | null = null;
  showForm = false;
  searchQuery = '';
  errorMessage = '';
  successMessage = '';

  constructor(
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private fb: FormBuilder,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.employeeForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      departmentCode: ['', Validators.required],
      isAdmin: [false]
    });
  }

  ngOnInit(): void {
    this.loadEmployees();
    this.departmentService.getAll().subscribe({
      next: (depts) => {
        this.departments = depts;
        this.cdr.detectChanges();
      },
      error: () => this.errorMessage = 'Departments could not be loaded.'
    });
  }

  loadEmployees(): void {
    this.employeeService.getAll().subscribe({
      next: (employees) => {
        this.employees = employees;
        this.cdr.detectChanges();
      },
      error: () => this.errorMessage = 'Employees could not be loaded.'
    });
  }

  get filteredEmployees(): Employee[] {
    if (!this.searchQuery.trim()) return this.employees;
    const q = this.searchQuery.toLowerCase();
    return this.employees.filter(e =>
      e.name?.toLowerCase().includes(q) ||
      e.email?.toLowerCase().includes(q) ||
      e.departmentCode?.toLowerCase().includes(q)
    );
  }

  openCreateForm(): void {
    this.selectedEmployee = null;
    this.employeeForm.reset({ isAdmin: false });
    this.employeeForm.get('password')?.setValidators(Validators.required);
    this.employeeForm.get('password')?.updateValueAndValidity();
    this.showForm = true;
    this.successMessage = '';
    this.errorMessage = '';
  }

  openEditForm(employee: Employee): void {
    this.selectedEmployee = employee;
    // Password optional on edit — only update if a new one is entered
    this.employeeForm.get('password')?.clearValidators();
    this.employeeForm.get('password')?.updateValueAndValidity();
    this.employeeForm.patchValue({
      name: employee.name,
      email: employee.email,
      password: '',
      departmentCode: employee.departmentCode,
      isAdmin: employee.isAdmin
    });
    this.showForm = true;
    this.successMessage = '';
    this.errorMessage = '';
  }

  saveEmployee(): void {
    if (this.employeeForm.invalid) return;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.selectedEmployee) {
      this.employeeService.update(this.selectedEmployee.id, this.employeeForm.value).subscribe({
        next: () => {
          this.successMessage = 'Employee updated.';
          this.showForm = false;
          this.loadEmployees();
        },
        error: () => this.errorMessage = 'This employee could not be updated.'
      });
    } else {
      this.employeeService.create(this.employeeForm.value).subscribe({
        next: () => {
          this.successMessage = 'Employee created.';
          this.showForm = false;
          this.loadEmployees();
        },
        error: () => this.errorMessage = 'This employee could not be created.'
      });
    }
  }

  deleteEmployee(id: string): void {
    if (!confirm('Delete this employee?')) return;
    this.employeeService.delete(id).subscribe({
      next: () => {
        this.successMessage = 'Employee deleted.';
        this.loadEmployees();
      },
      error: () => this.errorMessage = 'This employee could not be deleted.'
    });
  }

  cancel(): void {
    this.showForm = false;
    this.selectedEmployee = null;
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}
