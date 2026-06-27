package com.sivet.api.service.document;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sivet.api.domain.entity.Atencion;
import com.sivet.api.domain.entity.Cliente;
import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Mascota;
import com.sivet.api.domain.entity.Receta;
import com.sivet.api.domain.entity.RecetaItem;
import com.sivet.api.domain.entity.Venta;
import com.sivet.api.domain.entity.VentaItem;
import com.sivet.api.domain.enums.EstadoVenta;
import com.sivet.api.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Construye los PDF de comprobante de venta (ticket) y receta médica (A4) con la
 * marca de la clínica del tenant (§4.1 y §4.2). No accede a repositorios: recibe
 * las entidades ya resueltas por la capa de servicio.
 */
@Component
public class PdfDocumentGenerator {

    private static final Locale PE = Locale.forLanguageTag("es-PE");
    private static final DateTimeFormatter FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ---- Comprobante de venta (ticket, ~80mm de ancho) -------------------------------

    public byte[] comprobanteVenta(Clinica clinica, Venta venta) {
        // Página angosta tipo ticket (226pt ≈ 80mm de ancho).
        Document doc = new Document(new Rectangle(226, 720), 14, 14, 14, 14);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font fTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font fNormal = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Font fNormalBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);

            // Cabecera de la clínica.
            doc.add(centrado(clinica.getNombre(), fTitulo));
            doc.add(centrado("RUC: " + clinica.getRuc(), fNormal));
            doc.add(centrado(clinica.getSede(), fNormal));
            doc.add(centrado(clinica.getDireccion(), fNormal));
            doc.add(centrado("Tel: " + clinica.getTelefono(), fNormal));
            doc.add(separador());

            doc.add(centrado("COMPROBANTE DE VENTA", fNormalBold));
            doc.add(new Paragraph("Ticket: " + venta.getId(), fNormal));
            doc.add(new Paragraph("Fecha: " + venta.getFecha().format(FECHA_HORA), fNormal));
            doc.add(new Paragraph("Vendedor: " + venta.getVendedor(), fNormal));
            doc.add(new Paragraph("Cliente: " + venta.getCliente().getNombre()
                    + " (DNI " + venta.getCliente().getDni() + ")", fNormal));
            doc.add(separador());

            // Detalle de ítems.
            PdfPTable tabla = new PdfPTable(new float[]{4f, 1.2f, 2f, 2f});
            tabla.setWidthPercentage(100);
            celdaHeader(tabla, "Producto", fNormalBold);
            celdaHeader(tabla, "Cant", fNormalBold);
            celdaHeader(tabla, "P.Unit", fNormalBold);
            celdaHeader(tabla, "Subtot", fNormalBold);
            for (VentaItem item : venta.getItems()) {
                BigDecimal subtotal = item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
                celda(tabla, item.getNombre(), fNormal, Element.ALIGN_LEFT);
                celda(tabla, String.valueOf(item.getCantidad()), fNormal, Element.ALIGN_CENTER);
                celda(tabla, soles(item.getPrecio()), fNormal, Element.ALIGN_RIGHT);
                celda(tabla, soles(subtotal), fNormal, Element.ALIGN_RIGHT);
            }
            doc.add(tabla);
            doc.add(separador());

            Paragraph total = new Paragraph("TOTAL: " + soles(venta.getTotal()), fNormalBold);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);
            doc.add(new Paragraph("Método de pago: " + venta.getMetodoPago().getValue(), fNormal));

            // Sello de anulación.
            if (venta.getEstado() == EstadoVenta.ANULADA) {
                doc.add(separador());
                Font fAnulada = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.RED);
                doc.add(centrado("*** ANULADA ***", fAnulada));
                doc.add(centrado("Motivo: " + nullSafe(venta.getMotivoAnulacion()), fNormal));
            }

            doc.add(separador());
            doc.add(centrado("¡Gracias por su preferencia!", fNormal));

            doc.close();
            return out.toByteArray();
        } catch (DocumentException ex) {
            throw new BusinessException("No se pudo generar el comprobante PDF: " + ex.getMessage());
        }
    }

    // ---- Receta médica (A4) ----------------------------------------------------------

    public byte[] recetaMedica(Clinica clinica, Cliente cliente, Mascota mascota,
                               Atencion atencion, Receta receta) {
        Document doc = new Document(PageSize.A4, 48, 48, 48, 48);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font fClinica = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font fSub = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Font fTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
            Font fLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font fNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font fHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);

            // Cabecera de la clínica.
            doc.add(centrado(clinica.getNombre(), fClinica));
            doc.add(centrado("RUC: " + clinica.getRuc() + "  ·  " + clinica.getSede(), fSub));
            doc.add(centrado(clinica.getDireccion() + "  ·  Tel: " + clinica.getTelefono(), fSub));
            doc.add(espacio(10));

            Paragraph titulo = centrado("RECETA MÉDICA", fTitulo);
            doc.add(titulo);
            doc.add(espacio(8));

            // Datos de paciente / cliente / atención.
            String veterinario = atencion != null ? atencion.getVeterinario() : "—";
            String fecha = atencion != null && atencion.getFecha() != null
                    ? atencion.getFecha().format(FECHA_HORA) : "—";

            PdfPTable datos = new PdfPTable(new float[]{1.3f, 3f, 1.3f, 3f});
            datos.setWidthPercentage(100);
            par(datos, "Paciente:", fLabel);
            par(datos, mascota != null ? mascota.getNombre() : "—", fNormal);
            par(datos, "Especie:", fLabel);
            par(datos, mascota != null ? especie(mascota) : "—", fNormal);
            par(datos, "Dueño:", fLabel);
            par(datos, cliente != null ? cliente.getNombre() : "—", fNormal);
            par(datos, "DNI:", fLabel);
            par(datos, cliente != null ? cliente.getDni() : "—", fNormal);
            par(datos, "Veterinario:", fLabel);
            par(datos, veterinario, fNormal);
            par(datos, "Fecha:", fLabel);
            par(datos, fecha, fNormal);
            doc.add(datos);
            doc.add(espacio(14));

            // Tabla de ítems de la receta.
            PdfPTable tabla = new PdfPTable(new float[]{2.4f, 1.6f, 1.4f, 1.6f, 3f});
            tabla.setWidthPercentage(100);
            celdaHeaderColor(tabla, "Medicamento", fHeader);
            celdaHeaderColor(tabla, "Dosis", fHeader);
            celdaHeaderColor(tabla, "Vía", fHeader);
            celdaHeaderColor(tabla, "Duración", fHeader);
            celdaHeaderColor(tabla, "Indicaciones", fHeader);
            for (RecetaItem item : receta.getItems()) {
                celda(tabla, item.getMedicamento(), fNormal, Element.ALIGN_LEFT);
                celda(tabla, item.getDosis(), fNormal, Element.ALIGN_LEFT);
                celda(tabla, item.getVia(), fNormal, Element.ALIGN_LEFT);
                celda(tabla, item.getDuracion(), fNormal, Element.ALIGN_LEFT);
                celda(tabla, item.getIndicaciones(), fNormal, Element.ALIGN_LEFT);
            }
            doc.add(tabla);

            // Espacio para firma y sello.
            doc.add(espacio(70));
            PdfPTable firma = new PdfPTable(1);
            firma.setWidthPercentage(45);
            firma.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell linea = new PdfPCell(new Phrase("Firma y Sello", fNormal));
            linea.setHorizontalAlignment(Element.ALIGN_CENTER);
            linea.setBorder(Rectangle.TOP);
            linea.setPaddingTop(6);
            firma.addCell(linea);
            doc.add(firma);

            doc.close();
            return out.toByteArray();
        } catch (DocumentException ex) {
            throw new BusinessException("No se pudo generar la receta PDF: " + ex.getMessage());
        }
    }

    // ---- Helpers ---------------------------------------------------------------------

    private Paragraph centrado(String texto, Font font) {
        Paragraph p = new Paragraph(texto, font);
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }

    private Paragraph espacio(float alto) {
        Paragraph p = new Paragraph(" ");
        p.setSpacingAfter(alto);
        return p;
    }

    private Paragraph separador() {
        return centrado("------------------------------------------",
                FontFactory.getFont(FontFactory.HELVETICA, 7));
    }

    private void celdaHeader(PdfPTable tabla, String texto, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(texto, font));
        c.setBorder(Rectangle.BOTTOM);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(c);
    }

    private void celdaHeaderColor(PdfPTable tabla, String texto, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(texto, font));
        c.setBackgroundColor(new Color(33, 150, 243));
        c.setPadding(4);
        tabla.addCell(c);
    }

    private void celda(PdfPTable tabla, String texto, Font font, int alineacion) {
        PdfPCell c = new PdfPCell(new Phrase(nullSafe(texto), font));
        c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(2);
        c.setHorizontalAlignment(alineacion);
        tabla.addCell(c);
    }

    private void par(PdfPTable tabla, String texto, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(nullSafe(texto), font));
        c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(3);
        tabla.addCell(c);
    }

    private String especie(Mascota m) {
        String esp = m.getEspecie() != null ? m.getEspecie().getValue() : "—";
        return esp + (m.getRaza() != null ? " / " + m.getRaza() : "");
    }

    private String soles(BigDecimal monto) {
        return String.format(PE, "S/ %,.2f", monto != null ? monto : BigDecimal.ZERO);
    }

    private String nullSafe(String s) {
        return s != null ? s : "";
    }
}
