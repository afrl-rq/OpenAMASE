// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Copyright (c) 2005 FlexDock Development Team. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE.
 */
package org.flexdock.perspective.persist.xml;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.flexdock.docking.state.DockingPath;
import org.flexdock.docking.state.DockingState;
import org.flexdock.docking.state.FloatingGroup;
import org.flexdock.docking.state.LayoutNode;
import org.flexdock.docking.state.PersistenceException;
import org.flexdock.docking.state.tree.DockableNode;
import org.flexdock.docking.state.tree.DockingPortNode;
import org.flexdock.docking.state.tree.SplitNode;
import org.flexdock.perspective.Layout;
import org.flexdock.perspective.LayoutSequence;
import org.flexdock.perspective.Perspective;
import org.flexdock.perspective.persist.Persister;
import org.flexdock.perspective.persist.PerspectiveModel;
import org.flexdock.util.Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: XMLPersister.java,v 1.23 2006-12-20 20:55:21 kschaefe Exp $
 */
public class XMLPersister implements Persister {

    /**
     * {@inheritDoc}
     */
    public boolean store(OutputStream os, PerspectiveModel perspectiveModel) throws IOException, PersistenceException {
        DocumentBuilder documentBuilder = createDocumentBuilder();
        Document document = documentBuilder.newDocument();

        ISerializer perspectiveModelSerializer = SerializerRegistry.getSerializer(PerspectiveModel.class);
        Element perspectiveModelElement = perspectiveModelSerializer.serialize(document, perspectiveModel);

        document.appendChild(perspectiveModelElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        // the indent-number attribute causes an IllegalArgumentException under 1.4
        if(Utilities.JAVA_1_5) {
            transformerFactory.setAttribute("indent-number", new Integer(4));
        }

        try {
            Transformer transformer = transformerFactory.newTransformer();
            // this property is ignored under java 1.5.
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new OutputStreamWriter(os));

            transformer.transform(source, result);
        } catch (TransformerConfigurationException ex) {
            throw new PersistenceException("Unable to serialize perspectiveModel", ex);
        } catch (TransformerException ex) {
            throw new PersistenceException("Unable to serialize perspectiveModel", ex);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public PerspectiveModel load(InputStream is) throws IOException, PersistenceException {
        try {
            InputSource inputSource = new InputSource(is);
            DocumentBuilder documentBuilder = createDocumentBuilder();
            Document document = documentBuilder.parse(inputSource);

            ISerializer perspectiveModelSerializer = SerializerRegistry.getSerializer(PerspectiveModel.class);
            NodeList perspectiveModelNodeList = document.getElementsByTagName(PersistenceConstants.PERSPECTIVE_MODEL_ELEMENT_NAME);
            if (perspectiveModelNodeList.getLength() > 0 && perspectiveModelNodeList.item(0) instanceof Element) {
                Element perspectiveModelElement = (Element) perspectiveModelNodeList.item(0);
                return (PerspectiveModel) perspectiveModelSerializer.deserialize(perspectiveModelElement);
            }

            return null;
        } catch (SAXException ex) {
            throw new PersistenceException("Unable to deserialize perspectiveModel from xml", ex);
        }
    }

    private void registerSerializers() {
        SerializerRegistry.registerSerializer(Perspective.class, new PerspectiveSerializer());
        SerializerRegistry.registerSerializer(Layout.class, new LayoutSerializer());
        SerializerRegistry.registerSerializer(LayoutSequence.class, new LayoutSequenceSerializer());
        SerializerRegistry.registerSerializer(DockingState.class, new DockingStateSerializer());
        SerializerRegistry.registerSerializer(Point.class, new PointSerializer());
        SerializerRegistry.registerSerializer(Dimension.class, new DimensionSerializer());
        SerializerRegistry.registerSerializer(Rectangle.class, new RectangleSerializer());
        SerializerRegistry.registerSerializer(FloatingGroup.class, new FloatingGroupSerializer());
        SerializerRegistry.registerSerializer(DockingPath.class, new DockingPathSerializer());
        SerializerRegistry.registerSerializer(PerspectiveModel.class, new PerspectiveModelSerializer());
        SerializerRegistry.registerSerializer(LayoutNode.class, new LayoutNodeSerializer());
        SerializerRegistry.registerSerializer(SplitNode.class, new SplitNodeSerializer());
        SerializerRegistry.registerSerializer(DockingPortNode.class, new DockingPortNodeSerializer());
        SerializerRegistry.registerSerializer(DockableNode.class, new DockableNodeSerializer());
    }

    public static XMLPersister newDefaultInstance() {
        XMLPersister persister = new XMLPersister();
        persister.registerSerializers();

        return persister;
    }

    private DocumentBuilder createDocumentBuilder() throws PersistenceException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            return documentBuilder;
        } catch (ParserConfigurationException ex) {
            throw new PersistenceException("Unable to create document builder", ex);
        }
    }

}
