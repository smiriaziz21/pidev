package Controllers;

import Service.ServiceFeedback;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class FeedbackStatistiquesController implements Initializable {

    @FXML
    private BorderPane chartContainer;

    private final ServiceFeedback serviceFeedback = new ServiceFeedback();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayChart();
    }

    private void displayChart() {
        try {
            Map<Integer, Double> eventRatings = serviceFeedback.getEventRatings();

            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("Événements");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Évaluation Moyenne");
            yAxis.setTickUnit(1);
            yAxis.setLowerBound(1);
            yAxis.setUpperBound(5);

            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Évaluation par Événement");

            for (Map.Entry<Integer, Double> entry : eventRatings.entrySet()) {
                int rating = entry.getValue().intValue();
                if (rating >= 1 && rating <= 5) {
                    series.getData().add(new XYChart.Data<>(entry.getKey(), rating));
                }
            }

            lineChart.getData().add(series);

            chartContainer.setCenter(lineChart);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
