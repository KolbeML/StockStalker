import java.util.ArrayList;
import java.text.*;
import java.util.Calendar;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
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
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;

import yahoofinance.*;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;


public class Main extends Application{

	private XYChart.Series<Number, Number> leftStock;
	private XYChart.Series<Number, Number> rightStock;
	private ListView<String> leftStockList;
	private ListView<String> rightStockList;
	private LineChart<Number,Number> chart;
	private ComboBox<String> startDate;
	private ComboBox<String> endDate;
	private ComboBox<String> interval;
	private GridPane dataPane;
	private TextField investInput;
	private TextField rSearch;
	private TextField lSearch;
	private Text investLText;
	private Text investRText;
	private Text rStock;
	private Text lStock;
	
	private double investment;
	
	private StockInfo stockLData;
	private StockInfo stockRData;
	
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primary){

		//Builds the Pane
		BorderPane root = new BorderPane();
		GridPane infoPane = new GridPane();
		dataPane = new GridPane();
		HBox titlePane = new HBox();
		VBox left = new VBox();
		VBox right = new VBox();
		
		investLText = new Text();
		investRText = new Text();
		lStock = new Text();
		rStock = new Text();
		
		investInput = new TextField("0");
		
		TextField rSearch = new TextField();
		TextField lSearch = new TextField();

		//Sets up the chart
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Time Interval");
		xAxis.setTickMarkVisible(false);
		yAxis.setLabel("Percentage");
		chart = new LineChart<Number, Number>(xAxis,yAxis);
		chart.setTitle("Stock Comparison");



		StockLists list = new StockLists();
		list.readFile();
		ArrayList<String> array = list.getStocks();

		//Sets up the lists
		ObservableList<String> stocks = FXCollections.observableArrayList(array);
		leftStockList = new ListView<String>(stocks);
		rightStockList = new ListView<String>(stocks);
		leftStock = new XYChart.Series<Number, Number>();
		rightStock = new XYChart.Series<Number, Number>();
		left.getChildren().addAll(lSearch, leftStockList);
		right.getChildren().addAll(rSearch, rightStockList);
		chart.getData().add(leftStock);
		chart.getData().add(rightStock);
		
		
		

		startDate = new ComboBox<String>();
		endDate = new ComboBox<String>();
		interval = new ComboBox<String>();

		leftStockList.getSelectionModel().selectedItemProperty().addListener(e->{
			for(Integer i: leftStockList.getSelectionModel().getSelectedIndices()) {
				if(array.get(i).equals(leftStockList.getSelectionModel().getSelectedItem())) {

					Calendar start = Calendar.getInstance();
					start.add(Calendar.YEAR, -3);
					Calendar end = Calendar.getInstance();
					stockLData = new StockInfo(array.get(i),start,end,Interval.MONTHLY);
					lStock.setText(array.get(i));
					ArrayList<Double> list2 = new ArrayList<Double>(stockLData.GetPrices());
					
					Calculate(true, false);
					
					leftStock = setData(array.get(i), list2, leftStock);
					
					break;
					
				}
			}
		});

		rightStockList.getSelectionModel().selectedItemProperty().addListener(e->{
			for(Integer i: rightStockList.getSelectionModel().getSelectedIndices()) {
				if(array.get(i).equals(rightStockList.getSelectionModel().getSelectedItem())) {
					Calendar start = Calendar.getInstance();
					start.add(Calendar.YEAR, -3);
					Calendar end = Calendar.getInstance();
					stockRData = new StockInfo(array.get(i),start,end,Interval.MONTHLY);
					
					rStock.setText(array.get(i));
					ArrayList<Double> list2 = new ArrayList<Double>(stockRData.GetPrices());
					
					Calculate(false, true);
					
					rightStock = setData(array.get(i), list2, rightStock);
					
					break;
					
				}
			}
		});
		
		//select initial values
		leftStockList.getSelectionModel().select(0);
		rightStockList.getSelectionModel().select(1);

		
		dataPane.add(new Text("Invest Amount"), 0, 0);
		dataPane.add(investInput, 0, 1);
		dataPane.add(lStock, 0, 3);
		dataPane.add(rStock, 0, 4);
		dataPane.add(investLText, 1, 3);
		dataPane.add(investRText, 1, 4);
		
		
		
		investInput.textProperty().addListener( (obs, oldText, newText) -> {
			if( Main.isNumber(newText) ) {
				
				Calculate(true, true);
				
			}
		});
		
		
		// -----SEARCH------
		rSearch.textProperty().addListener( (obs, oldText, newText) -> {
			for(int i = 0; i < array.size(); i++) {
				if(  array.get(i).startsWith( newText )  ) {
					rightStockList.getSelectionModel().select(i);
					rightStockList.scrollTo(i);
					break;
				}
			}
			
		});
		
		lSearch.textProperty().addListener( (obs, oldText, newText) -> {
			for(int i = 0; i < array.size(); i++) {
				if(array.get(i).startsWith( newText )) {
					leftStockList.getSelectionModel().select(i);
					leftStockList.scrollTo(i);
					break;
				}
			}
			
		});
		
		
		
		//infoPane.add(new Text("test"), x, y);
		infoPane.add(chart, 1, 1);
		infoPane.add(startDate, 0, 0);
		infoPane.add(endDate, 2, 0);
		infoPane.add(interval, 1, 0);
		infoPane.setHalignment(interval,HPos.CENTER);

		titlePane.getChildren().add(new Text("Stock Stocker"));
		titlePane.setAlignment(Pos.CENTER);

		root.setTop(titlePane);
		root.setCenter(infoPane);
		root.setRight(right);
		root.setLeft(left);
		root.setBottom(dataPane);


		primary.setScene(new Scene(root,1000,600));
		primary.show();
	}
	
	
	protected void Calculate(boolean left, boolean right) {
		
		DecimalFormat df = new DecimalFormat("#.00"); 
		
		investment = Double.parseDouble(investInput.getText());
		
		if(left) {
			//right or both
			ArrayList<Double> profitL = new ArrayList<Double>(stockLData.GetProfitInfo(investment));
			investLText.setText("$" + df.format(profitL.get(profitL.size()-1))+"");
		}
		if(right) {
			ArrayList<Double> profitR = new ArrayList<Double>(stockRData.GetProfitInfo(investment));
			investRText.setText("$" + df.format(profitR.get(profitR.size()-1))+"");
		}
		
	}
	
	

	protected XYChart.Series<Number, Number> setData(String name, ArrayList<Double> data, XYChart.Series<Number, Number> oldSeries) {
		//chart.getData().remove(series);
		Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName(name);
		for(int i = 0; i < data.size(); i++) {
			series.getData().add(new XYChart.Data<Number, Number>(i,data.get(i)));
		}

		chart.getData().set( chart.getData().indexOf(oldSeries), series);
		
		return series;
	}
	
    static boolean isNumber(String s){

        char a;
        int c = 0;

        for(int i = 0; i < s.length(); i++){

            a = s.charAt(i);

            if(a == '.') {
                continue;
            }else if(!Character.isDigit(a))
                c++;
        }

        if(s.length() == 0)
            c++;

        return c == 0;
    }

	public static void main(String[]args){launch(args);}

}
