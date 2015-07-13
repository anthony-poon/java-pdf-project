/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools |
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 Templates
 * and open the template in the editor.
 */
package view;

import com.itextpdf.text.DocumentException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.coobird.thumbnailator.Thumbnails;
import pdfcompressor.PDFCompressor;
import pdfcompressor.ProgressListener;

/**
 *
 * @author anthony.poon
 */


public class CompressorView extends javax.swing.JFrame {
    private int currentPage = 1;
    private String pathToSoruce;
    private int compressRate = 50;
    private PDFCompressor compressor;
    private BufferedImage previewImage;
    private int zoom = 100;
    private int dpi = 200;
    private final DragZoomPanel previewPanel = new DragZoomPanel();
    private Thread fileSizeCalThread;   
    
    class fileSizeThread extends Thread {
        public fileSizeThread() {
            super();
        }
        public void run() {
            if (compressor != null) {
                fileSizeValue.setText("Calcuelating...");
                try {                
                    compressor.addSizeEstimateListener(new ProgressListener() {
                        @Override
                        public void haveProgress(int currentProgress, int totalProgress) {
                        }
                        @Override
                        public void finished() {
                        }
                        public void finished(int filesize) {
                            fileSizeValue.setText(String.valueOf(Math.round(filesize/1024)) + " KB");
                        }                    
                    });
                    compressor.getFileSize();
                } catch (DocumentException | IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, ex.getMessage());
                }
            }
        }
    }
    public CompressorView() {
        initComponents();        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        openPDFLabel = new javax.swing.JLabel();
        pathTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        dpiSlider = new javax.swing.JSlider();
        compressButton = new javax.swing.JButton();
        zoomSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        compressLevelSlider = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        previewProgressBar = new javax.swing.JProgressBar();
        wrapper = new javax.swing.JPanel();
        fileSizeLabel = new javax.swing.JLabel();
        fileSizeValue = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        pageNumField = new javax.swing.JTextField();
        nextButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        openPDFLabel.setText("Open PDF:");

        pathTextField.setEditable(false);
        pathTextField.setToolTipText("");

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseOnClick(evt);
            }
        });

        dpiSlider.setMajorTickSpacing(50);
        dpiSlider.setMaximum(300);
        dpiSlider.setMinimum(50);
        dpiSlider.setPaintLabels(true);
        dpiSlider.setPaintTicks(true);
        dpiSlider.setSnapToTicks(true);
        dpiSlider.setToolTipText("");
        dpiSlider.setValue(dpi);
        dpiSlider.setEnabled(false);
        Hashtable dpiLabelTable = new Hashtable();
        for (Integer i = 50; i <= 300; i = i + 50) {
            dpiLabelTable.put(i, new JLabel(i.toString() + "dpi") );
        }
        dpiSlider.setLabelTable(dpiLabelTable);
        dpiSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                dpiSliderStateChanged(evt);
            }
        });

        compressButton.setText("Compress...");
        compressButton.setEnabled(false);
        compressButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compressButtonPressed(evt);
            }
        });

        zoomSlider.setMajorTickSpacing(50);
        zoomSlider.setMaximum(300);
        zoomSlider.setMinimum(50);
        zoomSlider.setOrientation(javax.swing.JSlider.VERTICAL);
        zoomSlider.setPaintLabels(true);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setValue(zoom);
        zoomSlider.setEnabled(false);
        Hashtable zoomLabelTable = new Hashtable();
        for (Integer i = 50; i <= 300; i = i + 50) {
            zoomLabelTable.put(i, new JLabel(i.toString() + "%") );
        }
        zoomSlider.setLabelTable(zoomLabelTable);
        zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                zoomSliderStateChanged(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Zoom");

        Hashtable compressLabelTable = new Hashtable();
        for (Integer i = 10; i <= 100; i = i + 10) {
            compressLabelTable.put(i, new JLabel(i.toString() + "%") );
        }
        compressLevelSlider.setLabelTable(compressLabelTable);
        compressLevelSlider.setMajorTickSpacing(10);
        compressLevelSlider.setMinimum(10);
        compressLevelSlider.setPaintLabels(true);
        compressLevelSlider.setPaintTicks(true);
        compressLevelSlider.setEnabled(false);
        compressLevelSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                compressLevelSliderStateChanged(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Compress Level");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("DPI");

        previewProgressBar.setVisible(false);
        previewProgressBar.setIndeterminate(true);

        wrapper.setLayout(new java.awt.GridLayout(1, 0));

        fileSizeLabel.setText("Estimated File Size:");

        backButton.setText("Previous");
        backButton.setEnabled(false);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        pageNumField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pageNumField.setEnabled(false);

        nextButton.setText("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(compressButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(compressLevelSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(backButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pageNumField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(fileSizeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fileSizeValue, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(previewProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(wrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(zoomSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(openPDFLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(pathTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseButton))
                            .addComponent(dpiSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openPDFLabel)
                    .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dpiSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(compressLevelSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(compressButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(backButton)
                                .addComponent(pageNumField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(nextButton))
                            .addComponent(fileSizeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fileSizeValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(previewProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(zoomSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void browseOnClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseOnClick
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Document", "pdf");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(filter);
        int returnedVal = chooser.showOpenDialog(this);
        if(returnedVal == JFileChooser.APPROVE_OPTION) {
            pathTextField.setText(chooser.getSelectedFile().getPath());
            pathToSoruce = chooser.getSelectedFile().getPath();
            try {
                compressor = new PDFCompressor(pathToSoruce);
                
                /*dpiSlider.setEnabled(true);
                zoomSlider.setEnabled(true);
                compressLevelSlider.setEnabled(true);
                compressButton.setEnabled(true);
                nextButton.setEnabled(true);
                backButton.setEnabled(true);*/
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        wrapper.add(previewPanel, BorderLayout.PAGE_START);
                        wrapper.revalidate();
                        wrapper.repaint();
                    }
                });
                renderPreview();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            } 
        }
    }//GEN-LAST:event_browseOnClick

    private void dpiSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_dpiSliderStateChanged
        JSlider source = (JSlider)evt.getSource();
        if (!source.getValueIsAdjusting()) {
            dpi = dpiSlider.getValue();
            try {
                renderPreview();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
        }
    }//GEN-LAST:event_dpiSliderStateChanged

    private void compressButtonPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compressButtonPressed
        JFileChooser chooser = new JFileChooser(){
            @Override
                public void approveSelection(){
                    File f = getSelectedFile();
                    if(f.exists() && getDialogType() == SAVE_DIALOG){
                        int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
                        switch(result){
                            case JOptionPane.YES_OPTION:
                                super.approveSelection();
                                return;
                            case JOptionPane.NO_OPTION:
                                return;
                            case JOptionPane.CLOSED_OPTION:
                                return;
                            case JOptionPane.CANCEL_OPTION:
                                cancelSelection();
                                return;
                        }
                    }
                    super.approveSelection();
                } 
        };
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Document", "pdf");
        chooser.setFileFilter(filter);
        int returnedVal = chooser.showSaveDialog(this);
        if(returnedVal == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            boolean isAffixed = chooser.getSelectedFile().getName().lastIndexOf(".") != -1;
            if (!isAffixed) {
                filePath += ".pdf";
            }
            SaveDialog dialog = new SaveDialog(new javax.swing.JFrame(), true, filePath, compressor);
        }
    }//GEN-LAST:event_compressButtonPressed

    private void zoomSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_zoomSliderStateChanged
        JSlider source = (JSlider)evt.getSource();
        if (!source.getValueIsAdjusting()) {
            zoom = zoomSlider.getValue();
            try {
                renderPreview();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
        }
    }//GEN-LAST:event_zoomSliderStateChanged

    private void compressLevelSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_compressLevelSliderStateChanged
        // TODO add your handling code here:
        JSlider source = (JSlider)evt.getSource();
        if (!source.getValueIsAdjusting()) {
            compressRate = compressLevelSlider.getValue();
            try {
                renderPreview();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
        }
    }//GEN-LAST:event_compressLevelSliderStateChanged

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        if (currentPage + 1 > compressor.getNumberOfPage()) {
            currentPage = 0;
        } else {
            currentPage++;
        }
        try {
            renderPreview();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }//GEN-LAST:event_nextButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        if (currentPage - 1 < 1) {
            currentPage = compressor.getNumberOfPage();
        } else {
            currentPage--;
        }
        try {
            renderPreview();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }//GEN-LAST:event_backButtonActionPerformed
    
    
    private void toggleControl(boolean status) {
        List<JComponent> list = new ArrayList<>();
        list.add(dpiSlider);
        list.add(zoomSlider);
        list.add(compressLevelSlider);
        list.add(compressButton);
        list.add(nextButton);
        list.add(backButton);
        for (JComponent element : list) {
            element.setEnabled(status);
        }
    }
    private void renderPreview() throws IOException {  
        compressor.setDPI(dpi);
        compressor.setCompressRate(compressRate);
        previewProgressBar.setVisible(true);
        Thread previewLoadingThread = new Thread(){
            @Override
            public void run() {
                try {
                    previewImage = compressor.getBytePreview(currentPage);                
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {                            
                            try {
                                previewImage = Thumbnails.of(previewImage).scale((float) zoom/100).asBufferedImage();
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
                            }  
                            int width = previewImage.getWidth();
                            int height = previewImage.getHeight();
                            previewPanel.getInnterPanel().setPreferredSize(new Dimension(width, height));
                            previewPanel.setImage(previewImage);
                            pageNumField.setText(String.valueOf(currentPage));
                            previewProgressBar.setVisible(false);
                            toggleControl(true);
                        }
                    });
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, ex.getMessage());
                }
            }
        };
        fileSizeCalThread = new fileSizeThread();
        fileSizeCalThread.start();
        previewLoadingThread.start();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CompressorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CompressorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CompressorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CompressorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CompressorView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton browseButton;
    private javax.swing.JButton compressButton;
    private javax.swing.JSlider compressLevelSlider;
    private javax.swing.JSlider dpiSlider;
    private javax.swing.JLabel fileSizeLabel;
    private javax.swing.JLabel fileSizeValue;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel openPDFLabel;
    private javax.swing.JTextField pageNumField;
    private javax.swing.JTextField pathTextField;
    private javax.swing.JProgressBar previewProgressBar;
    private javax.swing.JPanel wrapper;
    private javax.swing.JSlider zoomSlider;
    // End of variables declaration//GEN-END:variables
}
