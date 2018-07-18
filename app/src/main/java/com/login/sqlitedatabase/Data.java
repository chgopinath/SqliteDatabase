package com.login.sqlitedatabase;

public class Data 
{
	String name,course;
	String id,number;
	byte[] image;
	
	public Data(String id, String name, String course, String num,byte[] buffer) 
	{
		this.course=course;
		this.id=id;
		this.name=name;
		this.number=num;
		this.image=buffer;
		
	}
	
	public byte[] getImage()
	{
		return image;
	}
	public void setImage(byte[] image) 
	{
		this.image = image;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
	
	

}
