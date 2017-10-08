import java.io.*;
import java.util.*;

public class StockLists {
	public static ArrayList<String> names = new ArrayList<String>();

	private static String fileName = "src/resources/stockNames.txt";

	public static void readFile() {
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				names.add(line);
				line = br.readLine();
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	protected static ArrayList<String> getStocks() {
		return names;
	}
}
