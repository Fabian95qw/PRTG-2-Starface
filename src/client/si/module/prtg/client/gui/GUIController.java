package si.module.prtg.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import si.module.prtg.client.utility.EnumHelper;
import si.module.prtg.client.utility.Log;
import si.module.prtg.client.utility.LogHelper;
import si.module.prtg.client.xmlrpc.XmlRpcConnector;
import si.module.prtg.client.xmlrpc.commands.version.Version;

public class GUIController
{	
	
	private Log log = null;
	
	@FXML ImageView IMAGEVIEW_TESTSTATE;
	@FXML TextField TEXTFIELD_IPORDNS;
	@FXML TextField TEXTFIELD_INSTANCENAME;
	@FXML TextField TEXTFIELD_USERNAME;
	@FXML PasswordField TEXTFIELD_PASSWORD;
	@FXML TextField TEXTFIELD_TOKEN;
	@FXML TextField TEXTFIELD_VERSION;
	@FXML TextField TEXTFIELD_SFVERSION;
	@FXML TextField TEXTFIELD_SENSORSTRING;
	@FXML TextField TEXTFIELD_SENSORNAME;
	@FXML CheckBox CHECKBOX_USE_SSL;

	public GUIController()
	{}

	@FXML
	protected void initialize() 
	{		
		log = new Log(this.getClass());
		log.debug("Initialized AddServerFormController");
		Error();
	}
	
    @FXML
    private void COPY_SENSORSTRING_ACTION(ActionEvent event)
    {
    	StringSelection S = new StringSelection(TEXTFIELD_SENSORSTRING.getText());
    	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	clipboard.setContents(S,S);
    }
	
    @FXML
    private void TEST_CONNECTION_ACTION(ActionEvent event)
    {
    	log.debug("TEST_CONNECTION_ACTION");
    	CalculateToken();
    	Trying();
    	
		try 
		{
			XmlRpcConnector XPC = new XmlRpcConnector(TEXTFIELD_INSTANCENAME.getText(), TEXTFIELD_IPORDNS.getText(), TEXTFIELD_TOKEN.getText(), CHECKBOX_USE_SSL.isSelected());
			Version VC = new Version();
			XPC.execute(VC);
			log.debug(VC.Result().Data().get("Version").toString());
			TEXTFIELD_VERSION.setText(VC.Result().Data().get("Version").toString());
			TEXTFIELD_SFVERSION.setText(VC.Result().Data().get("SFVersion").toString());
			StringBuilder SB = new StringBuilder();
			
			SB.append("-h %host -t " +TEXTFIELD_TOKEN.getText()+" -s " +TEXTFIELD_SENSORNAME.getText() +" -i "+ TEXTFIELD_INSTANCENAME.getText() +" -ssl "+ CHECKBOX_USE_SSL.isSelected() + " -d false");
			TEXTFIELD_SENSORSTRING.setText(SB.toString());
			
		} 
		catch (Exception e) 
		{
			LogHelper.EtoStringLog(log, e);
			Error();
			return;
		}
		OK();
		
    }
		
    private void CalculateToken()
    {
		String Login =TEXTFIELD_USERNAME.getText();
		String Passwort = TEXTFIELD_PASSWORD.getText();
		
		if(Login.isEmpty() || Passwort.isEmpty())
		{
			return;
		}
		
		String PW512 = sha512(Passwort);
		String Output512 = sha512(Login+"*"+PW512);
		String Token = Login+":"+Output512;
		
		TEXTFIELD_TOKEN.setText(Token);
    }
	
    private String sha512(String input) 
    { 
        try { 
            // getInstance() method is called with algorithm SHA-512 
            MessageDigest md = MessageDigest.getInstance("SHA-512"); 
  
            // digest() method is called 
            // to calculate message digest of the input string 
            // returned as array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
  
            // Add preceding 0s to make it 32 bit 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
  
            // return the HashText 
            return hashtext; 
        } 
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    } 
    
    private void OK()
    {
		InputStream IS = null;
		
		IS = GUIController.class.getResourceAsStream(EnumHelper.ResImage.OK.getValue());
		IMAGEVIEW_TESTSTATE.setImage(new Image(IS, 8, 8, false, false));
		try 
		{
			IS.close();
		} 
		catch (IOException e) 
		{
			LogHelper.EtoStringLog(log, e);
		}
    }
    
    private void Trying()
    {
		InputStream IS = null;
		IS = GUIController.class.getResourceAsStream(EnumHelper.ResImage.Trying.getValue());
		IMAGEVIEW_TESTSTATE.setImage(new Image(IS, 8, 8, false, false));
		try 
		{
			IS.close();
		} 
		catch (IOException e) 
		{
			LogHelper.EtoStringLog(log, e);
		}	
    }
    
    private void Error()
    {
		InputStream IS = null;
		
		IS = GUIController.class.getResourceAsStream(EnumHelper.ResImage.Error.getValue());
		IMAGEVIEW_TESTSTATE.setImage(new Image(IS, 8, 8, false, false));
		try 
		{
			IS.close();
		} 
		catch (IOException e) 
		{
			LogHelper.EtoStringLog(log, e);
		}	
    }

		
}
