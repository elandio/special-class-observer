package edu.uwp.cs.android.sco;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import edu.uwp.cs.android.sco.model.Student;

public class ConvertToPDFActivity extends Activity {

    Button sendMail;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convert_to_pdf);

        sendMail = (Button) findViewById(R.id.sendMail);

        Student stud1 = new Student("Peter", "Maffay", "Test");
        stud1.addDisability("Disability 1", "Info 1");
        stud1.addDisability("Disability 2", "Info 2");
        stud1.addDisability("Disability 3", "Info 3");

        createPDF(stud1);
        openPDF();

        sendMail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("application/pdf");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "m.duettmann@gmail.com" });
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");

                String root = Environment.getExternalStorageDirectory().toString();
                
                // Attention now follows the darkest vodoo out of the hell
                Uri pdfUri = Uri.fromFile(new File(root + "/../.." + getFilesDir() + "/"+ "export.pdf"));

                emailIntent.putExtra(Intent.EXTRA_STREAM, pdfUri); 
                startActivity(Intent.createChooser(emailIntent, "Choose something..."));

            }
        });
    }

    public void createPDF(Student student) {
        try {
            Document document = new Document();
            FileOutputStream fOut = openFileOutput("export.pdf", Context.MODE_WORLD_READABLE);
//            FileOutputStream fOut = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath(), "export.pdf"));
            PdfWriter.getInstance(document, fOut);
            document.open();

            // Left
            Paragraph paragraph = new Paragraph("This is right aligned text");
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph);

            // Centered
            paragraph = new Paragraph("This is centered text");
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            // Left
            paragraph = new Paragraph("This is left aligned text");
            paragraph.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph);

            // Left with indentation
            paragraph = new Paragraph("This is left aligned text with indentation");
            paragraph.setAlignment(Element.ALIGN_LEFT);
            paragraph.setIndentationLeft(50);
            document.add(paragraph);

            document.close();
            fOut.flush();
            fOut.close();

            System.out.println("Finished createPDF() method...");
            Toast.makeText(this, "Finished createPDF() method...", Toast.LENGTH_LONG).show();
        }
        catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND EXCEPTION");
            e.printStackTrace();
        }
        catch (DocumentException e) {
            System.out.println("DOCUMENT EXCEPTION");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("IO EXCEPTION");
            e.printStackTrace();
        }
    }

    public void openPDF() {
        String[] list = fileList();
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }

        System.out.println("FILES DIR: " + getFilesDir());

        File file = new File(getFilesDir() + "/" + "export.pdf");

        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
                System.out.println("INTENT GESTARTET !!!");
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(ConvertToPDFActivity.this, "Error: No application available to view the PDF file.", Toast.LENGTH_LONG).show();
                System.out.println("NO APPLICATION AVAILABLE !!!");
            }
        }
        else {
            System.out.println("ERROR OPEN PDF FILE !!!");
        }
    }

}
