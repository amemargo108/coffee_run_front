import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Employee } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-admin-employees',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-employees.html'
})
export class AdminEmployeesComponent implements OnInit {

  employees: Employee[] = [];
  employeeForm: FormGroup;
  selectedEmployee: Employee | null = null;
  showForm: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  departmentCode: string = '';

  constructor(private employeeService: EmployeeService, private fb: FormBuilder, private router: Router) {
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
}

loadEmployees(): void {
  this.employeeService.getByDepartment('ENG').subscribe({
    next: (employees) => this.employees = employees,
    error: () => this.errorMessage = "Employees could not be loaded."
  });
}

openCreateForm(): void {
  this.selectedEmployee = null;
  this.employeeForm.reset({isAdmin:false});
  this.showForm = true;
}

openEditForm(employee: Employee): void {
  this.selectedEmployee = employee;
  this.employeeForm.patchValue({
      name: employee.name,
      email: employee.email,
      password: '',
      departmentCode: employee.departmentCode,
      isAdmin: employee.isAdmin
    });
    this.showForm = true;
  }

  saveEmployee(): void {
    if (this.employeeForm.invalid) return;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.selectedEmployee) {
      this.employeeService.update(this.selectedEmployee.id, this.employeeForm.value).subscribe({
        next: () => {
          this.successMessage = 'Employee was updated.';
          this.showForm = false;
          this.loadEmployees();
        },
        error: () => this.errorMessage = "This employee could not be updated."
      });
    } else {
      this.employeeService.create(this.employeeForm.value).subscribe({
        next: () => {
          this.successMessage= "Employee was created.";
          this.showForm = false;
          this.loadEmployees();
        },
        error: () => this.errorMessage = "This employee could not be created."
      });
    }
  }
  deleteEmployee(id: string): void {
    this.employeeService.delete(id).subscribe({
      next: () => {
        this.successMessage = "Employee was deleted.";
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
