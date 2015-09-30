package com.example.counter;

import java.util.Date;

public class GroupCount extends Count {
	
	private int counts;

	public GroupCount() {
		super();
	}

	public GroupCount(Date date, int counts) {
		super(date);
		this.counts = counts;
	}

	/**
	 * @return the counts
	 */
	public int getCounts() {
		return counts;
	}

	/**
	 * @param counts the counts to set
	 */
	public void setCounts(int counts) {
		this.counts = counts;
	}

}
