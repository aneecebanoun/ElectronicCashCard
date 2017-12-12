package banoun.aneece.model;

import banoun.aneece.exceptions.NoSufficientFundException;

public class Card {

	private final String CARD_NUMBER;
	private Integer balance = 0;

	public Card(String cardNumber) {
		this.CARD_NUMBER = cardNumber;
	}

	public String getCardNumber() {
		return CARD_NUMBER;
	}

	public Integer getBalance() {
		return balance;
	}

	public void spendCredit(Integer credit) throws NoSufficientFundException {

		synchronized (balance) {
			if (credit < 0) {
				return;
			}
			if (balance - credit < 0) {
				throw new NoSufficientFundException();
			}
			this.balance -= credit;
		}
	}

	public void addCredit(Integer credit) {
		synchronized (balance) {
			if (credit < 0) {
				return;
			}
			this.balance += credit;
		}
	}

}
