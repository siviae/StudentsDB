package ru.ifmo.ctddev.isaev.studentsdb.editor;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Upload;

import java.io.File;
import java.io.OutputStream;


/**
 * @author iisaev
 */
public class ImageUploader implements Upload.Receiver, Upload.SucceededListener {
    public File file;

    public final Image image;

    public ImageUploader(Image image) {
        this.image = image;
    }

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        return null;
    }
    
    

    public void uploadSucceeded(Upload.SucceededEvent event) {
        // Show the uploaded file in the image viewer
        image.setSource(new FileResource(file));
    }
};