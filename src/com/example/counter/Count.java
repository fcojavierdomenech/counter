package com.example.counter;

import java.util.Date;

public class Count {

	private Date date;
	private int id_category;
	private int rating;
	
	public Count() {
		this.date = null;
	}

	public Date getDate() {
		return this.date;
	}

	public Count(Date date) {
		this.date = date;	
	}

	public void setDate(Long date) {
		this.date = new Date(date);
	}

	public int getCategoryId() {
		return this.id_category;
	}

	public void setCategoryId(int id_category) {
		this.id_category = id_category;
	}

	public int getRating() {
		return this.rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
