package nucom.module.prtg.client.gui.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class WidthResizer implements ChangeListener<Object>
{
	private BorderPane BP = null;
	private FlowPane FP = null;
	private TabPane TP = null;
	private Stage S = null;
	private ScrollPane SP = null;
	private HTMLEditor HTMLE = null;
	private Double Sub=0.0;
	private TitledPane TIP = null;
	private TextField TF = null;
	private ToolBar TB = null;
	private VBox VB = null;
	private AnchorPane AP = null;
	
	public WidthResizer (BorderPane BP)
	{
		this.BP = BP;
	}
	
	public WidthResizer (ScrollPane SP)
	{
		this.SP = SP;
	}
	
	public WidthResizer (FlowPane FP)
	{
		this.FP = FP;
	}
	
	public WidthResizer (TabPane TP)
	{
		this.TP=TP;
	}
	
	public WidthResizer (Stage S)
	{
		this.S=S;
	}
	
	public WidthResizer(HTMLEditor HTMLE) 
	{
		this.HTMLE=HTMLE;
	}

	public WidthResizer(TitledPane TIP) 
	{
		this.TIP=TIP;
	}

	public WidthResizer(TextField TF) 
	{
		this.TF=TF;
	}

	public WidthResizer(ToolBar TB) 
	{
		this.TB=TB;
	}

	public WidthResizer(VBox VB) 
	{
		this.VB=VB;
	}

	public WidthResizer(AnchorPane AP) 
	{
		this.AP=AP;
	}

	@Override
	public void changed(ObservableValue<?> OV, Object Old, Object New) 
	{
		//System.out.println("[WR]"+((Double)New-Sub));
		if(BP != null)
		{
			BP.setPrefWidth((Double)New-Sub);
			BP.setMinWidth((Double)New-Sub);
			
		}
		else if (FP != null)
		{
			FP.setPrefWidth((Double)New-Sub);
			FP.setMinWidth((Double)New-Sub);
		}
		else if (FP != null)
		{
			TP.setPrefWidth((Double)New-Sub);
			TP.setMinWidth((Double)New-Sub);
		}
		else if(S != null)
		{
			S.setWidth((Double)New);
		}
		else if(SP != null)
		{
			SP.setPrefWidth((Double)New-Sub);
		}
		else if(HTMLE != null)
		{
			HTMLE.setPrefWidth((Double)New-Sub);
			HTMLE.setMaxWidth((Double)New-Sub);
			HTMLE.setMinWidth((Double)New-Sub);
		}
		if(TIP != null)
		{
			TIP.setPrefWidth((Double)New-Sub);
		}
		if(TF != null)
		{
			TF.setPrefWidth((Double)New-Sub);
		}
		if(TB != null)
		{
			TB.setPrefWidth((Double)New-Sub);
			TB.setMinWidth((Double)New-Sub);
			TB.setMaxWidth((Double)New-Sub);
		}
		if(VB != null)
		{
			VB.setPrefWidth((Double)New-Sub);
		}
		
		if(AP != null)
		{
			AP.setPrefWidth((Double)New-Sub);
			AP.setMinWidth((Double)New-Sub);
			AP.setMaxWidth((Double)New-Sub);
		}
	}
	
	public void SetSub(Double Sub)
	{
		this.Sub=Sub;
	}
		
}
