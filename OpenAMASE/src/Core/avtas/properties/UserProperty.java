// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.properties;

import java.beans.PropertyEditor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.swing.JFileChooser;

/**
 *
 * @author AFRL/RQQD
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface UserProperty {
    
    
    public static enum FileTypes {
        Files(JFileChooser.FILES_ONLY), 
        Directories(JFileChooser.DIRECTORIES_ONLY), 
        FilesAndDirectories(JFileChooser.FILES_AND_DIRECTORIES);
        
        /** Uses values from JFileChooser */
        public int type;
        
        FileTypes(int type) {
            this.type = type;
        }
    }
    
    public static enum PathType {
        Absolute,
        Relative,
        Either
    }
    
    /** A textual description of the item. */
    String Description() default "";
    /** A formatted name for this property. */
    String DisplayName() default "";
    /** A category to use for display/organization purposes. */
    String Category() default "";
    
    /** An editor to use for this property.  Must implement {@link PropertyEditor}. */
    Class CustomEditor() default void.class;
    
    /** For file types, specifies the type of files that are valid. */
    FileTypes FileType() default FileTypes.FilesAndDirectories;
    
    /** Allowed extensions for file types. */
    String[] FileExtensions() default {"*"};
    
    /** For integral types, a minimum value allowed. */
    int MinValue() default Integer.MIN_VALUE;
    
    /** For integral types, a maximum value allowed. */
    int MaxValue() default Integer.MAX_VALUE;
    
    /** An increment for integral types */
    int Increment() default 1;
    
    /** For files, defines type type of path */
    PathType PathType() default PathType.Either;
    

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */