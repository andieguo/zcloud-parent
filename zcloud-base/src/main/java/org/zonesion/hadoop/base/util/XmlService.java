package org.zonesion.hadoop.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.zonesion.hadoop.base.bean.Gate;
import org.zonesion.hadoop.base.bean.Sensor;

public class XmlService {
	
	private DocumentBuilderFactory dbFactory ;
	private DocumentBuilder dbBuilder;	
	private static XmlService instance;
	
	private XmlService(){
		this.init();
	}
	
	public synchronized static XmlService getInstance(){
		if(instance == null){
			instance = new XmlService();
		}
		return instance;
	}
	
	public void init(){
		// 得到DOM解析器的工厂实例
		// 得到 DocumentBuilderFactory 对象, 由该对象可以得到 DocumentBuilder 对象
		 dbFactory = DocumentBuilderFactory.newInstance();
		// 从DOM工厂中获得DOM解析器
		 try {
			dbBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void putXML(List<Gate> gateList,OutputStream output){
		Document doc;
		Element gatesElement,gateElement,serverElement,useridElement,userkeyElement,sensorsElement,sensorElement;
		try {
			// 创建文档树模型对象
			doc = dbBuilder.newDocument();
			if (doc != null) {
				gatesElement = doc.createElement("gates");
				for(int i=0;i<gateList.size();i++){
					gateElement = doc.createElement("gate");
					serverElement = doc.createElement("serverAddr");
					serverElement.appendChild(doc.createTextNode(gateList.get(i).getServerAddr()));
					useridElement = doc.createElement("userid");
					useridElement.appendChild(doc.createTextNode(gateList.get(i).getUserid()));
					userkeyElement = doc.createElement("userkey");
					userkeyElement.appendChild(doc.createTextNode(gateList.get(i).getUserkey()));
					sensorsElement = doc.createElement("sensors");
					List<Sensor> sensors = gateList.get(i).getSensors();
					for(int j=0;j<sensors.size();j++){
						sensorElement = doc.createElement("sensor");
						sensorElement.appendChild(doc.createTextNode(sensors.get(j).getChannal()));
						sensorElement.setAttribute("unit",sensors.get(j).getUnit());
						sensorElement.setAttribute("title", sensors.get(j).getTitle());
						sensorsElement.appendChild(sensorElement);
					}
					//将serverAddr,userid,userkey,channals,sensors添加到gate中
					gateElement.appendChild(serverElement);// 
					gateElement.appendChild(useridElement);
					gateElement.appendChild(userkeyElement);
					gateElement.appendChild(sensorsElement);
					//将gate节点添加到gates
					gatesElement.appendChild(gateElement);
				}//for循环结束
				//添加gates到文档树中
				doc.appendChild(gatesElement);
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");// 设置缩进
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");// 此行为设置XML文档编码格式为GB2312
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(output);
				transformer.transform(source, result);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(output!=null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public List<Gate> readXML(InputStream input) throws SAXException, IOException{
		List<Gate> gateList = new ArrayList<Gate>();
		try {
			Document document = dbBuilder.parse(input);// 得到代表整个xml的Document对象
			Element root = document.getDocumentElement();// 得到 "根节点"
			// 读取record元素
			NodeList gateNodeList = root.getElementsByTagName("gate");// 获取根节点的所有items的节点
			for(int i=0;i<gateNodeList.getLength();i++){
				Element gateElement = (Element) gateNodeList.item(i);
				String serverAddr = gateElement.getElementsByTagName("serverAddr").item(0).getTextContent();
				String userid = gateElement.getElementsByTagName("userid").item(0).getTextContent();
				String userkey = gateElement.getElementsByTagName("userkey").item(0).getTextContent();
				Gate gate = new Gate(serverAddr,userid,userkey);
				Element sensorsElement = (Element) gateElement.getElementsByTagName("sensors").item(0);
				NodeList sensorNodeList = sensorsElement.getElementsByTagName("sensor");
				List<Sensor> sensors = new ArrayList<Sensor>();
				for(int j=0;j<sensorNodeList.getLength();j++){
					//生成一个新的Sensor对象
					Sensor sensor = new Sensor();
					String channal =  sensorNodeList.item(j).getTextContent();//NodeList有多个相同的Element
					//获取Sensor对象属性值
					NamedNodeMap attributes= sensorNodeList.item(j).getAttributes();
					sensor.setUnit(attributes.getNamedItem("unit").getNodeValue());
					sensor.setTitle(attributes.getNamedItem("title").getNodeValue());
					sensor.setChannal(channal);
					//将sensor添加到List<Sensor>集合中
					sensors.add(sensor);
				}
				gate.setSensors(sensors);
				gateList.add(gate);
			}
		}  finally{
			if(input!=null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return gateList;
	}
	
}
