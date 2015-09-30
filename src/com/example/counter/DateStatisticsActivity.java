package com.example.counter;

import android.os.Bundle;
import android.view.Menu;

import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DateStatisticsActivity extends Activity {

	private GraphicalView mChart;
	private StatisticsDataSource statistics;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		statistics = new StatisticsDataSource(this.getApplicationContext());

		//for the moment the only statistics are for today
		Date date = Calendar.getInstance().getTime();
		openDateChart(date);
	}

	private void openDateChart(Date date){

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		//get all counts
		List<GroupCount> group_counts = statistics.getAllCategoriesCountsGroupedByDate();

		ArrayList<Category> categories = statistics.getArrayCategories();
		Map<Integer, ArrayList<GroupCount>> map_category_counts = new HashMap<Integer, ArrayList<GroupCount>>();

		for(Category category:categories)
		{
			map_category_counts.put(category.getId(), new ArrayList<GroupCount>());
		}

		for(GroupCount count:group_counts)
		{
			ArrayList<GroupCount> array_counts = map_category_counts.get(count.getCategoryId());
			if(array_counts!=null)
				array_counts.add(count);
			map_category_counts.put(count.getCategoryId(),array_counts);
		}

		for(Entry<Integer,ArrayList<GroupCount>> entry: map_category_counts.entrySet())
		{
			int id_category = entry.getKey();
			ArrayList<GroupCount> a_counts= entry.getValue();
			TimeSeries counts_date_idcat = new TimeSeries(statistics.getCategoryName(id_category));

			if(a_counts != null)
			{
				for(GroupCount c:a_counts)
				{
					counts_date_idcat.add(c.getDate(), c.getCounts());
				}
			}

			// Adding the category counts statistic to the dataset
			dataset.addSeries(counts_date_idcat);
		}


		// Creating XYSeriesRenderer to customize visitsSeries
		XYSeriesRenderer categoryRenderer = new XYSeriesRenderer();
		categoryRenderer.setColor(Color.RED);
		categoryRenderer.setPointStyle(PointStyle.CIRCLE);
		categoryRenderer.setFillPoints(true);
		categoryRenderer.setLineWidth(2);
		categoryRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

		multiRenderer.setChartTitle("All categories click count Chart");
		multiRenderer.setXTitle("Days");
		multiRenderer.setYTitle("Count");
		multiRenderer.setZoomButtonsVisible(true);

		// Adding visitsRenderer and viewsRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same

		for(Entry<Integer,ArrayList<GroupCount>> entry: map_category_counts.entrySet())
		{
			multiRenderer.addSeriesRenderer(categoryRenderer);
		}

		// Getting a reference to LinearLayout of the activity Layout
		LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_view);

		// Creating a Time Chart
		mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");

		multiRenderer.setClickEnabled(true);
		multiRenderer.setSelectableBuffer(10);

		// Setting a click event listener for the graph
		mChart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Format formatter = new SimpleDateFormat("dd-MMM-yyyy");

				SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();

				if (seriesSelection != null) {
					int seriesIndex = seriesSelection.getSeriesIndex();
					String selectedSeries="Visits";
					if(seriesIndex==0)
						selectedSeries = "Visits";
					else
						selectedSeries = "Views";

					// Getting the clicked Date ( x value )
					long clickedDateSeconds = (long) seriesSelection.getXValue();
					Date clickedDate = new Date(clickedDateSeconds);
					String strDate = formatter.format(clickedDate);

					// Getting the y value
					int amount = (int) seriesSelection.getValue();

					// Displaying Toast Message
					Toast.makeText(
							getBaseContext(),
							selectedSeries + " on "  + strDate + " : " + amount ,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Adding the Line Chart to the LinearLayout
		chartContainer.addView(mChart);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.statistics, menu);
		return true;
	}
}
