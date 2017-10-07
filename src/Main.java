
import java.util.Calendar;

import yahoofinance.histquotes.Interval;


public class Main {
    
	public static void main(String[] args) {
		
		Calendar from = Calendar.getInstance();
		from.add(Calendar.YEAR, -5); // from 5 years ago
		Calendar to = Calendar.getInstance();
		
		StockInfo test = new StockInfo("TSLA", from, to, Interval.WEEKLY);
		
		test.PrintHistory();
		
		
		
    }
}