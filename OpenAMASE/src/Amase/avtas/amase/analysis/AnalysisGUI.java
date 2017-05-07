// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.analysis;

import avtas.util.WindowUtils;
import avtas.xml.Comment;
import avtas.xml.ui.XMLEditPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import avtas.xml.Element;
import avtas.xml.XmlNode;
import avtas.xml.XmlWriter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Runs a series of analysis tests on a list of LMCP messages or a playback file
 *
 * @author AFRL/RQQD
 */
public class AnalysisGUI extends JPanel {

    private XMLEditPane textArea = new XMLEditPane();
    private JFileChooser chooser;
    private JPanel thisPanel = this;
    private AnalysisManager analysisMgr;

    public AnalysisGUI(AnalysisManager analysisMgr) {

        this.analysisMgr = analysisMgr;

        setLayout(new BorderLayout(2, 2));
        textArea.setPreferredSize(new Dimension(400, 400));
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        chooser = WindowUtils.getFilteredChooser("xml", new FileNameExtensionFilter("XML Files", "XML", "xml"));
        chooser.setMultiSelectionEnabled(false);

        JPanel butPanel = new JPanel();

        final JButton saveBut = new JButton("Save Output");
        butPanel.add(saveBut);
        saveBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Element el = refreshReport();
                if (el != null) {
                    chooser.showSaveDialog(thisPanel);
                    File file = chooser.getSelectedFile();
                    if (file != null) {
                        el.toFile(file);
                    }
                }
            }
        });

        final JButton refreshBut = new JButton("Refresh");
        butPanel.add(refreshBut);
        refreshBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshReport();
            }
        });

        final JButton copyBut = new JButton("Copy");
        butPanel.add(copyBut);
        saveBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = getToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(textArea.getText()), null);
            }
        });

        add(butPanel, BorderLayout.SOUTH);
        refreshReport();

    }

    public Element refreshReport() {
        textArea.setText("");
        if (analysisMgr != null) {
            try {
                Element el = analysisMgr.getAnalysisReportXML();
                textArea.setText(XmlWriter.toFormattedString(el));
                return el;
            } catch (Exception ex) {
                Logger.getLogger(AnalysisGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        textArea.setCaretPosition(0);
        return null;
    }

    protected String getHTML(Element el) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        appendHtml(sb, el, "");
        sb.append("</html>");
        return sb.toString();
    }

    protected void appendHtml(StringBuilder sb, XmlNode n, String ws) {
        if (n instanceof Element) {
            Element el = (Element) n;
            sb.append(ws);
            if (el.hasChildren()) {
                sb.append("<b>").append(el.getName()).append(" = ").append(el.getText()).append("</b><br/>");
            }
            else {
                sb.append(el.getName()).append(" = ").append(el.getText()).append("<br/>");
            }
            if (el.hasAttributes()) {
                sb.append(ws).append("<em>");
                for (int i = 0; i < el.getAttributeCount(); i++) {
                    sb.append(el.getAttributeName(i)).append(" = ").append(el.getAttributeValue(i)).append(", ");
                }
                sb.append("</em><br/>");
            }
            for (XmlNode child : el.getChildren()) {
                appendHtml(sb, child, ws + "&nbsp;&nbsp;");
            }
        }
        else if (n instanceof Comment) {
            sb.append("<p><em>").append(ws).append(((Comment) n).getComment()).append("</em></p>");
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */