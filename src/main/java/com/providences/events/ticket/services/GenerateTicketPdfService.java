package com.providences.events.ticket.services;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.providences.events.ticket.entities.TicketEntity;
import java.awt.image.BufferedImage;

@Service
public class GenerateTicketPdfService {

    public byte[] generateTicketPdf(TicketEntity ticket) throws Exception {
        // gerar QR code com ZXing (o QR conterá o link público ou token)
        String link = "https://yourdomain.com/public/tickets/" + ticket.getAccessToken();
        BufferedImage qrImage = generateQrCode(link, 200, 200);

        // gerar PDF com OpenPDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        com.lowagie.text.Document document = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter.getInstance(document, baos);
        document.open();

        // adicionar título
        document.add(new com.lowagie.text.Paragraph(ticket.getEvent().getTitle(),
                new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 18)));

        // adicionar dados do guest / seat / code
        document.add(new com.lowagie.text.Paragraph("Nome: " + ticket.getGuest().getName()));
        if (ticket.getSeat() != null) {
            document.add(new com.lowagie.text.Paragraph("Seat: " + ticket.getSeat().getName()));
        }
        document.add(new com.lowagie.text.Paragraph("Código: " + ticket.getTicketCode()));
        document.add(new com.lowagie.text.Paragraph("Data: " + ticket.getEvent().getDateStart()));

        // adicionar imagem do QR code
        ByteArrayOutputStream qrBaos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", qrBaos);
        com.lowagie.text.Image img = com.lowagie.text.Image.getInstance(qrBaos.toByteArray());
        img.scaleToFit(100, 100);
        document.add(img);

        document.close();
        return baos.toByteArray();
    }

    private BufferedImage generateQrCode(String text, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

}