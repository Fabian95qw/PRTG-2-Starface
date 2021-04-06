package nucom.module.prtg.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
public class Main_App extends Application
{
	public static AnchorPane AP_ROOT = null;
	FXMLLoader GUILOADER = null;
	
	public static boolean Shutdown = false;
	public static Stage ROOT_STAGE = null;
	public static String Path ="";
	
	
	@Override
	public void start(Stage Root_Stage)
	{
		try 
		{	

			FXMLLoader GUILOADER = new FXMLLoader();
			//
			AP_ROOT = GUILOADER.load(Main_App.class.getResourceAsStream("/nucom/module/prtg/client/gui/GUI.fxml"));;	
			
			Scene S = new Scene(AP_ROOT);
			Root_Stage.setScene(S);
				
			Root_Stage.setTitle("PRTG-Monitor Client Konfiguration");
			
			Root_Stage.show();
			ROOT_STAGE = Root_Stage;
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		ROOT_STAGE = Root_Stage;
				
		ROOT_STAGE.setOnCloseRequest( event -> 
		{
			Shutdown = true;
			System.exit(0);
		});
	}
	
	
	public static void main(String[] args) 
	{
		if(args.length > 0)
		{
			System.out.println(args[0]);
			Main_App.Path = args[0];
		}
		Main_App.launch(args);
	}
}
