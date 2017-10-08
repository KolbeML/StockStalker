
public enum TextItems {
	// left info
	L_STOCK(0),
	L_PROFIT(1),
	L_DIFFERENCE(2),
	L_PERCENT(3),
	L_PRICE(4),
	
	L_SIZE(5),
	
	// right info
	R_STOCK(6),
	R_PROFIT(7),
	R_DIFFERENCE(8),
	R_PERCENT(9),
	R_PRICE(10),
	
	R_SIZE(11),
	
	//constants
	RETURNS(12),
	GAIN_LOSS(13),
	PERCENT_CHANGE(14),
	STOCK_PRICE(15);
	
	
	
	private final int value;
	
	TextItems(int value) {
		this.value = value;
	}
	
	public int val() {
		return value;
	}
}
