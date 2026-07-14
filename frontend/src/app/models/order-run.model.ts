export interface OrderRunItem {
    id: string;
    employeeName: string;
    orderSummary: string;
}

export interface OrderRun {
    id: string;
    runnerName: string;
    coffeeShopName: string;
    departmentCode: string;
    pulledAt: string;
    items: OrderRunItem[];
}