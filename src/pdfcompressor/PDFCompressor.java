/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfcompressor;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PageExtractor;


/**
 *
 * @author anthony.poon
 */
public class PDFCompressor {
    private static int numOfPages;
    private static File sourceIO;
    private static PDDocument sourceDocument;
    private static float compressRate = 0.7f;
    private static int dpi = 200;
    private final List<ProgressListener> saveListener = new ArrayList<>();
    private final List<ProgressListener> sizeEstimateListener = new ArrayList<>();

    public PDFCompressor(String pathToSurce) throws IOException {
        sourceIO = new File(pathToSurce);
        if (sourceDocument != null) {
            sourceDocument.close();
        }
        sourceDocument = PDDocument.loadNonSeq(sourceIO, null);
    }
    
    public int getNumberOfPage() {
        if (sourceDocument != null) {
            return sourceDocument.getNumberOfPages();
        } else {
            return 0;
        }
    }
    
    public BufferedImage getBytePreview(int pageNum) throws IOException {
        PageExtractor extractor = new PageExtractor(sourceDocument, pageNum, pageNum+1);
        PDDocument extractedDoc = extractor.extract();        
        PDPage page = (PDPage) extractedDoc.getDocumentCatalog().getAllPages().get(0);
        extractedDoc.close();
        return ImageIO.read(new ByteArrayInputStream(getImageByteArray(page.convertToImage(BufferedImage.TYPE_INT_BGR, dpi),compressRate)));
    }
    
    public void outputPDF(String pathToOutput) throws FileNotFoundException, DocumentException, BadElementException, IOException{        
        com.itextpdf.text.Image itextImg;        
        Document outDocument = new Document();
        outDocument.setMargins(0f, 0f, 0f, 0f);
        PdfWriter writer = PdfWriter.getInstance(outDocument, new FileOutputStream(pathToOutput));
        writer.setFullCompression();        
        writer.open();
        outDocument.open();
        int progress = 0;
        for (java.awt.Image img : getBuffedImg()) {            
            itextImg = Image.getInstance(getImageByteArray(img, compressRate));
            itextImg.scaleToFit(595f, 842f);
            outDocument.add(itextImg);
            
            for (ProgressListener listener : saveListener) {
                listener.haveProgress(++progress, numOfPages);
            }
        }
        outDocument.close();
        writer.close();
        for (ProgressListener listener : saveListener) {
            listener.finished();
        }
    }
    
    public void getFileSize() throws DocumentException, IOException {
        com.itextpdf.text.Image itextImg;        
        Document outDocument = new Document();
        outDocument.setMargins(0f, 0f, 0f, 0f);
        ByteArrayOutputStream memoryOutput = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(outDocument, memoryOutput);
        writer.setFullCompression();        
        writer.open();
        outDocument.open();
        for (java.awt.Image img : getBuffedImg()) {            
            itextImg = Image.getInstance(getImageByteArray(img, compressRate));
            itextImg.scaleToFit(595f, 842f);
            outDocument.add(itextImg);            
        }
        outDocument.close();
        writer.close();
        for (ProgressListener listener : sizeEstimateListener) {
            listener.finished(memoryOutput.toByteArray().length);
        }
    }
    
    private byte[] getImageByteArray(java.awt.Image img, float compressionLevel) throws IOException {
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter imgWriter = iter.next();
        ImageWriteParam param = imgWriter.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(compressionLevel);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        imgWriter.setOutput(new MemoryCacheImageOutputStream(outStream));
        imgWriter.write(null, new IIOImage((RenderedImage) img,null,null), param);
        return outStream.toByteArray();
    }
    
    public void addSaveListener(ProgressListener listener) {
        saveListener.add(listener);
    }
    
    public void addSizeEstimateListener(ProgressListener listener) {
        sizeEstimateListener.add(listener);
    }
    
    public void setDPI(int dpi) {
        this.dpi = dpi;
    }
    
    public void setCompressRate(int rate) {
        this.compressRate = (float) rate/100;
    }
    
    private List<BufferedImage> getBuffedImg() throws IOException {
        List<PDPage> pdPages = sourceDocument.getDocumentCatalog().getAllPages();
        List<BufferedImage> returnedList = new ArrayList<>();
        numOfPages = 0;
        for (PDPage pdPage : pdPages)
        { 
            numOfPages++;
            BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_BGR, dpi);
            returnedList.add(bim);
        }
        
        //sourceDocument.close();
        return returnedList;
    }
}
