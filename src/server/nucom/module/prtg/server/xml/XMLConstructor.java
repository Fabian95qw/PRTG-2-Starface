package nucom.module.prtg.server.xml;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import nucom.module.prtg.server.utility.EnumHelper.Unit;
import nucom.module.prtg.server.utility.LogHelper;

public class XMLConstructor 
{
	private Element RootNode = null;
	private List<Result> Results = null;
	private Log log = null;
	
	public XMLConstructor(Log log)
	{
		this.log=log;
		Results = new ArrayList<Result>();
	}
	
	public void AddResult(String Channel, String Value, Unit U, String CustomUnit, Map<String, String> Params)
	{
		//Checks if the Same Channel is already existing, overrides variables if so, otherwise adds the Result
		for(Result R : Results)
		{
			if(R.getChannel().equals(Channel))
			{
				R.setValue(Value);
				R.setU(U);
				R.setCustomUnit(CustomUnit);
				R.setParams(Params);
				return;
			}
		}
		Result R = new Result(Channel, Value, U, CustomUnit, Params);
		Results.add(R);
	}
	
	@Override
	public String toString()
	{
		//Parsing the XML-Construct to a PRTG-Monitor friendly xml
		DocumentBuilderFactory DBF = null;
		DocumentBuilder DB = null;
		Document D = null;
		
		DBF = DocumentBuilderFactory.newInstance();
		try 
		{
			DB = DBF.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		D = DB.newDocument();
		RootNode = D.createElement("prtg");
		D.appendChild(RootNode);
		
		for(Result R : Results)
		{
			//Create an XML Structure as expected by the prtg-monitor
			Element EResult =  D.createElement("result");
			RootNode.appendChild(EResult);
			
			Element EChannel = D.createElement("channel");
			EChannel.setTextContent(R.getChannel());
			EResult.appendChild(EChannel);
			
			Element EValue = D.createElement("value");
			EValue.setTextContent(R.getValue());
			EResult.appendChild(EValue);
			
			Element EUnit = D.createElement("unit");
			EUnit.setTextContent(R.getU().toString());
			EResult.appendChild(EUnit);
			
			//Append Custom Unit if set
			if(R.getCustomUnit() != null && !R.getCustomUnit().isEmpty())
			{
				Element ECustomUnit = D.createElement("customunit");
				ECustomUnit.setTextContent(R.getCustomUnit());
				EResult.appendChild(ECustomUnit);
			}

			//Append additional XML-Elements, if Map was Provided
			if(R.getParams() != null)
			{
				for(Entry<String, String> Entry: R.getParams().entrySet())
				{
					Element E = D.createElement(Entry.getKey());
					E.setTextContent(Entry.getValue());
					EResult.appendChild(E);
				}
			}
			
		}
		
		try
		{
			//Transform XML, and return as String
			TransformerFactory TF = TransformerFactory.newInstance();
			Transformer T = TF.newTransformer();
			T.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter SW = new StringWriter();
			T.transform(new DOMSource(D), new StreamResult(SW));
			log.debug("[XMLC] XML Constructed:" + SW.getBuffer().toString());
			return SW.getBuffer().toString();
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
		//If an error occures, return an empty String, causing an XML Malformed exception in the prtg-monitor
		return "";
	}
		
}
