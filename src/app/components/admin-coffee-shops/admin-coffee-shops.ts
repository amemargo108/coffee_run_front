import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CoffeeShopService } from '../../services/CoffeeShopService';
import { CoffeeShop, MenuOption } from '../../models/CoffeeShopModel';

@Component({
  selector: 'app-admin-coffee-shops',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-coffee-shops.html'
})
export class AdminCoffeeShopsComponent implements OnInit {
  coffeeShops: CoffeeShop[] = [];
  selectedShop: CoffeeShop | null = null;
  menuOptions: MenuOption[] = [];
  shopForm: FormGroup;
  menuForm: FormGroup;
  showMenuForm: boolean = false;
  showShopForm: boolean = false;
  selectedOption: MenuOption | null = null;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private coffeeShopService: CoffeeShopService, private fb: FormBuilder, private router: Router) {
    this.shopForm = this.fb.group({
      name: ['', Validators.required], 
      location: ['']
    });

    this.menuForm = this.fb.group({
      name: ['', Validators.required],
      category: ['', Validators.required],
      isAvailable: [true]
    });
  }

  ngOnInit(): void {
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
    this.showMenuForm = false;
    this.loadMenuOptions(shop.id);
  }

  loadMenuOptions(shopId: string): void {
    this.coffeeShopService.getMenuOptions(shopId).subscribe({
      next: (options) => this.menuOptions = options,
      error: () => this.errorMessage = "The menu options could not be loaded."
    });
  }

  openAddMenuOption(): void {
    this.selectedOption = null;
    this.menuForm.reset({isAvailable:true});
    this.showMenuForm = true;
  }

  saveMenuOption(): void {
    if (!this.selectedShop || this.menuForm.invalid) return;
    const option = {
      ...this.menuForm.value,
      coffeeShopId: this.selectedShop.id
    };

    this.coffeeShopService.addMenuOption(this.selectedShop.id, option).subscribe({
      next: () => {
        this.successMessage = 'Menu option saved.';
        this.showMenuForm = false;
        this.loadMenuOptions(this.selectedShop!.id);
      },
      error: () => this.errorMessage = "This menu option could not be saved."
    });
  }

  deleteMenuOption(optionId: string): void {
    this.coffeeShopService.deleteMenuOption(optionId).subscribe({
      next: () => {
        this.successMessage = 'Menu option deleted.';
        this.loadMenuOptions(this.selectedShop!.id);
      },
      error: () => this.errorMessage = "This menu option could not be deleted."
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}
