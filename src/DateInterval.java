import yahoofinance.histquotes.Interval;

public enum DateInterval {
	MONTHLY	("MONTHLY", Interval.MONTHLY),
	WEEKLY	("WEEKLY", Interval.WEEKLY),
	DAILY	("DAILY", Interval.DAILY);
	
	private final String name;
	private final Interval diff;
	private DateInterval(String name, Interval diff) {
		this.name = name;
		this.diff = diff;
	}
	
	public Interval getVal() {
		return diff;
	}
	
	public String getStr() {
		return name;
	}
	
}