// Anthony Liscio

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application
{
    // lists for warning types and count of each warning type
    List<String> warningTypes = new ArrayList<String>();
    List<Integer> warningTypesCount = new ArrayList<Integer>();

    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * start method to start the JavaFX GUI
     * @param primaryStage - JavaFX Stage
     */
    @Override
    public void start(Stage primaryStage)
    {
        // get the warning types and count
        getWarningTypeData();

        //** Pie Chart **
        PieChart pieChart = new PieChart();

        List<PieChart.Data> dataSlices = new ArrayList<PieChart.Data>();

        // pie chart data
        for (int i = 0; i < warningTypes.size(); i++) {
            PieChart.Data slice = new PieChart.Data(warningTypes.get(i), warningTypesCount.get(i));
            //dataSlices.add(slice);
            pieChart.getData().add(slice);
        }

        // custom colours
        String[] pieColours = {"cyan", "gold", "orange", "salmon"};
        applyCustomColours(pieChart.getData(), pieColours);
        pieChart.setLegendVisible(true);        // legend was not matching the custom colours

        // VBox
        VBox vbox = new VBox(pieChart);

        // scene
        Scene scene = new Scene(vbox, 700, 500);
        primaryStage.setScene(scene);

        // stage
        primaryStage.setTitle("Weather Warnings");
        primaryStage.setHeight(500);
        primaryStage.setWidth(1200);
        primaryStage.show();
    }

    /**
     * applyCustomColours method used to apply a custom set of colours to the pie chart data
     * @param pieChartData - list of the pie chart data
     * @param pieColours - String array of the custom colour names
     */
    private void applyCustomColours(List<PieChart.Data> pieChartData, String[] pieColours)
    {
        int i = 0;
        for (PieChart.Data data : pieChartData)
        {
            data.getNode().setStyle("-fx-pie-color: " + pieColours[i % pieColours.length] + ";");
            i++;
        }
    }

    /**
     * getWarningTypeData method used to get the warning types and the count of each warning type
     */
    public void getWarningTypeData()
    {
        boolean warningTypeNotFoundYet = true;

        // path to the csv file
        String currentPath = System.getProperty("user.dir");
        currentPath += "/src/main/resources/weatherwarnings-2015.csv";

        try
        {
            BufferedReader input = new BufferedReader(new FileReader(currentPath));
            String line = null;

            // reading line by line
            while ((line = input.readLine()) != null)
            {
                String [] lineArray = line.split(",");      // splitting the String into a String[], using commas
                                                                  // as delimiters since it's coming from a csv file

                // if the warningType list is empty, add the element to warningTypes and warningTypesCount
                if (warningTypes.isEmpty())
                {
                    warningTypes.add(lineArray[5]);
                    warningTypesCount.add(1);
                }
                else  // if the warningType list is not empty
                {
                    // for loop to check each element of the warningTypes list
                    for (int j = 0; j < warningTypes.size(); j++)
                    {
                        // if there's a match, increase the appropriate element in the warningTypesCount list
                        // and update the warningTypeNotFoundYet boolean variable
                        if (warningTypes.get(j).equals(lineArray[5]))
                        {
                            warningTypesCount.set(j, warningTypesCount.get(j) + 1);
                            warningTypeNotFoundYet = false;
                            break;
                        }
                    }

                    // if a match was not found, add new elements to the list, add put the count as 1
                    if (warningTypeNotFoundYet)
                    {
                        warningTypes.add(lineArray[5]);
                        warningTypesCount.add(1);
                    }
                }
                warningTypeNotFoundYet = true;
            }
        }
        catch (FileNotFoundException fnf)
        {
            System.out.println("Filepath not found");
        }
        catch (IOException io)
        {
            System.out.println("Input exception");
        }

    }
}
