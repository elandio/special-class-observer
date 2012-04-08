package edu.uwp.cs.android.sco;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.entities.Student;
import edu.uwp.cs.android.sco.entities.StudentDao;

//extends Activity
public class ConvertToPDFActivity extends Activity {
	
	// database management
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private StudentDao studentDao;
//    private long courseId;
    
    // buttons
    private Button sendMailButton;
    private Button openPdfButton;
    private Button doneButton;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convert_to_pdf);
        
        
        
        sendMailButton = (Button) findViewById(R.id.convert_to_pdf_sendMail);
        openPdfButton = (Button) findViewById(R.id.convert_to_pdf_openPdf);
        doneButton = (Button) findViewById(R.id.convert_to_pdf_done);

        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "sco-v1.db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
        
        long studentId = getIntent().getLongExtra("studentID", -1l);
//        courseId = getIntent().getLongExtra("courseId", -1l);
        
        if(studentId == -1l){
           Log.e("OH MY GOD:", "WE HAVE A PROBLEM!!!!!!"); 
        }
        
	    Student student = studentDao.load(studentId);
        
        createPDF(student);
        
        sendMailButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMail();

            }
        });
        
        openPdfButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
               openPDF();
                
            }
        });
        
        doneButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                finish();
//                Intent studentOverview = new Intent(ConvertToPDFActivity.this, StudentOverviewActivity.class);
//                studentOverview.putExtra("courseId", courseId);
//                Log.i("ConvertToPDFActivity", "--> CLOSED the pdf dialog");
//                startActivity(studentOverview);
            }
        });
        
//        openPdfDialog();

//        sendMail.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                sendMail();
//
//            }
//        });
//        
//        openPdf.setOnClickListener(new OnClickListener() {
//            
//            @Override
//            public void onClick(View v) {
//               openPDF();
//                
//            }
//        });
    }
    
//    private void openPdfDialog() {
//        final Dialog pdfDialog = new Dialog(ConvertToPDFActivity.this);
//        
//        pdfDialog.setContentView(R.layout.convert_to_pdf);
//        pdfDialog.setTitle("PDF Dialog");
//        pdfDialog.setCancelable(true);
//        pdfDialog.show();
//        
//        Button sendMailButton = (Button) pdfDialog.findViewById(R.id.convert_to_pdf_sendMail);
//        Button openPdfButton = (Button) pdfDialog.findViewById(R.id.convert_to_pdf_openPdf);
//        Button doneButton = (Button) pdfDialog.findViewById(R.id.convert_to_pdf_done);
//        
//        sendMailButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                sendMail();
//
//            }
//        });
//        
//        openPdfButton.setOnClickListener(new OnClickListener() {
//            
//            @Override
//            public void onClick(View v) {
//               openPDF();
//                
//            }
//        });
//        
//        doneButton.setOnClickListener(new OnClickListener() {
//            
//            @Override
//            public void onClick(View v) {
//                pdfDialog.dismiss(); 
//                Intent studentOverview = new Intent(ConvertToPDFActivity.this, StudentOverviewActivity.class);
//                studentOverview.putExtra("courseId", courseId);
//                Log.i("ConvertToPDFActivity", "--> CLOSED the pdf dialog");
//                startActivity(studentOverview);
//            }
//        });
//        
//    }

    private void createPDF(Student student) {
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

    private void openPDF() {
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

    /**
     * Sends the pdf file as a mail.
     */
    private void sendMail() {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("application/pdf");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        String root = Environment.getExternalStorageDirectory().toString();
        
        // Attention now follows the darkest vodoo out of the hell
        Uri pdfUri = Uri.fromFile(new File(root + "/../.." + getFilesDir() + "/"+ "export.pdf"));

        emailIntent.putExtra(Intent.EXTRA_STREAM, pdfUri); 
        startActivity(Intent.createChooser(emailIntent, "Choose you mail client..."));
    }
}
