package loaders;

import javafx.scene.image.Image;



public class LoadingPictures 
{
	//mutiple selections
	protected static Image checkedImage;
	protected static Image uncheckedImage;
	private static LoadingPictures instance;
	
	private LoadingPictures()
	{
		//handle reflection
	}


	static
	{
		try 
		{
			uncheckedImage = new Image("/ressources/unchecked.png");
			checkedImage = new Image("/ressources/checked.png");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Image getCheckedImage() 
	{
		return checkedImage;
	}

	public static Image getUncheckedImage() 
	{
		return uncheckedImage;
	}

	public static LoadingPictures getInstance() 
	{
		if(instance == null)
		{
			instance = new LoadingPictures();
		}
		return instance;
	}

}
