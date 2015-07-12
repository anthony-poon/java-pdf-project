/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author anthony.poon
 */

public class DragZoomPanel extends JScrollPane {
    
    private JScrollPane scrollPane;
    private MouseEvent mouseHoldEvent;
    private BufferedImage image;
    private final JPanel imagePanel = new ImagePanel();
    private Point comparePoint = null;
    
    MouseInputAdapter mouseAction = new MouseInputAdapter() {
        public void mousePressed(MouseEvent e){
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }
        public void mouseReleased(MouseEvent e){
            comparePoint = null;
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }        
    };
    
    MouseMotionListener doScrollRectToVisible = new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
            double moveRate = 1;
            //panelBeingScrolled.scrollRectToVisible(r);
            if (comparePoint == null) {
                comparePoint = new Point(e.getX(), e.getY());
            }
            int xDisplacement = comparePoint.x - e.getX();
            int yDisplacement = comparePoint.y - e.getY();
            comparePoint = new Point(e.getX(), e.getY());
            //scrollPane.setAutoscrolls(true);
            //scrollPane.getViewport().toViewCoordinates(new Point(10000, 10000));
            JScrollBar hScroll = scrollPane.getHorizontalScrollBar();
            JScrollBar vScroll = scrollPane.getVerticalScrollBar();
            hScroll.setValue((int) Math.round(hScroll.getValue() + xDisplacement * moveRate));
            vScroll.setValue((int) Math.round(vScroll.getValue() + yDisplacement * moveRate));
            Rectangle r = new Rectangle(10000, 10000);
            //scrollPane.scrollRectToVisible(r);            
        }       
        
    };
    class ImagePanel extends JPanel{
        public ImagePanel() {
            super();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, null);
            }
        }
    }
    
    public DragZoomPanel() {
        super();        
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setVisible(true);
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
        this.scrollPane = this;
        this.addMouseListener(mouseAction);
        this.addMouseMotionListener(doScrollRectToVisible);
        this.setViewportView(imagePanel);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                imagePanel.repaint();
                imagePanel.revalidate();
            }
        });
        //imagePanel.repaint();
    }
    
    public JPanel getInnterPanel() {
        return imagePanel;
    }
}

