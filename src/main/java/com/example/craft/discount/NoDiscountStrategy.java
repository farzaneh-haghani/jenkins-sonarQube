package com.example.craft.discount;

import com.example.craft.domain.Order;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public int calculateDiscount(Order order, int subtotal) {
        return 0;
    }
}
