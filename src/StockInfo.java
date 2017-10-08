import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class StockInfo {

	private Calendar fromDate, toDate;
	
	Interval dateInterval;
	
	private Stock stock;
	
	private List<HistoricalQuote> history;
	
	public List<HistoricalQuote> GetHistory() {
		return history;
	}
	
	
	/*
	 * 
	 */
	public StockInfo(String stockName, Calendar fromDate, Calendar toDate, Interval dateInterval) {
		
		UpdateInfo(stockName, fromDate, toDate, dateInterval);
		
	}
	
	
	public void UpdateInfo() {
		
		UpdateInfo(stock.getSymbol(), fromDate, toDate, dateInterval);
		
	}
	
	public void UpdateInfo(String stockName, Calendar fromDate, Calendar toDate, Interval dateInterval) {
		
		this.fromDate = fromDate;
		this.toDate = toDate;
		
		this.dateInterval = dateInterval;
		
		try {
			stock = YahooFinance.get(stockName, fromDate, toDate, dateInterval);
			
			history = stock.getHistory();
			
			boolean dateModified = false;
			for( Iterator<HistoricalQuote> iter = history.iterator(); iter.hasNext();){
				
				HistoricalQuote item = iter.next();
				
				int index = history.indexOf(item);
				if(item.getClose() == null && index == 0) {
					// if this is the starting item, and it is null
					// we can't use it
					iter.remove();
					dateModified = true;
					
				}
				
			}
			
			this.fromDate = history.get(0).getDate();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void PrintHistory() {
		
		for(int i = 0; i < history.size(); i++) {
			
			Calendar date = history.get(i).getDate();
			String sDate = date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
			sDate += "-" + date.get(Calendar.DATE);
			sDate += "-" + date.get(Calendar.YEAR);
			
			BigDecimal high = history.get(i).getHigh();
			BigDecimal low = history.get(i).getLow();
			
			System.out.println(sDate + "\t\tlow: " + low + "\t\thigh: " + high);
			
		}
		
	}
	
	/*
	 * Get the percent difference from the last index to this index
	 */
	public double PercentDiff(int index) {
		
		double percent = 0.0;
		
		if(  index > 0 && history.get(index).getClose() != null && history.get(index - 1).getClose() != null  ) {
			
			double lastValue = history.get(index-1).getClose().doubleValue();
			double value = history.get(index).getClose().doubleValue();
			
			// calculate the percent
			// differenceInValue divided by lastValue times
			percent =  (value - lastValue) / lastValue;
			
		}
		
		return percent;
		
	}
	
	
}
