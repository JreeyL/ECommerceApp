package dev.ecommerce.payment;

public record PayPal(String email) implements Payment {
	
	@Override
	public void pay(double amount) {

		System.out.println("Processing PayPal payment of " + amount
                + " using account " + email);
	}

}
