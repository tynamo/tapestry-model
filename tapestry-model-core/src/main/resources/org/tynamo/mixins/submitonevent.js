Tapestry.Initializer.submitOnChange = function(eventName, formId, elementId, requiredField) {
	$(elementId).observe (eventName, function(event) {
		if ( ( requiredField && $(requiredField).value ) || ! requiredField) {
			var hiddenSubmit = document.createElement("input");
			hiddenSubmit.type = "submit";
			hiddenSubmit.name = "submit_" + elementId;
			hiddenSubmit.style.visibility = "hidden";
			hiddenSubmit.style.display = "none";
			$(formId).appendChild(hiddenSubmit);
			hiddenSubmit.click();
		}
	});
};
