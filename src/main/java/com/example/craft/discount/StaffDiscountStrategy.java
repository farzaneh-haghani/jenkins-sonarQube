package com.example.craft.discount;

import com.example.craft.domain.Order;

public class StaffDiscountStrategy implements DiscountStrategy {

    private static final double STAFF_DISCOUNT_RATE = 0.2;
    private static final double STAFF_BOUNUS_THRESHOLD = 20000;
    private static final double STAFF_BONUS_DISCOUNT = 500;

    @Override
    public int calculateDiscount(Order order, int subtotal) {

        int discount = (int) (subtotal * STAFF_DISCOUNT_RATE);

        if (subtotal > STAFF_BOUNUS_THRESHOLD) {
            discount += STAFF_BONUS_DISCOUNT;
        }
        return Math.min(discount, subtotal);
    }

}
