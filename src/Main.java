import java.util.ArrayList;

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


public class Main extends Application{

    @Override
    public void start(Stage primary){

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
    	LineChart<Number,Number> chart = new LineChart<Number, Number>(xAxis,yAxis);
    	chart.setTitle("Stock Comparison");
        
    	
    	//Test array
        ArrayList<String> array = new ArrayList<String>();
        array.add("Test1");
        array.add("Test2");
        
        //Sets up the lists
        ObservableList<String> stocks = FXCollections.observableArrayList(array);
        ListView<String> leftStockList = new ListView<String>(stocks);
        ListView<String> rightStockList = new ListView<String>(stocks);
        XYChart.Series leftStock = new XYChart.Series();
        XYChart.Series rightStock = new XYChart.Series();
        left.getChildren().add(leftStockList);
        right.getChildren().add(rightStockList);
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

    public static void main(String[]args){launch(args);}
		