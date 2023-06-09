package org.sixLoadTester.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sixLoadTester.data.Request;
import org.sixLoadTester.data.ResponseStatistics;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.NoSuchElementException;

public class StatisticsUtils {
    public static void createChart(Request request, List<Long> responseTimes) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < responseTimes.size(); i++) {
            dataset.addValue(responseTimes.get(i), "Response Time", Integer.valueOf(i));
        }

        JFreeChart chart = ChartFactory.createLineChart(request.method.toString() + " " + request.endpoint + " Response Times", "Requests Over Time",
                "Response Time (ms)", dataset, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelsVisible(false);
        domainAxis.setTickMarksVisible(false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Display the chart
        JFrame frame = new JFrame(request.method.toString() + " " + request.endpoint + " Response Times");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void calculateStatistics(ResponseStatistics responseStatistics)
    {
        List<Long> responseTimes = responseStatistics.responseTimes;
        float avgResponseTime = responseTimes.stream().mapToLong(response -> response).sum() / ((float) responseTimes.size());
        long minTime = responseTimes.stream().mapToLong(response -> response).min().orElseThrow(NoSuchElementException::new);
        long maxTime = responseTimes.stream().mapToLong(response -> response).max().orElseThrow(NoSuchElementException::new);

        System.out.println();
        System.out.println("Statistics");
        System.out.println("-------------------------------------------------------");
        System.out.println("Average response time: " + avgResponseTime + "ms");
        System.out.println("Minimum response time: " + minTime + "ms");
        System.out.println("Maximum response time: " + maxTime + "ms");
        System.out.println("Maximum requests per second: " + responseStatistics.maxRequestsPerSecond);
        System.out.println("Error count: " + responseStatistics.errorCount);
    }
}
