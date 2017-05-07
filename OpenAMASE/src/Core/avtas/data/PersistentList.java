// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A list view for storing objects to a file.  This implementation uses the Java object serialization method
 * to store objects to a binary file.  An internal array list allows access (by index number) to any object in
 * the file using a file location value.  Objects are stored using a length parameter (4 byte int) preceding the
 * serialized object.
 *
 * <br/>
 * To implement other serialization methods, extend this class and override the {@link #readObject(java.io.RandomAccessFile, long) } and
 * {@link #writeObject(Object)} methods.
 * <br/>
 * Objects must implement the #java.io.Serializable interface to be stored in this list.
 *
 * @author AFRL/RQQD
 */
public class PersistentList<E> extends AbstractList<E> {

    /** List of locations of objects in the storage file. */
    ArrayList<Long> fileloc = new ArrayList<Long>();
    /** Reading view of the storage file */
    RandomAccessFile readFile;
    /** Writing view of the storage file */
    RandomAccessFile writeFile;

    /**
     * Creates a new list using the specified file.
     * @param filestore the file to use as a data store (caution: this file will be overwritten)
     * @param append set to true if the given file is a valid file store and should be appended.
     */
    public PersistentList(File filestore, boolean append) {
        try {
            if (!filestore.exists()) {
                filestore.createNewFile();
                this.readFile = new RandomAccessFile(filestore, "r");
                this.writeFile = new RandomAccessFile(filestore, "rw");
            } else {
                this.readFile = new RandomAccessFile(filestore, "r");
                this.writeFile = new RandomAccessFile(filestore, "rw");
                if (append) {
                    while (readFile.getFilePointer() < readFile.length() - 1) {
                        fileloc.add(readFile.getFilePointer());
                        readFile.seek(readFile.getFilePointer() + readFile.readInt() + 4);
                    }
                    writeFile.seek(readFile.getFilePointer());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PersistentList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Creates a new list using a temporary file that is created in the temp directory based on operating system
     * settings.  This file is marked for deletion upon exit.
     *
     */
    public PersistentList() {
        try {
            final File tmpFile = File.createTempFile("PersistentList", "");

            if (!tmpFile.exists()) {
                tmpFile.createNewFile();
            }

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    close();
                    tmpFile.delete();
                }
            });

            this.readFile = new RandomAccessFile(tmpFile, "r");
            this.writeFile = new RandomAccessFile(tmpFile, "rw");
        } catch (IOException ex) {
            Logger.getLogger(PersistentList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Appends an element to this list.
     * @param e the element to add.
     * @return true if the element was appended successfully.
     */
    @Override
    public boolean add(E e) {
        try {
            fileloc.add(writeFile.getFilePointer());
            byte[] bytes = writeObject(e);
            writeFile.writeInt(bytes.length);
            writeFile.write(bytes);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PersistentList.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     *
     * Adds an object to the list at the given location.  The object reference is properly stored
     * in the list, but the object itself is appended to the end of the file.
     */
    @Override
    public void add(int index, E e) {
        try {
            fileloc.add(index, writeFile.getFilePointer());
            byte[] bytes = writeObject(e);
            writeFile.writeInt(bytes.length);
            writeFile.write(bytes);
        } catch (IOException ex) {
            Logger.getLogger(PersistentList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets the element in the given index.  This does NOT erase the element from the underlying file, but replaces
     * the reference with a new reference, and appends the new object to the end of the file.
     * @param index index of the element to replace.
     * @param element the new element to add.
     * @return the object passed, or null if an error occurs
     */
    @Override
    public E set(int index, E element) {
        try {
            fileloc.set(index, writeFile.getFilePointer());
            byte[] bytes = writeObject(element);
            writeFile.writeInt(bytes.length);
            writeFile.write(bytes);
            return element;
        } catch (IOException ex) {
            Logger.getLogger(PersistentList.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Removes an element from the list.  This does NOT erase the object in the underlying file.
     * @param index element to remove.
     * @return the removed element.
     */
    @Override
    public E remove(int index) {
        try {
            long pos = fileloc.get(index);
            fileloc.remove(index);
            return readObject(readFile, pos);
        } catch (IOException ex) {
            Logger.getLogger(PersistentList.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Clears the list and resets the file reader/writer pointers.  This does not change the size of the
     * underlying file.
     */
    @Override
    public void clear() {
        try {
            fileloc.clear();
            writeFile.seek(0);
            readFile.seek(0);
        } catch (IOException ex) {
            Logger.getLogger(PersistentList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns an object at the given index.
     * @param index
     * @return an object at the given index
     */
    public E get(int index) {
        try {
            return readObject(readFile, fileloc.get(index));
        } catch (IOException ex) {
            Logger.getLogger(PersistentList.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Writes an object to a byte array for storage to the underlying file.
     * @param obj The object to be written.
     * @throws IOException if there is a problem serializing the object.
     */
    protected byte[] writeObject(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        bos.close();
        out.close();
        return bos.toByteArray();
    }

    /**
     * Reads an object from the peristent storage file.
     * @param pos the file pointer position from which to start the read
     * @return a new object or null if an object cannot be recognized.
     * @throws IOException If there is a problem reading from the file.
     */
    protected E readObject(RandomAccessFile file, long pos) throws IOException {
        file.seek(pos);
        byte[] b = new byte[file.readInt()];
        file.readFully(b);
        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        try {
            return (E) new ObjectInputStream(bis).readObject();
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    /**
     *
     * @return number of objects in this list.
     */
    public int size() {
        return fileloc.size();
    }

    /** Closes the storage file for reading and writing.  Subsequent calls to the list will result in
     *  exceptions.
     */
    public void close() {
        try {
            readFile.close();
            writeFile.close();
        } catch (IOException ex) {
            Logger.getLogger(PersistentList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    /**
     *
     * @return size of the persistent storage file underlying this list.
     */
    public long fileSize() {
        try {
            return readFile.length();
        } catch (IOException ex) {
            Logger.getLogger(PersistentList.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    /** Copies the contents of this file store to a new location.  Will overwrite any data that
     *  currently exists.
     * @param destination
     * @throws IOException
     */
    public synchronized void copy(File destination) throws IOException {
        if (!destination.exists()) {
            destination.createNewFile();
        }
        PersistentList<E> newList = new PersistentList<E>(destination, false);
        for (E e : this) {
            newList.add(e);
        }
        newList.close();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */