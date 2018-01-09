package banoun.aneece;


import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;

import banoun.aneece.exceptions.NoSufficientFundException;
import banoun.aneece.model.Card;
import banoun.aneece.model.Retailer;

@RunWith(ConcurrentTestRunner.class)
public class ElectronicCashCardTests {

	boolean noSufficientFundException = false;
	boolean sufficientFund = true;
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
		try{
			retailer.makeCardSale(gold, 100);	
		}catch(NoSufficientFundException e){
			noSufficientFundException = true;
		}
	}
	
	@Test
	@ThreadCount(100)
	public void sufficientFundTest() {
		Retailer retailer = new Retailer("R" + (retailerNumber++));
		try{
			retailer.makeCardSale(platinum, 100);	
		}catch(NoSufficientFundException e){
			sufficientFund = false;
		}
	}
	
	@After
	public void allDone() {
		assertTrue("Sufficient Fund ERROR", sufficientFund);
		assertTrue("Sufficient Fund ERROR", 1 == platinum.getBalance());

		assertTrue("No Sufficient Fund Exception", 99 == gold.getBalance());
		assertTrue("No Sufficient Fund Exception", noSufficientFundException);
	}

}
