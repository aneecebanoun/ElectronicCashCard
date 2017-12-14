package banoun.aneece;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import banoun.aneece.model.Card;
import banoun.aneece.model.Retailer;

public class ElectronicCashCardApplicationTests {

	@Test
	public void sufficientFundTest() throws InterruptedException {

		Card card = new Card("111");
		card.addCredit(10001);

		List<ConcurrentTransactionTest> concurrentTransactionTests = new ArrayList<>();
		final int TRANSACTION_THREADS = 100;
		for (int n = 0; n < TRANSACTION_THREADS; n++) {
			Retailer retailer = new Retailer("R" + n);
			ConcurrentTransactionTest concurrentTransactionTest = new ConcurrentTransactionTest(card, retailer, 100);
			concurrentTransactionTests.add(concurrentTransactionTest);
		}
		
		// junit thread join & wait all transaction threads to finish
	    for(ConcurrentTransactionTest concurrentTransactionTest :  concurrentTransactionTests){
	    	concurrentTransactionTest.thread.join();
	    }

		// test if any of the thread flagged an error
		Boolean noError = !transactionError(concurrentTransactionTests);

		assertTrue("Sufficient Fund Test Case Failure", noError);
		assertTrue("Sufficient Fund Test Case Failure", card.getBalance() == 1);
	}

	// @Test(expected = NoSufficientFundException.class)
	// tried expected does not seem to work in multithreaded case so this is a work around
	@Test
	public void noSufficientFundTest() throws InterruptedException {

		Card card = new Card("111");
		card.addCredit(9999);

		List<ConcurrentTransactionTest> concurrentTransactionTests = new ArrayList<>();
		final int TRANSACTION_THREADS = 100;
		for (int n = 0; n < TRANSACTION_THREADS; n++) {
			Retailer retailer = new Retailer("R" + n);
			ConcurrentTransactionTest concurrentTransactionTest = new ConcurrentTransactionTest(card, retailer, 100);
			concurrentTransactionTests.add(concurrentTransactionTest);
		}

		// junit thread join & wait all transaction threads to finish
	    for(ConcurrentTransactionTest concurrentTransactionTest :  concurrentTransactionTests){
	    	concurrentTransactionTest.thread.join();
	    }

		// test if any of the thread flagged an error
		Boolean error = transactionError(concurrentTransactionTests);

		assertTrue("No Sufficient Fund Test Case Failure", error);
		assertTrue("No Sufficient Fund Test Case Failure", card.getBalance() == 99);
	}

	private Boolean transactionError(List<ConcurrentTransactionTest> concurrentTransactionTests){
		
		Boolean error = false;
		for (ConcurrentTransactionTest concurrentTransactionTest : concurrentTransactionTests) {
			if (concurrentTransactionTest.error) {
				error = true;
				break;
			}
		}
		return error;
	}

}
