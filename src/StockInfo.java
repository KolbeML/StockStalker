import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

	public List<HistoricalQuote> getHistory() {
		return history;
	}
	
	public String getName() {
		
		return stock.getName();
		
	}

	/*
	 * 
	 */
	public StockInfo(String stockName, Calendar fromDate, Calendar toDate, Interval dateInterval) {

		updateInfo(stockName, fromDate, toDate, dateInterval);

	}

	public void updateInfo() {

		updateInfo(stock.getSymbol(), fromDate, toDate, dateInterval);

	}

	public void updateInfo(String stockName, Calendar fromDate, Calendar toDate, Interval dateInterval) {

		this.fromDate = fromDate;
		this.toDate = toDate;

		this.dateInterval = dateInterval;

		try {
			stock = YahooFinance.get(stockName, fromDate, toDate, dateInterval);

			history = stock.getHistory();

			for (Iterator<HistoricalQuote> iter = history.iterator(); iter.hasNext();) {

				HistoricalQuote item = iter.next();

				int index = history.indexOf(item);
				if (item.getClose() == null && index == 0) {
					// if this is the starting item, and it is null
					// we can't use it
					iter.remove();

				}

			}

			this.fromDate = history.get(0).getDate();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void printHistory() {

		for (int i = 0; i < history.size(); i++) {

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
	public double percentDiff(int index) {

		double percent = 0.0;

		if (index > 0 && history.get(index).getClose() != null && history.get(index - 1).getClose() != null) {

			double lastValue = history.get(index - 1).getClose().doubleValue();
			double value = history.get(index).getClose().doubleValue();

			// calculate the percent
			// differenceInValue divided by lastValue times
			percent = getPercentChange(lastValue, value);

		}

		return percent;

	}

	/*
	 * Get the profit info over time
	 */
	public List<Double> getProfitInfo(double initialInvestment) {

		List<Double> profits = new ArrayList<Double>();

		double profit = initialInvestment;
		int number = (int) (profit / history.get(0).getClose().doubleValue());
		double leftover = getValueChange((double) (history.get(0).getClose().doubleValue() * number), profit);

		for (int i = 0; i < history.size(); i++) {
			profit = (profit - leftover) * (1 + percentDiff(i)) + leftover;
			if (percentDiff(i) != 0 && leftover > history.get(i).getClose().doubleValue()) {
				number += (int) (leftover / history.get(i).getClose().doubleValue());
				leftover = getValueChange((double) (history.get(i).getClose().doubleValue() * number), profit);
			}
			profits.add(profit);
		}

		return profits;
		
		//User is able to add the percentage rise/drop in order to sell/buy stocks.
		//When the price hits too low (max percentage dropped), sell all.
		
		/*List<Double> profits = new ArrayList<Double>();

		double setDrop = 0.02;
		double setRise = 0.02;
		double dropMax = 10.0;
		
		double profit = initialInvestment;
		int number = 0;
		double leftover =  getValueChange((double) (history.get(0).getClose().doubleValue() * number), profit);

		for (int i = 0; i < history.size(); i++) {
			profit = (profit - leftover) * (1 + percentDiff(i)) + leftover;
			if (percentDiff(i) >= setRise || percentDiff(i) <= dropMax) {
				number = 0;
				if (percentDiff(i) != 0)
					leftover = getValueChange((double) (history.get(i).getClose().doubleValue() * number), profit);
			}
			if (percentDiff(i) <= -setDrop) {
				number += (int) (leftover / history.get(i).getClose().doubleValue());
				if (percentDiff(i) != 0)
					leftover = getValueChange((double) (history.get(i).getClose().doubleValue() * number), profit);
			}
			profits.add(profit);
		}

		return profits;*/


	}


	public List<Double> getPercentChanges() {

		List<Double> percents = new ArrayList<Double>();

		double percent = percentDiff(0);
		for (int i = 0; i < history.size(); i++) {

			if (history.get(i).getClose() != null) {
				percent = percentDiff(i);
			}

			percents.add(percent);

		}

		return percents;

	}
	
	public List<Double> getPrices() {

		List<Double> prices = new ArrayList<Double>();

		
		double lastPrice = history.get(0).getClose().doubleValue();;
		for (int i = 0; i < history.size(); i++) {

			if( history.get(i).getClose() != null ) {
				lastPrice = history.get(i).getClose().doubleValue();
			}
			
			prices.add(lastPrice);

		}
		return prices;
	}

	public static double getValueChange(double initial, double fin) {
		return fin - initial;
	}

	public static double getPercentChange(double initial, double fin) {
		return getValueChange(initial, fin)/initial;
	}

}
