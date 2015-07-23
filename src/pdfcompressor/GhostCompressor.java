/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfcompressor;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;

/**
 *
 * @author anthony.poon
 */

public class GhostCompressor {
    private static int numOfPages;
    private static File sourceIO;
    private PDFDocument sourceDocument = new PDFDocument();
    private SimpleRenderer renderer = new SimpleRenderer();
    private RenderThread renderThread;
    private SizeThread sizeThread;
    private static float compressRate = 0.7f;
    private static int dpi = 200;
    private final List<ProgressListener> sizeEstimateListener = new ArrayList<>();
    private final List<ProgressListener> renderListener = new ArrayList<>();
    private List<java.awt.Image> images = new ArrayList<>();
    private FileOutputStream outputStrem;
    private int fileSize = 0;
    private boolean isRendered = false;

    class RenderThread extends Thread {
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
                images = renderer.render(sourceDocument);
                isRendered = true;
                sizeThread = new SizeThread();
                sizeThread.start();
            } catch (IOException | RendererException | DocumentException ex) {
                System.out.println(ex.getMessage());
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
                System.out.println(ex.getMessage());
            }                
        }
    }
    
    public GhostCompressor(){        
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
        for (java.awt.Image img : images) {            
            itextImg = Image.getInstance(img, Color.white);
            itextImg.scaleToFit(595f, 842f);
            outDocument.add(itextImg);
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
            for (java.awt.Image img : images) {            
                itextImg = Image.getInstance(img, Color.white);
                itextImg.scaleToFit(595f, 842f);
                outDocument.add(itextImg);
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
    
    public void setDPI(int dpi) throws IOException, RendererException, DocumentException {
        if (this.dpi != dpi) {
            this.dpi = dpi;
            rerender();
        }
    }
    
    public void setCompressRate(int rate) {
        this.compressRate = (float) rate/100;
    }
    
}
