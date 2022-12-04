package com.sfwebackend;
//App java file 

//Jsoup 
// Used for our webscraping
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//IO
// Basic Java methods
import java.io.IOException;
import java.net.URL;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

//Image
import javax.imageio.*;

//Awt
// Provides image generating methods used in QR code generation
import java.awt.image.*;
import java.awt.Color;
import java.awt.Graphics2D;

//NIO
// Used for file handling and writing/rewriting in html file creation
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Util
// Provide hashtable data type used in the QR code generation
import java.util.Hashtable;

//Zxing
// This library provides methods for generating our QR codes
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class App {
    public static void main(String[] args) throws Exception {

        System.out.println("Main Started");

        // This matrix holds a website information, html class name,
        // and desired label for webscraping html elements from a given website.
        String[][] webscrapeSites = { { "https://engineering.arizona.edu/majors/software", "col-sm-7", "major" }
        };

        // Iterate through the webscrape data and gather html elements
        // as a string, then output the string into a html file
        for (int i = 0; i < webscrapeSites.length; i++) {

            String url = webscrapeSites[i][0];
            String element = webscrapeSites[i][1];
            String name = webscrapeSites[i][2];

            String storage = webscrape(url, "div." + element);

            htmlcreatorA(name, storage, element);

        }

        // This matrix holds website information and the desired label
        // for generting a QR code png
        String[][] qrSites = {
                { "https://ua-trellis.force.com/uastudent/s/catcloud/services/calendar/?NetId=julianalincoln",
                        "appointmentQR" }
        };

        // Iterate through the qr generation data and create png files
        // for the corresponding website in png folder
        for (int i = 0; i < qrSites.length; i++) {

            String url = qrSites[i][0];
            String name = qrSites[i][1];

            QRgenerator(url, name);

        }

        // Grab pdfs from given link for the 4 year plan document
        pdfGrab("https://engineering.arizona.edu/pdfs/4-year-degree-plans/",
                "4yearplan");

        // Run webscrape & QRgenerator above for all required qr-codes and website info
        // Run the frontend executable beneath once all the datafiles have been made

        System.out.println("Main Ended");

    }
    // End of main

    public static String webscrape(String url, String element) {
        // Thiis function collects the html of a given website and
        // converts it to a string

        try {
            // Attempt connection to website
            Document webdoc = Jsoup.connect(url).get();

            // Collect all html data
            Elements content = webdoc.select(element);

            // Convert html into string
            return content.toString();

        } catch (IOException IOexception) {

            // Print error info to console
            IOexception.printStackTrace();

        }

        return "";
    }
    // End of webscrape

    public static void htmlcreatorA(String objname, String content, String element) {
        // This function filters the html string to find the useful target element and
        // removes all hyperlinks so there is no opportunity for the frontend window
        // to be closed/minimized for opening up the browser. The useful html elment is
        // then outputted to a html file sotred in the html folder.

        try {
            // Create the file object, if the target file already exists delete it so that
            // new data will be generated

            String filename = "html/" + objname + ".html";

            File file = new File(filename);

            if (file.exists()) {

                file.delete();

            }

            File htmlfile = new File(filename);

            boolean filemade = htmlfile.createNewFile();

            boolean fileexists = htmlfile.exists();

            // If the file exists, then proceed to filter the html string
            if (filemade && fileexists) {

                // This removes all hyperlinks but unfortunately does leave "</a>"
                // within the string sometimes. However, this is not an issue for
                // displaying the content properly

                while (content.indexOf("<a href=") != -1) {

                    int i1 = content.indexOf("<a href=");

                    int i2 = content.indexOf(">", i1 + 1);

                    String s1 = content.substring(0, i1);

                    String s2 = content.substring(i2 + 1, content.length() - 1);

                    content = s1 + s2;

                    content.replaceAll("</a>", "");

                    System.out.println("A");
                }

                // For testing - indicates when this stage is completed
                System.out.println("File " + filename + " made");

                // Create a means to write to the html file
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));

                // Write the updated content string to the html file
                writer.append(content);

                // Close the file as writing is complete
                writer.close();

                // The following lines are simply replacing all unusual, previous format names
                // for the html element with our desired reference names
                Path filepath = Paths.get(filename);

                Charset charset = StandardCharsets.UTF_8;

                String htmlstuff = new String(Files.readAllBytes(filepath), charset);

                htmlstuff = htmlstuff.replaceAll(element, objname);

                Files.write(filepath, htmlstuff.getBytes(charset));

            } else {

                // While execute if there was trouble finding the file yet perhaps no error
                // occurred
                System.out.println("File " + filename + " does not exist");

            }

        } catch (IOException IOexception) {

            // Print error info to console
            IOexception.printStackTrace();

        }
    }
    // End of htmlcreatorA

    public static void QRgenerator(String url, String name) throws WriterException, IOException {
        // This function generates a QR code png for a given url

        try {
            // Create the file object, if the target file already exists delete it so that
            // new data will be generated

            String filename = "png/" + name + ".png";

            File file = new File(filename);

            if (file.exists()) {

                file.delete();

            }

            // Create new file object
            File QRFile = new File("png/" + name + ".png");

            // For testing - indicates when this stage is completed
            System.out.println("File png/" + name + ".png made");

            // Width and Height size value of our square QR code image
            int imgSize = 800;

            // Create a hashtable which will be passed through a function to create a
            // bitmatrix
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintsHash = new Hashtable<>();
            hintsHash.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            // Create means to write our QR code data
            QRCodeWriter qrWriter = new QRCodeWriter();

            // Turns our url string into a matrix of bits which shall be turned into an
            // image
            BitMatrix bmatrix = qrWriter.encode(url, BarcodeFormat.QR_CODE, imgSize, imgSize, hintsHash);

            int matWidth = bmatrix.getWidth();

            // Create a bufferedimage with a size correspondiing to the bitmatrix and
            // regular image type (r, g, b)
            BufferedImage image = new BufferedImage(matWidth, matWidth, BufferedImage.TYPE_INT_RGB);

            // The graphics are made and filled with white as the background,
            // based on the bit array certain areas will be seen as 0 or 1
            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, matWidth, matWidth);
            graphics.setColor(Color.BLACK);

            // Iterate through the width of the graphic
            for (int i = 0; i < matWidth; i++) {

                // Iterate through the height of the graphic
                for (int j = 0; j < matWidth; j++) {

                    // Check if a given bit is positive, if so
                    // then its corresponding place on the image
                    // will be filled with a black square
                    if (bmatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            // Write the image graphic to the file object
            ImageIO.write(image, "png", QRFile);

        } catch (IOException IOexception) {

            // Print error info to console
            IOexception.printStackTrace();

        }

    }
    // End of QRgenerator

    public static void pdfGrab(String url, String name) throws IOException {

        String pathname = "pdf/" + name + ".pdf";

        DateTimeFormatter yearForm = DateTimeFormatter.ofPattern("yyyy");

        DateTimeFormatter monthForm = DateTimeFormatter.ofPattern("MM");

        LocalDateTime now = LocalDateTime.now();

        String year = yearForm.format(now);

        String month = monthForm.format(now);

        int monthNum = Integer.parseInt(month);

        int yearNumFull = Integer.parseInt(year);

        if (monthNum <= 7) {

            yearNumFull -= 1;

        }

        int yearNum = (yearNumFull % 100);

        String pdfYearString = String.valueOf(yearNumFull) + "/SoftwareEngineering" + String.valueOf(yearNum) + "-"
                + String.valueOf(yearNum + 1) + ".pdf";

        URL fileurl = new URL(url + pdfYearString);

        System.out.println(url + pdfYearString);

        InputStream is = fileurl.openStream();

        FileOutputStream fs = new FileOutputStream(pathname);

        System.out.println("File pdf/" + name + ".pdf made");

        byte[] buffer = new byte[1024];

        int l = -1;

        while ((l = is.read(buffer)) > -1) {

            fs.write(buffer, 0, l);

        }

        fs.close();
        is.close();

    }
    // End of pdfGrab

    public static void docRead(String filename) {

    }

}
