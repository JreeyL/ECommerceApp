package dev.ecommerce.concurrency;

import dev.ecommerce.payment.CreditCard;
import dev.ecommerce.payment.PayPal;
import dev.ecommerce.payment.Payment;

import java.lang.ScopedValue;
import java.util.concurrent.Callable;

public class CheckoutTask implements Callable<Boolean> {

    public static final ScopedValue<String> CURRENT_USER = ScopedValue.newInstance();

    private final Payment payment;
    private final double amount;

    public CheckoutTask(Payment payment, double amount) {
        this.payment = payment;
        this.amount = amount;
    }

    @Override
    public Boolean call() throws Exception {
        String user = CURRENT_USER.get();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        }

        switch (payment) {
            case CreditCard c -> System.out.println(
                    "User " + user + " processing credit card ending in " +
                            c.cardNumber().substring(c.cardNumber().length() - 4) +
                            " for " + amount);
            case PayPal p -> System.out.println(
                    "User " + user + " processing PayPal account " + p.email() +
                            " for " + amount);
        }

        return true;
    }
}
