import java.io.IOException;

import yahoofinance.*;


public class Main {
    
	public static void main(String[] args) {
		
		System.out.println("Hello");
		
		Stock stock;
		try {
			stock = YahooFinance.get("INTC");
			
			System.out.println(stock.getHistory());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(price);
		
    }
}