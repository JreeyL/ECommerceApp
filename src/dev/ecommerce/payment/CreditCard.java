package dev.ecommerce.payment;

public record CreditCard(String cardNumber, String expiry) implements Payment {

	@Override
	public void pay(double amount) {
		
		System.out.println("Processing credit card payment of " + amount
                + " with card ending in " + cardNumber.substring(cardNumber.length() - 4));
	}

}
