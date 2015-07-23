/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
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
    
    private CompressorView parent;
    private JScrollPane scrollPane;
    private BufferedImage image;
    private final JPanel imagePanel = new ImagePanel();
    private Point comparePoint = null;
    
    MouseInputAdapter mouseAction = new MouseInputAdapter() {
        @Override
        public void mousePressed(MouseEvent e){
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }
        @Override
        public void mouseReleased(MouseEvent e){
            comparePoint = null;
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }      
    };
    
    MouseMotionListener doScrollRectToVisible = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (comparePoint == null) {
                comparePoint = new Point(e.getX(), e.getY());
            }
            int xDisplacement = comparePoint.x - e.getX();
            int yDisplacement = comparePoint.y - e.getY();
            comparePoint = new Point(e.getX(), e.getY());
            JScrollBar hScroll = scrollPane.getHorizontalScrollBar();
            JScrollBar vScroll = scrollPane.getVerticalScrollBar();
            hScroll.setValue((int) Math.round(hScroll.getValue() + xDisplacement));
            vScroll.setValue((int) Math.round(vScroll.getValue() + yDisplacement));   
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
    
    public DragZoomPanel(CompressorView parent) {
        super();
        this.parent = parent;
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
    }
    
    public JPanel getInnterPanel() {
        return imagePanel;
    }
}

