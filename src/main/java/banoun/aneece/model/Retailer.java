package banoun.aneece.model;

import banoun.aneece.exceptions.NoSufficientFundException;

public class Retailer {

	private Integer totalCredit = 0;
	private String retailerName;

	public Retailer(String retailerName) {
		this.retailerName = retailerName;
	}

	public String getRetailerName() {
		return this.retailerName;
	}

	public Integer getTotalCredit() {
		return this.totalCredit;
	}

	public void makeCardSale(Card card, Integer amount) throws NoSufficientFundException {

		if(amount < 0){
			return;
		}
		synchronized(card.getBalance()){
			card.spendCredit(amount);
		}
		synchronized (this.totalCredit) {
			this.totalCredit += amount;
		}
	}

}
