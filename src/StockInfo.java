import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
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
	
	
}
