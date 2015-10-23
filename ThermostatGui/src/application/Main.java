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

import application.thermostat.Thermostat;
import application.thermostat.xml.XMLFileBuilder;
import application.thermostat.xml.XMLFileLoader;

/***
 * Class Description
 *
 * Date of Last Change: 2015-10-22
 *
 * @author J Nelson
 *
 */
public class Main extends Application
{
	/** Constants for number of points on the chart/graph */
    private static final int MAX_DATA_POINTS = 50;
    private static final int NUM_SECONDS_IN_DEFAULT = 30;
    private static final int NUM_SECONDS_IN_ONE_MINUTE = 60;
    private static final int NUM_SECONDS_IN_TEN_MINUTES = 600;
    private static final int NUM_SECONDS_IN_ONE_HOUR = 3600;
    private static final int NUM_SECONDS_IN_ONE_DAY = 86400;
    private static final int NUM_SECONDS_IN_ONE_WEEK = 604800;

	/** Comment */
	Group root = new Group();

	/** Comment */
	Scene scene = new Scene(root, 800, 500, Color.WHITE);

	/** Comment */
	TabPane tabPane = new TabPane();

	/** Comment */
	MenuBar menuBar = new MenuBar();

	/** Comment */
    private int graphTimeSetting = NUM_SECONDS_IN_TEN_MINUTES;

    /** Comment */
    int numGraphPoints = NUM_SECONDS_IN_DEFAULT;

    /** Comment */
    private XYChart.Series<Number, Number> series1 = new XYChart.Series<>();

    /** Comment */
    private int xSeries1Data = 0;

    /** Comment */
    private NumberAxis series1XAxis =new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);

    /** Used for the Graph */
    NumberAxis yAxis1 = new NumberAxis();
    final LineChart<Number, Number> lineChart1 = new LineChart<Number, Number>(series1XAxis, yAxis1);

    /** Comment */
    private ExecutorService executor;

    /** Comment */
    Tab settingsTab = new Tab();
    VBox settingsVBox = new VBox();
    TextField highThresholdTextField = new TextField();
    TextField lowThresholdTextField = new TextField();
    Button settingsApplyButton = new Button("Apply");

    /** Comment */
    ObservableList<String> options = FXCollections.observableArrayList(
    	        "Last 10 Minutes",
    	        "Last 60 Minutes",
    	        "Last 24 Hours",
    	        "Last Week"
    	    );

    /** Comment */
    AnimationTimer graphTimer = new AnimationTimer()
    {
        @Override
        public void handle(long now)
        {
        	addDataToSeries1();
        }
    };

    /** Comment */
    Tab currentTab = new Tab();

    /** XMLFileLoader to load application XML Settings file */
    XMLFileLoader configurationFileLoader = new XMLFileLoader();

    /** FileBuilder to save application XML Settings file */
    XMLFileBuilder configureationFileSaver = new XMLFileBuilder();

    /** Thermostat object to handle all major operations (primary backend object) */
    Thermostat thermostat = null;

    /** TextField for displyaing the current temperature reading */
    TextField tempReadingTextField = new TextField();

    /** Comment */
    Tab tab3 = new Tab();

    /** Comment */
    HBox hbox3 = new HBox();

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
	                	tempReadingTextField.setText(Double.toString((double)arg));
                	}
                	else //Display in Celcius
                	{
                		series1.getData().add(new XYChart.Data<>(xSeries1Data++, (((double)arg-32)*5)/9));
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

        //Set preferred height/width for the pane
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        //Add the menubar and tabPane
        borderPane.setTop(menuBar);
        borderPane.setCenter(tabPane);

        //Add the borderpane object to the primary GUI
        root.getChildren().add(borderPane);

        scene.getStylesheets().add("application/application.css");

        //Setup the primary stage
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
    	graphTimer.start();
    }

    private void changeGraph(int numPoints)
    {
    	System.out.println("Called changeGraph()------------------------------------------");

    	//if(numPoints )
    	//{
    		graphTimeSetting = numPoints;
    	//}
		/*
    	lineChart1.getData().removeAll(series1);
    	hbox3.getChildren().remove(lineChart1);

    	series1XAxis.setLowerBound(0);
        series1XAxis.setUpperBound(500);
        //series1XAxis.setScaleX(NUM_SECONDS_IN_ONE_WEEK);
       // lineChart1.getData().
        //lineChart1.getData().
        lineChart1.getData().addAll(series1);


        hbox3.getChildren().add(lineChart1);
        hbox3.setAlignment(Pos.CENTER);
        tab3.setContent(hbox3);

       // hbox3.getChildren().remove(lineChart1);
	*/
    }

    /**
     * Method used to setup the chart for temperature over the last 10 minutes
     */
    private void addDataToSeries1()
    {
    	series1XAxis.setLowerBound(xSeries1Data - graphTimeSetting );
        series1XAxis.setUpperBound(xSeries1Data - 1);
    }

    /**
     * Method used to setup the chart for temperature over the last 60 minutes
     */
    private void addDataToSeries2()
    {
    	series1XAxis.setLowerBound(xSeries1Data - NUM_SECONDS_IN_ONE_HOUR );
        series1XAxis.setUpperBound(xSeries1Data - 1);
    }

    /**
     * Method used to setup the chart for temperature over the last 24 hours
     */
    private void addDataToSeries3()
    {
    	series1XAxis.setLowerBound(xSeries1Data - NUM_SECONDS_IN_ONE_DAY );
        series1XAxis.setUpperBound(xSeries1Data - 1);
    }

    /**
     * Method used to setup the chart for temperature over the last week
     */
    private void addDataToSeries4()
    {
        series1XAxis.setLowerBound(xSeries1Data - NUM_SECONDS_IN_ONE_WEEK );
        series1XAxis.setUpperBound(xSeries1Data - 1);
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
                System.out.println("Series Size: "+series1.getData().size());
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

    	//Add MenuItems to the "File" Menu
    	menuFile.getItems().add(startThermostatOption);
    	menuFile.getItems().add(stopThermostatOption);
    	menuFile.getItems().add(loadXMLSettingsOption);
    	menuFile.getItems().add(saveXMLSettingsOption);
    	menuFile.getItems().add(exportToCSVOption);
    	menuFile.getItems().add(exitOption);

    	//Add
		Menu menuEdit = new Menu("Edit");

		MenuItem graphDisplayOfThirtySeconds = new MenuItem("Display Last 30 Seconds");
		graphDisplayOfThirtySeconds.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                changeGraph(NUM_SECONDS_IN_DEFAULT);
            }
        });

		MenuItem graphDisplayOfLastOneMinute = new MenuItem("Display Last 60 Seconds");
		graphDisplayOfLastOneMinute.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                changeGraph(NUM_SECONDS_IN_ONE_MINUTE);
            }
        });

		MenuItem graphDisplayOfLastOneHour = new MenuItem("Display Last 60 Minutes");
		graphDisplayOfLastOneHour.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                changeGraph(NUM_SECONDS_IN_ONE_HOUR);
            }
        });
		MenuItem graphDisplayOfLastOneDay = new MenuItem("Display Last 24 Hours");
		graphDisplayOfLastOneDay.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                changeGraph(NUM_SECONDS_IN_ONE_DAY);
            }
        });
		MenuItem graphDisplayOfLastWeek = new MenuItem("Display Last 1 Week");
		graphDisplayOfLastWeek.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                changeGraph(NUM_SECONDS_IN_ONE_WEEK);
            }
        });

		//Add MenuItems to the "Edit" Menu
    	menuEdit.getItems().add(graphDisplayOfThirtySeconds);
    	menuEdit.getItems().add(graphDisplayOfLastOneMinute);
    	menuEdit.getItems().add(graphDisplayOfLastOneHour);
    	menuEdit.getItems().add(graphDisplayOfLastOneDay);
    	menuEdit.getItems().add(graphDisplayOfLastWeek);

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
    	tab3.setText("History");

        series1XAxis.setForceZeroInRange(false);
        series1XAxis.setAutoRanging(false);
        series1XAxis.setTickLabelsVisible(true);
        series1XAxis.setTickMarkVisible(true);
        series1XAxis.setMinorTickVisible(true);

        lineChart1.setAnimated(false);
        lineChart1.setTitle("Temperature vs. Time");
        lineChart1.setHorizontalGridLinesVisible(true);
        lineChart1.setPrefWidth(780);

        // Set Name for Series
        series1.setName("TMP102 Reading");

        // Add Chart Series
        lineChart1.getData().addAll(series1);

        hbox3.getChildren().add(lineChart1);
        hbox3.setAlignment(Pos.CENTER);
        tab3.setContent(hbox3);
        tabPane.getTabs().add(tab3);
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