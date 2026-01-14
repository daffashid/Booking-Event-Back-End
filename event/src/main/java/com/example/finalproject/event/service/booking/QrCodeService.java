package com.example.finalproject.event.service.booking;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QrCodeService {

    public String generateQrBase64(String content) {
        try {
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(content, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);

            return Base64.getEncoder().encodeToString(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR Code");
        }
    }
}

