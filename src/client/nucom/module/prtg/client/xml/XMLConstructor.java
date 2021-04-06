package nucom.module.prtg.client.xml;

import java.io.StringWriter;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import nucom.module.prtg.client.utility.Log;
import nucom.module.prtg.client.utility.LogHelper;

public class XMLConstructor 
{
	private Element RootNode = null;
	private Log log = null;
	
	public XMLConstructor()
	{
		log = new Log(this.getClass());
	}
		
	@SuppressWarnings("unchecked")
	public String ConstructXML(Map<String, Object> Data)
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
				
		for(Entry<String, Object> Entry : Data.entrySet())
		{
			if(!(Entry.getValue() instanceof Map)) {continue;}
			Map<String, Object> Channel = (Map<String, Object>) Entry.getValue();
			
			//Create an XML Structure as expected by the prtg-monitor
			Element EResult =  D.createElement("result");
			RootNode.appendChild(EResult);
			
			Element EChannel = D.createElement("channel");
			EChannel.setTextContent(Channel.get("Channel").toString());
			EResult.appendChild(EChannel);
			
			Element EValue = D.createElement("value");
			EValue.setTextContent(Channel.get("Value").toString());
			EResult.appendChild(EValue);
			
			Element EUnit = D.createElement("unit");
			EUnit.setTextContent(Channel.get("Unit").toString());
			EResult.appendChild(EUnit);
			
			//Append Custom Unit if set
			
			String CustomUnit = Channel.get("CustomUnit").toString();
			if(CustomUnit != null && !CustomUnit.isEmpty())
			{
				Element ECustomUnit = D.createElement("customunit");
				ECustomUnit.setTextContent(CustomUnit);
				EResult.appendChild(ECustomUnit);
			}

			//Append additional XML-Elements, if Map was Provided
			
			Map<String, String>Params = (Map<String, String>) Channel.get("Params");
			
			if(Params != null)
			{
				for(Entry<String, String> Param: Params.entrySet())
				{
					Element E = D.createElement(Param.getKey());
					E.setTextContent(Param.getValue());
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
		return "";
	}
		
}
