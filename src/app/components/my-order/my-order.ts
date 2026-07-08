import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { CoffeeShopService } from '../../services/CoffeeShopService';
import { PreferredOrderService } from '../../services/PreferredOrderService'
import { CoffeeShop, MenuOption } from '../../models/CoffeeShopModel'
import { PreferredOrder } from '../../models/PreferredOrderModel';

@Component({
  selector: 'app-my-order',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-order.html',
})
export class MyOrderComponent implements OnInit {
  coffeeShops: CoffeeShop[] = [];
  selectedShop: CoffeeShop | null = null;
  menuOptions: MenuOption[] = [];
  currentOrder: PreferredOrder | null = null;
  selectedOptionIds: string[] = [];
  categories: string[] = [];
  isLoading: boolean = false;
  isSaving: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';
  employeeId: string = '';

  constructor(private authService: AuthService, private coffeeShopService: CoffeeShopService, private preferredOrderService: PreferredOrderService, private router: Router) {}

  ngOnInit(): void {
    this.employeeId = this.authService.getEmployeeId() ?? '';
    this.loadCoffeeShops();
  }

  loadCoffeeShops(): void {
    this.coffeeShopService.getAll().subscribe({
      next: (shops) => this.coffeeShops = shops,
      error: () => this.errorMessage = "Coffee shops could not be loaded."
    });
  }

  selectShop(shop: CoffeeShop): void {
    this.selectedShop = shop;
    this.selectedOptionIds = [];
    this.currentOrder = null;
    this.successMessage = '';
    this.errorMessage = '';
    this.isLoading = true;

    this.coffeeShopService.getMenuOptions(shop.id).subscribe({
      next: (options) => {
        this.menuOptions = options.filter(o => o.isAvailable);
        this.categories = [...new Set(options.map(o=>o.category))];

        // if an existing order exists already, this loads it
        const deptCode = this.authService.getDepartmentCode() ?? '';
        this.preferredOrderService.getOrdersForDepartmentAndShop(deptCode, shop.id).subscribe({
          next: (orders) => {
            const myOrder = orders.find(o => o.employeeName !== null);
            if (myOrder) {
              this.currentOrder = myOrder;
              this.selectedOptionIds = this.menuOptions.filter(o => myOrder.selections.includes(o.name))
              .map(o => o.id);
            }
            this.isLoading = false;
          },
          error: () => this.isLoading = false
        });
      },
      error: () => {
        this.errorMessage = "Menu options could not be loaded.";
        this.isLoading = false;
      }
    });
  }

  getOptionsByCategory(category: string): MenuOption[] {
    return this.menuOptions.filter(o => o.category === category);
  }

  toggleOption(optionId: string): void {
    if (this.selectedOptionIds.includes(optionId)) {
      this.selectedOptionIds = this.selectedOptionIds.filter(id => id !== optionId);
    } else {
      this.selectedOptionIds.push(optionId);
    }
  }

  isSelected(optionId: string): boolean {
    return this.selectedOptionIds.includes(optionId);
  }

  saveOrder(): void {
    if (!this.selectedShop) return;
    this.isSaving = true;
    this.successMessage = '';
    this.errorMessage = '';

    this.preferredOrderService.saveOrUpdate(this.employeeId, this.selectedShop.id, this.selectedOptionIds).subscribe({
      next: () => {
        this.isSaving = false;
        this.successMessage = "Order saved!";
      },
      error: () => {
        this.isSaving = false;
        this.errorMessage = "This order could not be saved.";
      }
    });
  }

  deleteOrder(): void {
    if (!this.selectedShop || !this.currentOrder) return;
    this.preferredOrderService.delete(this.currentOrder.id, this.employeeId).subscribe({
      next: () => {
        this.currentOrder = null;
        this.selectedOptionIds = [];
        this.successMessage = "Order successfully deleted.";
      },
      error: () => this.errorMessage = "This order could not be saved."
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}


