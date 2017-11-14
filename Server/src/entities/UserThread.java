package entities;

public class UserThread 
{
	protected Thread thread;

	private int id; //burde selF komme fra en database af, og være koblet sammen med User objektet.
	
	public UserThread(Thread thread, int id)
	{
		super(); //ved ikke helt hvorfor jeg laver et super() kald her, jeg extender jo ikke noget. tjek det ud.
		this.id = id;
		this.thread = thread;
	}

	public Thread getThread() 
	{
		return thread;
	}

	public void setThread(Thread thread) 
	{
		this.thread = thread;
	}
}
