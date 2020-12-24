package com.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.InvoiceItem;

@Component(value = "invoice/detail")
public class InvoicePdfView extends AbstractPdfView {
	@Autowired
	private MessageSource messagesSources;
	@Autowired
	private LocaleResolver localeResolver;

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Locale locale = localeResolver.resolveLocale(request);
		MessageSourceAccessor messagesRe = getMessageSourceAccessor();

		response.setContentType("application/pdf");
		Invoice invoice = (Invoice) model.get("invoice");
		response.setHeader("Content-Disposition", "inline; attachment; filename=".concat(
				invoice.getCustomer().toString().concat("-").concat(invoice.getId().toString().concat(".pdf"))));
		PdfPTable tabla = new PdfPTable(1);
		PdfPCell cell = null;
		cell = new PdfPCell(new Phrase(messagesRe.getMessage("text.factura.ver.datos.cliente")));
		cell.setBackgroundColor(new Color(190, 229, 235));
		cell.setPadding(8f);
		tabla.setSpacingAfter(20);
		tabla.addCell(cell);
		tabla.addCell(messagesSources.getMessage("text.cliente.nombre", null, locale).concat(": ")
				.concat(invoice.getCustomer().getName()).concat(" ").concat(invoice.getCustomer().getSurname()));
		tabla.addCell(messagesSources.getMessage("text.cliente.email", null, locale).concat(": ")
				.concat(invoice.getCustomer().getEmail()));
		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20);

		cell = new PdfPCell(new Phrase(messagesSources.getMessage("text.factura.ver.datos.factura", null, locale)));
		cell.setBackgroundColor(new Color(190, 229, 235));
		cell.setPadding(8f);
		tabla2.addCell(cell);
		tabla2.addCell(messagesSources.getMessage("text.cliente.factura.folio", null, locale).concat(": ")
				.concat(invoice.getId().toString()));
		tabla2.addCell(messagesSources.getMessage("text.cliente.factura.descripcion", null, locale).concat(": ")
				.concat(invoice.getDescription()));
		tabla2.addCell(messagesSources.getMessage("text.cliente.factura.fecha", null, locale).concat(": ")
				.concat(invoice.getDate().toString()));
		document.add(tabla);
		document.add(tabla2);

		PdfPTable tabla3 = new PdfPTable(4);
		tabla3.setWidths(new float[] { 3.5f, 1, 1, 1 });

		tabla3.addCell(messagesSources.getMessage("text.factura.form.item.nombre", null, locale));
		tabla3.addCell(messagesSources.getMessage("text.factura.form.item.precio", null, locale));
		tabla3.addCell(messagesSources.getMessage("text.factura.form.item.cantidad", null, locale));
		tabla3.addCell(messagesSources.getMessage("text.factura.form.item.total", null, locale));
		for (InvoiceItem item : invoice.getItems()) {
			tabla3.addCell(item.getProduct().getName());
			tabla3.addCell(item.getProduct().getPrice().toString());

			cell = new PdfPCell(new Phrase(item.getAmount().toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tabla3.addCell(cell);
			tabla3.addCell(item.calculateImport().toString());
		}

		cell = new PdfPCell(new Phrase(messagesSources.getMessage("text.factura.form.total", null, locale)));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabla3.addCell(cell);
		tabla3.addCell(invoice.getTotal().toString());
		document.add(tabla3);
	}

}
