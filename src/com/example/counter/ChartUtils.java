package com.example.counter;

import java.util.Random;

import org.achartengine.chart.PointStyle;

import android.graphics.Color;

public class ChartUtils {

	private static final int[] colors = { Color.BLUE,Color.GREEN,Color.MAGENTA,Color.RED,Color.YELLOW,Color.GRAY };
	private static final PointStyle[] point_styles = { PointStyle.CIRCLE,PointStyle.DIAMOND,PointStyle.POINT,PointStyle.SQUARE,PointStyle.TRIANGLE };
	private int index_colors=0;
	private int index_point_styles=0;

	public ChartUtils() {
	}

	/** returns a random color
	*/
	public int getRandomColor()
	{
		Random generator = new Random();
		Integer i = generator.nextInt(colors.length - 1);
		return colors[i];
	}

	/** returns a random point style
	*/
	public PointStyle getRandomPointStyle()
	{
		Random generator = new Random();
		Integer i = generator.nextInt(point_styles.length - 1);
		return point_styles[i];
	}

	/** returns a color
	*/
	public int getColor()
	{
		Integer i = this.index_colors % colors.length;
		index_colors++;

		return colors[i];
	}

	/** returns a point style
	*/
	public PointStyle getPointStyle()
	{
		Integer i = index_point_styles % point_styles.length;
		index_point_styles++;

		return point_styles[i];
	}


}
