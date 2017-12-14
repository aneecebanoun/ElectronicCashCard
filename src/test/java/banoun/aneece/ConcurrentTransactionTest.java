package banoun.aneece;

import banoun.aneece.exceptions.NoSufficientFundException;
import banoun.aneece.model.Card;
import banoun.aneece.model.Retailer;

public class ConcurrentTransactionTest implements Runnable {

	private Card card;
	private Retailer retailer;
	private Integer amount;
	public boolean error;
	public Thread thread;

	ConcurrentTransactionTest(Card card, Retailer retailer, Integer amount) {
		this.card = card;
		this.retailer = retailer;
		this.amount = amount;
		Thread thread = new Thread(this);
		this.thread = thread;
		thread.start();
	}

	@Override
	public void run() {
				try {
					retailer.makeCardSale(card, amount);
				} catch (NoSufficientFundException e) {
					error = true;
				}
	}

}
