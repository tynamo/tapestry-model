dojo.provide("tacos.widget.Navigator");
dojo.provide("tacos.widget.html.Navigator");
dojo.require("dojo.html");
dojo.require("dojo.style");
dojo.require("dojo.event.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.*");
dojo.require("tacos.widget.ReportGridDict");

dojo.widget.tags.addParseTreeHandler("dojo:navigator");

tacos.widget.Navigator=function(){

	dojo.widget.Widget.call(this);
	this.widgetType="Navigator";
	
	this.maxRows=0;
	this.pageStart=0;
	this.pageEnd=0;
	this.totalRows=0;
	this.pages=-1;
	this.showAll=false;
    this.language="en";
	
};
dojo.inherits(tacos.widget.Navigator, dojo.widget.Widget);


tacos.widget.html.Navigator=function(){

	dojo.widget.HtmlWidget.call(this);
	tacos.widget.Navigator.call(this);

	this.templatePath = dojo.uri.dojoUri("../tacos/widget/templates/Navigator.html");
    this.templateCssPath = dojo.uri.dojoUri("../tacos/widget/templates/Navigator.css");
    this.firstPage = false;
    this.lastPage = false;
	
    this.onFirst = function(/* DomEvent */ e){
        if(this.firstPage)
            return;
        this.onMove(0);
	}
	
	this.onPrev = function(/* DomEvent */ e){
        if(this.firstPage)
            return;
		var start = this.pageStart - this.maxRows;
		if (start < 0) start=0;
		this.onMove(start);
	}
	
	this.onNext = function(/* DomEvent */ e){
        if(this.lastPage)
            return;
		var start = this.pageStart + this.maxRows;
		if (start > this.totalRows) start=this.totalRows-this.maxRows;
		this.onMove(start);	
	}
	
	this.onLast = function(/* DomEvent */ e){
        if(this.lastPage)
            return;
		this.onMove((this.pages-1) * this.maxRows);
	}
	
	this.onPageChange = function(/* DomEvent */ e){
		var page = this.navPageSelect.value;
		var start=0;
		if (page>1)
			start = this.maxRows * (page-1);

		this.onMove(start);
	}

	this.onMove = function(/* integer */ pageStart){

	}
	
	this.setParameters = function(max,start,end,total){
		//alert('this.showAll ' + this.showAll);						
		if (end > total)
			end = total;
		if (start == 0 && end == total)
			this.hide();
		else
			this.show();
		
		
		var pages = parseInt(total / max)+1;
		if (pages != this.pages){
			// clear the page options
			this.navPageSelect.options.length = 0;
			
			for(i=1;i<=pages;i++){
				var name, value;
				name = translate(this.language,"page") + " " + i;
				value = i;
				this.navPageSelect[i-1] = new Option(name,value);
			}
            this.navPageSelect[pages] = new Option(translate(this.language,"all"),"-1");
            this.pages = pages;
		}
		
		this.maxRows=max;
		this.pageStart=start;
		this.pageEnd=end;
		this.totalRows=total;
		var eend = end;
		if (this.showAll) {
			eend = total;
		}
		
		this.navDisplay.innerHTML = (start + 1) + " " + translate(this.language,"to") + " " + eend + " " + translate(this.language,"of") + " " + total;
			
		var currentPage=1;
		if (start>0)
			currentPage=parseInt(start / this.maxRows) + 1;
		if (this.showAll) {
			this.navPageSelect.value = -1;
		}
		else {
		   this.navPageSelect.value = currentPage;
		}
			
		if (start == 0){
            this.navFirstImg.src = dojo.uri.dojoUri("../tacos/widget/templates/images/first-blueH.gif");
            this.navPrevImg.src = dojo.uri.dojoUri("../tacos/widget/templates/images/prev-blueH.gif");
            dojo.html.setClass(this.navFirst,"dojoNavButtonDisable");
			dojo.html.setClass(this.navPrev,"dojoNavButtonDisable");
            this.firstPage = true;
        }
		else{
            this.navFirstImg.src = dojo.uri.dojoUri("../tacos/widget/templates/images/first-blue.gif");
            this.navPrevImg.src = dojo.uri.dojoUri("../tacos/widget/templates/images/prev-blue.gif");
			dojo.html.setClass(this.navFirst,"dojoNavButton");
			dojo.html.setClass(this.navPrev,"dojoNavButton");
		    this.firstPage = false;
        }
		
		
		if (end == total){
            this.navNextImg.src = dojo.uri.dojoUri("../tacos/widget/templates/images/next-blueH.gif");
            this.navLastImg.src = dojo.uri.dojoUri("../tacos/widget/templates/images/last-blueH.gif");
			dojo.html.setClass(this.navNext,"dojoNavButtonDisable");
			dojo.html.setClass(this.navLast,"dojoNavButtonDisable");
            this.lastPage = true;
		}
		else{
            this.navNextImg.src = dojo.uri.dojoUri("../tacos/widget/templates/images/next-blue.gif");
            this.navLastImg.src = dojo.uri.dojoUri("../tacos/widget/templates/images/last-blue.gif");
			dojo.html.setClass(this.navNext,"dojoNavButton");
			dojo.html.setClass(this.navLast,"dojoNavButton");
            this.lastPage = false;
        }
			
	}
	
	this.postCreate = function(){
		dojo.style.insertCssFile(this.templateCssPath);
	}
	
	
};
dojo.inherits(tacos.widget.html.Navigator, dojo.widget.HtmlWidget);






