package com.sivet.api.service.document;

import com.sivet.api.domain.entity.Clinica;
import com.sivet.api.domain.entity.Venta;
import com.sivet.api.domain.entity.VentaItem;
import com.sivet.api.domain.enums.EstadoVenta;
import com.sivet.api.domain.enums.MetodoPago;
import com.sivet.api.exception.BusinessException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Construye el reporte de ventas en Excel (.xlsx) del tenant para un rango de fechas
 * (§4.3): una hoja de detalle y una hoja de KPIs con desglose por método de pago.
 */
@Component
public class ExcelDocumentGenerator {

    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] reporteVentas(Clinica clinica, List<Venta> ventas, LocalDate desde, LocalDate hasta) {
        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            CellStyle headerStyle = headerStyle(wb);
            CellStyle moneyStyle = moneyStyle(wb);
            CellStyle titleStyle = titleStyle(wb);

            crearHojaDetalle(wb, headerStyle, moneyStyle, titleStyle, clinica, ventas, desde, hasta);
            crearHojaKpis(wb, headerStyle, moneyStyle, titleStyle, ventas);

            wb.write(out);
            return out.toByteArray();
        } catch (IOException ex) {
            throw new BusinessException("No se pudo generar el reporte Excel: " + ex.getMessage());
        }
    }

    private void crearHojaDetalle(Workbook wb, CellStyle header, CellStyle money, CellStyle title,
                                  Clinica clinica, List<Venta> ventas, LocalDate desde, LocalDate hasta) {
        Sheet sheet = wb.createSheet("Ventas");

        Row t1 = sheet.createRow(0);
        celdaTexto(t1, 0, "Reporte de Ventas · " + clinica.getNombre(), title);
        Row t2 = sheet.createRow(1);
        celdaTexto(t2, 0, "Rango: " + desde + " a " + hasta, null);

        String[] cols = {"Fecha", "Cliente", "Ítems", "Total (S/)", "Método", "Vendedor", "Estado"};
        Row head = sheet.createRow(3);
        for (int i = 0; i < cols.length; i++) {
            celdaTexto(head, i, cols[i], header);
        }

        int fila = 4;
        for (Venta v : ventas) {
            Row r = sheet.createRow(fila++);
            celdaTexto(r, 0, v.getFecha().format(FECHA_HORA), null);
            celdaTexto(r, 1, v.getCliente().getNombre(), null);
            celdaTexto(r, 2, resumenItems(v), null);
            celdaMoneda(r, 3, v.getTotal(), money);
            celdaTexto(r, 4, v.getMetodoPago().getValue(), null);
            celdaTexto(r, 5, v.getVendedor(), null);
            celdaTexto(r, 6, v.getEstado().getValue(), null);
        }

        for (int i = 0; i < cols.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void crearHojaKpis(Workbook wb, CellStyle header, CellStyle money, CellStyle title,
                               List<Venta> ventas) {
        Sheet sheet = wb.createSheet("Resumen (KPIs)");

        // KPIs solo sobre ventas completadas (§4.3).
        List<Venta> completadas = ventas.stream()
                .filter(v -> v.getEstado() == EstadoVenta.COMPLETADA)
                .toList();

        BigDecimal recaudacion = completadas.stream()
                .map(v -> v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int numVentas = completadas.size();
        BigDecimal ticketPromedio = numVentas == 0
                ? BigDecimal.ZERO
                : recaudacion.divide(BigDecimal.valueOf(numVentas), 2, RoundingMode.HALF_UP);

        Row t = sheet.createRow(0);
        celdaTexto(t, 0, "Indicadores (ventas completadas)", title);

        int fila = 2;
        fila = kpi(sheet, fila, "Recaudación total", recaudacion, money);
        fila = kpi(sheet, fila, "N.º de ventas", BigDecimal.valueOf(numVentas), null);
        fila = kpi(sheet, fila, "Ticket promedio", ticketPromedio, money);

        fila++;
        Row headDesglose = sheet.createRow(fila++);
        celdaTexto(headDesglose, 0, "Método de pago", header);
        celdaTexto(headDesglose, 1, "Monto (S/)", header);

        Map<MetodoPago, BigDecimal> porMetodo = new EnumMap<>(MetodoPago.class);
        for (Venta v : completadas) {
            porMetodo.merge(v.getMetodoPago(),
                    v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO, BigDecimal::add);
        }
        for (Map.Entry<MetodoPago, BigDecimal> e : porMetodo.entrySet()) {
            Row r = sheet.createRow(fila++);
            celdaTexto(r, 0, e.getKey().getValue(), null);
            celdaMoneda(r, 1, e.getValue(), money);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private int kpi(Sheet sheet, int fila, String etiqueta, BigDecimal valor, CellStyle money) {
        Row r = sheet.createRow(fila);
        celdaTexto(r, 0, etiqueta, null);
        if (money != null) {
            celdaMoneda(r, 1, valor, money);
        } else {
            Cell c = r.createCell(1);
            c.setCellValue(valor.doubleValue());
        }
        return fila + 1;
    }

    private String resumenItems(Venta v) {
        StringBuilder sb = new StringBuilder();
        for (VentaItem item : v.getItems()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(item.getCantidad()).append("x ").append(item.getNombre());
        }
        return sb.toString();
    }

    // ---- Estilos y celdas ------------------------------------------------------------

    private CellStyle headerStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle titleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 13);
        style.setFont(font);
        return style;
    }

    private CellStyle moneyStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(wb.createDataFormat().getFormat("\"S/\" #,##0.00"));
        return style;
    }

    private void celdaTexto(Row row, int col, String valor, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(valor != null ? valor : "");
        if (style != null) {
            c.setCellStyle(style);
        }
    }

    private void celdaMoneda(Row row, int col, BigDecimal valor, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(valor != null ? valor.doubleValue() : 0d);
        if (style != null) {
            c.setCellStyle(style);
        }
    }
}
