package dev.ecommerce.contracts;

public interface Discountable {

	double applyDiscount(double price);
	
	static double getDefaultRate() {
		return 0;
	}
	
	default double getSeasonalDiscount() {
		return 0.1;
	}
}
