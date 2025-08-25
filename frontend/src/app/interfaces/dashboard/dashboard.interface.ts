export interface DashboardMetrics {
  totalSales: number;
  totalOrders: number;
  totalProducts: number;
  totalUsers: number;
  averageOrderValue: number;
  conversionRate: number;
}

export interface SalesChartData {
  labels: string[];
  datasets: {
    label: string;
    data: number[];
    backgroundColor: string;
    borderColor: string;
    borderWidth: number;
  }[];
}

export interface TopProduct {
  id: number;
  name: string;
  totalSold: number;
  revenue: number;
  imageUrl: string;
}

export interface TopCategory {
  id: number;
  name: string;
  totalSold: number;
  revenue: number;
}

export interface RecentOrder {
  id: number;
  orderNumber: string;
  customerName: string;
  total: number;
  status: string;
  createdAt: Date;
}

export interface RevenueData {
  period: string;
  revenue: number;
  orders: number;
  growth: number;
}

export interface DashboardData {
  metrics: DashboardMetrics;
  salesChart: SalesChartData;
  topProducts: TopProduct[];
  topCategories: TopCategory[];
  recentOrders: RecentOrder[];
  revenueData: RevenueData[];
}
