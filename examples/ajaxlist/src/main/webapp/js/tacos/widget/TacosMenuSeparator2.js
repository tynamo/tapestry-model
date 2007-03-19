dojo.provide("tacos.widget.TacosMenuSeparator2");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Menu2");

tacos.widget.TacosMenuSeparator2 = function() {
    dojo.widget.MenuSeparator2.call(this);
    this.widgetType = "TacosMenuSeparator2";
    this.defaultContentHeight = 15;
}

dojo.inherits(tacos.widget.TacosMenuSeparator2, dojo.widget.MenuSeparator2);

dojo.lang.extend(tacos.widget.TacosMenuSeparator2, {
    layoutItem: function(label_w, accel_w) {
        if (this.parent) {
            tacos.widget.TacosMenuSeparator2.superclass.layoutItem.call(this, label_w, accel_w);
        } else {
            dojo.style.setContentHeight(this.domNode,this.defaultContentHeight);
        }
    }
});

dojo.widget.tags.addParseTreeHandler("dojo:TacosMenuSeparator2");

dojo.lang.extend(dojo.widget.Widget, {
    label: "",
    open: false
});
