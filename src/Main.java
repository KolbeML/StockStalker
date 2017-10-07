
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;


public class Main {
    
	public static void main(String[] args) {
		
		Calendar from = Calendar.getInstance();
		from.add(Calendar.YEAR, -5); // from 5 years ago
		Calendar to = Calendar.getInstance();
		
		StockInfo test = new StockInfo("TSLA", from, to, Interval.WEEKLY);
		
		//test.PrintHistory();
		
		double investment = 5000.00;
		
		
		List<Double> profitData = GetProfitInfo(test, investment);
		for(int i = 0; i < profitData.size(); i++) {
			
			System.out.println( profitData.get(i) );
			
		}
		
		
    }
	
	
	public static List<Double> GetProfitInfo(StockInfo stock, double investment) {
		
		List<Double> profits = new ArrayList<Double>();
		
		double profit = investment;
		for(int i = 0; i < stock.GetHistory().size(); i++) {
			
			profit *= ( 1 + stock.PercentDiff(i) );
			
			profits.add(profit);
			
		}
		
		return profits;
		
		
	}
	
	
}