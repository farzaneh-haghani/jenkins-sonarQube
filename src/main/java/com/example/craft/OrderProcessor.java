package com.example.craft;

import com.example.craft.domain.Customer;
import com.example.craft.domain.CustomerType;
import com.example.craft.domain.Order;
import com.example.craft.domain.OrderItem;

/*
 * Deliberately bad-but-working starter code.
 *
 * It mixes validation, pricing, discount rules, delivery rules, payment,
 * persistence, notification and receipt generation in one class.
 *
 * This version is intentionally slightly worse than it needs to be so that
 * SonarQube, PMD and code review have useful things to find.
 */
public class OrderProcessor {

    public String process(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }

        if (order.getCustomer() == null) {
            throw new IllegalArgumentException("Customer must not be null");
        }

        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Customer customer = order.getCustomer();

        if (customer.getName() == null || customer.getName().trim().length() == 0) {
            throw new IllegalArgumentException("Customer name is invalid");
        }

        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            throw new IllegalArgumentException("Customer email is invalid");
        }

        if (customer.getType() == null) {
            throw new IllegalArgumentException("Customer type is required");
        }

        int subtotal = 0;
        int itemCount = 0;

        for (OrderItem item : order.getItems()) {
            if (item == null) {
                throw new IllegalArgumentException("Order item must not be null");
            }

            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item quantity must be positive");
            }

            if (item.getUnitPricePence() <= 0) {
                throw new IllegalArgumentException("Item price must be positive");
            }

            subtotal = subtotal + item.getQuantity() * item.getUnitPricePence();
            itemCount = itemCount + item.getQuantity();
        }

        int discount = 0;

        if (customer.getType() == CustomerType.STUDENT) {
            discount = (int) (subtotal * 0.15);

            if (subtotal > 10000) {
                discount = discount + 250;
            }

            System.out.println("Student discount applied");
        } else if (customer.getType() == CustomerType.PREMIUM) {
            discount = (int) (subtotal * 0.10);

            if (itemCount > 5) {
                discount = discount + 300;
            }

            System.out.println("Premium discount applied");
        } else if (customer.getType() == CustomerType.STAFF) {
            discount = (int) (subtotal * 0.20);

            if (subtotal > 20000) {
                discount = discount + 500;
            }

            System.out.println("Staff discount applied");
        } else {
            discount = 0;
            System.out.println("No discount applied");
        }

        if (discount > subtotal) {
            discount = subtotal;
        }

        int deliveryFee = 0;

        if (order.getDeliveryType().equalsIgnoreCase("STANDARD")) {
            deliveryFee = 399;

            if (subtotal > 5000) {
                deliveryFee = 0;
            }

            System.out.println("Standard delivery selected");
        } else if (order.getDeliveryType().equalsIgnoreCase("NEXT_DAY")) {
            deliveryFee = 799;

            if (subtotal > 15000) {
                deliveryFee = 499;
            }

            System.out.println("Next day delivery selected");
        } else if (order.getDeliveryType().equalsIgnoreCase("COLLECTION")) {
            deliveryFee = 0;

            if (customer.getPhoneNumber() == null) {
                System.out.println("Collection selected but no phone number was provided");
            }

            System.out.println("Collection selected");
        } else {
            throw new IllegalArgumentException("Unknown delivery type: " + order.getDeliveryType());
        }

        int total = subtotal - discount + deliveryFee;

        if (total <= 0) {
            throw new IllegalStateException("Order total must be positive");
        }

        if (order.getPaymentType().equalsIgnoreCase("CARD")) {
            System.out.println("Taking card payment for £" + formatPounds(total));

            if (total > 100000) {
                System.out.println("Large card payment requires manual review");
            }
        } else if (order.getPaymentType().equalsIgnoreCase("PAYPAL")) {
            System.out.println("Taking PayPal payment for £" + formatPounds(total));

            if (customer.getEmail().endsWith("@example.com")) {
                System.out.println("PayPal payment using test-like email address");
            }
        } else if (order.getPaymentType().equalsIgnoreCase("BANK_TRANSFER")) {
            System.out.println("Creating bank transfer request for £" + formatPounds(total));

            if (total < 1000) {
                System.out.println("Bank transfer for low value order may not be worth processing");
            }
        } else {
            throw new IllegalArgumentException("Unknown payment type: " + order.getPaymentType());
        }

        System.out.println("Saving order " + order.getOrderId());
        System.out.println("Saving order " + order.getOrderId() + " for customer " + customer.getName());

        System.out.println("Sending email to " + customer.getEmail());
        System.out.println("Dear " + customer.getName() + ", your order has been processed.");
        System.out.println("Order " + order.getOrderId() + " total was £" + formatPounds(total));

        if (customer.getPhoneNumber() != null && customer.getPhoneNumber().startsWith("07")) {
            System.out.println("Sending SMS to " + customer.getPhoneNumber());
            System.out.println("Order " + order.getOrderId() + " confirmed by SMS");
        }

        if (customer.getType() == CustomerType.PREMIUM && total > 5000) {
            System.out.println("Sending premium customer follow-up email");
        }

        String receipt = "Receipt\n"
                + "-------\n"
                + "Order: " + order.getOrderId() + "\n"
                + "Customer: " + customer.getName() + "\n"
                + "Subtotal: £" + formatPounds(subtotal) + "\n"
                + "Discount: £" + formatPounds(discount) + "\n"
                + "Delivery: £" + formatPounds(deliveryFee) + "\n"
                + "Total: £" + formatPounds(total) + "\n";

        System.out.println(receipt);
        return receipt;
    }

    private String formatPounds(int pence) {
        return String.format("%.2f", pence / 100.0);
    }

    private boolean isLargeOrder(int total) {
        return total > 50000;
    }
}
