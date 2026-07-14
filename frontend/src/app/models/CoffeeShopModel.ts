export interface CoffeeShop {
    id:  string;
    name: string;
    location?: string;
}

export interface MenuOption {
    id: string;
    coffeeShopId: string;
    category: string;
    name: string;
    isAvailable: boolean;
}