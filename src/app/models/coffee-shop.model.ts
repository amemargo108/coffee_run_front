export interface CoffeeShop {
    id:  string;
    name: string;
    lacation?: string;
}

export interface MenuOption {
    id: string;
    coffeeShopId: string;
    category: string;
    name: string;
    isAvailable: boolean;
}