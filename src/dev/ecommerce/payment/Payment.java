package dev.ecommerce.payment;

public sealed interface Payment permits PayPal, CreditCard {
	
	void pay(double amount);
}
