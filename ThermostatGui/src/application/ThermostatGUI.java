package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jssc.SerialPortException;

import java.io.File;
import java.util.Observable;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import application.thermostat.Thermostat;
import application.thermostat.message.MessageReceiver;
import application.thermostat.xml.XMLFileBuilder;
import application.thermostat.xml.XMLFileLoader;

/***
 * The ThermostatGUI class represents the front-end GUI.
 *
 * Date of Last Change: 2015-11-13
 *
 * @author J Nelson
 *
 */
public class ThermostatGUI extends Application
{
	/** Constants for number of points on the chart/graph */
    private final int NUM_SECONDS_IN_HALF_MINUTE= 30;
    private final int NUM_SECONDS_IN_ONE_MINUTE = 60;
    private final int NUM_SECONDS_IN_TEN_MINUTES = 600;
    private final int NUM_SECONDS_IN_ONE_HOUR = 3600;
    private final int NUM_SECONDS_IN_ONE_DAY = 86400;
    private final int NUM_SECONDS_IN_ONE_WEEK = 604800;

    /** Values for the default Thermostat GUI size */
    private static final int GUI_WIDTH = 800;
    private static final int GUI_HEIGHT = 500;

    /** Default values for the graph lower and upper bounds */
    private final int GRAPH_LOWER_BOUND = 0;
    private final int GRAPH_UPPER_BOUND = 5;
    private final int TICK_UNIT = 1;

    /** Default display setting for history graph */
    private int graphTimeSetting = NUM_SECONDS_IN_ONE_MINUTE;

	/** Root node for the GUI  */
	private Group guiRoot = new Group();

	/** Scene */
	private Scene guiScene = new Scene(guiRoot, GUI_WIDTH, GUI_HEIGHT, Color.WHITE);

	/** TabPane container for adding all GUI tabs */
	private TabPane thermostatTabPane = new TabPane();

	/** Menubar for the GUI */
	private MenuBar thermostatMenuBar = new MenuBar();

    /** Chart for the temperature measurements */
    private XYChart.Series<Number, Number> tempMeasurementSeries = new XYChart.Series<>();

    /** The number of measured points in the tempMeasurementSeries XYChart */
    private int tempMeasurementSeriesPointCount = 1;

    /** X and Y axes for the line chart that displays the temperature measurements */
    private NumberAxis tempMeasurementSeriesXAxis = new NumberAxis(GRAPH_LOWER_BOUND, GRAPH_UPPER_BOUND, TICK_UNIT);
    private NumberAxis tempMeasurementSeriesYAxis = new NumberAxis();

    /** Line chart for the temperature measurements */
    private final LineChart<Number, Number> tempMeasurementChart =
    		new LineChart<Number, Number>(tempMeasurementSeriesXAxis, tempMeasurementSeriesYAxis);

    /** GUI Components for the Settings Tab */
    private Tab settingsTab = new Tab();
    private VBox settingsTabVerticalLayoutBox = new VBox();
    private TextField highThresholdTextField = new TextField();
    private TextField lowThresholdTextField = new TextField();
    private Button settingsApplyButton = new Button("Apply");

    /** GUI Components for the "Current" Tab */
    private Tab currentTab = new Tab();
    private TextField tempReadingTextField = new TextField();

    /** GUI Components for the "History" Tab */
    private Tab historyTab = new Tab();
    private HBox historyTabHorizontalLayoutBox = new HBox();

    /** AnimationTimer for updating the graph in real time */
    private AnimationTimer graphUpdateTimer = new AnimationTimer()
    {
        @Override
        public void handle(long now)
        {
        	updateGraph();
        }
    };

    /** XMLFileLoader to load application XML Settings file */
    private XMLFileLoader configurationFileLoader = new XMLFileLoader();

    /** FileBuilder to save application XML Settings file */
    private XMLFileBuilder configurationFileSaver = new XMLFileBuilder();

    /** Thermostat object to handle all major operations (primary backend object) */
    private Thermostat thermostat = null;

    /**
     * The init() method required by the Application class.
     * Called after Application initialization.
     *
     * @param primaryStage The stage.
     */
    private void init(Stage primaryStage)
    {
    	//Load Configuration File and Add Observers
    	thermostat = configurationFileLoader.loadXMLConfigurationFile();

    	//Setup the primary stage
    	primaryStage.setTitle("Thermostat");

        BorderPane borderPane = new BorderPane();

        //Add data observers
        thermostat.addObserver((Observable obj, Object arg)->
        {
        	//Used to keep in JavaFX Thread
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                	if(thermostat.isTemperatureUnitsInFarenheit()) //Display in Farenheit
                	{
                		tempMeasurementSeries.getData().add(new XYChart.Data<>(tempMeasurementSeriesPointCount++, (double)arg));
                		tempReadingTextField.setText(String.format("%.2f", (double)arg));
                	}
                	else //Display in Celcius
                	{
                		tempMeasurementSeries.getData().add(new XYChart.Data<>(tempMeasurementSeriesPointCount++, (((double)arg-32)*5)/9));
	                	tempReadingTextField.setText(String.format("%.2f", ((((double)arg-32)*5)/9)));
                	}
                }
           });
        });

        //Add menu to the GUI
        buildMenu(primaryStage);

        //Add tabs to the GUI
        buildSettingsTab();
        buildCurrentTab();
        buildHistoryTab();

        //Set preferred height/width for the pane
        borderPane.prefHeightProperty().bind(guiScene.heightProperty());
        borderPane.prefWidthProperty().bind(guiScene.widthProperty());

        //Set where users can't accidentally close the tabs
        thermostatTabPane.tabClosingPolicyProperty().set(TabClosingPolicy.UNAVAILABLE);

        //Add the menubar and tabPane
        borderPane.setTop(thermostatMenuBar);
        borderPane.setCenter(thermostatTabPane);

        //Add the borderpane object to the primary GUI and apply CSS file
        guiRoot.getChildren().add(borderPane);
        guiScene.getStylesheets().add("application/application.css");

        //Setup the primary stage
        primaryStage.setScene(guiScene);
        primaryStage.show();
    }

    @Override
    public void start(Stage stage)
    {
        stage.setTitle("Temperature vs. Time");
        init(stage);
        stage.show();

        startTimeline();
    }

    /**
     * Method used to start the AnimationTimer for the Graph
     */
    private void startTimeline()
    {
    	graphUpdateTimer.start();
    }

    /**
     * Method used to change the graph display
     *
     * @param numPoints The number of points for the graph
     */
    private void changeGraph(int numPoints)
    {
    	graphTimeSetting = numPoints;

    	if(numPoints == NUM_SECONDS_IN_HALF_MINUTE)
    	{
    		tempMeasurementSeriesXAxis.setTickUnit(1);	// one tick/per second
    		System.out.println("tempMeasurementSeriesXAxis.setTickUnit(1);");
    	}
    	else if(numPoints == NUM_SECONDS_IN_ONE_MINUTE)
    	{
    		tempMeasurementSeriesXAxis.setTickUnit(2);	// one tick/two seconds
    		System.out.println("tempMeasurementSeriesXAxis.setTickUnit(2);");
    	}
    	else if(numPoints == NUM_SECONDS_IN_TEN_MINUTES)
    	{
    		tempMeasurementSeriesXAxis.setTickUnit(60);	// one tick/minute
    		System.out.println("tempMeasurementSeriesXAxis.setTickUnit(60);");
    	}
    	else if(numPoints == NUM_SECONDS_IN_ONE_HOUR)
    	{
    		tempMeasurementSeriesXAxis.setTickUnit(360); // one tick/six minutes
    		System.out.println("tempMeasurementSeriesXAxis.setTickUnit(360);");
    	}
    	else if(numPoints == NUM_SECONDS_IN_ONE_DAY)
    	{
    		tempMeasurementSeriesXAxis.setTickUnit(3600); // one tick/hour
    		System.out.println("tempMeasurementSeriesXAxis.setTickUnit(3600);");
    	}
    	else if(numPoints == NUM_SECONDS_IN_ONE_WEEK)
    	{
    		tempMeasurementSeriesXAxis.setTickUnit(86400);	// one tick/day
    		System.out.println("tempMeasurementSeriesXAxis.setTickUnit(86400);");
    	}
    	else
    	{
    		//Do Nothing
    	}
    }

    /**
     * Method which is used to update the graph as the temperature measurements are received.
     * Every time that a new measurement is taken, the graph updates to remove the oldest point.
     * This is done based upon the display of the graph that is chosen from the
     * "Edit" menu.
     */
    private void updateGraph()
    {
    	tempMeasurementSeriesXAxis.setLowerBound(tempMeasurementSeriesPointCount - graphTimeSetting );
    	tempMeasurementSeriesXAxis.setUpperBound(tempMeasurementSeriesPointCount - 1);
    }

    /**
     * Method which is used to build the different menus at the top of the Thermostat application window.
     *
     * @param primaryStage The primary stage.
     */
    private void buildMenu(Stage primaryStage)
    {
    	//Create the different available menus
    	Menu fileMenu = new Menu("File");

    	//Create the different "File" menu options and add their event handlers
    	MenuItem startThermostatOption = new MenuItem("System ON");
    	startThermostatOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                thermostat.startSensorMeasurements();
            }
        });

    	MenuItem stopThermostatOption = new MenuItem("System OFF");
    	stopThermostatOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                thermostat.stopSensorMeasurements();
            }
        });

    	MenuItem loadXMLSettingsOption = new MenuItem("Load XML Settings");
    	loadXMLSettingsOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
            	FileChooser loadfileChooser = new FileChooser();
            	loadfileChooser.setTitle("Open XML Settings File");
                File file = loadfileChooser.showOpenDialog(primaryStage);

                if(file != null)
                {
                	configurationFileLoader.setDefaultFilePath(file.getAbsolutePath());
                	thermostat = configurationFileLoader.loadXMLConfigurationFile();
                	updateSettingsTab();
                }
            }
        });

    	MenuItem saveXMLSettingsOption = new MenuItem("Save XML Settings");
    	saveXMLSettingsOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
            	FileChooser saveFileChooser = new FileChooser();
                saveFileChooser.setTitle("Save Application XML Settings File");
                File file = saveFileChooser.showSaveDialog(primaryStage);

                if(file != null)
                {
                	configurationFileSaver.buildXMLConfigurationFile(thermostat, file.getAbsolutePath());
                }
            }
        });

    	MenuItem exportToCSVOption = new MenuItem("Export to CSV");
    	exportToCSVOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                thermostat.stopSensorMeasurements();
                thermostat.saveTemperatureRecords();
            }
        });

    	MenuItem exitOption = new MenuItem("Exit");
    	exitOption.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                thermostat.stopSensorMeasurements();

                try
                {
					MessageReceiver.getSerialPortInstance().closePort();
				}
                catch (SerialPortException e)
                {
					e.printStackTrace();
				}

                Platform.exit();
            }
        });

    	//Add MenuItems to the "File" Menu
    	fileMenu.getItems().add(startThermostatOption);
    	fileMenu.getItems().add(stopThermostatOption);
    	fileMenu.getItems().add(new SeparatorMenuItem());
    	fileMenu.getItems().add(loadXMLSettingsOption);
    	fileMenu.getItems().add(saveXMLSettingsOption);
    	fileMenu.getItems().add(new SeparatorMenuItem());
    	fileMenu.getItems().add(exportToCSVOption);
    	fileMenu.getItems().add(new SeparatorMenuItem());
    	fileMenu.getItems().add(exitOption);

    	//Create the "Edit" Menu
		Menu menuEdit = new Menu("Edit");

		//Create the "Edit" Menu Items and add their EventHandlers
		MenuItem graphDisplayOfThirtySeconds = new MenuItem("Graph the Last 30 Seconds");
		graphDisplayOfThirtySeconds.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                changeGraph(NUM_SECONDS_IN_HALF_MINUTE);
            }
        });

		MenuItem graphDisplayOfLastOneMinute = new MenuItem("Graph the Last 1 Minute");
		graphDisplayOfLastOneMinute.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                changeGraph(NUM_SECONDS_IN_ONE_MINUTE);
            }
        });

		MenuItem graphDisplayOfLastOneHour = new MenuItem("Graph the Last 1 Hour");
		graphDisplayOfLastOneHour.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                changeGraph(NUM_SECONDS_IN_ONE_HOUR);
            }
        });

		MenuItem graphDisplayOfLastOneDay = new MenuItem("Graph the Last 1 Day");
		graphDisplayOfLastOneDay.setOnAction(new EventHandler<ActionEvent>()
    	{
            public void handle(ActionEvent t)
            {
                changeGraph(NUM_SECONDS_IN_ONE_DAY);
            }
        });

		MenuItem graphDisplayOfLastWeek = new MenuItem("Graph the Last 1 Week");
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

    	//Create the "Help" Menu
		Menu menuHelp = new Menu("Help");

		//Add the Menus to the Menubar
		thermostatMenuBar.getMenus().addAll(fileMenu, menuEdit, menuHelp);
    }

    /**
     * Method used to build the settings tab of the Application
     */
    private void buildSettingsTab()
    {
    	int textFieldWidth = 100;
    	int buttonWidth = 100;

    	//Settings Tab
        settingsTab.setText("Settings");

        settingsTabVerticalLayoutBox.getChildren().add(new Label("High Threshold:"));
        highThresholdTextField.setMaxWidth(textFieldWidth);
        settingsTabVerticalLayoutBox.getChildren().add(highThresholdTextField);

        settingsTabVerticalLayoutBox.getChildren().add(new Label("Low Threshold:"));
        lowThresholdTextField.setMaxWidth(textFieldWidth);
        settingsTabVerticalLayoutBox.getChildren().add(lowThresholdTextField);

        settingsApplyButton.setPrefWidth(buttonWidth);
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
            		else
            		{
            			Alert errorAlert = new Alert(AlertType.ERROR);
            			errorAlert.setTitle("Invalid Input Value");
            			errorAlert.setContentText("Please enter a value for Low Threshold that is less than that of High Threshold");
            			errorAlert.showAndWait();
            		}
            	}
            	catch(NumberFormatException exception)
            	{
            		Alert errorAlert = new Alert(AlertType.ERROR);
        			errorAlert.setTitle("Invalid Input Value");
        			errorAlert.setContentText("Please enter a valid number.");
        			errorAlert.showAndWait();
            	}
            }
        });

        settingsTabVerticalLayoutBox.getChildren().add(settingsApplyButton);

        //Add current settings into the textfields, etc.
        updateSettingsTab();

        settingsTabVerticalLayoutBox.setAlignment(Pos.TOP_LEFT);
        settingsTab.setContent(settingsTabVerticalLayoutBox);
        thermostatTabPane.getTabs().add(settingsTab);
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
        HBox temperatureUnitHBoxContainer = new HBox();

        //ToggleGroup for the temperature unit radio buttons
        ToggleGroup temperatureScaleRadioButtonGroup = new ToggleGroup();

        //Build radio buttons for temperature unit selection
        RadioButton farenheitRadioButton = new RadioButton("Fahrenheit");
        farenheitRadioButton.setUserData("F");
        farenheitRadioButton.setToggleGroup(temperatureScaleRadioButtonGroup);
        farenheitRadioButton.setSelected(true);

        RadioButton celciusRadioButton = new RadioButton("Celcius");
        celciusRadioButton.setUserData("C");
        celciusRadioButton.setToggleGroup(temperatureScaleRadioButtonGroup);

        //Add listener for the button toggle group
        temperatureScaleRadioButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle)
            {
            	if (temperatureScaleRadioButtonGroup.getSelectedToggle() != null)
            	{
            		if(temperatureScaleRadioButtonGroup.getSelectedToggle().getUserData().toString() == "F")
            		{
            			thermostat.setTemperatureUnitsInFarenheit(true);
            		}
            		else
            		{
            			thermostat.setTemperatureUnitsInFarenheit(false);
            		}

            	}
            }
            });

        //Add radio buttons to the container and set alignment
        temperatureUnitHBoxContainer.getChildren().add(farenheitRadioButton);
        temperatureUnitHBoxContainer.getChildren().add(celciusRadioButton);
        temperatureUnitHBoxContainer.setAlignment(Pos.CENTER);

        Font font1 = new Font("Helvetica", 120);
        tempReadingTextField.setMaxWidth(600);
        tempReadingTextField.setMaxHeight(300);
        tempReadingTextField.setPrefHeight(300);
        tempReadingTextField.setFont(font1);
        tempReadingTextField.setAlignment(Pos.CENTER);
        tempReadingTextField.setStyle("-fx-text-box-border: transparent;-fx-text-fill: grey;");

        //Add components to container and set alignment
        currentTabVBoxContainer.getChildren().add(temperatureUnitHBoxContainer);
        currentTabVBoxContainer.getChildren().add(tempReadingTextField);
        currentTabVBoxContainer.setAlignment(Pos.CENTER);

        currentTab.setContent(currentTabVBoxContainer);

        thermostatTabPane.getTabs().add(currentTab);
    }

    /**
     * Method used to build the "History" tab of the application GUI
     */
    private void buildHistoryTab()
    {
    	historyTab.setText("History");

    	//Set X Axis properties
    	tempMeasurementSeriesXAxis.setForceZeroInRange(false);
    	tempMeasurementSeriesXAxis.setAutoRanging(false);
    	tempMeasurementSeriesXAxis.setTickLabelsVisible(true);
    	tempMeasurementSeriesXAxis.setTickMarkVisible(true);
    	tempMeasurementSeriesXAxis.setMinorTickVisible(false);

    	//Set Axis Labels
    	tempMeasurementSeriesXAxis.setLabel("Seconds Past");
    	tempMeasurementSeriesYAxis.setLabel("Temperature (Degrees)");

    	//Set Chart properties
    	tempMeasurementChart.setAnimated(false);
    	tempMeasurementChart.setTitle("Temperature vs. Time");
    	tempMeasurementChart.setHorizontalGridLinesVisible(true);
    	tempMeasurementChart.setVerticalGridLinesVisible(true);
    	tempMeasurementChart.setCreateSymbols(false);
    	tempMeasurementChart.prefHeightProperty().bind(guiScene.heightProperty());
    	tempMeasurementChart.prefWidthProperty().bind(guiScene.widthProperty());

        tempMeasurementSeries.setName("Temperature Reading");

        tempMeasurementChart.getData().addAll(tempMeasurementSeries);

        historyTabHorizontalLayoutBox.getChildren().add(tempMeasurementChart);
        historyTabHorizontalLayoutBox.setAlignment(Pos.CENTER);
        historyTab.setContent(historyTabHorizontalLayoutBox);

        thermostatTabPane.getTabs().add(historyTab);
    }

    /**
     * Method used to update and load information into the GUI components of the
     * Settings tab.
     */
    private void updateSettingsTab()
    {
    	highThresholdTextField.setText(Double.toString(thermostat.getHighThreshold()));
    	lowThresholdTextField.setText(Double.toString(thermostat.getLowThreshold()));
    }

    /**
     * Method Description
     *
     * @param messageToDisplay The message to display on the error dialog box
     */
    public static void showErrorDialog(String messageToDisplay)
    {
    	Platform.runLater(new Runnable()
    	{
            @Override
            public void run()
            {
		    	try
		    	{
		    		Alert errorAlert = new Alert(AlertType.ERROR);
		    		errorAlert.setTitle("Error");
		    		errorAlert.setContentText(messageToDisplay);
		    		errorAlert.showAndWait();
		    	}
		    	catch(Exception e)
		    	{
		    		System.out.println(e);
		    	}
            }
        });
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