import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { CoffeeShopService } from '../../services/CoffeeShopService';
import { PreferredOrderService } from '../../services/PreferredOrderService'
import { CoffeeShop, MenuOption } from '../../models/CoffeeShopModel'
import { PreferredOrder } from '../../models/PreferredOrderModel';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-my-order',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './my-order.html',
})
export class MyOrderComponent implements OnInit {
  coffeeShops: CoffeeShop[] = [];
  selectedShop: CoffeeShop | null = null;
  menuOptions: MenuOption[] = [];
  currentOrder: PreferredOrder | null = null;
  selections: { [category: string]: string } = {};
  categories: string[] = [];
  isLoading: boolean = false;
  isSaving: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';
  employeeId: string = '';

  constructor(private authService: AuthService, private coffeeShopService: CoffeeShopService, private preferredOrderService: PreferredOrderService, private router: Router, private changeDetect: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.employeeId = this.authService.getEmployeeId() ?? '';
    this.loadCoffeeShops();
  }

  loadCoffeeShops(): void {
    this.coffeeShopService.getAll().subscribe({
      next: (shops) => {
        this.coffeeShops = shops;
        this.changeDetect.detectChanges();
      },
      error: () => this.errorMessage = "Coffee shops could not be loaded."
    });
  }

  selectShop(shop: CoffeeShop): void {
    this.selectedShop = shop;
    this.selections = {};
    this.currentOrder = null;
    this.successMessage = '';
    this.errorMessage = '';
    this.isLoading = true;

    this.coffeeShopService.getMenuOptions(shop.id).subscribe({
      next: (options) => {
        this.menuOptions = options.filter(o => o.isAvailable || (o as any).is_available);
        this.categories = [...new Set(options.map(o=>o.category))];

        // if an existing order exists already, this loads it
        this.preferredOrderService.getMyOrder(this.employeeId, shop.id).subscribe({
          next: (order) => {
            if (order) {
              this.currentOrder = order;
              this.menuOptions.forEach(opt => {
                if (order.selections.includes(opt.name)) {
                  this.selections[opt.category] = opt.id;
                }
              });
            }
            this.isLoading = false;
            this.changeDetect.detectChanges();
          },
          error: () => {
            this.isLoading = false;
            this.changeDetect.detectChanges();
          }
        });
      },
      error: () => {
        this.errorMessage = "Menu options could not be loaded.";
        this.isLoading = false;
        this.changeDetect.detectChanges();
      }
    });
  }

  backToList(): void {
    this.selectedShop = null;
    this.selections = {};
    this.currentOrder = null;
    this.menuOptions = [];
    this.categories = [];
    this.successMessage = '';
    this.errorMessage = '';
  }

  getOptionsByCategory(category: string): MenuOption[] {
    return this.menuOptions.filter(o => o.category === category);
  }

  saveOrder(): void {
    if (!this.selectedShop) return;
    this.isSaving = true;
    this.successMessage = '';
    this.errorMessage = '';

    const selectedOptionIds = Object.values(this.selections).filter(id => id);

    this.preferredOrderService.saveOrUpdate(this.employeeId, this.selectedShop.id, selectedOptionIds).subscribe({
      next: () => {
        this.isSaving = false;
        this.successMessage = "Order saved!";
        this.changeDetect.detectChanges();
      },
      error: () => {
        this.isSaving = false;
        this.errorMessage = "This order could not be saved.";
        this.changeDetect.detectChanges();
      }
    });
  }

  deleteOrder(): void {
    if (!this.selectedShop || !this.currentOrder) return;
    this.preferredOrderService.delete(this.currentOrder.id, this.employeeId).subscribe({
      next: () => {
        this.currentOrder = null;
        this.selections = {};
        this.successMessage = "Order successfully deleted.";
        this.changeDetect.detectChanges();
      },
      error: () => this.errorMessage = "This order could not be saved."
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}


