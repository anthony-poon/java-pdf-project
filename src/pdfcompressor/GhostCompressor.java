/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfcompressor;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.ghost4j.display.PageRaster;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;

/**
 *
 * @author anthony.poon
 */

public class GhostCompressor {
    private static final float pptInchRate = 0.0138889f;
    private PDFDocument sourceDocument = new PDFDocument();
    private SimpleRenderer renderer = new SimpleRenderer();
    private RenderThread renderThread;
    private SizeThread sizeThread;
    private static int dpi = 200;
    private final List<ProgressListener> sizeEstimateListener = new ArrayList<>();
    private final List<ProgressListener> renderListener = new ArrayList<>();
    private List<java.awt.Image> images = new ArrayList<>();
    private List<PageRaster> rasters = new ArrayList<>();
    private int fileSize = 0;
    private boolean isRendered = false;

    class RenderThread extends Thread{
        public RenderThread() {
            super();
        }
        public void run() {
            renderer.setResolution(dpi);
            isRendered = false;
            try {
                for (ProgressListener listener : renderListener) {
                    listener.start();
                }
                if (sizeThread != null && sizeThread.isAlive()) {
                    sizeThread.interrupt();
                }
                rasters = renderer.run(sourceDocument, 0, sourceDocument.getPageCount());
                images = renderer.render(sourceDocument);
                isRendered = true;
                sizeThread = new SizeThread();
                sizeThread.start();
            } catch (IOException | RendererException | DocumentException | UnsatisfiedLinkError ex) {                
                if (sizeThread != null && sizeThread.isAlive()) {
                    sizeThread.interrupt();
                }
                for (ProgressListener listener : renderListener) {
                    listener.exceptionHandling((Throwable) ex);
                }
                this.interrupt();
            }
            
            for (ProgressListener listener : renderListener) {
                listener.finished();
            }
        }
    }
    
    class SizeThread extends Thread {
        public SizeThread() {
            super();
        }
        @Override
        public void run() {
            try {
                if (isRendered == true) { 
                    for (ProgressListener listener : sizeEstimateListener) {
                        listener.start();
                    }
                    getFileSize();
                }
                for (ProgressListener listener : sizeEstimateListener) {
                    listener.finished(fileSize);
                }                
            } catch (DocumentException | com.itextpdf.text.DocumentException | IOException | RendererException ex) {
                for (ProgressListener listener : renderListener) {
                    listener.exceptionHandling((Throwable) ex);
                }
                this.interrupt();
            }                
        }
    }
    
    public GhostCompressor(){
        try {
            InputStream inStream = this.getClass().getResourceAsStream("gsdll64.dll");            
            File tempLib = new File(System.getProperty("java.io.tmpdir") + File.separator + "gsdll64.dll");
            OutputStream outStream = FileUtils.openOutputStream(tempLib);
            IOUtils.copy(inStream, outStream);
            inStream.close();
            outStream.close();
            System.load(tempLib.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(GhostCompressor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setInput(String pathToSurce) throws IOException, RendererException, DocumentException, com.itextpdf.text.DocumentException {
        sourceDocument.load(new File(pathToSurce));
        rerender();
    }
    
    private void rerender() {
        if (renderThread != null && renderThread.isAlive()) {
            renderThread.interrupt();
            renderThread = new RenderThread();
            renderThread.start();
        } else {
            renderThread = new RenderThread();
            renderThread.start();
        }
        
    }
    
    public BufferedImage getPreview(int pageNum) throws IOException, RendererException, DocumentException {
        return (BufferedImage) images.get(pageNum - 1);
    }
    
    public int getNumberOfPages() throws DocumentException{
        return sourceDocument.getPageCount();
    }

    public void outputPDF(String pathToOutput) throws com.itextpdf.text.DocumentException, FileNotFoundException, BadElementException, IOException, RendererException, DocumentException{
        com.itextpdf.text.Image itextImg;        
        Document outDocument = new Document();
        outDocument.setMargins(0f, 0f, 0f, 0f);
        PdfWriter writer = PdfWriter.getInstance(outDocument, new FileOutputStream(pathToOutput));
        writer.setFullCompression();        
        writer.open();
        outDocument.open();
        int page = 0;
        for (java.awt.Image img : images) {
            float width = rasters.get(page).getWidth()/pptInchRate/dpi;
            float height = rasters.get(page).getHeight()/pptInchRate/dpi;
            Rectangle pageSize = new Rectangle(width, height);
            outDocument.setPageSize(pageSize);
            outDocument.newPage();
            itextImg = Image.getInstance(img, Color.white);
            itextImg.scaleToFit(width, height);
            outDocument.add(itextImg);
            page++;
        }
        outDocument.close();
        writer.close();
    }
    
    private int getFileSize() throws DocumentException, com.itextpdf.text.DocumentException, IOException, RendererException{
        if (isRendered){
            ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
            com.itextpdf.text.Image itextImg;        
            Document outDocument = new Document();
            outDocument.setMargins(0f, 0f, 0f, 0f);
            PdfWriter writer = PdfWriter.getInstance(outDocument, memoryStream);
            writer.setFullCompression();        
            writer.open();
            outDocument.open();        
            int page = 0;
            for (java.awt.Image img : images) {
                float width = rasters.get(page).getWidth()/pptInchRate/dpi;
                float height = rasters.get(page).getHeight()/pptInchRate/dpi;
                Rectangle pageSize = new Rectangle(width, height);
                outDocument.setPageSize(pageSize);
                outDocument.newPage();
                itextImg = Image.getInstance(img, Color.white);
                itextImg.scaleToFit(width, height);
                outDocument.add(itextImg);
                page++;
            }
            outDocument.close();
            writer.close();
            fileSize = memoryStream.toByteArray().length;
            memoryStream = null;
            return fileSize;
        } else {
            return 0;
        }
    }
    
    public void addSizeEstimateListener(ProgressListener listener) {
        sizeEstimateListener.add(listener);
    }
    
    public void addRenderListener(ProgressListener listener) {
        renderListener.add(listener);
    }
    
    public void setDPIByPassRerender(int dpi) {
        this.dpi = dpi;
    }
    public void setDPI(int dpi) throws IOException, RendererException, DocumentException {
        if (this.dpi != dpi) {
            this.dpi = dpi;
            rerender();
        }
    }    
}
