package com.springboot.app.view.xls;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.InvoiceItem;

@Component(value = "invoice/detail.xlsx")
public class InvoiceXlscView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MessageSourceAccessor messagesRe = getMessageSourceAccessor();
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		Invoice invoice = (Invoice) model.get("invoice");
		response.setHeader("Content-Disposition", "attachment; filename=".concat(
				invoice.getCustomer().toString().concat("_").concat(invoice.getId().toString().concat(".xlsx"))));

		Sheet sheet = workbook.createSheet(invoice.getCustomer().toString());
		Row row = sheet.createRow(0);

		Cell cell = row.createCell(0);

		CellStyle tCustomerStyle = workbook.createCellStyle();
		tCustomerStyle.setBorderBottom(BorderStyle.MEDIUM);
		tCustomerStyle.setBorderBottom(BorderStyle.MEDIUM);
		tCustomerStyle.setBorderTop(BorderStyle.MEDIUM);
		tCustomerStyle.setBorderLeft(BorderStyle.MEDIUM);
		tCustomerStyle.setBorderRight(BorderStyle.MEDIUM);
		tCustomerStyle.setFillForegroundColor(IndexedColors.BLUE1.index);
		tCustomerStyle.setFillPattern(FillPatternType.DIAMONDS);
		tCustomerStyle.setAlignment(HorizontalAlignment.JUSTIFY);

		CellStyle tCustomerStyleBody = workbook.createCellStyle();
		tCustomerStyleBody.setBorderBottom(BorderStyle.THIN);
		tCustomerStyleBody.setBorderTop(BorderStyle.THIN);
		tCustomerStyleBody.setBorderLeft(BorderStyle.THIN);
		tCustomerStyleBody.setBorderRight(BorderStyle.THIN);
		tCustomerStyleBody.setAlignment(HorizontalAlignment.JUSTIFY);

		cell.setCellValue(messagesRe.getMessage("text.factura.ver.datos.cliente"));
		cell.setCellStyle(tCustomerStyle);
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellStyle(tCustomerStyleBody);
		cell.setCellValue(invoice.getCustomer().toString());

		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(invoice.getCustomer().getEmail());
		cell.setCellStyle(tCustomerStyleBody);

		row = sheet.createRow(4);
		cell = row.createCell(0);
		cell.setCellValue(messagesRe.getMessage("text.factura.ver.datos.factura"));
		cell.setCellStyle(tCustomerStyle);

		row = sheet.createRow(5);
		cell = row.createCell(0);
		cell.setCellValue(
				messagesRe.getMessage("text.cliente.factura.folio").concat(": ").concat(invoice.getId().toString()));
		cell.setCellStyle(tCustomerStyleBody);

		row = sheet.createRow(6);
		cell = row.createCell(0);
		cell.setCellValue(messagesRe.getMessage("text.cliente.factura.descripcion").concat(": ")
				.concat(invoice.getDescription().toString()));
		cell.setCellStyle(tCustomerStyleBody);

		row = sheet.createRow(7);
		cell = row.createCell(0);
		cell.setCellValue(
				messagesRe.getMessage("text.cliente.factura.fecha").concat(": ").concat(invoice.getDate().toString()));
		cell.setCellStyle(tCustomerStyleBody);

// 		Creando Estilos
		CellStyle theadStyle = workbook.createCellStyle();
		theadStyle.setBorderBottom(BorderStyle.MEDIUM);
		theadStyle.setBorderTop(BorderStyle.MEDIUM);
		theadStyle.setBorderLeft(BorderStyle.MEDIUM);
		theadStyle.setBorderRight(BorderStyle.MEDIUM);
		theadStyle.setFillForegroundColor(IndexedColors.AQUA.index);
		theadStyle.setFillPattern(FillPatternType.DIAMONDS);
		theadStyle.setAlignment(HorizontalAlignment.CENTER);

		CellStyle tbodyStyle = workbook.createCellStyle();
		tbodyStyle.setBorderBottom(BorderStyle.THIN);
		tbodyStyle.setBorderTop(BorderStyle.THIN);
		tbodyStyle.setBorderLeft(BorderStyle.THIN);
		tbodyStyle.setBorderRight(BorderStyle.THIN);
		tbodyStyle.setAlignment(HorizontalAlignment.CENTER);

		Row header = sheet.createRow(9);
		header.createCell(0).setCellValue(messagesRe.getMessage("text.factura.form.item.nombre"));
		header.createCell(1).setCellValue(messagesRe.getMessage("text.factura.form.item.precio"));
		header.createCell(2).setCellValue(messagesRe.getMessage("text.factura.form.item.cantidad"));
		header.createCell(3).setCellValue(messagesRe.getMessage("text.factura.form.item.total"));

//		Añadiendo los estilos
		header.getCell(0).setCellStyle(theadStyle);
		header.getCell(1).setCellStyle(theadStyle);
		header.getCell(2).setCellStyle(theadStyle);
		header.getCell(3).setCellStyle(theadStyle);

		int rowNum = 10;
		sheet.autoSizeColumn(0);
		for (InvoiceItem item : invoice.getItems()) {
			Row fila = sheet.createRow(rowNum++);

			cell = fila.createCell(0);
			cell.setCellValue(item.getProduct().getName());
			cell.setCellStyle(tbodyStyle);
			cell = fila.createCell(1);
			cell.setCellValue(item.getProduct().getPrice());
			cell.setCellStyle(tbodyStyle);
			cell = fila.createCell(2);
			cell.setCellValue(item.getAmount());
			cell.setCellStyle(tbodyStyle);
			cell = fila.createCell(3);
			cell.setCellValue(item.calculateImport());
			cell.setCellStyle(tbodyStyle);

		}

		Row filaTotal = sheet.createRow(rowNum);
		sheet.setColumnWidth(2, 14 * 256);
//		sheet.setDefaultColumnWidth(15);
		cell = filaTotal.createCell(2);
		cell.setCellValue(messagesRe.getMessage("text.factura.form.total"));
		cell.setCellStyle(tbodyStyle);

		cell = filaTotal.createCell(3);
		cell.setCellValue(invoice.getTotal());
		cell.setCellStyle(tbodyStyle);
	}

}
