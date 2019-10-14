/*import com.github.sarxos.webcam.Webcam;
import javax.xml.transform.Result;
import java.awt.image.BufferedImage;

public class main {

    Webcam webcam = Webcam.getDefault(); // non-default (e.g. USB) webcam can be used too
webcam.open();

    Result result = null;
    BufferedImage image = null;

        if(webcam.isOpen())

    {
        if ((image = webcam.getImage()) == null) {
            continue;
        }

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            result = new MultiFormatReader().decode(bitmap);
        } catch (NotFoundException e) {
            // fall thru, it means there is no QR code in image
        }
    }

        if(result !=null)

    {
        System.out.println("QR code data is: " + result.getText());
    }
}*/
//package com.github.sarxos.example1;
package WebcamQRCodeExample;



import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;


// this needed to be the version for java SE
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import net.sqlitetutorial.InsertApp;

public class main extends JFrame implements Runnable, ThreadFactory {

    private static final long serialVersionUID = 6441489157408381878L;

    private Executor executor = Executors.newSingleThreadExecutor(this);

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private JTextArea textarea = null;

    public main() {
        super();

        /*
        System.out.println("should be instering");
        InsertApp inserter = new InsertApp();

        inserter.insert("warehouses", "junk", 9001);
        */

        setLayout(new FlowLayout());
        setTitle("Read QR / Bar Code With Webcam");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension size = WebcamResolution.QVGA.getSize();

        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        textarea = new JTextArea();
        textarea.setEditable(false);
        textarea.setPreferredSize(size);

        add(panel);
        add(textarea);

        pack();
        setVisible(true);

        executor.execute(this);
    }

    @Override
    public void run() {

        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {

                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // fall through: there is no QR code in image
                }
            }

            if (result != null) {
                // this is where it gets sent somewhere, in the example to a text box
                InsertApp in = new InsertApp();
                in.insert("warehouses", result.getText(), 9001.0);
                textarea.setText(result.getText());

            }

        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "example-runner");
        t.setDaemon(true);
        return t;
    }

    public static void main(String[] args) {
        new main();
    }
}