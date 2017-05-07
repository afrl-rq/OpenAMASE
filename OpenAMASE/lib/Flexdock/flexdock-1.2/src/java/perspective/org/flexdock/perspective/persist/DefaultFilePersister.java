// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 30, 2005
 */
package org.flexdock.perspective.persist;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.flexdock.docking.state.PersistenceException;

/**
 * @author Christopher Butler
 */
public class DefaultFilePersister implements Persister {

    public PerspectiveModel load(InputStream in) throws IOException, PersistenceException {
        if(in==null)
            return null;

        ObjectInputStream ois = null;
        try {
            ois = in instanceof ObjectInputStream? (ObjectInputStream)in:
                  new ObjectInputStream(in);
            return (PerspectiveModel) ois.readObject();
        } catch(ClassNotFoundException ex) {
            throw new PersistenceException("Unable to unmarshal data", ex);
        } finally {
            if(ois != null) {
                ois.close();
            }
        }
    }

    public boolean store(OutputStream out, PerspectiveModel info) throws IOException {
        if(info==null || out==null)
            return false;

        ObjectOutputStream oos = null;
        try {
            oos = out instanceof ObjectOutputStream? (ObjectOutputStream) out:new ObjectOutputStream(out);
            oos.writeObject(info);

            return true;
        } finally {
            if(oos != null) {
                oos.close();
            }
        }
    }

}
