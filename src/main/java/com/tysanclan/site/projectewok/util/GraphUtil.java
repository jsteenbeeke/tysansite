/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.util;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.topicus.wqplot.components.JQPlot;
import nl.topicus.wqplot.components.plugins.JQPlotBezierCurveRenderer;
import nl.topicus.wqplot.components.plugins.JQPlotCategoryAxisRenderer;
import nl.topicus.wqplot.components.plugins.JQPlotDateAxisRenderer;
import nl.topicus.wqplot.components.plugins.Renderer;
import nl.topicus.wqplot.data.AbstractSeries;
import nl.topicus.wqplot.data.DateNumberSeries;
import nl.topicus.wqplot.options.PlotBarRendererOptions;
import nl.topicus.wqplot.options.PlotLineRendererOptions;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotPieRendererOptions;
import nl.topicus.wqplot.options.PlotTitle;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

public class GraphUtil {

	private GraphUtil() {
	}

	public static JQPlot makePieChart(String id, String title,
			IModel<? extends List<? extends AbstractSeries<?, ?, ?>>> data) {
		JQPlot jq = new JQPlot(id, data);

		PlotOptions p = jq.getOptions();
		p.setTitle(new PlotTitle(title));
		p.getGrid().setBackground("#000000");
		p.getLegend().setShow(true);

		PlotPieRendererOptions renderOptions = new PlotPieRendererOptions();

		renderOptions.setSliceMargin(0.0);
		renderOptions.setDiameter(200.0);

		p.getSeriesDefaults().setRendererOptions(renderOptions);
		p.getSeriesDefaults().setRenderer("$.jqplot.PieRenderer");

		return jq;
	}

	public static JQPlot makeDonationBarChart(String id, String title,
			IModel<? extends List<? extends AbstractSeries<?, ?, ?>>> data) {
		JQPlot jq = new JQPlot(id, data);

		PlotOptions p = jq.getOptions();
		p.getGrid().setBackground("#000000");
		p.getAxes().getYaxis().setLabel("USD");
		p.getAxes().getXaxis().setLabel("Date");
		p.getAxes().getXaxis()
				.setRenderer(JQPlotDateAxisRenderer.get().getName());
		p.getAxes().getYaxis().setMin(0.0);
		p.getLegend().setShowLabels(true);
		p.getLegend().setShow(true);
		p.addNewSeries().setLabel("Expenses");
		p.addNewSeries().setLabel("Donations");

		p.setTitle(new PlotTitle(title));

		PlotBarRendererOptions renderOptions = new PlotBarRendererOptions();

		int tot = 0;
		for (AbstractSeries<?, ?, ?> s : data.getObject()) {
			tot += s.getData().size();
		}
		renderOptions.setBarWidth(tot > 10 ? 2.0 : 8.0);

		p.getSeriesDefaults().setRendererOptions(renderOptions);
		p.getSeriesDefaults().setRenderer("$.jqplot.BarRenderer");

		return jq;
	}

	public static JQPlot makeReservesBarChart(String id, String title,
			IModel<? extends List<? extends AbstractSeries<?, ?, ?>>> data) {
		JQPlot jq = new JQPlot(id, data);

		PlotOptions p = jq.getOptions();
		p.getGrid().setBackground("#000000");

		p.setTitle(new PlotTitle(title));
		p.getAxes().getYaxis().setLabel("USD");
		p.getAxes().getYaxis().setMin(0.0);
		p.getAxes().getXaxis().setLabel("Donator");
		p.getAxes().getXaxis().setRenderer(JQPlotCategoryAxisRenderer.get());

		PlotBarRendererOptions renderOptions = new PlotBarRendererOptions();
		renderOptions.setBarWidth(8.0);

		p.getSeriesDefaults().setRendererOptions(renderOptions);
		p.getSeriesDefaults().setRenderer("$.jqplot.BarRenderer");

		return jq;
	}

	public static JQPlot makeBarChart(String id, String title,
			IModel<? extends List<? extends AbstractSeries<?, ?, ?>>> data) {
		JQPlot jq = new JQPlot(id, data);

		PlotOptions p = jq.getOptions();
		p.getGrid().setBackground("#000000");

		p.setTitle(new PlotTitle(title));
		p.getAxes().getYaxis().setLabel("USD");
		p.getAxes().getXaxis().setLabel("Donator");

		PlotBarRendererOptions renderOptions = new PlotBarRendererOptions();
		renderOptions.setBarWidth(8.0);

		p.getSeriesDefaults().setRendererOptions(renderOptions);
		p.getSeriesDefaults().setRenderer("$.jqplot.BarRenderer");

		return jq;
	}

	public static JQPlot makeMemberCountLineChart(String id, String title,
			SortedMap<Date, Integer> counts) {

		List<DateNumberSeries<Integer>> list = new LinkedList<DateNumberSeries<Integer>>();

		DateNumberSeries<Integer> countSeries = new DateNumberSeries<Integer>();

		int max = 0;

		for (Entry<Date, Integer> entry : counts.entrySet()) {
			Date next = entry.getKey();
			int val = entry.getValue();
			countSeries.addEntry(next, val);
			if (val > max)
				max = val;
		}
		list.add(countSeries);

		JQPlot jq = new JQPlot(id, new ListModel<DateNumberSeries<Integer>>(
				list));

		PlotOptions p = jq.getOptions();
		p.getGrid().setBackground("#000000");

		max = max + (50 - (max % 50));

		p.setTitle(new PlotTitle(title));
		p.getAxes().getYaxis().setLabel("Members");
		p.getAxes().getYaxis().setMin(0);
		p.getAxes().getYaxis().setMax(max);
		p.getAxes().getXaxis().setLabel("Date");
		p.getAxes().getXaxis()
				.setRenderer(JQPlotDateAxisRenderer.get().getName());

		PlotLineRendererOptions renderOptions = new PlotLineRendererOptions();
		p.getSeriesDefaults().setRendererOptions(renderOptions);
		p.getSeriesDefaults().setRenderer(
				JQPlotBezierCurveRenderer.get().getName());

		return jq;
	}

	public static final class SerializableRenderer extends Renderer implements
			Serializable {

		private static final long serialVersionUID = 1L;

		protected SerializableRenderer(Renderer renderer) {
			super(renderer.getName(), renderer.getJavaScriptResourceReference());
		}

	}

	public static JQPlot makeCashFlowLineChart(String id, String title,
			IModel<? extends List<? extends AbstractSeries<?, ?, ?>>> model) {
		JQPlot jq = new JQPlot(id, model);

		PlotOptions p = jq.getOptions();
		p.getGrid().setBackground("#000000");

		p.setTitle(new PlotTitle(title));
		p.getAxes().getYaxis().setLabel("Cash");
		p.getAxes().getYaxis().setMin(0);
		p.getAxes().getXaxis().setLabel("Date");
		p.getAxes().getXaxis()
				.setRenderer(JQPlotDateAxisRenderer.get().getName());

		PlotLineRendererOptions renderOptions = new PlotLineRendererOptions();
		p.getSeriesDefaults().setRendererOptions(renderOptions);
		p.getSeriesDefaults().setRenderer(
				JQPlotBezierCurveRenderer.get().getName());

		return jq;
	}
}
