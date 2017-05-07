// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 * A JButton that fires actions repeatedly when the user is pressing it.
 * @author AFRL/RQQD
 */
public class RepeatingButton extends JButton{
    
    MouseEvent pressedAction = null;
    boolean repeat = true;
    int delayTime = 50; // millisec
    
    public RepeatingButton() {
        super();
        setupMouseListener();
    }
    
    public RepeatingButton(Action action) {
        super(action);
        setupMouseListener();
    }
    
    public RepeatingButton(String text) {
        super(text);
        setupMouseListener();
    }
    
    public RepeatingButton(String text, Icon icon) {
        super(text, icon);
        setupMouseListener();
    }
    
    /** Time between issuing new action events when a switch remains pressed.  A  
     * value of zero is equivalent to turning off the repeat function.
     * 
     * @param millisec time between event dispatch 
     */
    public void setDelayTime(int millisec) {
        this.delayTime = millisec;
    }
    
    /**
     * @return number of millisecs between action events
     */
    public int getDelayTime() {
        return delayTime;
    }

    /** sets the repeat behavior of the buttons.  A value of true cause the buttons
     * to fire events if the mouse remains pressed.  Set the repeat rate to adjust
     * the rate at which events are dispatched.
     *  
     * @param repeat true to dispatch events when mouse is held down
     */
    public void setRepeatEvents(boolean repeat) {
        this.repeat = repeat;
    }
    
    /**
     * @return true if this button is repeating action events when the user is holding it down
     */
    public boolean isRepeatingEvents() {
        return repeat;
    }

    /**
     * Sets up the key repeater mechanism for each button
     */
    final void setupMouseListener() {
        MouseListener listener = new MouseAdapter() {

            Timer timer = null;

            @Override
            public void mousePressed(final MouseEvent e) {
                setSelected(true);

                if (repeat) {
                    timer = new Timer(delayTime, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            doClick();
                        }
                    });
                    timer.start();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setSelected(false);
                if (timer != null) {
                    timer.stop();
                }
            }
        };
        addMouseListener(listener);
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */