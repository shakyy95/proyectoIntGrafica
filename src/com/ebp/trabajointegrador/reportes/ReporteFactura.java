package com.ebp.trabajointegrador.reportes;
import com.ebp.trabajointegrador.modelo.DetallePedido;
import com.ebp.trabajointegrador.modelo.Factura;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ReporteFactura {

    private final Factura factura;

    public ReporteFactura(Factura factura) {
        this.factura = factura;
    }

    public void generarFactura() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Encabezado de la factura
            Font font = FontFactory.getFont(FontFactory.COURIER, 18, BaseColor.BLACK);
            Paragraph encabezado = new Paragraph("Factura " + factura.getId(), font);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            document.add(encabezado);

            // Información de fecha y hora de emisión
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);
            document.add(new Paragraph("Fecha de Emisión: " + factura.getFechaEmision().toString(), infoFont));
            document.add(new Paragraph("Hora de Emisión: " + factura.getHoraEmision().toString(), infoFont));
            document.add(Chunk.NEWLINE); // Agregar espacio en blanco

            // Información del cliente
            Font clienteFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
            document.add(new Paragraph("Cliente: " + factura.getCliente(), clienteFont));
            document.add(Chunk.NEWLINE); // Agregar espacio en blanco

            // Lista de ítems
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            PdfPCell c1 = new PdfPCell(new Phrase("Cantidad"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c1);

            PdfPCell c2 = new PdfPCell(new Phrase("Descripción"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase("Tipo"));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c3);

            PdfPCell c4 = new PdfPCell(new Phrase("Variedad"));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c4);

            PdfPCell c5 = new PdfPCell(new Phrase("Porciones"));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c5);

            PdfPCell c6 = new PdfPCell(new Phrase("Precio Unitario"));
            c6.setHorizontalAlignment(Element.ALIGN_CENTER);
            c6.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c6);

            PdfPCell c7 = new PdfPCell(new Phrase("SubTotal"));
            c7.setHorizontalAlignment(Element.ALIGN_CENTER);
            c7.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c7);

            for (DetallePedido detalle : factura.getDetalleFactura()) {
                table.addCell(String.valueOf(detalle.getCantidad()));
                table.addCell(detalle.getPizza().getNombre());
                table.addCell(detalle.getPizza().getTipoPizza().getNombre());
                table.addCell(detalle.getPizza().getVariedadPizza().getNombre());
                table.addCell(String.valueOf(detalle.getPizza().getTamanioPizza().getCantPorciones()));
                table.addCell(String.format("$%.2f", detalle.getPrecio())); // Formato de precio
                table.addCell(String.format("$%.2f", detalle.calcularSubtotal())); // Formato de subtotal
            }

            document.add(table);

            // Monto total
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            document.add(Chunk.NEWLINE); // Agregar espacio en blanco
            document.add(new Paragraph("Monto Total: " + String.format("$%.2f", factura.calcularTotalFactura()), totalFont));

            document.close();

            byte[] pdfBytes = outputStream.toByteArray();
            var tempFile = File.createTempFile("factura", ".pdf");
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(pdfBytes);

            Desktop.getDesktop().open(tempFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
