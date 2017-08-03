package ru.ifmo.ctddev.isaev.studentsdb.editor;

import com.vaadin.data.Binder;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Upload;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author iisaev
 */
public class ImageUploader implements Upload.Receiver, Upload.SucceededListener {
    private final Binder<Student> binder;

    private final Random random = new Random();

    private AtomicReference<ByteArrayOutputStream> baosRef;

    public final Image image;

    public ImageUploader(Binder<Student> binder, Image image) {
        this.image = image;
        this.binder = binder;
    }

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(32 * 1024 * 1024);
        baosRef = new AtomicReference<>(baos);
        return baos;
    }


    public void uploadSucceeded(Upload.SucceededEvent event) {
        // Show the uploaded file in the image viewer
        binder.getBean().setPhotoBase64(Base64.getEncoder().encodeToString(baosRef.get().toByteArray()));
        image.setSource(new StreamResource(() -> new ByteArrayInputStream(baosRef.get().toByteArray()), getRandomFileName()));
    }

    public String getRandomFileName() {
        return  new BigInteger(130, random).toString(32);
    }
}