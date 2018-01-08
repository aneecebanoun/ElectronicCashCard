package banoun.aneece;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import banoun.aneece.model.Card;
import banoun.aneece.model.Retailer;

public class ElectronicCashCardApplicationTests {

	private CountDownLatch lock = new CountDownLatch(1);
	
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

	    lock.await(7000, TimeUnit.MILLISECONDS);
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

		final int TEST_THREAD_SLEEP_TIME = 30;
		
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
	    
		// testing all threads finished their tasks
		Boolean notDone = true;
		while (notDone) {
			notDone = isThreadsStillWorking(concurrentTransactionTests, TEST_THREAD_SLEEP_TIME);
		}

		lock.await(7000, TimeUnit.MILLISECONDS);
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
	private Boolean isThreadsStillWorking(List<ConcurrentTransactionTest> concurrentTransactionTests, int TEST_THREAD_SLEEP_TIME) throws InterruptedException{

		Boolean threadsWorking = false;
		for (ConcurrentTransactionTest concurrentTransactionTest : concurrentTransactionTests) {
			if (concurrentTransactionTest.thread.isAlive()) {
				Thread.sleep(TEST_THREAD_SLEEP_TIME);
				threadsWorking = true;
				break;
			}
		}
		
		return threadsWorking;
	}

}
