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
package org.flexdock.perspective.persist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.flexdock.docking.state.PersistenceException;
import org.flexdock.perspective.persist.xml.XMLPersister;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: FilePersistenceHandler.java,v 1.9 2006-12-20 20:55:22 kschaefe Exp $
 */
public class FilePersistenceHandler implements PersistenceHandler {
    public static final File DEFAULT_PERSPECTIVE_DIR = new File(System.getProperty("user.home") + "/flexdock/perspectives");

    protected File defaultPerspectiveFile;
    protected Persister m_persister = null;

    public FilePersistenceHandler(String absolutePath) {
        this(new File(absolutePath), null);
    }

    public FilePersistenceHandler(File file) {
        this(file, null);
    }

    public FilePersistenceHandler(String absolutePath, Persister persister) {
        this(new File(absolutePath), persister);
    }

    public FilePersistenceHandler(File defaultFile, Persister persister) {
        defaultPerspectiveFile = defaultFile;
        if(persister==null)
            persister = createDefaultPersister();
        m_persister = persister;
    }

    public static FilePersistenceHandler createDefault(String fileName) {
        String path = DEFAULT_PERSPECTIVE_DIR.getAbsolutePath() + "/" + fileName;
        return new FilePersistenceHandler(path);
    }

    /**
     * {@inheritDoc}
     */
    public boolean store(String persistenceKey, PerspectiveModel perspectiveModel) throws IOException, PersistenceException {
        File file = getPerspectiveFile(persistenceKey);
        validatePerspectiveFile(file);

//        XMLDebugger.println(perspectiveModel);

        FileOutputStream fos = new FileOutputStream(file);
        try {
            return m_persister.store(fos, perspectiveModel);
        } finally {
            fos.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public PerspectiveModel load(String persistenceKey) throws IOException, PersistenceException {
        File file = getPerspectiveFile(persistenceKey);
        if(file==null || !file.exists())
            return null;

        FileInputStream fis = new FileInputStream(file);

        try {
            PerspectiveModel perspectiveModel = m_persister.load(fis);

//            LayoutNode node = perspectiveModel.getPerspectives()[1].getLayout().getRestorationLayout();
//            XMLDebugger.println(node);

            return perspectiveModel;
        } finally {
            fis.close();
        }
    }

    protected void validatePerspectiveFile(File file) throws IOException {
        File dir = file.getParentFile();
        if(!dir.exists())
            dir.mkdirs();

        if(!file.exists())
            file.createNewFile();
    }


    public File getPerspectiveFile(String persistenceKey) {
        if(persistenceKey==null)
            return defaultPerspectiveFile;

        String filePath = persistenceKey;
        if(filePath.indexOf('/')==-1 && filePath.indexOf('\\')==-1)
            filePath = DEFAULT_PERSPECTIVE_DIR.getAbsolutePath() + "/" + filePath;
        return new File(filePath);

    }

    public void setDefaultPerspectiveFile(File file) {
        defaultPerspectiveFile = file;
    }

    public void setDefaultPerspectiveFile(String absolutePath) {
        defaultPerspectiveFile = new File(absolutePath);
    }

    public Persister createDefaultPersister() {
//		return new DefaultFilePersister();
        return XMLPersister.newDefaultInstance();
    }

}
