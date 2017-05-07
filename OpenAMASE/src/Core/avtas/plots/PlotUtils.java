// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================


package avtas.plots;

import avtas.swing.ImageSelection;
import avtas.util.WindowUtils;
import de.erichseifert.gral.data.AbstractDataSource;
import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.data.statistics.Histogram1D;
import de.erichseifert.gral.data.statistics.Statistics;
import de.erichseifert.gral.plots.AbstractPlot;
import de.erichseifert.gral.plots.BarPlot;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.legends.Legend;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;
import de.erichseifert.gral.util.Orientation;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author AFRL/RQQD
 */
public class PlotUtils {

    public static enum PlotType {

        Scatter, Line;
    }

    public static Color[] defaultColorSeq = new Color[]{
        Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN
    };

    public static DataSeries createDataSeries(String name, double[] x, double[] y) {
        if (x.length != y.length) {
            throw new RuntimeException("X and Y data must be of equal length");
        }

        DataTable data = new DataTable(Double.class, Double.class);
        for (int i = 0; i < x.length; i++) {
            data.add(x[i], y[i]);
        }

        return new DataSeries(name, data, 0, 1);
    }

    public static InteractivePanel createScatterPlot(PlotSeries2D data, String title, String xLabel, String yLabel) {

        // Create a new xy-plot
        DataSeries series = data.getDataSeries();
        XYPlot plot = new XYPlot(series);

        // Format plot
        plot.setInsets(new Insets2D.Double(40.0, 40.0, 40.0, 40.0));
        plot.setLegendVisible(true);

        // Format legend
        plot.getLegend().setOrientation(Orientation.HORIZONTAL);
        plot.getLegend().setAlignmentY(1.0);

        InteractivePanel panel = new InteractivePanel(plot);
        addLegendDragger(panel);
        return panel;
    }

    public static InteractivePanel createLinePlot(PlotSeries2D data, String title, String xLabel, String yLabel) {

        // Create a new xy-plot
        DataSeries series = data.getDataSeries();
        XYPlot plot = new XYPlot(series);

        // Format plot
        plot.setInsets(new Insets2D.Double(40.0, 40.0, 40.0, 40.0));
        plot.setLegendVisible(true);

        // Format legend
        plot.getLegend().setOrientation(Orientation.HORIZONTAL);
        plot.getLegend().setAlignmentY(1.0);

        plot.setPointRenderer(series, null);
        DefaultLineRenderer2D line = new DefaultLineRenderer2D();
        line.setColor(defaultColorSeq[0]);
        plot.setLineRenderer(series, line);

        InteractivePanel panel = new InteractivePanel(plot);
        addLegendDragger(panel);

        return panel;
    }

    public static InteractivePanel createHistogram(double[] vals, int numDivisions, String title, String xLabel, String yLabel) {

        DataTable data = new DataTable(Double.class);
        for (int i = 0; i < vals.length; i++) {
            data.add(vals[i]);
        }

        // Create histogram from data
        Histogram1D histogram = new Histogram1D(data, Orientation.VERTICAL, numDivisions);

        // Create new bar plot
        BarPlot plot = new BarPlot(histogram);

        // Format plot
        plot.setInsets(new Insets2D.Double(20.0, 40.0, 40.0, 40.0));
        //plot.setLegendVisible(true);

        // Format legend
        //plot.getLegend().setOrientation(Orientation.HORIZONTAL);
        //plot.getLegend().setAlignmentY(0.0);
        InteractivePanel panel = new InteractivePanel(plot);
        return panel;
    }

    public static XYPlot createPlot(DataSeries data, String title, String xAxis, String yAxis) {

        // Create a new xy-plot
        XYPlot plot = new XYPlot(data);

        // Format plot
        plot.setInsets(new Insets2D.Double(40.0, 40.0, 40.0, 40.0));
        plot.setLegendVisible(true);

        // Format legend
        plot.getLegend().setOrientation(Orientation.HORIZONTAL);
        plot.getLegend().setAlignmentY(1.0);

        if (title != null) {
            plot.getTitle().setText(title);
        }
        

        
        return plot;

    }

    public static void addLineSeries(XYPlot plot, PlotSeries2D data, Color color) {
        DataSeries series = data.getDataSeries();
        plot.add(series);
        formatDataSeries(plot, series, color, null);
    }

    public static void addScatterSeries(XYPlot plot, PlotSeries2D data, Color color) {
        DataSeries series = data.getDataSeries();
        plot.add(series);
        formatDataSeries(plot, series, null, color);
    }

    public static void formatDataSeries(XYPlot plot, DataSource data, Color lineColor, Color markerColor) {
        if (!plot.getData().contains(data)) {
            return;
        }
        DefaultLineRenderer2D lineRenderer = lineColor == null ? null : new DefaultLineRenderer2D();
        if (lineRenderer != null) {
            lineRenderer.setColor(lineColor);
        }
        plot.setLineRenderer(data, lineRenderer);

        DefaultPointRenderer2D ptRenderer = markerColor == null ? null : new DefaultPointRenderer2D();
        if (ptRenderer != null) {
            ptRenderer.setColor(markerColor);
        }
        plot.setPointRenderer(data, ptRenderer);
    }

    protected static void setDefaultColors(XYPlot plot) {
        List<DataSource> data = plot.getData();
        for (int i = 0; i < data.size(); i++) {
            int colorIndex = i % defaultColorSeq.length;
            DataSource dataSource = plot.getData().get(i);
            LineRenderer lineRender = plot.getLineRenderer(dataSource);
            if (lineRender != null) {
                lineRender.setColor(defaultColorSeq[colorIndex]);
            }
            PointRenderer ptRender = plot.getPointRenderer(dataSource);
            if (ptRender != null) {
                ptRender.setColor(defaultColorSeq[colorIndex]);
            }
        }
    }

    public static JFrame showChart(InteractivePanel chart) {
        JFrame frame = new JFrame();
        frame.add(chart);

        frame.setJMenuBar(new JMenuBar());
        frame.getJMenuBar().add(getPlotTools(chart));

        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return frame;
    }

    public static JMenu getPlotTools(final InteractivePanel panel) {
        JMenu menu = new JMenu("File");
        final JFileChooser chooser = WindowUtils.getFilteredChooser(".png", new FileNameExtensionFilter("PNG file", "png"));
        menu.add(new AbstractAction("Save as...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ans = chooser.showSaveDialog(panel);
                if (ans == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
                    try {
                        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2 = image.createGraphics();
                        panel.print(g2);
                        g2.dispose();
                        ImageIO.write(image, "png", chooser.getSelectedFile());
                    } catch (IOException ex) {
                        Logger.getLogger(PlotUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        menu.add(new AbstractAction("Copy to Clipboard...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image.createGraphics();
                panel.print(g2);
                g2.dispose();
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new ImageSelection(image), null);
            }
        });

        return menu;
    }

    protected static void addLegendDragger(final InteractivePanel panel) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            MouseEvent mouseDown = null;

            @Override
            public void mousePressed(MouseEvent e) {
                if (panel.getDrawable() instanceof AbstractPlot) {
                    AbstractPlot plot = (AbstractPlot) panel.getDrawable();
                    Legend legend = plot.getLegend();
                    if (legend.getBounds().contains(e.getPoint())) {
                        mouseDown = e;
                        e.consume();
                        panel.setPannable(false);
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseDown != null) {
                    double dx = e.getPoint().x - mouseDown.getPoint().x;
                    double dy = e.getPoint().y - mouseDown.getPoint().y;
                    AbstractPlot plot = (AbstractPlot) panel.getDrawable();
                    Legend legend = plot.getLegend();
                    Rectangle2D rect = legend.getBounds();
                    rect.setRect(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());
                    legend.setBounds(rect);
                    legend.refresh();
                    panel.repaint();
                    mouseDown = e;
                    e.consume();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDown = null;
                panel.setPannable(true);
            }

        };

        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
        //panel.setPannable(false);
    }
    
    
    
    
    
    public static class PlotSeries2D  {
        
        //ArrayList<Point2D> points = new ArrayList<>();
        DataTable table = new DataTable(2, Double.class);
        DataSeries series;
        
        public PlotSeries2D(String name) {
            series = new DataSeries(name, table, 0, 1);
        }
        
        public PlotSeries2D(String name, double[] x, double[] y) {
            this(name);
            for (int i=0; i<x.length; i++) {
                add(x[i], y[i]);
            }
        }
        
        public void add(double x, double y) {
            table.add(x, y);
        }
        
        public double getX(int index) {
            return (Double) table.get(0, index);
        }
        
        public double getY(int index) {
            return (Double) table.get(1, index);
        }
        
        public int size() {
            return table.getRowCount();
        }
        
        public void clear() {
            table.clear();
        }
        
        public DataSeries getDataSeries() {
            return series;
        }


    }
    
    
    
    
    
    

    public static void main(String[] args) {

        PlotSeries2D series = new PlotSeries2D("test");
        for (int i = 0; i < 100; i++) {
            series.add(i / 20, Math.random() * 25);
        }
        InteractivePanel chart = createScatterPlot(series, "test plot", "x", "y");
        JFrame frame = showChart(chart);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        PlotSeries2D data = new PlotSeries2D("two", new double[]{1, 2, 3, 4, 5}, new double[]{1, 4, 9, 16, 25});

        addLineSeries((XYPlot) chart.getDrawable(), data, Color.RED);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */