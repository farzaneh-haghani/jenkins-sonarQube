package com.example.craft.discount;

import com.example.craft.domain.Order;

public class StudentDiscountStrategy implements DiscountStrategy {

    private static final double STUDENT_DISCOUNT_RATE = 0.15;
    private static final double STUDENT_BONUS_THRESHOLD = 10000;
    private static final double STUDENT_BONUS_DISCOUNT = 250;

    @Override
    public int calculateDiscount(Order order, int subtotal) {

        int discount = (int) (subtotal * STUDENT_DISCOUNT_RATE);

        if (subtotal > STUDENT_BONUS_THRESHOLD) {
            discount += STUDENT_BONUS_DISCOUNT;
        }
        return Math.min(discount, subtotal);
    }

}
