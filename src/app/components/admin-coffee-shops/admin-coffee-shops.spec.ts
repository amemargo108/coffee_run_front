import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCoffeeShops } from './admin-coffee-shops';

describe('AdminCoffeeShops', () => {
  let component: AdminCoffeeShops;
  let fixture: ComponentFixture<AdminCoffeeShops>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminCoffeeShops],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminCoffeeShops);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
