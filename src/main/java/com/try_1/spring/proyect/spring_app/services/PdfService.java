package com.try_1.spring.proyect.spring_app.services;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.try_1.spring.proyect.spring_app.models.ContratoAlquiler;
import com.try_1.spring.proyect.spring_app.models.OrdenPago;


@Service
public class PdfService {

    public byte[] generarContratoPdf(ContratoAlquiler contrato) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            Font titulo = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph p1 = new Paragraph("CONTRATO DE ALQUILER", titulo);
            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);

            document.add(new Paragraph(" "));

            document.add(new Paragraph("ID Contrato: " + contrato.getIdContratoAlquiler()));
            document.add(new Paragraph("Fecha Inicio: " + contrato.getFechaInicio()));
            document.add(new Paragraph("Fecha Fin: " + contrato.getFechaFin()));
            document.add(new Paragraph("Condiciones: " + contrato.getCondiciones()));
            document.add(new Paragraph("Estado: " + contrato.getEstado()));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] generarOrdenPagoPdf(OrdenPago orden) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            Font titulo = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph p1 = new Paragraph("ORDEN DE PAGO", titulo);
            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);

            document.add(new Paragraph(" "));

            document.add(new Paragraph("ID Orden de Pago: " + orden.getIdOrdenPago()));

            document.add(new Paragraph("ID Reserva: " +
                (orden.getReserva() != null ? orden.getReserva().getIdReserva() : "N/A")
            ));

            document.add(new Paragraph("Fecha Emisión: " + orden.getFechaEmision()));
            document.add(new Paragraph("Total: $" + orden.getTotal()));
            document.add(new Paragraph("Detalles: " + orden.getDetalles()));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
