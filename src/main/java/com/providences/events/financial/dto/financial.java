package com.providences.events.financial.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class financial {

    public static class Response {
        public double totalRevenue;
        public double totalExpenses;
        public double netRevenue;
        public double totalFees;
        public double totalDiscounts;
        public double netProfitMargin;

        public Map<String, Double> expensesByCategory;
        public Map<String, Double> expensesByStatus;
        public List<RevenueBySeat> revenueBySeat;
        public List<Transaction> recentTransactions;
        public List<RevenueByPaymentMethod> revenueByPaymentMethod;
    }

    public static record RevenueBySeat(String seatName, double revenue, int quantity) {
    }

    public static record RevenueByPaymentMethod(String method, double revenue, int quantity) {
    }

    public static record Transaction(
            String id,
            LocalDateTime date,
            String guestName,
            String guestId,
            String seatName,
            double amount,  
            String paymentMethod,
            String paymentStatus) {
    }

}