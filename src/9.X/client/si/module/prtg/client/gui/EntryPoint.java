package si.module.prtg.client.gui;

public class EntryPoint 
{
	public static void main(String[] args) 
	{
		if(args.length == 0)
		{
			Main_App.main(args);
		}
		else
		{
			CommandLineRunner CLR = new CommandLineRunner();
			CLR.Run(args);
		}
	}

}
