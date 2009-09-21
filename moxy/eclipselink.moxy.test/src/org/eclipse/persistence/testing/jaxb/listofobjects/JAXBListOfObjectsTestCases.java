/*******************************************************************************
 * Copyright (c) 1998, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Denise Smith  June 05, 2009 - Initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.listofobjects;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.internal.jaxb.JaxbClassLoader;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBUnmarshaller;
import org.eclipse.persistence.jaxb.JAXBTypeElement;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.oxm.XMLRoot;
import org.eclipse.persistence.platform.xml.SAXDocumentBuilder;
import org.eclipse.persistence.platform.xml.XMLPlatformFactory;
import org.eclipse.persistence.testing.jaxb.JAXBTestCases;
import org.eclipse.persistence.testing.jaxb.JAXBXMLComparer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public abstract class JAXBListOfObjectsTestCases extends JAXBTestCases {
	private Class[] classes;

	public JAXBListOfObjectsTestCases(String name) throws Exception {
		super(name);
	}

	public void setClasses(Class[] newClasses) throws Exception {
		classLoader = new JaxbClassLoader(Thread.currentThread()
				.getContextClassLoader());
		JAXBContextFactory factory = new JAXBContextFactory();
		jaxbContext = factory.createContext(newClasses, null, classLoader);
		jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	}

	public void setTypes(Type[] newTypes) throws Exception {
		classLoader = new JaxbClassLoader(Thread.currentThread()
				.getContextClassLoader());
		JAXBContextFactory factory = new JAXBContextFactory();
		jaxbContext = factory.createContext(newTypes, null, classLoader);
		jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	}

	protected Object getControlObject() {
		return null;
	}

    public void testXMLToObjectFromXMLStreamReader() throws Exception { 
         if(null != XML_INPUT_FACTORY) {
             InputStream instream = ClassLoader.getSystemResourceAsStream(resourceName);
             XMLStreamReader xmlStreamReader = XML_INPUT_FACTORY.createXMLStreamReader(instream);
             Object testObject = jaxbUnmarshaller.unmarshal(xmlStreamReader);
             instream.close();
             xmlToObjectTest(testObject);
         } 
     } 
  
    public void testXMLToObjectFromXMLEventReader() throws Exception {
        if(null != XML_INPUT_FACTORY) {
            InputStream instream = ClassLoader.getSystemResourceAsStream(resourceName);
            javax.xml.stream.XMLEventReader reader = XML_INPUT_FACTORY.createXMLEventReader(instream);
            Object obj = jaxbUnmarshaller.unmarshal(reader);
            this.xmlToObjectTest(obj);
        }
    }

	public void testObjectToXMLStreamWriter() throws Exception {
		if (System.getProperty("java.version").contains("1.6")) {
			StringWriter writer = new StringWriter();
			Object objectToWrite = getWriteControlObject();
			javax.xml.stream.XMLOutputFactory factory = javax.xml.stream.XMLOutputFactory
					.newInstance();
			javax.xml.stream.XMLStreamWriter streamWriter = factory
					.createXMLStreamWriter(writer);

			getJAXBMarshaller().marshal(objectToWrite, streamWriter);

			StringReader reader = new StringReader(writer.toString());
			InputSource inputSource = new InputSource(reader);
			Document testDocument = parser.parse(inputSource);
			writer.close();
			reader.close();

			objectToXMLDocumentTest(testDocument);
		}
	}
	
    public void testObjectToXMLEventWriter() throws Exception {
        if (System.getProperty("java.version").contains("1.6")) {
            StringWriter writer = new StringWriter();
            Object objectToWrite = getWriteControlObject();
            javax.xml.stream.XMLOutputFactory factory = javax.xml.stream.XMLOutputFactory
                    .newInstance();
            javax.xml.stream.XMLEventWriter eventWriter = factory
                    .createXMLEventWriter(writer);

            getJAXBMarshaller().marshal(objectToWrite, eventWriter);

            StringReader reader = new StringReader(writer.toString());
            InputSource inputSource = new InputSource(reader);
            Document testDocument = parser.parse(inputSource);
            writer.close();
            reader.close();

            objectToXMLDocumentTest(testDocument);
        }
    }	

	//Override and don't compare namespaceresolver size
	public void testObjectToXMLStringWriter() throws Exception {
        StringWriter writer = new StringWriter();
        Object objectToWrite = getWriteControlObject();        
  
        jaxbMarshaller.marshal(objectToWrite, writer);
               
        StringReader reader = new StringReader(writer.toString());
        InputSource inputSource = new InputSource(reader);
        Document testDocument = parser.parse(inputSource);
        writer.close();
        reader.close();

        objectToXMLDocumentTest(testDocument);
    }
	
	
	public void testRoundTrip() throws Exception {
		//This test is not applicable because to Marshal we need a specialized jaxbelement
    }
	

	//Override and don't compare namespaceresolver size
	 public void testObjectToContentHandler() throws Exception {
	        SAXDocumentBuilder builder = new SAXDocumentBuilder();
	        Object objectToWrite = getWriteControlObject();
	      
	        jaxbMarshaller.marshal(objectToWrite, builder);
	        
            Document controlDocument = getWriteControlDocument();
	        Document testDocument = builder.getDocument();

	        log("**testObjectToContentHandler**");
	        log("Expected:");
	        log(controlDocument);
	        log("\nActual:");
	        log(testDocument);

	        assertXMLIdentical(controlDocument, testDocument);
	    }

		//Override and don't compare namespaceresolver size
	    public void testObjectToXMLDocument() throws Exception {
	        Object objectToWrite = getWriteControlObject();
	     
	        Document testDocument = XMLPlatformFactory.getInstance().getXMLPlatform().createDocument(); 
	        jaxbMarshaller.marshal(objectToWrite, testDocument);	     
	        objectToXMLDocumentTest(testDocument);
	    }

	 
	public void testXMLToObjectFromStreamSource() throws Exception {
		InputStream instream = ClassLoader
				.getSystemResourceAsStream(resourceName);
		StreamSource source = new StreamSource();
		source.setInputStream(instream);
		Object obj = getJAXBUnmarshaller().unmarshal(source);
		this.xmlToObjectTest(obj);
	}

	public abstract Map<String, InputStream> getControlSchemaFiles();
	
	public void testSchemaGen() throws Exception {
		testSchemaGen(getControlSchemaFiles());
	}
	
	public Object getWriteControlObject(){
		JAXBElement jaxbElement = (JAXBElement)getControlObject();

		try{
			Type typeToUse = getTypeToUnmarshalTo();
			if(typeToUse instanceof ParameterizedType){
				JAXBTypeElement typed = new JAXBTypeElement(jaxbElement.getName(), jaxbElement.getValue(), (ParameterizedType)typeToUse);
				return typed;
			}else if(typeToUse instanceof Class){
				JAXBTypeElement typed = new JAXBTypeElement(jaxbElement.getName(), jaxbElement.getValue(), (Class)typeToUse);
				return typed;
			}
		}catch(Exception e){
			fail(e.getMessage());
		}
		return null;
	}

	protected abstract Type getTypeToUnmarshalTo() throws Exception;

	protected abstract String getNoXsiTypeControlResourceName();
	
	
	

}
