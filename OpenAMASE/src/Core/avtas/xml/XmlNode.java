// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.xml;

/**
 * Basic interface for an XML Node
 * @author AFRL/RQQD
 */
public abstract class XmlNode implements Cloneable {
    private Element parent;

    /** Sets the parent node of this node. */
    public void setParent(Element parent) {
        this.parent = parent;
    }

    /** Returns the node that this node is attached to.  May be null. */
    public Element getParent() {
        return parent;
    }

    /** Removes this node from its parent, if it currently has a parent.
     *
     * @return true if this node has a parent and is removed from it.
     */
    public boolean detach() {
        if (getParent() != null) {
            getParent().remove(this);
            return true;
        }
        return false;
    }

    public XmlNode clone() {
        return null;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */