/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfcompressor;

/**
 *
 * @author anthony.poon
 */
public interface ProgressListener {
    public void start();
    public void haveProgress(int currentProgress, int totalProgress);
    public void finished();
    public void finished(int totalProgress);
    public void exceptionHandling(Throwable ex);
}
