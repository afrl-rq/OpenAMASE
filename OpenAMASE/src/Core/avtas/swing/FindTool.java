// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author AFRL/RQQD
 */
public class FindTool {

    int matchCase = Pattern.CASE_INSENSITIVE;
    int useRegex = Pattern.LITERAL;
    boolean matchSelection = false;
    int startIndex = 0;
    String findTerm = "";
    protected JTextComponent textComp = null;
    protected Matcher matcher = null;

    public FindTool(JTextComponent textComp) {
        setTextComponent(textComp);
    }

    public final void setTextComponent(JTextComponent textComp) {
        this.textComp = textComp;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        matchCase = caseSensitive ? 0 : Pattern.CASE_INSENSITIVE;
    }

    public void setUseRegex(boolean useRegex) {
        this.useRegex = useRegex ? 0 : Pattern.LITERAL;
    }

    public void clearFind() {
        matcher = null;
        findTerm = "";
        for (Highlight h : textComp.getHighlighter().getHighlights()) {
            HighlightPainter p = h.getPainter();
            if (p instanceof FindHighlight) {
                textComp.getHighlighter().removeHighlight(h);
            }
        }
    }

    /**
     * returns true if the search term is found, starting from the beginning of
     * the document. A call to this method should be made prior to any calls to
     * {@link #findNext()}
     *
     * @param term
     * @return true if the term is found.
     */
    public boolean find(String term) {
        return find(term, 0);
    }

    public boolean find(String term, int startIndex) {
        if (textComp == null) {
            return false;
        }

        this.findTerm = term;
        this.startIndex = startIndex;

        for (Highlight h : textComp.getHighlighter().getHighlights()) {
            HighlightPainter p = h.getPainter();
            if (p instanceof FindHighlight) {
                textComp.getHighlighter().removeHighlight(h);
            }
        }
        try {
            String text = "";
            if (matchSelection) {
                int selStart = textComp.getSelectionStart();
                int selEnd = textComp.getSelectionEnd();
                if (startIndex < selStart) {
                    startIndex = selStart;
                }
                if (startIndex >= selEnd) {
                    startIndex = selStart;
                }
                text = textComp.getDocument().getText(selStart, selEnd - selStart);

            }
            else {
                text = textComp.getDocument().getText(startIndex, textComp.getDocument().getLength() - startIndex);
            }

            Pattern p = Pattern.compile(term, matchCase | useRegex);

            this.matcher = p.matcher(text);

            if (matcher.find()) {
                textComp.setCaretPosition(startIndex + matcher.end());
                textComp.getHighlighter().addHighlight(matcher.start() + startIndex, matcher.end() + startIndex, new FindHighlight());
                return true;
            }

        } catch (Exception ex) {
            //Logger.getLogger(FindTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean findNext() {
        if (findTerm.isEmpty()) {
            return false;
        }
        if (!find(findTerm, textComp.getCaretPosition())) {
            return find(findTerm, 0);
        }
        return true;
    }

    public boolean replace(String replaceTerm) {
        if (textComp == null || matcher == null) {
            return false;
        }
        try {
            textComp.setSelectionStart(matcher.start() + startIndex);
            textComp.setSelectionEnd(matcher.end() + startIndex);
        } catch (Exception ex) {
            return false;
        }

        if (!textComp.getSelectedText().equals(matcher.group())) {
            return false;
        }

        textComp.replaceSelection(replaceTerm);
        // need to offset start index based on length of replacement term 
        startIndex = startIndex - matcher.group().length() + replaceTerm.length();
        findNext();

        try {
            textComp.scrollRectToVisible(textComp.modelToView(textComp.getCaretPosition()));
        } catch (BadLocationException ex) {
            Logger.getLogger(FindTool.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public int replaceAll(String replaceTerm) {
        if (findTerm.isEmpty()) {
            return 0;
        }
        find(findTerm, 0);
        int numReplace = 0;
        while (replace(replaceTerm)) {
            numReplace++;
        }
        clearFind();
        return numReplace;
    }

    public JDialog getFindWindow() {
        JDialog f = new JDialog(JOptionPane.getFrameForComponent(textComp));
        f.add(new FindPanel());
        f.pack();
        f.setModal(false);
        f.setResizable(true);
        f.setLocationRelativeTo(textComp);
        f.setVisible(true);
        return f;
    }

    public JDialog getReplaceWindow() {
        JDialog f = new JDialog(JOptionPane.getFrameForComponent(textComp));
        f.add(new ReplacePanel());
        f.pack();
        f.setModal(false);
        f.setResizable(true);
        f.setLocationRelativeTo(textComp);
        f.setVisible(true);
        return f;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        final JTextPane pane = new JTextPane();
        f.add(new JScrollPane(pane));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane.setPreferredSize(new Dimension(480, 640));
        f.pack();
        f.setVisible(true);
        try {
            StringBuilder b = new StringBuilder();
            FileReader r = new FileReader(new File("c:/users/matt/Desktop/Test.xml"));
            while (r.ready()) {
                b.append((char) r.read());
            }
            pane.setText(b.toString());
        } catch (Exception ex) {
            Logger.getLogger(FindTool.class.getName()).log(Level.SEVERE, null, ex);
        }

        FindTool findTool = new FindTool(pane);
        findTool.getReplaceWindow();
    }

    static class FindHighlight extends DefaultHighlightPainter {

        public FindHighlight() {
            super(Color.YELLOW);
        }
    }

    class FindPanel extends JPanel {

        private JButton findBut = new JButton("Find");
        private JTextField findField = new JTextField(25);
        private JCheckBox matchCaseBox = new JCheckBox("Match Case");
        private JCheckBox selectionBox = new JCheckBox("Use Selection");
        private JCheckBox useRegexBox = new JCheckBox("Use Regex");

        public FindPanel() {

            setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(5, 5, 5, 5);
            constraints.fill = GridBagConstraints.NONE;

            constraints.gridx = 0;
            constraints.gridy = 0;
            add(new JLabel("Find: "), constraints);

            constraints.gridx = 2;
            add(findBut, constraints);

            constraints.gridx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1.0;
            add(findField, constraints);

            JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            checkBoxPanel.add(matchCaseBox);
            checkBoxPanel.add(useRegexBox);
            checkBoxPanel.add(selectionBox);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 3;
            add(checkBoxPanel, constraints);


            findField.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                void update() {
                    if (!find(findField.getText())) {
                        findField.setForeground(Color.RED);
                    }
                    else {
                        findField.setForeground(UIManager.getColor("TextField.foreground"));
                    }
                }
            });

            ActionListener selectionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    matchCase = matchCaseBox.isSelected() ? 0 : Pattern.CASE_INSENSITIVE;
                    matchSelection = selectionBox.isSelected();
                    useRegex = useRegexBox.isSelected() ? 0 : Pattern.LITERAL;
                }
            };

            matchCaseBox.addActionListener(selectionListener);
            useRegexBox.addActionListener(selectionListener);
            selectionBox.addActionListener(selectionListener);

            ActionListener findAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (matcher != null) {
                        findNext();
                    }
                }
            };

            findBut.addActionListener(findAction);
            findField.addActionListener(findAction);


        }
    }

    class ReplacePanel extends FindPanel {

        JTextField replaceField = new JTextField(25);
        JButton replaceButton;
        JButton replaceAll;

        public ReplacePanel() {

            replaceButton = new JButton(new AbstractAction("Replace") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    replace(replaceField.getText());
                }
            });
            
            replaceAll = new JButton(new AbstractAction("Replace All") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    replaceAll(replaceField.getText());
                }
            });
            
            replaceField.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    replace(replaceField.getText());
                }
            });

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(5, 5, 5, 5);
            constraints.fill = GridBagConstraints.NONE;

            constraints.gridx = 0;
            constraints.gridy = 2;
            add(new JLabel("Replace:"), constraints);

            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridx = 1;
            add(replaceField, constraints);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(replaceButton);
            buttonPanel.add(replaceAll);
            
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridy = 3;
            constraints.gridx = 1;
            constraints.gridwidth = 2;
            constraints.anchor = GridBagConstraints.EAST;
            add(buttonPanel, constraints);



        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */