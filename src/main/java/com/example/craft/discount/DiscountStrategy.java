package com.example.craft.discount;

import com.example.craft.domain.Order;

public interface DiscountStrategy {
    int calculateDiscount(Order order, int subtotal);
}
