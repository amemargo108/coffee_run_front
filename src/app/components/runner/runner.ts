import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { CoffeeShopService } from '../../services/CoffeeShopService';
import { OrderRunService } from '../../services/OrderRunService';
import { CoffeeShop } from '../../models/CoffeeShopModel';
import { OrderRun } from '../../models/order-run.model';

@Component({
  selector: 'app-runner',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './runner.html'
})

export class RunnerComponent implements OnInit {
  coffeeShops: CoffeeShop[] = [];
  selectedShop: CoffeeShop | null = null;
  currentRun: OrderRun | null = null;
  isPulling: boolean = false;
  errorMessage: string = '';
  runnerId: string = '';
  departmentCode: string = '';

  constructor(private authService: AuthService, private coffeeShopService: CoffeeShopService, private orderRunService: OrderRunService, private router: Router) {}

  ngOnInit(): void {
    this.runnerId = this.authService.getEmployeeId() ?? '';
    this.departmentCode = this.authService.getDepartmentCode() ?? '';
    this.loadCoffeeShops();
  }

  loadCoffeeShops(): void {
    this.coffeeShopService.getAll().subscribe({
      next: (shops) => this.coffeeShops = shops,
      error: () => this.errorMessage = "Could not load coffee shops."
    })
  }

  selectShop(shop: CoffeeShop): void {
    this.selectedShop = shop;
    this.currentRun = null;
    this.errorMessage = '';
  }

  pullList(): void {
    if (!this.selectedShop) return;
    this.isPulling = true;
    this.errorMessage = '';

    this.orderRunService.pullOrderList(
      this.runnerId, this.selectedShop.id, this.departmentCode)
      .subscribe({
        next: (run) => {
          this.currentRun = run;
          this.isPulling = false;
        },
        error: () => {
          this.errorMessage = "Order list could not be pulled.";
          this.isPulling = false;
        }
      });
    }
  
  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}
