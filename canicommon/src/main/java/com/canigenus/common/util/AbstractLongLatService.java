package com.canigenus.common.util;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.primefaces.model.map.LatLng;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public abstract class AbstractLongLatService {
	/*private static final String GEOCODE_REQUEST_URL = "http://maps.googleapis.com/maps/api/geocode/xml?sensor=false&";*/
	
	public abstract String getUrl();

	private static HttpClient httpClient = new HttpClient(
			new MultiThreadedHttpConnectionManager());

	public  List<LatLng> getLongitudeLatitude(String address) {
		List<LatLng> latLngs = new ArrayList<LatLng>();
		try {

			StringBuilder urlBuilder = new StringBuilder(getUrl());
			if (!JavaUtil.isBlank(address)) {
				urlBuilder.append("&address=").append(
						URLEncoder.encode(address, "UTF-8"));
			}

			final GetMethod getMethod = new GetMethod(urlBuilder.toString());
			try {
				httpClient.executeMethod(getMethod);
				Reader reader = new InputStreamReader(
						getMethod.getResponseBodyAsStream(),
						getMethod.getResponseCharSet());

				int data = reader.read();
				char[] buffer = new char[1024];
				Writer writer = new StringWriter();
				while ((data = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, data);
				}

				String result = writer.toString();
				System.out.println(result.toString());

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader("<"
						+ writer.toString().trim()));
				Document doc = db.parse(is);

				String strLatitude = getXpathValue(doc,
						"//GeocodeResponse/result/geometry/location/lat/text()");
				System.out.println("Latitude:" + strLatitude);

				String strLongtitude = getXpathValue(doc,
						"//GeocodeResponse/result/geometry/location/lng/text()");
				System.out.println("Longitude:" + strLongtitude);
				LatLng latLng = new LatLng(Double.parseDouble(strLatitude),
						Double.parseDouble(strLongtitude));
				latLngs.add(latLng);
				return latLngs;

			} finally {
				getMethod.releaseConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return latLngs;
		}
	}

	private static String getXpathValue(Document doc, String strXpath)
			throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xPath.compile(strXpath);
		String resultData = null;
		Object result4 = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result4;
		for (int i = 0; i < nodes.getLength();) {
			resultData = nodes.item(i).getNodeValue();
			break;
		}
		return resultData;
	}

}
