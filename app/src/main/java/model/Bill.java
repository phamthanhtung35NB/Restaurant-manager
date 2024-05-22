package model;

import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class Bill {
    private String id;
    private double totalAmount;
    private String tableNumber;

    // Constructor
    public Bill(String id, double totalAmount, String tableNumber) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.tableNumber = tableNumber;
    }
    private void createPdf() {
        try {
            // Tạo một đối tượng Document
            Document document = new Document();

            // Tạo một đối tượng PdfWriter
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bill.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(path));

            // Mở Document
            document.open();

            // Thêm nội dung vào Document
            document.add(new Paragraph("Hóa đơn thanh toán"));
            // Thêm nội dung của hóa đơn tại đây

            // Đóng Document
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }
}