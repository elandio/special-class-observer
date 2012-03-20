package edu.uwp.cs.android.sco.model;

public class Disability {
	public String name;
	public int rating;
	public String info;

	public Disability(String name, String info){
		this.name=name;
		this.rating=0;
		this.info=info;
	}
}
