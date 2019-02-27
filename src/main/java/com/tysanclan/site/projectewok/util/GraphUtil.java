/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.util;

import org.wicketstuff.jqplot.JqPlotChart;
import org.wicketstuff.jqplot.lib.ChartConfiguration;
import org.wicketstuff.jqplot.lib.JqPlotResources;
import org.wicketstuff.jqplot.lib.axis.YAxis;
import org.wicketstuff.jqplot.lib.chart.BarChart;
import org.wicketstuff.jqplot.lib.chart.LineChart;
import org.wicketstuff.jqplot.lib.chart.PieChart;
import org.wicketstuff.jqplot.lib.elements.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GraphUtil {

	private GraphUtil() {
	}

	public static <T extends Number> JqPlotChart makePieChart(String id, String title,
															  Map<String, T> data) {
		PieChart<T> chart = new PieChart<>();
		ChartConfiguration<String> configuration = chart.getChartConfiguration();

		configuration.getTitle().setText(title);
		Grid<String> grid;
		configuration.setGrid(grid = new Grid<>());

		grid.setBackground("#000000");
		configuration.getLegend().setShow(true);

		RendererOptions rendererOptions = configuration.getSeriesDefaults().getRendererOptions();

		data.forEach(chart::addValue);

		rendererOptions.setSliceMargin(0);

		return new JqPlotChart(id, chart);
	}


	public static JqPlotChart makeReservesBarChart(String id, String title,
												   SortedMap<String, BigDecimal> donationsPerUser) {
		BarChart<BigDecimal> chart = new BarChart<>();

		ChartConfiguration<Long> configuration = chart.getChartConfiguration();
		configuration.getTitle().setText(title);
		configuration.setGrid(new Grid<>());
		configuration.getGrid().setBackground("#000000");
		Axes<Long> axes = configuration.getAxes();

		axes.setYaxis(new YAxis<>());
		axes.getYaxis().setLabel("USD");
		axes.getXaxis().setMin(BigDecimal.ZERO);
		axes.getXaxis().setLabel("Donator");

		configuration.setLegend(new Legend());
		configuration.getLegend().setShow(true);
		configuration.getLegend().setShowLables(true);

		chart.addValue(donationsPerUser.values());
		chart.getChartConfiguration().getSeriesDefaults()
			 .setPointLabels(new PointLabels().setLabels(new ArrayList<>(donationsPerUser.keySet())));

		return new JqPlotChart(id, chart);
	}

	public static <T extends Number> JqPlotChart makeDonationsBarChart(String id, String title,
																	   SortedMap<String, T> donationsPerUser) {
		BarChart<T> chart = new BarChart<>();

		ChartConfiguration<Long> configuration = chart.getChartConfiguration();
		configuration.getTitle().setText(title);
		Grid<Long> grid;
		configuration.setGrid(grid = new Grid<>());
		grid.setBackground("#000000");
		Axes<Long> axes = configuration.getAxes();

		axes.setYaxis(new YAxis<>());
		axes.getYaxis().setLabel("USD");
		axes.getXaxis().setLabel("Donator");

		configuration.setLegend(new Legend());
		configuration.getLegend().setShow(true);
		configuration.getLegend().setShowLables(true);

		configuration.getSeriesDefaults().getRendererOptions().setBarWidth(8);

		chart.addValue(donationsPerUser.values());
		chart.getChartConfiguration().getSeriesDefaults()
			 .setPointLabels(new PointLabels().setLabels(new ArrayList<>(donationsPerUser.keySet())));

		return new JqPlotChart(id, chart);
	}

	public static JqPlotChart makeMemberCountLineChart(String id, String title,
													   SortedMap<Date, Integer> counts) {
		LineChart<Integer> chart = new LineChart<>();

		ChartConfiguration<String> configuration = chart.getChartConfiguration();

		configuration.getTitle().setText(title);
		Grid<String> grid;
		configuration.setGrid(grid = new Grid<>());
		grid.setBackground("#000000");
		Axes<String> axes = configuration.getAxes();

		int max = counts.values().stream().max(Comparator.naturalOrder()).orElse(0);

		axes.setYaxis(new YAxis<>());
		axes.getYaxis().setLabel("Members");
		axes.getXaxis().setLabel("Date");
		axes.getXaxis().setRenderer(JqPlotResources.DateAxisRenderer);
		axes.getXaxis().setMin(0);
		max = max + (50 - (max % 50));
		axes.getXaxis().setMax(max);

		return new JqPlotChart(id, chart);

	}

	public static JqPlotChart makeCashFlowLineChart(String id, String title,
													SortedMap<Date, BigDecimal> cashFlow) {
		LineChart<BigDecimal> chart = new LineChart<>();

		ChartConfiguration<String> configuration = chart.getChartConfiguration();

		configuration.getTitle().setText(title);
		Grid<String> grid;
		configuration.setGrid(grid = new Grid<>());
		grid.setBackground("#000000");
		Axes<String> axes = configuration.getAxes();

		axes.setYaxis(new YAxis<>());
		axes.getYaxis().setLabel("Cash");
		axes.getXaxis().setLabel("Date");
		axes.getXaxis().setRenderer(JqPlotResources.DateAxisRenderer);
		axes.getXaxis().setMin(0);

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		cashFlow.values().forEach(chart::addValue);
		chart.getChartConfiguration().setSeriesDefaults(new SeriesDefaults());
		chart.getChartConfiguration().getSeriesDefaults()
			 .setPointLabels(new PointLabels().setLabels(cashFlow
																 .keySet()
																 .stream()
																 .map(format::format)
																 .collect(Collectors.toList())
			 ));
		return new JqPlotChart(id, chart);
	}
}
