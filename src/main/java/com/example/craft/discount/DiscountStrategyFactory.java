package com.example.craft.discount;

import com.example.craft.domain.CustomerType;

public class DiscountStrategyFactory {
    public DiscountStrategy getStrategy(CustomerType customerType) {
        if (customerType == CustomerType.STUDENT) {
            return new StudentDiscountStrategy();
        }
        if (customerType == CustomerType.PREMIUM) {
            return new PremiumDiscountStrategy();
        }
        if (customerType == CustomerType.STAFF) {
            return new StaffDiscountStrategy();
        }
        return new NoDiscountStrategy();
    }
}
