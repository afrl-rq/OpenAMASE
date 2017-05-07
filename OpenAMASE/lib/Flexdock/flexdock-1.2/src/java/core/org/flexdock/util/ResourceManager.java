// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/* Copyright (c) 2004 Christopher M Butler

 Permission is hereby granted, free of charge, to any person obtaining a copy of
 this software and associated documentation files (the "Software"), to deal in the
 Software without restriction, including without limitation the rights to use,
 copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 Software, and to permit persons to whom the Software is furnished to do so, subject
 to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. */
package org.flexdock.util;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



import org.w3c.dom.Document;

/**
 * This class provides {@code static} convenience methods for resource
 * management, including resource lookups and image, icon, and cursor creation.
 *
 * @author Chris Butler
 */
public class ResourceManager {

    private ResourceManager() {
        // does nothing
    }

    /**
     * Defines the file extension used by native shared libraries on the current
     * system.
     */
    public static final String LIBRARY_EXTENSION = getLibraryExtension();

    private static String getLibraryExtension() {
        return isWindowsPlatform() ? ".dll" : ".so";
    }

    /**
     * Returns {@code true} if the JVM is currently running on {@code Windows};
     * {@code false} otherwise.
     *
     * @return {@code true} if the JVM is currently running on {@code Windows};
     *         {@code false} otherwise.
     */
    public static boolean isWindowsPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.indexOf("windows") != -1 || osName.endsWith(" nt");
    }

    /**
     * Performs resource lookups using the {@code ClassLoader} and classpath.
     * This method attemps to consolidate several techniques used for resource
     * lookup in different situations, providing a common API that works the
     * same from standalone applications to applets to multiple-classloader
     * container-managed applications. Returns {@code null} if specified
     * resource cannot be found.
     *
     * @param uri
     *            the String describing the resource to be looked up
     * @return a {@code URL} representing the resource that has been looked up.
     */
    public static URL getResource(String uri) {
        if (uri == null) {
            return null;
        }

        URL url = ResourceManager.class.getResource(uri);
        if (url == null)
            url = ClassLoader.getSystemResource(uri);

        // if we still couldn't find the resource, then slash it and try again
        if (url == null && !uri.startsWith("/"))
            url = getResource("/" + uri);

        // if resource is still null, then check to see if it's a filesystem
        // path
        if (url == null) {
            try {
                File file = new File(uri);
                if (file.exists())
                    url = file.toURL();
            } catch (MalformedURLException e) {
                System.err.println("Exception: " +e.getMessage());
                url = null;
            }
        }
        return url;
    }

    /**
     * Returns an {@code Image} object based on the specified resource URL. Does
     * not perform any caching on the {@code Image} object, so a new object will
     * be created with each call to this method.
     *
     * @param url
     *            the {@code String} describing the resource to be looked up
     * @exception NullPointerException
     *                if specified resource cannot be found.
     * @return an {@code Image} created from the specified resource URL
     */
    public static Image createImage(String url) {
        try {
            URL location = getResource(url);
            return Toolkit.getDefaultToolkit().createImage(location);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to locate image: " + url);
        }
    }

    /**
     * Returns an {@code Image} object based on the specified resource URL. Does
     * not perform any caching on the {@code Image} object, so a new object will
     * be created with each call to this method.
     *
     * @param imageLocation
     *            the {@code URL} indicating where the image resource may be
     *            found.
     * @exception NullPointerException
     *                if specified resource cannot be found.
     * @return an {@code Image} created from the specified resource URL
     */
    public static Image createImage(URL imageLocation) {
        try {
            return Toolkit.getDefaultToolkit().createImage(imageLocation);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to locate image: "
                                           + imageLocation);
        }
    }

    /**
     * Returns an {@code ImageIcon} object based on the specified resource URL.
     * Uses the {@code ImageIcon} constructor internally instead of dispatching
     * to {@code createImage(String url)}, so {@code Image} objects are cached
     * via the {@code MediaTracker}.
     *
     * @param url
     *            the {@code String} describing the resource to be looked up
     * @exception NullPointerException
     *                if specified resource cannot be found.
     * @return an {@code ImageIcon} created from the specified resource URL
     */
    public static ImageIcon createIcon(String url) {
        try {
            URL location = getResource(url);
            return new ImageIcon(location);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to locate image: " + url);
        }
    }

    /**
     * Returns a {@code Cursor} object based on the specified resource URL.
     * Throws a {@code NullPointerException} if specified resource cannot be
     * found. Dispatches to {@code createImage(URL imageLocation)}, so
     * {@code Image} objects are <b>not</b> cached via the{@code MediaTracker}.
     *
     * @param imageURL
     *            the {@code URL} indicating where the image resource may be
     *            found.
     * @param hotPoint
     *            the X and Y of the large cursor's hot spot. The hotSpot values
     *            must be less than the Dimension returned by
     *            getBestCursorSize().
     * @param name
     *            a localized description of the cursor, for Java Accessibility
     *            use.
     * @exception NullPointerException
     *                if specified resource cannot be found.
     * @exception IndexOutOfBoundsException
     *                if the hotSpot values are outside
     * @return a {@code Cursor} created from the specified resource URL
     */
    public static Cursor createCursor(URL imageURL, Point hotPoint, String name) {
        Image image = createImage(imageURL);
        Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(image,
                   hotPoint, name);
        return c;
    }

    /**
     * Returns a {@code Cursor} object based on the specified resource URL.
     * Throws a {@code NullPointerException} if specified resource cannot be
     * found. Dispatches to {@code createImage(String url)}, so {@code Image}
     * objects are <b>not</b> cached via the{@code MediaTracker}.
     *
     * @param url
     *            the {@code String} describing the resource to be looked up
     * @param hotPoint
     *            the X and Y of the large cursor's hot spot. The hotSpot values
     *            must be less than the Dimension returned by
     *            getBestCursorSize().
     * @param name
     *            a localized description of the cursor, for Java Accessibility
     *            use.
     * @exception NullPointerException
     *                if specified resource cannot be found.
     * @exception IndexOutOfBoundsException
     *                if the hotSpot values are outside
     * @return a {@code Cursor} created from the specified resource URL
     */
    public static Cursor createCursor(String url, Point hotPoint, String name) {
        Image image = createImage(url);
        Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(image,
                   hotPoint, name);
        return c;
    }

    /**
     * Attempts to load the specified native {@code library}, using
     * {@code classpathResource} and the filesystem to implement several
     * fallback mechanisms in the event the library cannot be loaded. This
     * method should provide seamless installation and loading of native
     * libraries from within the classpath so that native libraries may be
     * packaged within the relavant library JAR, rather than requiring separate
     * user installation of the native libraries into the system {@code $PATH}.
     * <p>
     * If the specified {@code library} is {@code null}, then this method
     * returns with no action taken.
     * <p>
     * This method will first attempt to call
     * {@code System.loadLibrary(library)}. If this call is successful, then
     * the method will exit here. If an {@code UnsatisfiedLinkError} is
     * encountered, then this method attempts to locate a FlexDock-specific
     * filesystem resource for the native library, called the "FlexDock
     * Library".
     * <p>
     * The FlexDock Library will reside on the filesystem under the user's home
     * directory with the path <tt>${user.home}/flexdock/${library}${native.lib.extension}</tt>.
     * Thus, if this method is called with an argument of {@code "foo"} for the
     * library, then under windows the FlexDock Library should be
     * <tt>C:\Documents and Settings\${user.home}\flexdock\foo.dll</tt>. Under
     * any type of Unix system, the FlexDock library should be
     * <tt>/home/${user.home}/flexdock/foo.so</tt>.
     * <p>
     * If the FlexDock Library exists on the filesystem, then this method will
     * attempt to load it by calling {@code System.load(String filename)} with
     * the FlexDock Library's absolute path. If this call is successful, then
     * the method exits here.
     * <p>
     * If the FlexDock Library cannot be loaded, then the specified
     * {@code classpathResource} is checked. If {@code classpathResource} is
     * {@code null}, then there is no more information available to attempt to
     * resolve the requested library and this method throws the last
     * {@code UnsatisfiedLinkError} encountered.
     * <p>
     * If {@code classpathResource} is non-{@code null}, then an
     * {@code InputStream} to the specified resource is resolved from the class
     * loader. The contents of the {@code InputStream} are read into a
     * {@code byte} array and written to disk as the FlexDock Library file. The
     * FlexDock Library is then loaded with a call to
     * {@code System.load(String filename)} with the FlexDock Library's absolute
     * path. If the specified {@code classpathResource} cannot be resolved by
     * the class loader, if any errors occur during this process of extracting
     * and writing to disk, or if the resulting FlexDock Library file cannot be
     * loaded as a native library, then this method throws an appropriate
     * {@code UnsatisfiedLinkError} specific to the situation that prevented the
     * native library from loading.
     * <p>
     * Note that because this method may extract resources from the classpath
     * and install to the filesystem as a FlexDock Library, subsequent calls to
     * this method across JVM sessions will find the FlexDock Library on the
     * filesystem and bypass the extraction process.
     *
     * @param library
     *            the native library to load
     * @param classpathResource
     *            the fallback location within the classpath from which to
     *            extract the desired native library in the event it is not
     *            already installed on the target system
     * @exception UnsatisfiedLinkError
     *                if the library cannot be loaded
     */
    public static void loadLibrary(String library, String classpathResource) {
        if (library == null)
            return;

        UnsatisfiedLinkError linkageError = null;
        try {
            System.loadLibrary(library);
            return;
        } catch (UnsatisfiedLinkError err) {
            // pass through here
            linkageError = err;
        }

        // determine a file from which we can load our library.
        File file = new File(System.getProperty("user.home") + "/flexdock");
        file.mkdirs();
        file = new File(file.getAbsolutePath() + "/" + library
                        + LIBRARY_EXTENSION);

        // if the file already exists, try to load from it
        if (file.exists()) {
            try {
                System.load(file.getAbsolutePath());
                return;
            } catch (UnsatisfiedLinkError err) {
                // pass through here
                linkageError = err;
            }
        }

        // if we can't load from the classpath, then we're stuck.
        // throw the last UnsatisfiedLinkError we encountered.
        if (classpathResource == null && linkageError != null)
            throw linkageError;

        // if the file didn't exist, or we couldn't load from it,
        // we'll have to pull from the classpath resource and write it
        // to this file. We'll then try to load from the file again.
        FileOutputStream fileOut = null;

        // get a handle to our resource in the classpath
        ClassLoader cl = ResourceManager.class.getClassLoader();
        InputStream in = cl.getResourceAsStream(classpathResource);
        if (in == null)
            throw new UnsatisfiedLinkError(
                "Unable to locate classpath resource: " + classpathResource);

        try {
            // create an outputstream to our destination file
            fileOut = new FileOutputStream(file);

            byte[] tmp = new byte[1024];
            // copy the contents of our resource out to the destination
            // file 1K at a time. 1K may seem arbitrary at first, but today
            // is a Tuesday, so it makes perfect sense.
            int bytesRead = in.read(tmp);
            while (bytesRead != -1) {
                fileOut.write(tmp, 0, bytesRead);
                bytesRead = in.read(tmp);
            }
        } catch (IOException giveUp) {
            // well, I guess we're screwed, aren't we?
            UnsatisfiedLinkError err = new UnsatisfiedLinkError(
                "Unable to extract resource to file: "
                + file.getAbsolutePath());
            err.initCause(giveUp);
            throw err;
        } finally {
            close(fileOut);
            close(in);
        }

        // now that our classpath resource has been written to disk, load the
        // native
        // library from this file
        System.load(file.getAbsolutePath());
    }

    /**
     * Returns a {@code Document} object based on the specified resource
     * {@code uri}. This method resolves a {@code URL} from the specified
     * {@code String} via {@code getResource(String uri)} and dispatches to
     * {@code getDocument(URL url)}. If the specified {@code uri} is
     * {@code null}, then this method returns {@code null}.
     *
     * @param uri
     *            the {@code String} describing the resource to be looked up
     * @return a {@code Document} object based on the specified resource
     *         {@code uri}
     * @see #getResource(String)
     * @see #getDocument(URL)
     */
    public static Document getDocument(String uri) {
        URL resource = getResource(uri);
        return getDocument(resource);
    }

    /**
     * Returns a {@code Document} object based on the specified resource
     * {@code URL}. This method will open an {@code InputStream} to the
     * specified {@code URL} and construct a {@code Document} instance. If any
     * {@code Exceptions} are encountered in the process, this method returns
     * {@code null}. If the specified {@code URL} is {@code null}, then this
     * method returns {@code null}.
     *
     * @param url
     *            the {@code URL} describing the resource to be looked up
     * @return a {@code Document} object based on the specified resource
     *         {@code URL}
     */
    public static Document getDocument(URL url) {
        if (url == null)
            return null;

        InputStream inStream = null;
        try {
            inStream = url.openStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory
                                             .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(inStream);
        } catch (Exception e) {
            System.err.println("Exception: " +e.getMessage());
        } finally {
            close(inStream);
        }
        return null;
    }

    /**
     * Returns a {@code Properties} object based on the specified resource
     * {@code uri}. This method resolves a {@code URL} from the specified
     * {@code String} via {@code getResource(String uri)} and dispatches to
     * {@code getProperties(URL url, boolean failSilent)} with an argument of
     * {@code false} for {@code failSilent}. If the specified {@code uri} is
     * {@code null}, then this method will print a stack trace for the ensuing
     * {@code NullPointerException} and return {@code null}.
     *
     * @param uri
     *            the {@code String} describing the resource to be looked up
     * @return a {@code Properties} object based on the specified resource
     *         {@code uri}.
     * @see #getResource(String)
     * @see #getProperties(URL, boolean)
     */
    public static Properties getProperties(String uri) {
        return getProperties(uri, false);
    }

    /**
     * Returns a {@code Properties} object based on the specified resource
     * {@code uri}. This method resolves a {@code URL} from the specified
     * {@code String} via {@code getResource(String uri)} and dispatches to
     * {@code getProperties(URL url, boolean failSilent)}, passing the
     * specified {@code failSilent} parameter. If the specified {@code uri} is
     * {@code null}, this method will return {@code null}. If
     * {@code failSilent} is {@code false}, then the ensuing
     * {@code NullPointerException's} stacktrace will be printed to the
     * {@code System.err} before returning.
     *
     * @param uri
     *            the {@code String} describing the resource to be looked up
     * @param failSilent
     *            {@code true} if no errors are to be reported to the
     *            {@code System.err} before returning; {@code false} otherwise.
     * @return a {@code Properties} object based on the specified resource
     *         {@code uri}.
     * @see #getResource(String)
     * @see #getProperties(URL, boolean)
     */
    public static Properties getProperties(String uri, boolean failSilent) {
        URL url = getResource(uri);
        return getProperties(url, failSilent);
    }

    /**
     * Returns a {@code Properties} object based on the specified resource
     * {@code URL}. This method dispatches to
     * {@code getProperties(URL url, boolean failSilent)}, with an argument of
     * {@code false} for {@code failSilent}. If the specified {@code uri} is
     * {@code null}, this method will print the ensuing
     * {@code NullPointerException} stack tracke to the {@code System.err} and
     * return {@code null}.
     *
     * @param url
     *            the {@code URL} describing the resource to be looked up
     * @return a {@code Properties} object based on the specified resource
     *         {@code url}.
     * @see #getProperties(URL, boolean)
     */
    public static Properties getProperties(URL url) {
        return getProperties(url, false);
    }

    /**
     * Returns a {@code Properties} object based on the specified resource
     * {@code url}. If the specified {@code uri} is {@code null}, this method
     * will return {@code null}. If any errors are encountered during the
     * properties-load process, this method will return {@code null}. If
     * {@code failSilent} is {@code false}, then the any encoutered error
     * stacktraces will be printed to the {@code System.err} before returning.
     *
     * @param url
     *            the {@code URL} describing the resource to be looked up
     * @param failSilent
     *            {@code true} if no errors are to be reported to the
     *            {@code System.err} before returning; {@code false} otherwise.
     * @return a {@code Properties} object based on the specified resource
     *         {@code url}.
     */
    public static Properties getProperties(URL url, boolean failSilent) {
        if (failSilent && url == null)
            return null;

        InputStream in = null;
        try {
            in = url.openStream();
            Properties p = new Properties();
            p.load(in);
            return p;
        } catch (Exception e) {
            if (!failSilent)
                System.err.println("Exception: " +e.getMessage());
            return null;
        } finally {
            close(in);
        }
    }

    /**
     * Calls {@code close()} on the specified {@code InputStream}. Any
     * {@code Exceptions} encountered will be printed to the {@code System.err}.
     * If {@code in} is {@code null}, then no {@code Exception} is thrown and
     * no action is taken.
     *
     * @param in
     *            the {@code InputStream} to close
     * @see InputStream#close()
     */
    public static void close(InputStream in) {
        try {
            if (in != null)
                in.close();
        } catch (Exception e) {
            System.err.println("Exception: " +e.getMessage());
        }
    }

    /**
     * Calls {@code close()} on the specified {@code OutputStream}. Any
     * {@code Exceptions} encountered will be printed to the {@code System.err}.
     * If {@code out} is {@code null}, then no {@code Exception} is thrown and
     * no action is taken.
     *
     * @param out
     *            the {@code OutputStream} to close
     * @see OutputStream#close()
     */
    public static void close(OutputStream out) {
        try {
            if (out != null)
                out.close();
        } catch (Exception e) {
            System.err.println("Exception: " +e.getMessage());
        }
    }

    /**
     * Calls {@code close()} on the specified {@code Socket}. Any
     * {@code Exceptions} encountered will be printed to the {@code System.err}.
     * If {@code socket} is {@code null}, then no {@code Exception} is thrown
     * and no action is taken.
     *
     * @param socket
     *            the {@code Socket} to close
     * @see Socket#close()
     */
    public static void close(Socket socket) {
        try {
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            System.err.println("Exception: " +e.getMessage());
        }
    }
}
