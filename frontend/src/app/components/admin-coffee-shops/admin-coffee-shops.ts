import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormsModule } from '@angular/forms';
import { CoffeeShopService } from '../../services/CoffeeShopService';
import { CoffeeShop, MenuOption } from '../../models/CoffeeShopModel';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-coffee-shops',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './admin-coffee-shops.html'
})
export class AdminCoffeeShopsComponent implements OnInit {
  coffeeShops: CoffeeShop[] = [];
  selectedShop: CoffeeShop | null = null;
  menuOptions: MenuOption[] = [];
  categorizedMenuOptions: { category: string; options: MenuOption[] }[] = [];
  shopForm: FormGroup;
  showMenuForm = false;
  showShopForm = false;
  showEditShopForm = false;
  errorMessage: string = '';
  successMessage: string = '';
  newCategory = '';
  newOptionNames: string[] = [''];

  readonly predefinedCategories = ['Drink', 'Temperature', 'Size', 'Milk', 'Syrup', 'Other'];

  constructor(private coffeeShopService: CoffeeShopService, private fb: FormBuilder, private router: Router, private changeDetect: ChangeDetectorRef) {
    this.shopForm = this.fb.group({
      name: ['', Validators.required], 
      location: ['']
    });
  }

  ngOnInit(): void {
    this.loadCoffeeShops();
  }

  loadCoffeeShops(): void {
    this.coffeeShopService.getAll().subscribe({
      next: (shops) => {
        console.log('shops loaded:', shops);
        this.coffeeShops = shops;
        this.changeDetect.detectChanges();
      },
      error: (err) => {
        console.error('load error:', err);
        this.errorMessage = "Coffee shops could not be loaded.";
      }
    });
  }

  openAddShop(): void {
    this.shopForm.reset();
    this.showShopForm = true;
  }

  openEditShop(): void {
      this.shopForm.patchValue({
          name: this.selectedShop!.name,
          location: this.selectedShop!.location
          });
      this.showEditShopForm = true;
    }

  saveShop(): void {
    if(this.shopForm.invalid) return;
    this,this.coffeeShopService.create(this.shopForm.value).subscribe({
      next: () => {
        this.successMessage = "This coffee shop has been added.";
        this.showShopForm = false;
        this.loadCoffeeShops();
      },
      error: () => this.errorMessage = "This coffee shop could not be saved."
    });
  }

  updateShop(): void {
      if (this.shopForm.invalid) return;
      this.coffeeShopService.update(this.selectedShop!.id, this.shopForm.value).subscribe({
          next: () => {
              this.successMessage = "This coffee shop has been updated.";
              this.showEditShopForm = false;
              this.selectedShop = { ...this.selectedShop!, ...this.shopForm.value };
              this.changeDetect.detectChanges();
              },
          error: () => this.errorMessage = "This coffee shop could not be updated."
              });
      }

  selectShop(shop: CoffeeShop): void {
    this.selectedShop = shop;
    this.showMenuForm = false;
    this.successMessage = '';
    this.errorMessage = '';
    this.loadMenuOptions(shop.id);
  }

  backToList(): void {
    this.selectedShop = null;
    this.showMenuForm = false;
    this.showShopForm = false;
    this.successMessage = '';
    this.errorMessage = '';
  }

  deleteShop(shopId: string): void {
    if(!confirm(`Delete "${this.selectedShop?.name}" and all its menu options?`)) return;
    this.coffeeShopService.delete(shopId).subscribe({
      next:() => {
        this.successMessage = 'Coffee shop has been successfully deleted.';
        this.backToList();
        this.loadCoffeeShops();
      },
      error: () => this.errorMessage = "This coffee shop could not be deleted."
    });
  }

  loadMenuOptions(shopId: string): void {
    this.coffeeShopService.getMenuOptions(shopId).subscribe({
      next: (options) => {
        this.menuOptions = options;
        this.buildCategorizedOptions();
        this.changeDetect.detectChanges();
      },
      error: () => this.errorMessage = "The menu options could not be loaded."
    });
  }

  buildCategorizedOptions(): void {
    const grouped = this.menuOptions.reduce((acc, opt) => {
      if (!acc[opt.category]) acc[opt.category] = [];
      acc[opt.category].push(opt);
      return acc;
    },
    {} as { [key: string]: MenuOption[] });

    this.categorizedMenuOptions = Object.entries(grouped).map(([category, options]) => ({
      category, options}));
  }

  openAddMenuOption(): void {
    this.newCategory = '';
    this.newOptionNames = [''];
    this.showMenuForm = true;
  }

  openAddToCategory(category: string): void {
    this.newCategory = '';
    this.newOptionNames = [''];
    this.showMenuForm = true;
  }

  addOptionInput(): void {
    this.newOptionNames.push('');
  }

  removeOptionInput(index: number): void {
    this.newOptionNames.splice(index, 1);
  }

  trackByIndex(index: number): number {
    return index;
  }

  saveCategory(): void {
    if(!this.selectedShop || !this.newCategory) return;
    const validNames = this.newOptionNames.filter(n => n.trim());
    if (!validNames.length) return;

    const saves = validNames.map(name =>
      this.coffeeShopService.addMenuOption(this.selectedShop!.id, {
        name: name.trim(),
        category: this.newCategory,
        isAvailable: true
      })
    );

    forkJoin(saves).subscribe({
      next: () => {
        this.successMessage = `${this.newCategory} options saved.`;
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
