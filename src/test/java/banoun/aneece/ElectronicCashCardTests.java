package banoun.aneece;


import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;

import banoun.aneece.model.Card;
import banoun.aneece.model.Retailer;

@RunWith(ConcurrentTestRunner.class)
public class ElectronicCashCardTests {

	int retailerNumber = 0;

	Card gold = new Card("GOLD");
	Card platinum = new Card("PLATINUM");

	@Before
	public void addInitialBalance() {
		gold.addCredit(9999);
		platinum.addCredit(10001);
	}

	@Test
	@ThreadCount(100)
	public void noSufficientFundTest() {
		Retailer retailer = new Retailer("R" + (retailerNumber++));
			retailer.makeCardSale(gold, 100);	
	}
	
	@Test
	@ThreadCount(100)
	public void sufficientFundTest() {
		Retailer retailer = new Retailer("R" + (retailerNumber++));
			retailer.makeCardSale(platinum, 100);	
	}
	
	@After
	public void allDone() {
		assertTrue("Sufficient Fund ERROR", 1 == platinum.getBalance());
		assertTrue("No Sufficient Fund Exception", 99 == gold.getBalance());
	}

}
