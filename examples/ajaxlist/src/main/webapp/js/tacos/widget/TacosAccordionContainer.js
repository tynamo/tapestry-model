dojo.provide("tacos.widget.TacosAccordionContainer");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.AccordionContainer");
dojo.require("dojo.widget.AccordionPane");

tacos.widget.TacosAccordionContainer = function() {
    dojo.widget.AccordionContainer.call(this);
    this.widgetType = "TacosAccordionContainer";
    this.defaultPaneSize = 200;
}

dojo.inherits(tacos.widget.TacosAccordionContainer, dojo.widget.AccordionContainer);

dojo.lang.extend(tacos.widget.TacosAccordionContainer, {
    setSizes: function(paneSize) {
        paneSize = this.defaultPaneSize;
        var totalHeight = 0;
        if (this.children.length <= 0) {
            return;
        }
        for (var x = 0; x < this.children.length; x++) {
            var widg = this.children[x];
            if (widg.isShowing()) {
                var height = widg.getCollapsedHeight();
                if (height) {
                    totalHeight += height;
                }
            }
        } 
        if (totalHeight != dojo.style.getOuterHeight(this.domNode) - paneSize) {
            dojo.style.setOuterHeight(this.domNode, totalHeight + paneSize);
        }
        this.children[0].setSizes();
    }
});

dojo.widget.tags.addParseTreeHandler("dojo:TacosAccordionContainer");

dojo.lang.extend(dojo.widget.Widget, {
    label: "",
    open: false
});
