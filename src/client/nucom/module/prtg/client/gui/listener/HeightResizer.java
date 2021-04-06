package nucom.module.prtg.client.gui.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HeightResizer implements ChangeListener<Object>
{
	private BorderPane BP = null;
	private FlowPane FP = null;
	private TabPane TP = null;
	private Stage S = null;
	private ScrollPane SP = null;
	private VBox VB = null;
	private AnchorPane AP = null;
	
	public HeightResizer (BorderPane BP)
	{
		this.BP = BP;
	}
	
	public HeightResizer (ScrollPane SP)
	{
		this.SP = SP;
	}
	
	public HeightResizer (FlowPane FP)
	{
		this.FP = FP;
	}
	
	public HeightResizer (TabPane TP)
	{
		this.TP=TP;
	}
	
	public HeightResizer (Stage S)
	{
		this.S=S;
	}
	
	public HeightResizer(VBox VB) 
	{
		this.VB=VB;
	}
	
	public HeightResizer(AnchorPane AP)
	{
		this.AP=AP;
	}

	@Override
	public void changed(ObservableValue<?> OV, Object Old, Object New) 
	{
		if(BP != null)
		{
			BP.setPrefHeight((Double)New);
			BP.setMinHeight((Double)New);
			
		}
		else if (FP != null)
		{
			FP.setPrefHeight((Double)New);
			FP.setMinHeight((Double)New);
		}
		else if (FP != null)
		{
			TP.setPrefHeight((Double)New);
			TP.setMinHeight((Double)New);
		}
		else if(S != null)
		{
			S.setHeight((Double)New);
		}
		else if(SP != null)
		{
			SP.setPrefHeight((Double)New);
			SP.setMaxHeight((Double)New);
			SP.setMinHeight((Double)New);
		}
		if(VB != null)
		{
			VB.setPrefHeight((Double)New);
			VB.setMaxHeight((Double)New);
			VB.setMinHeight((Double)New);
		}
		if(AP != null)
		{
			AP.setPrefWidth((Double)New);
			AP.setMinWidth((Double)New);
			AP.setMaxWidth((Double)New);
		}
	}
		
}
