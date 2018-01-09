package banoun.aneece;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import banoun.aneece.exceptions.NoSufficientFundException;
import banoun.aneece.model.Card;
import banoun.aneece.model.Retailer;

public class ElectronicCashCardTests {

	@Test(expected = NoSufficientFundException.class)
	public void noSufficientFundExceptionTest() {
		Card gold = new Card("GOLD");
		gold.addCredit(9999);
		for(int n = 0; n < 100; n++){
			new Retailer("R" + n).makeCardSale(gold, 100);
		}
		assertTrue("No Sufficient Fund Exception", 99 == gold.getBalance());
	}
	
	@Test
	public void noSufficientFundTest() {
		Card gold = new Card("GOLD");
		gold.addCredit(9999);
		Boolean noSufficientFundException = false;
		for(int n = 0; n < 100; n++){
			try{
				new Retailer("R" + n).makeCardSale(gold, 100);
			}catch(NoSufficientFundException e){
				 noSufficientFundException = true;
			}
		}
		assertTrue("No Sufficient Fund Exception", 99 == gold.getBalance());
		assertTrue("No Sufficient Fund Exception", noSufficientFundException);
	}

	
	@Test
	public void sufficientFundTest() {
		Card platinum = new Card("PLATINUM");
		platinum.addCredit(10_001);
		for(int n = 0; n < 100; n++){
			new Retailer("R" + n).makeCardSale(platinum, 100);
		}
		assertTrue("Sufficient Fund ERROR", 1 == platinum.getBalance());
	}

}
