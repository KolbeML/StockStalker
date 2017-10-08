import java.util.ArrayList;
import java.text.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Date;

import yahoofinance.histquotes.Interval;

public class Main extends Application {

	private XYChart.Series<Number, Number> leftStock;
	private XYChart.Series<Number, Number> rightStock;
	private ListView<String> leftStockList;
	private ListView<String> rightStockList;
	private LineChart<Number, Number> chart;
	private DatePicker startDate;
	private DatePicker endDate;
	private ComboBox<String> interval;
	private GridPane dataPane;
	private TextField investInput;
	private Text investLText;
	private Text investRText;
	private Text rStock;
	private Text lStock;
	private Text lDifference;
	private Text rDifference;
	private Text lPercent;
	private Text rPercent;
	private NumberAxis yAxis;

	private double investment;

	private boolean percent;

	// stock info
	private Calendar start;
	private Calendar end;
	private Interval dateInterval;

	private StockInfo stockLData;
	private StockInfo stockRData;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primary) {

		// Builds the Pane
		BorderPane root = new BorderPane();
		GridPane infoPane = new GridPane();
		dataPane = new GridPane();
		HBox titlePane = new HBox();
		VBox left = new VBox();
		VBox right = new VBox();

		//Toggle value and button
		percent = true;
		Button change = new Button("Price");
		change.getStyleClass().add("stocker-button");

		//Data Text
		investLText = new Text();
		investRText = new Text();
		lStock = new Text();
		rStock = new Text();
		lDifference = new Text();
		rDifference = new Text();
		lPercent = new Text();
		rPercent = new Text();
		investInput = new TextField("0");

		//Search Fields
		TextField rSearch = new TextField();
		TextField lSearch = new TextField();
		
		// Sets up the chart
		final NumberAxis xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		xAxis.setLabel("Time Interval");
		xAxis.setTickLabelsVisible(false);
		yAxis.setLabel("Percentage");
		leftStock = new XYChart.Series<Number, Number>();
		rightStock = new XYChart.Series<Number, Number>();
		chart = new LineChart<Number, Number>(xAxis, yAxis);
		chart.setTitle("Stock Comparison");
		chart.getData().add(leftStock);
		chart.getData().add(rightStock);

		//Pulls in the data
		StockLists.readFile();
		ArrayList<String> array = StockLists.getStocks();

		// Sets up the lists
		ObservableList<String> stocks = FXCollections.observableArrayList(array);
		leftStockList = new ListView<String>(stocks);
		rightStockList = new ListView<String>(stocks);
		left.getChildren().addAll(lSearch, leftStockList);
		left.setPadding(new Insets(5));
		left.setSpacing(5);
		right.getChildren().addAll(rSearch, rightStockList);
		right.setPadding(new Insets(5));
		right.setSpacing(5);
		
		

		// DATE STUFF
		startDate = new DatePicker();
		endDate = new DatePicker();

		Callback<DatePicker, DateCell> dayCellFactoryStart = new Callback<DatePicker, DateCell>() {

			public DateCell call(final DatePicker datePicker) {

				return new DateCell() {

					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						// disable future dates
						if (item.isAfter(LocalDate.now().minusDays(1))) {
							this.setDisable(true);
						}
					}

				};

			}

		};
		Callback<DatePicker, DateCell> dayCellFactoryEnd = new Callback<DatePicker, DateCell>() {

			public DateCell call(final DatePicker datePicker) {

				return new DateCell() {

					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						// disable future dates
						if (item.isAfter(LocalDate.now())) {
							this.setDisable(true);
						}
					}

				};

			}

		};

		startDate.setDayCellFactory(dayCellFactoryStart);
		endDate.setDayCellFactory(dayCellFactoryEnd);

		ObservableList<String> intervals = FXCollections.observableArrayList(DateInterval.MONTHLY.getStr(),
				DateInterval.WEEKLY.getStr(), DateInterval.DAILY.getStr());
		interval = new ComboBox<String>(intervals);
		interval.getSelectionModel().select(0);
		dateInterval = DateInterval.valueOf(interval.getSelectionModel().getSelectedItem()).getVal();

		startDate.setMinWidth(100.0);
		endDate.setMinWidth(100.0);
		interval.setMinWidth(85.0);

		start = Calendar.getInstance();
		start.add(Calendar.YEAR, -1);
		end = Calendar.getInstance();

		Date dateTime = new Date(start.getTimeInMillis());
		startDate.setValue(dateTime.toLocalDate());
		dateTime = new Date(end.getTimeInMillis());
		endDate.setValue(dateTime.toLocalDate());

		//List functionality
		leftStockList.getSelectionModel().selectedItemProperty().addListener(e -> {
			for (Integer i : leftStockList.getSelectionModel().getSelectedIndices()) {
				if (array.get(i).equals(leftStockList.getSelectionModel().getSelectedItem())) {

					stockLData = new StockInfo(array.get(i), start, end, dateInterval);
					lStock.setText(array.get(i));

					ArrayList<Double> list2 = new ArrayList<Double>();
					if (percent) {
						list2 = new ArrayList<Double>(stockLData.getPercentChanges());
					} else {
						list2 = new ArrayList<Double>(stockLData.getPrices());
					}

					calculate(true, false);

					leftStock = setData(stockLData.getName(), list2, leftStock);

					break;

				}
			}
		});

		rightStockList.getSelectionModel().selectedItemProperty().addListener(e -> {
			for (Integer i : rightStockList.getSelectionModel().getSelectedIndices()) {
				if (array.get(i).equals(rightStockList.getSelectionModel().getSelectedItem())) {

					stockRData = new StockInfo(array.get(i), start, end, dateInterval);

					rStock.setText(array.get(i));
					ArrayList<Double> list2 = new ArrayList<Double>();

					if (percent) {
						list2 = new ArrayList<Double>(stockRData.getPercentChanges());
					} else {
						list2 = new ArrayList<Double>(stockRData.getPrices());
					}
					calculate(false, true);

					rightStock = setData(stockRData.getName(), list2, rightStock);

					break;

				}
			}
		});

		// select initial values
		leftStockList.getSelectionModel().select(0);
		rightStockList.getSelectionModel().select(1);

		//Arranges the data at the bottom
		int y = 3;
		int x = 0;
		Pane[] panes = new Pane[11];
		for(int i = 0; i < panes.length; i++) {
			panes[i] = new Pane();
			
			if(y == 5) {
				x++;
				y = 2;
			}
			dataPane.add(panes[i], x, y);
			y++;
		}
		
		panes[0].getChildren().add(lStock);
		panes[1].getChildren().add(rStock);
		panes[2].getChildren().add(new Text("Return"));
		panes[3].getChildren().add(investLText);
		panes[4].getChildren().add(investRText);
		panes[5].getChildren().add(new Text("Gain/Loss"));
		panes[6].getChildren().add(lDifference);
		panes[7].getChildren().add(rDifference);
		panes[8].getChildren().add(new Text("Percent Change"));
		panes[9].getChildren().add(lPercent);
		panes[10].getChildren().add(rPercent);
		
		dataPane.add(new Text("Investment Amount"), 0, 0);
		dataPane.add(investInput, 0, 1);
		
		for(int i = 0; i < panes.length; i++) {
			
		}
		dataPane.setPadding(new Insets(5));
		dataPane.setVgap(5);
		dataPane.setHgap(5);
		dataPane.add(change, 4, 0);

		//Toggle button for percentage and price
		change.setOnAction(e -> {
			if (percent) {
				percent = false;
				change.setText("Percentage");
				yAxis.setLabel("Price");
				updateLists();
			} else {
				percent = true;
				change.setText("Price");
				yAxis.setLabel("Percentage");
				updateLists();
			}
		});

		// ----INTERVAL AND DATES----
		interval.setOnAction(e -> {

			dateInterval = DateInterval.valueOf(interval.getSelectionModel().getSelectedItem()).getVal();
			updateLists();

		});

		startDate.valueProperty().addListener(event -> {
			java.util.Date date = Date.from(startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			start = Calendar.getInstance();
			start.setTime(date);
			updateLists();
		});

		endDate.valueProperty().addListener(event -> {
			java.util.Date date = Date.from(startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			end = Calendar.getInstance();
			end.setTime(date);
			updateLists();
		});

		// ---CALCULATE TEXT---
		investInput.textProperty().addListener((obs, oldText, newText) -> {
			if (Main.isNumber(newText)) {

				calculate(true, true);

			}
		});

		// -----SEARCH------
		rSearch.textProperty().addListener((obs, oldText, newText) -> {
			for (int i = 0; i < array.size(); i++) {
				if (array.get(i).startsWith(newText.toUpperCase())) {
					rightStockList.getSelectionModel().select(i);
					rightStockList.scrollTo(i);
					break;
				}
			}

		});

		lSearch.textProperty().addListener((obs, oldText, newText) -> {
			for (int i = 0; i < array.size(); i++) {
				if (array.get(i).startsWith(newText.toUpperCase())) {
					leftStockList.getSelectionModel().select(i);
					leftStockList.scrollTo(i);
					break;
				}
			}

		});

		//Arranges the chart and ComboBoxes
		infoPane.add(chart, 0, 1);
		infoPane.add(startDate, 0, 0);
		infoPane.add(interval, 1, 0);
		infoPane.add(endDate, 2, 0);
		infoPane.setPadding(new Insets(5));
		GridPane.setColumnSpan(chart, GridPane.REMAINING);
		GridPane.setHalignment(startDate, HPos.LEFT);
		GridPane.setHalignment(interval, HPos.CENTER);
		GridPane.setHalignment(endDate, HPos.RIGHT);
		
		//Sets up the title
		Text title = new Text("Stock Stalker");
		title.setId("title-text");
		titlePane.getChildren().add(title);
		titlePane.setAlignment(Pos.CENTER);
		titlePane.setPadding(new Insets(5));

		//Sets up the base layout
		root.setTop(titlePane);
		root.setCenter(infoPane);
		root.setRight(right);
		root.setLeft(left);
		root.setBottom(dataPane);

		Scene scene = new Scene(root, 1000, 600);
		scene.getStylesheets().add("resources/stylesheet.css");
		primary.setScene(scene);
		primary.show();
	}

	protected void calculate(boolean left, boolean right) {

		DecimalFormat df = new DecimalFormat("#.00");

		investment = Double.parseDouble(investInput.getText());

		if (left) {
			// right or both
			ArrayList<Double> profitL = new ArrayList<Double>(stockLData.getProfitInfo(investment));
			investLText.setText("$" + df.format(profitL.get(profitL.size() - 1)) + "");
			double difference = StockInfo.getValueChange(investment, profitL.get(profitL.size() - 1));
			lDifference.setText((difference < 0 ? "-$" : "$") + df.format(Math.abs(difference)));
			double percent = StockInfo.getPercentChange(investment, profitL.get(profitL.size() - 1));
			lPercent.setText(df.format(percent * 100.0) + "%");

		}
		if (right) {
			ArrayList<Double> profitR = new ArrayList<Double>(stockRData.getProfitInfo(investment));
			investRText.setText("$" + df.format(profitR.get(profitR.size() - 1)) + "");
			double difference = StockInfo.getValueChange(investment, profitR.get(profitR.size() - 1));
			rDifference.setText((difference < 0 ? "-$" : "$") + df.format(Math.abs(difference)));
			double percent = StockInfo.getPercentChange(investment, profitR.get(profitR.size() - 1));
			rPercent.setText(df.format(percent * 100.0) + "%");

		}

	}

	protected void updateLists() {

		int index = leftStockList.getSelectionModel().getSelectedIndex();

		leftStockList.getSelectionModel().clearSelection();
		leftStockList.getSelectionModel().select(index);

		index = rightStockList.getSelectionModel().getSelectedIndex();

		rightStockList.getSelectionModel().clearSelection();
		rightStockList.getSelectionModel().select(index);

	}

	protected XYChart.Series<Number, Number> setData(String name, ArrayList<Double> data,
			XYChart.Series<Number, Number> oldSeries) {
		Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName(name);
		for (int i = 0; i < data.size(); i++) {
			series.getData().add(new XYChart.Data<Number, Number>(i, data.get(i)));
		}
		chart.setCreateSymbols(false);
		chart.getData().set(chart.getData().indexOf(oldSeries), series);

		return series;
	}

	// Checks to make sure a string contains only numbers
	static boolean isNumber(String s) {
		char a;
		int c = 0;

		for (int i = 0; i < s.length(); i++) {
			a = s.charAt(i);
			if (a == '.') {
				continue;
			} else if (!Character.isDigit(a))
				c++;
		}

		if (s.length() == 0)
			c++;

		return c == 0;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
