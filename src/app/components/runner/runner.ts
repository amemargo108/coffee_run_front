import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { CoffeeShopService } from '../../services/CoffeeShopService';
import { OrderRunService } from '../../services/OrderRunService';
import { CoffeeShop, MenuOption } from '../../models/CoffeeShopModel';
import { OrderRun } from '../../models/order-run.model';
import { DepartmentService } from '../../services/DepartmentService';
import { Department } from '../../models/DepartmentModel';

@Component({
  selector: 'app-runner',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './runner.html'
})
export class RunnerComponent implements OnInit {
  coffeeShops: CoffeeShop[] = [];
  selectedShop: CoffeeShop | null = null;
  departments: Department[] = [];
  selectedDeptCode: string = '';
  menuOptions: MenuOption[] = [];
  categories: string[] = [];
  currentRun: OrderRun | null = null;
  tableData: { [key: string]: string }[] = [];
  isPulling = false;
  runnerId = '';
  errorMessage = '';
  successMessage = '';

  constructor(
    private authService: AuthService,
    private coffeeShopService: CoffeeShopService,
    private orderRunService: OrderRunService,
    private departmentService: DepartmentService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.runnerId = this.authService.getEmployeeId() ?? '';
    this.loadCoffeeShops();
    this.loadDepartments();
  }

  loadCoffeeShops(): void {
    this.coffeeShopService.getAll().subscribe({
      next: (shops) => {
        this.coffeeShops = shops;
        this.cdr.detectChanges();
      },
      error: () => this.errorMessage = 'Coffee shops could not be loaded.'
    });
  }

  loadDepartments(): void {
    this.departmentService.getAll().subscribe({
      next: (depts) => {
        this.departments = depts;
        this.cdr.detectChanges();
      },
      error: () => this.errorMessage = 'Departments could not be loaded.'
    });
  }

  selectShop(shop: CoffeeShop): void {
    this.selectedShop = shop;
    this.selectedDeptCode = '';
    this.currentRun = null;
    this.tableData = [];
    this.errorMessage = '';
    this.successMessage = '';

    // Load menu options so we can map orderSummary items to categories for the table
    this.coffeeShopService.getMenuOptions(shop.id).subscribe({
      next: (options) => {
        this.menuOptions = options;
        this.categories = [...new Set(options.map(o => o.category))];
        this.cdr.detectChanges();
      },
      error: () => this.errorMessage = 'Menu options could not be loaded.'
    });
  }

  pullList(): void {
    if (!this.selectedShop || !this.selectedDeptCode) return;
    this.isPulling = true;
    this.currentRun = null;
    this.tableData = [];
    this.errorMessage = '';

    this.orderRunService.pullOrderList(this.runnerId, this.selectedShop.id, this.selectedDeptCode).subscribe({
      next: (run) => {
        this.currentRun = run;
        this.buildTableData(run.items);
        this.isPulling = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Order list could not be pulled.';
        this.isPulling = false;
        this.cdr.detectChanges();
      }
    });
  }

  // Parse each item's orderSummary string against menu options to populate category columns
  buildTableData(items: any[]): void {
    this.tableData = items.map(item => {
      const optionNames = (item.orderSummary as string)
        .split(',')
        .map((s: string) => s.trim());

      const row: { [key: string]: string } = { name: item.employeeName };
      this.categories.forEach(category => {
        const match = this.menuOptions.find(opt =>
          opt.category === category && optionNames.includes(opt.name)
        );
        row[category] = match ? match.name : '—';
      });
      return row;
    });
  }

  backToList(): void {
    this.selectedShop = null;
    this.selectedDeptCode = '';
    this.currentRun = null;
    this.tableData = [];
    this.menuOptions = [];
    this.categories = [];
    this.errorMessage = '';
    this.successMessage = '';
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}
