package com.example.counter;

public class Category {
	private int id;
	private String name;
	private boolean in_use;

	public Category()
	{
		this.id = 0;
		this.name = "";
		this.in_use = false;
	}

	public int getId(int id)
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/* Is the category in use (flag in_use active)
	*/
	public boolean isBeingUsed()
	{
		return this.in_use;
	}

	public void setInUse(boolean in_use)
	{
		this.in_use = in_use;
	}

}
