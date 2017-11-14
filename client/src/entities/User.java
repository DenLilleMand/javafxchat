package entities;

import java.io.Serializable;


public class User implements Serializable
{
	private static final long serialVersionUID = 312224942535543291L;
	private Boolean online;
	//public SimpleStringProperty userName;
	public String userName;
	private int id;
	
	public User()
	{
	}
	
	public int getId() 
	{
		return id;
	}

	public String getUserName()
	{
		return userName;
	}
	
	public Boolean isOnline()
	{
		return online;
	}
	
	public void setOnline(boolean online)
	{
		this.online = online;
	}
	
	public User(String userName, Boolean b, int id)
	{
		this.id = id;
		this.userName = userName;
		this.online = b;
	}
}
