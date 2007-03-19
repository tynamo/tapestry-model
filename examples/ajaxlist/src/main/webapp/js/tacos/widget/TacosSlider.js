dojo.provide("tacos.widget.TacosSlider");
dojo.require("dojo.widget.*");

dojo.widget.tags.addParseTreeHandler("dojo:TacosSlider");

tacos.widget.TacosSlider=function() {

    // standard widget stuff
    dojo.widget.HtmlWidget.call(this);
    this.templatePath = dojo.uri.dojoUri("../tacos/widget/templates/TacosSlider.html");
    this.templateCssPath = dojo.uri.dojoUri("../tacos/widget/templates/TacosSlider.css");
    this.widgetType="TacosSlider";

    this.width = 200;
    this.startPos = 0;
    this.minimum = 0;
    this.maximum = 100;
    this.value = 0;

    // provide callback handle
    this.onUpdate = function(e) {};

    this._isSliding = false;
    this._currentX = 0;

    this.fillInTemplate = function() {
        this.track.style.width = this.width+'px';
        this.thumb.style.position = "relative";
        this._isSliding = false;
        this.setPosition();
    };

    this.getValueWidth = function() {
        return this.width/this.getExtent();
    };

    this.getExtent = function() {
        return this.maximum - this.minimum;
    };

    this.setPosition = function() {
        if (this.value < this.minimum) {
            this.value = this.minimum;
        }
        if (this.value > this.maximum) {
            this.value = this.maximum;
        }
        this._currentX =  Math.ceil(this.value * this.getValueWidth());;
        this.thumb.style.left = this._currentX + "px";
    };

    this.getPosition = function() {
        return this._currentX;
    };

    this.postCreate = function() {
        dojo.event.connect(this.thumb, "onmousedown", this, "startSliding");
        dojo.event.connect(this.track, "onmouseup", this, "moveThumb");
        this.width = this.width - 20;
        this.onUpdate({op:"update",value:this.value});
    };

    this.moveThumb = function(e) {
        if (this._isSliding) { 
            return false; 
        }
        var obj = this.track;
        var offset = obj.offsetLeft;
        while (obj.offsetParent != null) {
            obj = obj.offsetParent;
            offset += obj.offsetLeft;
            if (obj.tagName == 'BODY') {break;}
        }

        var dx = e.clientX - offset - 10 ;
        if (dx < 0 )
            this._currentX = 0;
        else  if ( dx > this.width)
            this._currentX = this.width;
        else 
            this._currentX = dx;
        this.thumb.style.left = (this._currentX) + "px";
        this.value = Math.ceil(this._currentX/this.getValueWidth());
        this.onUpdate({op:"update",value:this.value});
    };

    this.startSliding = function(e) {
        if (this._isSliding) { 
            return false; 
        }
        this._isSliding = true;
        this.startPoint = e.clientX;
        dojo.event.kwConnect({
            srcObj: document.body,
            srcFunc: "onmousemove",
            targetObj: this,
            targetFunc: "slide",
            rate:25 
        });
        dojo.event.connect(document.body, "onmouseup", this, "stopSliding");
        e.preventDefault();
    };

    this.slide = function(e) {
        if (e.clientX == 0) { return; }
        //dojo.debug("startPoint: " + this.startPoint + " currentX: " + this._currentX);
        var dx = this.startPoint - e.clientX;
        e.preventDefault();
        if ((this._currentX - dx) > this.width) {
            //dojo.debug("too positive: " + (this._currentX - dx));
            dx = this._currentX - this.width;
        } else if ((this._currentX - dx) < 0) {
            //dojo.debug("too negative: " (this._currentX - dx));
            dx = this._currentX;
        }
        //dojo.debug("setting left to: " + (this._currentX - dx) + "px");
        this.thumb.style.left = (this._currentX - dx) + "px";
        if (dojo.render.html.safari && window.getSelection) {
            window.getSelection().collapse();
        }
        e.preventDefault();
    };

    this.stopSliding = function(e) {
        dojo.event.disconnect(document.body, "onmousemove", this, "slide");
        dojo.event.disconnect(document.body, "onmouseup", this, "stopSliding");
        this._isSliding = false;

        var obj = this.track;
        var offset = obj.offsetLeft;
        while (obj.offsetParent != null) {
            obj = obj.offsetParent;
            offset += obj.offsetLeft;
            if (obj.tagName == 'BODY') {break;}
        }
        if (e.clientX < offset) {
            this._currentX = 0;
            this.thumb.style.left = (this._currentX) + "px";
            this.value = Math.ceil(this._currentX/this.getValueWidth());
        } else {
            var dx = this.startPoint - e.clientX;
            if ((this._currentX - dx) > this.width) {
                dx = - this.width + this._currentX;
            } else if ((this._currentX - dx) < 0) {
                dx = - this._currentX;
            } 
            this.thumb.style.left = (this._currentX - dx) + "px";
            
            this.value = Math.ceil((this._currentX - dx)/this.getValueWidth());
            this._currentX = this._currentX - dx;
        }
        this.onUpdate({op:"update",value:this.value});
    }
};

dojo.inherits(tacos.widget.TacosSlider, dojo.widget.HtmlWidget);

