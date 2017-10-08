
public enum TextItems {
	L_STOCK(0),
	R_STOCK(1),
	RETURNS(2),
	L_PROFIT(3),
	R_PROFIT(4),
	GAIN_LOSS(5),
	L_DIFFERENCE(6),
	R_DIFFERENCE(7),
	PERCENT_CHANGE(8),
	L_PERCENT(9),
	R_PERCENT(10),
	STOCK_PRICE(11),
	L_PRICE(12),
	R_PRICE(13);
	
	private final int value;
	
	TextItems(int value) {
		this.value = value;
	}
	
	public int val() {
		return value;
	}
}
