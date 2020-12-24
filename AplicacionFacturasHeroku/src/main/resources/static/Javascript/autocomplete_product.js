$(document).ready(function() {
	$("#loadItemProducts").hide();
	$("#pTotal").hide();
	$("#find_product").autocomplete({
		source: function(request, response) {
			$.ajax({
				url: "/invoice/load-products/" + request.term,
				dataType: "json",
				data: {
					term: request.term
				},
				success: function(data) {
					response($.map(data, function(item) {
						return {
							label: item.name,
							id: item.id,
							price: item.price,
						};
					}));
				},
			});
		},
		select: function(event, ui) {
			$("#find_product").val("");
			if (itemsHelper.hasProduct(ui.item.id)) {
				itemsHelper.incrementAmount(ui.item.id, ui.item.price);
				return false;
			}
			var line = $("#templateItemsInvoice").html();
			line = line.replace(/{id}/g, ui.item.id);
			line = line.replace(/{name}/g, ui.item.label);
			line = line.replace(/{price}/g, ui.item.price);
			$("#loadItemProducts tbody").append(line);
			itemsHelper.calculateImport(ui.item.id, ui.item.price, 1);
			$("#loadItemProducts").show();
			$("#pTotal").show();
			itemsHelper.recuperarLinea();
			return false;
		}
	});
	var memo = localStorage.getItem("line");
	if (memo != null && memo != "") {
		$("#loadItemProducts tbody").append(memo);
		itemsHelper.calculateFullTotal();
		$("#loadItemProducts").show();
	}

	$("#myForm").submit(function() {
		itemsHelper.recuperarLinea();
		$("#templateItemsInvoice").remove();
		return;
	});
});
//Metodos para el line
var itemsHelper = {
	calculateImport: function(id, price, amount) {
		if (parseInt(amount) < 0) {
			this.deleteInvoiceItem(id);
		}
		let amountc = $("#amount_" + id).val() ? parseInt($("#amount_" + id).val()) : 0;
		$("#amount_" + id).attr("value", amountc);
		$("#total_import_" + id).html(parseInt(price) * parseInt(amountc));
		this.calculateFullTotal();
		itemsHelper.recuperarLinea();
	},
	hasProduct: function(id) {
		let result = false;
		$('input[name="item_Id[]"]').each(function() {
			if (parseInt(id) == parseInt($(this).val())) {
				result = true;
			}
		});
		return result;
	},
	incrementAmount: function(id, price) {
		let amount = $("#amount_" + id).val() ? parseInt($("#amount_" + id).val()) : 0;
		$("#amount_" + id).val(++amount);
		this.calculateImport(id, price, amount);
		itemsHelper.recuperarLinea();
	},
	deleteInvoiceItem: function(id) {
		$("#row_" + id).remove();
		this.calculateFullTotal();
		itemsHelper.recuperarLinea();
	},
	calculateFullTotal: function() {
		let total = 0;
		$('span[id^="total_import_"]').each(function() {
			total += parseInt($(this).html());
		});
		$('#final_total').html(total);
		itemsHelper.recuperarLinea();
	},
	recuperarLinea: function() {
		localStorage.setItem("line", $("#loadItemProducts tbody").html());
	}
}