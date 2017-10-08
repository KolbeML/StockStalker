import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

import yahoofinance.*;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;


public class Main extends Application{

	private XYChart.Series leftStock;
	private XYChart.Series rightStock;
	private ListView<String> leftStockList;
	private ListView<String> rightStockList;
	private LineChart<Number,Number> chart;

	@Override
	public void start(Stage primary){


		/*
    	 	Calendar from = Calendar.getInstance();
		from.add(Calendar.YEAR, -10);
		Calendar to = Calendar.getInstance();

		StockInfo test = new StockInfo("BTCUSD=X", from, to, Interval.WEEKLY);

		test.PrintHistory();

		double investment = 5000.00;


		List<Double> profitData = test.GetProfitInfo(investment);
		for(int i = 0; i < profitData.size(); i++) {

			System.out.println( profitData.get(i) );

		}
		 */


		Stock stock;
		try {
			stock = YahooFinance.get("INTC");

			System.out.println(stock.getHistory());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Builds the Pane
		BorderPane root = new BorderPane();
		GridPane infoPane = new GridPane();
		HBox titlePane = new HBox();
		VBox left = new VBox();
		VBox right = new VBox();

		//Sets up the chart
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Week Interval");
		yAxis.setLabel("Percentage");
		chart = new LineChart<Number, Number>(xAxis,yAxis);
		chart.setTitle("Stock Comparison");


		//Test array
		StockLists list = new StockLists();
		list.readFile();
		ArrayList<String> array = list.getStocks();

		//Sets up the lists
		ObservableList<String> stocks = FXCollections.observableArrayList(array);
		leftStockList = new ListView<String>(stocks);
		rightStockList = new ListView<String>(stocks);
		leftStock = new XYChart.Series();
		rightStock = new XYChart.Series();
		left.getChildren().add(leftStockList);
		right.getChildren().add(rightStockList);


		leftStockList.getSelectionModel().selectedItemProperty().addListener(e->{
			for(Integer i: leftStockList.getSelectionModel().getSelectedIndices()) {
				if(array.get(i).equals(leftStockList.getSelectionModel().getSelectedItem())) {
					ArrayList<Integer> list1 = new ArrayList<Integer>();
					list1.add(1);
					list1.add(2);
					list1.add(3);
					list1.add(4);
					ArrayList<Double> list2 = new ArrayList<Double>();
					list2.add(10.2);
					list2.add(5.6);
					list2.add(3.9);
					list2.add(4.8);

					setLeftData(array.get(i),list1,list2);
				}
			}
		});

		rightStockList.getSelectionModel().selectedItemProperty().addListener(e->{
			for(Integer i: rightStockList.getSelectionModel().getSelectedIndices()) {
				if(array.get(i).equals(rightStockList.getSelectionModel().getSelectedItem())) {
					ArrayList<Integer> list1 = new ArrayList<Integer>();
					list1.add(1);
					list1.add(2);
					list1.add(3);
					list1.add(4);
					ArrayList<Double> list2 = new ArrayList<Double>();
					list2.add(8.2);
					list2.add(13.6);
					list2.add(6.9);
					list2.add(9.8);

					setRightData(array.get(i),list1,list2);
				}
			}
		});


		//Setting Data values into the Chart
		leftStock.setName("Stuff");
		leftStock.getData().add(new XYChart.Data(1,2));
		leftStock.getData().add(new XYChart.Data(2,3));
		leftStock.getData().add(new XYChart.Data(3,6));
		leftStock.getData().add(new XYChart.Data(4,3));
		rightStock.setName("More Stuff");
		rightStock.getData().add(new XYChart.Data(1,6));
		rightStock.getData().add(new XYChart.Data(2,5));
		rightStock.getData().add(new XYChart.Data(3,3));
		rightStock.getData().add(new XYChart.Data(4,1));
		chart.getData().add(leftStock);
		chart.getData().add(rightStock);

		//infoPane.add(new Text("test"), x, y);
		infoPane.add(chart, 1, 0);

		titlePane.getChildren().add(new Text("Stock Stocker"));
		titlePane.setAlignment(Pos.CENTER);

		root.setTop(titlePane);
		root.setCenter(infoPane);
		root.setRight(right);
		root.setLeft(left);


		primary.setScene(new Scene(root,800,250));
		primary.show();
	}

	protected void setLeftData(String name, ArrayList<Integer> time, ArrayList<Double> data) {
		chart.getData().remove(leftStock);
		leftStock = new XYChart.Series();
		leftStock.setName(name);
		for(int i = 0; i < time.size(); i++) {
			leftStock.getData().add(new XYChart.Data(time.get(i),data.get(i)));
		}

		chart.getData().add(leftStock);

	}

	protected void setRightData(String name, ArrayList<Integer> time, ArrayList<Double> data) {
		chart.getData().remove(rightStock);
		rightStock = new XYChart.Series();
		rightStock.setName(name);
		for(int i = 0; i < time.size(); i++) {
			rightStock.getData().add(new XYChart.Data(time.get(i),data.get(i)));
		}

		chart.getData().add(rightStock);
	}

	public static void main(String[]args){launch(args);}

}
