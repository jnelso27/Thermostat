package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.io.File;
import java.util.Observable;

import application.thermostat.Thermostat;
import application.thermostat.xml.XMLFileBuilder;
import application.thermostat.xml.XMLFileLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/***
 * TODO: Class Description
 *
 * @author Joshua Nelson
 *
 */
public class Main extends Application
{
	//
	Group root = new Group();

	//
	Scene scene = new Scene(root, 800, 500, Color.WHITE);

	//
	TabPane tabPane = new TabPane();

	//
	MenuBar menuBar = new MenuBar();

	 //
    private int graphTimeSetting = NUM_SECONDS_IN_TEN_MINUTES;

	//
    private static final int MAX_DATA_POINTS = 50;
    private static final int NUM_SECONDS_IN_TEN_MINUTES = 600;
    private static final int NUM_SECONDS_IN_ONE_HOUR = 3600;
    private static final int NUM_SECONDS_IN_ONE_DAY = 86400;
    private static final int NUM_SECONDS_IN_ONE_WEEK = 604800;

    //Chart for displaying the current temperature
    private XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series4 = new XYChart.Series<>();

    //
    private int xSeries1Data = 0;
    private int xSeries2Data = 0;
    private int xSeries3Data = 0;
    private int xSeries4Data = 0;

    //
    private NumberAxis series1XAxis;
    private NumberAxis series2XAxis;
    private NumberAxis series3XAxis;
    private NumberAxis series4XAxis;

    //
    private ExecutorService executor;

    //Variables for Settings Tab
    Tab settingsTab = new Tab();
    VBox settingsVBox = new VBox();
    TextField highThresholdTextField = new TextField();
    TextField lowThresholdTextField = new TextField();
    Button settingsApplyButton = new Button("Apply");

    //
    ObservableList<String> options = FXCollections.observableArrayList(
    	        "Last 10 Minutes",
    	        "Last 60 Minutes",
    	        "Last 24 Hours",
    	        "Last Week"
    	    );
    final ComboBox<String> graphSelectionComboBox = new ComboBox<String>(options);

    AnimationTimer graphTimer = new AnimationTimer()
    {
        @Override
        public void handle(long now)
        {
        	addDataToSeries1();
        	addDataToSeries2();
        	addDataToSeries3();
        	addDataToSeries4();
        }
    };

    //Variables for Current Tab
    Tab currentTab = new Tab();

    //XMLFileLoader to load application XML Settings file
    XMLFileLoader configurationFileLoader = new XMLFileLoader();

    //FileBuilder to save application XML Settings file
    XMLFileBuilder configureationFileSaver = new XMLFileBuilder();

    //Thermostat object to handle all major operations (primary backend object)
    Thermostat thermostat = null;

    //TextField for displyaing the current temperature reading
    TextField tempReadingTextField = new TextField();

    /**
     * Method Description
     *
     * @param primaryStage
     */
    private void init(Stage primaryStage)
    {
    	//Load Configuration File and Add Observers
    	thermostat = configurationFileLoader.loadXMLConfigurationFile();

    	//Setup the primary stage
    	primaryStage.setTitle("Thermostat");

        BorderPane borderPane = new BorderPane();

        thermostat.addObserver((Observable obj, Object arg)->
        {
        	//Used to keep in JavaFX Thread
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    //javaFX operations should go here
                	if(thermostat.isTemperatureUnitsInFarenheit())
                	{
	                	series1.getData().add(new XYChart.Data<>(xSeries1Data++, (double)arg));
	                	series2.getData().add(new XYChart.Data<>(xSeries2Data++, (double)arg));
	                	series3.getData().add(new XYChart.Data<>(xSeries3Data++, (double)arg));
	                	series4.getData().add(new XYChart.Data<>(xSeries4Data++, (double)arg));
	                	tempReadingTextField.setText(Double.toString((double)arg));
                	}
                	else //Display in Celcius
                	{
                		series1.getData().add(new XYChart.Data<>(xSeries1Data++, (((double)arg-32)*5)/9)); //
                		series2.getData().add(new XYChart.Data<>(xSeries2Data++, (((double)arg-32)*5)/9));
                		series3.getData().add(new XYChart.Data<>(xSeries3Data++, (((double)arg-32)*5)/9));
                		series4.getData().add(new XYChart.Data<>(xSeries4Data++, (((double)arg-32)*5)/9));
	                	tempReadingTextField.setText(Double.toString((((double)arg-32)*5)/9));
                	}
                }
           });

        });

        //Add menu to the GUI
        buildMenu(primaryStage);

        //Add tabs to the GUI
        buildSettingsTab();
        buildCurrentTab();
        buildTenMinuteHistoryTab();
        buildSixtyMinuteHistoryTab();
        buildOneDayHistoryTab();
        buildOneWeekHistoryTab();

        //Set preferred height/width for the pane
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        //Add the menubar and tabPane
        borderPane.setTop(menuBar);
        borderPane.setCenter(tabPane);

        //Add the borderpane object to the primary GUI
        root.getChildren().add(borderPane);

        scene.getStylesheets().add("application/application.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @Override
    public void start(Stage stage)
    {
        stage.setTitle("Temperature vs. Time");
        init(stage);
        stage.show();

        prepareTimeline();
    }

    /**
     * Method
     */
    private void prepareTimeline()
    {
    	//if(graphTimer != null)
    	//{
    	//	graphTimer.stop();
    	//}

    	graphTimer.start();
    }

    /**
     * Method used to setup the chart for temperature over the last 10 minutes
     */
    private void addDataToSeries1()
    {
        if (series1.getData().size() > NUM_SECONDS_IN_TEN_MINUTES )
        {
            series1.getData().remove(0, series1.getData().size() - NUM_SECONDS_IN_TEN_MINUTES );
        }

        series1XAxis.setLowerBound(xSeries1Data - NUM_SECONDS_IN_TEN_MINUTES );
        series1XAxis.setUpperBound(xSeries1Data - 1);
    }

    /**
     * Method used to setup the chart for temperature over the last 60 minutes
     */
    private void addDataToSeries2()
    {
        if (series2.getData().size() > NUM_SECONDS_IN_ONE_HOUR)
        {
            series2.getData().remove(0, series2.getData().size() - NUM_SECONDS_IN_ONE_HOUR);
        }

        series2XAxis.setLowerBound(xSeries2Data - NUM_SECONDS_IN_ONE_HOUR);
        series2XAxis.setUpperBound(xSeries2Data - 1);
    }

    /**
     * Method used to setup the chart for temperature over the last 24 hours
     */
    private void addDataToSeries3()
    {
        if (series3.getData().size() > NUM_SECONDS_IN_ONE_DAY)
        {
            series3.getData().remove(0, series3.getData().size() - NUM_SECONDS_IN_ONE_DAY);
        }

        series3XAxis.setLowerBound(xSeries3Data - NUM_SECONDS_IN_ONE_DAY);
        series3XAxis.setUpperBound(xSeries3Data - 1);
    }

    /**
     * Method used to setup the chart for temperature over the last week
     */
    private void addDataToSeries4()
    {
        if (series4.getData().size() > NUM_SECONDS_IN_ONE_WEEK)
        {
            series4.getData().remove(0, series4.getData().size() - NUM_SECONDS_IN_ONE_WEEK);
        }

        series4XAxis.setLowerBound(xSeries4Data - NUM_SECONDS_IN_ONE_WEEK);
        series4XAxis.setUpperBound(xSeries4Data - 1);
    }

    /**
     * Method Description
     *
     * @param primaryStage
     */
    private void buildMenu(Stage primaryStage)
    {
    	Menu menuFile = new Menu("File");

    	MenuItem startThermostatOption = new MenuItem("Start Thermostat");
    	startThermostatOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                System.out.println("Testing Start Thermostat Menu Item");
                thermostat.startSensorMeasurements();
            }
        });

    	MenuItem stopThermostatOption = new MenuItem("Stop Thermostat");
    	stopThermostatOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                System.out.println("Testing Stop Thermostat Menu Item");
                thermostat.stopSensorMeasurements();
            }
        });

    	MenuItem loadXMLSettingsOption = new MenuItem("Load XML Settings");
    	loadXMLSettingsOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                System.out.println("Load XML Settings was selected!");
                //TODO: Add code to handle file browser to load XML File

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open XML Settings File");
                File file = fileChooser.showOpenDialog(primaryStage);

                //This is the section that call needs to be made to load
                System.out.println("You selected: "+ file.getAbsolutePath());
            }
        });

    	MenuItem saveXMLSettingsOption = new MenuItem("Save XML Settings");
    	saveXMLSettingsOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                System.out.println("Save XML Settings was selected!");
                //TODO: Add code to save the current XML Settings

                FileChooser saveFileChooser = new FileChooser();
                saveFileChooser.setTitle("Save Application XML Settings File");
                File file = saveFileChooser.showSaveDialog(primaryStage);

                configureationFileSaver.buildXMLConfigurationFile(thermostat, file.getAbsolutePath());

                //This is the section that call needs to be made to load
                System.out.println("You selected: "+ file.getAbsolutePath());
            }
        });

    	MenuItem exportToCSVOption = new MenuItem("Export to CSV");
    	exportToCSVOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                System.out.println("Export to CSV");
                thermostat.stopSensorMeasurements();
                thermostat.printRecords();
            }
        });

    	MenuItem exitOption = new MenuItem("Exit");
    	exitOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                System.out.println("Exit");
                thermostat.stopSensorMeasurements();
                Platform.exit();
            }
        });

    	//Add MenuItems to the File Menu Dropdown
    	menuFile.getItems().add(startThermostatOption);
    	menuFile.getItems().add(stopThermostatOption);
    	menuFile.getItems().add(loadXMLSettingsOption);
    	menuFile.getItems().add(saveXMLSettingsOption);
    	menuFile.getItems().add(exportToCSVOption);
    	menuFile.getItems().add(exitOption);

    	//Add
		Menu menuEdit = new Menu("Edit");
		Menu menuHelp = new Menu("Help");

		//Add all menus to the menubar
		menuBar.getMenus().addAll(menuFile, menuEdit, menuHelp);
    }

    /**
     * Method used to build the settings tab of the Application
     */
    private void buildSettingsTab()
    {
    	//Settings Tab
        settingsTab.setText("Settings");

        settingsVBox.getChildren().add(new Label("High Threshold:"));
        highThresholdTextField.setMaxWidth(100);
        settingsVBox.getChildren().add(highThresholdTextField);

        settingsVBox.getChildren().add(new Label("Low Threshold:"));
        lowThresholdTextField.setMaxWidth(100);
        settingsVBox.getChildren().add(lowThresholdTextField);

        //Add ComboBox for Graph Options
        //graphSelectionComboBox.getItems().addAll("Last 10 Minutes","Last 60 Minutes","Last 24 Hours","Last Week");
       // graphSelectionComboBox.setValue("Last 10 Minutes");
        graphSelectionComboBox.valueProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(final ObservableValue ov, final String t, final String t1)
            {
                  if(t1.equals(options.get(0)))
                  {
                	  System.out.print("Equals Option1 Mofo!");
                	  //setGraphTimeSetting(NUM_SECONDS_IN_TEN_MINUTES);
                	  //prepareTimeline();
                  }
                  else if(t1.equals(options.get(1)))
                  {
                	  System.out.print("Equals Option2 Mofo!");
                	  //setGraphTimeSetting(NUM_SECONDS_IN_ONE_HOUR);
                	  //prepareTimeline( );
                  }
                  else if(t1.equals(options.get(2)))
                  {
                	  System.out.print("Equals Option3 Mofo!");
                	  //setGraphTimeSetting(NUM_SECONDS_IN_ONE_DAY);
                	  //prepareTimeline( );
                  }
                  else
                  {
                	  System.out.print("Equals Option4 Mofo!");
                	  //setGraphTimeSetting(NUM_SECONDS_IN_ONE_WEEK);
                	 // prepareTimeline( );
                  }

              }
          });
        settingsVBox.getChildren().add(graphSelectionComboBox);

        settingsApplyButton.setPrefWidth(100);
        settingsApplyButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
            	System.out.println("Pressed the Apply Button!");

            	try
            	{
            		double highThreshold = Double.parseDouble(highThresholdTextField.getText());
            		double lowThreshold = Double.parseDouble(lowThresholdTextField.getText());

            		if(highThreshold > lowThreshold)
            		{
            			thermostat.setHighThreshold(highThreshold);
            			thermostat.setLowThreshold(lowThreshold);
            		}
            	}
            	catch(NumberFormatException exception)
            	{
            		System.out.println("Please Input a Number");
            	}
            }
        });

        settingsVBox.getChildren().add(settingsApplyButton);

        settingsVBox.setAlignment(Pos.TOP_LEFT);
        settingsTab.setContent(settingsVBox);
        tabPane.getTabs().add(settingsTab);
    }


    /**
     * Method used to build the "Current" tab of the application GUI
     */
    private void buildCurrentTab()
    {
    	//Current Tab
        currentTab.setText("Current");

        //Vertical Box for the Tab's Components
        VBox currentTabVBoxContainer = new VBox();

        ToggleGroup temperatureScaleRadioButtonGroup = new ToggleGroup();

        RadioButton farenheitRadioButton = new RadioButton("Fahrenheit");
        farenheitRadioButton.setUserData("F");
        farenheitRadioButton.setToggleGroup(temperatureScaleRadioButtonGroup);
        farenheitRadioButton.setSelected(true);

        RadioButton celciusRadioButton = new RadioButton("Celcius");
        celciusRadioButton.setUserData("C");
        celciusRadioButton.setToggleGroup(temperatureScaleRadioButtonGroup);

        temperatureScaleRadioButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle)
            		{
                        if (temperatureScaleRadioButtonGroup.getSelectedToggle() != null)
                        {
                        	System.out.println(temperatureScaleRadioButtonGroup.getSelectedToggle().getUserData().toString());


                        	if(temperatureScaleRadioButtonGroup.getSelectedToggle().getUserData().toString() == "F")
                        	{
                        		System.out.print("Attempting to set units to F");
                        		thermostat.setTemperatureUnitsInFarenheit(true);
                        	}
                        	else
                        	{
                        		System.out.print("Attempting to set units to C");
                        		thermostat.setTemperatureUnitsInFarenheit(false);
                        	}

                        }
                    }
            });


        currentTabVBoxContainer.getChildren().add(farenheitRadioButton);
        currentTabVBoxContainer.getChildren().add(celciusRadioButton);



       // tempReadingTextField.setT
        tempReadingTextField.setMaxWidth(100);
        currentTabVBoxContainer.getChildren().add(tempReadingTextField);

        currentTabVBoxContainer.setAlignment(Pos.TOP_LEFT);

        currentTab.setContent(currentTabVBoxContainer);

        tabPane.getTabs().add(currentTab);
    }

    /**
     * Method used to build the "History" tab of the application GUI
     */
    private void buildTenMinuteHistoryTab()
    {
    	//History Tab
        Tab tab3 = new Tab();
        tab3.setText("History");
        HBox hbox3 = new HBox();


    	series1XAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        series1XAxis.setForceZeroInRange(false);
        series1XAxis.setAutoRanging(false);
        series1XAxis.setTickLabelsVisible(true);
        series1XAxis.setTickMarkVisible(true);
        series1XAxis.setMinorTickVisible(true);

        NumberAxis yAxis = new NumberAxis();

        // Create a LineChart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(series1XAxis, yAxis);

        lineChart.setAnimated(false);
        lineChart.setTitle("Temperature vs. Time");
        lineChart.setHorizontalGridLinesVisible(true);
        lineChart.setPrefWidth(780);

        // Set Name for Series
        series1.setName("TMP102 Reading");

        // Add Chart Series
        lineChart.getData().addAll(series1);

        hbox3.getChildren().add(lineChart);
        hbox3.setAlignment(Pos.CENTER);
        tab3.setContent(hbox3);
        tabPane.getTabs().add(tab3);
    }

    private void buildSixtyMinuteHistoryTab()
    {
    	//History Tab
        Tab sixtyMinuteHistoryTab = new Tab();
        sixtyMinuteHistoryTab.setText("History-Last 60 Minutes");
        HBox sixtyMinuteHistoryTabHBox = new HBox();

    	series2XAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        series2XAxis.setForceZeroInRange(false);
        series2XAxis.setAutoRanging(false);
        series2XAxis.setTickLabelsVisible(false);
        series2XAxis.setTickMarkVisible(false);
        series2XAxis.setMinorTickVisible(false);

        NumberAxis yAxis = new NumberAxis();

        // Create a LineChart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(series2XAxis, yAxis);

        lineChart.setAnimated(false);
        lineChart.setTitle("Temperature vs. Time (Last 60 Minutes)");
        lineChart.setHorizontalGridLinesVisible(true);
        lineChart.setPrefWidth(780);

        // Set Name for Series
        series2.setName("TMP102 Reading");

        // Add Chart Series
        lineChart.getData().addAll(series2);

        sixtyMinuteHistoryTabHBox.getChildren().add(lineChart);
        sixtyMinuteHistoryTabHBox.setAlignment(Pos.CENTER);
        sixtyMinuteHistoryTab.setContent(sixtyMinuteHistoryTabHBox);
        tabPane.getTabs().add(sixtyMinuteHistoryTab);
    }

    private void buildOneDayHistoryTab()
    {
    	//History Tab
        Tab oneDayHistoryTab = new Tab();
        oneDayHistoryTab.setText("History-Last 24 Hours");
        HBox oneDayHistoryTabHBox = new HBox();

    	series3XAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        series3XAxis.setForceZeroInRange(false);
        series3XAxis.setAutoRanging(false);
        series3XAxis.setTickLabelsVisible(false);
        series3XAxis.setTickMarkVisible(false);
        series3XAxis.setMinorTickVisible(false);

        NumberAxis yAxis = new NumberAxis();

        // Create a LineChart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(series3XAxis, yAxis);

        lineChart.setAnimated(false);
        lineChart.setTitle("Temperature vs. Time (Last 24 Hours)");
        lineChart.setHorizontalGridLinesVisible(true);
        lineChart.setPrefWidth(780);

        // Set Name for Series
        series3.setName("TMP102 Reading");

        // Add Chart Series
        lineChart.getData().addAll(series3);

        oneDayHistoryTabHBox.getChildren().add(lineChart);
        oneDayHistoryTabHBox.setAlignment(Pos.CENTER);
        oneDayHistoryTab.setContent(oneDayHistoryTabHBox);
        tabPane.getTabs().add(oneDayHistoryTab);
    }

    /**
     * Method Description
     */
    private void buildOneWeekHistoryTab()
    {
    	//History Tab
        Tab oneWeekHistoryTab = new Tab();
        oneWeekHistoryTab.setText("History-Last 24 Hours");
        HBox oneWeekHistoryTabHBox = new HBox();

    	series4XAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        series4XAxis.setForceZeroInRange(false);
        series4XAxis.setAutoRanging(false);
        series4XAxis.setTickLabelsVisible(false);
        series4XAxis.setTickMarkVisible(false);
        series4XAxis.setMinorTickVisible(false);

        NumberAxis yAxis = new NumberAxis();

        // Create a LineChart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(series4XAxis, yAxis);

        lineChart.setAnimated(false);
        lineChart.setTitle("Temperature vs. Time (Last 1 Week)");
        lineChart.setHorizontalGridLinesVisible(true);
        lineChart.setPrefWidth(780);

        // Set Name for Series
        series4.setName("TMP102 Reading");

        // Add Chart Series
        lineChart.getData().addAll(series4);

        oneWeekHistoryTabHBox.getChildren().add(lineChart);
        oneWeekHistoryTabHBox.setAlignment(Pos.CENTER);
        oneWeekHistoryTab.setContent(oneWeekHistoryTabHBox);
        tabPane.getTabs().add(oneWeekHistoryTab);
    }

    /**
     * Main Method
     *
     * @param args
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}