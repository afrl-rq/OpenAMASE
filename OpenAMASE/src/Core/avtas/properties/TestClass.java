// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.properties;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

/**
 *
 * @author AFRL/RQQD
 */
public class TestClass {
    
    @UserProperty
    private int intVal = 5;
    @UserProperty( Description="Color")
    private Color color = Color.RED;
    @UserProperty( )
    private File file = new File("../test");
    @UserProperty( FileType = UserProperty.FileTypes.Directories)
    private File dir;
    @UserProperty
    private Font font = new Font("Arial", Font.PLAIN, 12);
    @UserProperty
    private TestEnum anEnum = TestEnum.One;
    @UserProperty
    private List list = new ArrayList();
    
    @UserProperty
    Action push = new AbstractAction("Push") {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Hello");
        }
    };
    
    private Object ignoreMe;

    public TestClass() {
        
        list.add("one");
        list.add("two");
        list.add("three");
    }
    
    

    /**
     * @return the intVal
     */
    public int getIntVal() {
        return intVal;
    }

    /**
     * @param intVal the intVal to set
     */
    //public void setIntVal(int intVal) {
    //    this.intVal = intVal;
    //}

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * @return the anEnum
     */
    public TestEnum getAnEnum() {
        return anEnum;
    }

    /**
     * @param anEnum the anEnum to set
     */
    public void setAnEnum(TestEnum anEnum) {
        this.anEnum = anEnum;
    }

    /**
     * @return the ignoreMe
     */
    public Object getIgnoreMe() {
        return ignoreMe;
    }

    /**
     * @param ignoreMe the ignoreMe to set
     */
    public void setIgnoreMe(Object ignoreMe) {
        this.ignoreMe = ignoreMe;
    }

    /**
     * @return the dir
     */
    public File getDir() {
        return dir;
    }

    /**
     * @param dir the dir to set
     */
    public void setDir(File dir) {
        this.dir = dir;
    }
    
    
    public static enum TestEnum {
        One, Two, Three
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        System.out.println("a new list");
        this.list = list;
    }
    
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */