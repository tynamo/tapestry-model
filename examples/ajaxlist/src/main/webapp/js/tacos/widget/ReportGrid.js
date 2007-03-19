dojo.provide("tacos.widget.ReportGrid");
dojo.provide("tacos.widget.html.ReportGrid");
dojo.require("dojo.widget.*");
dojo.require("tacos.collections.QuickSort");
dojo.require("dojo.lang");
dojo.require("dojo.date");
dojo.require("dojo.html");
dojo.require("dojo.dom");
dojo.require("dojo.event.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("tacos.widget.Navigator");
dojo.require("tacos.widget.ReportGridDict");
dojo.require("dojo.json");

dojo.hostenv.setModulePrefix('MochiKit', '../MochiKit'); 
dojo.require('MochiKit.Format'); 

dojo.widget.tags.addParseTreeHandler("dojo:reportGrid");

//	set up the general widget
tacos.widget.ReportGrid=function(){
	//	summary
	//	base class for the SortableTable
	dojo.widget.Widget.call(this);
	this.widgetType="ReportGrid";
	this.isContainer=false;
    this.numberPattern='/^[+-]?[0-9\.\,\-eE]{1,}$/';
	//	custom properties
	this.hasFooter = false;
	this.enableMultipleSelect=true;
	this.maximumNumberOfSelections=0;	//	0 for unlimited, is the default.
	this.enableAlternateRows=true;
	this.minRows=0;	//	0 means ignore.
	this.defaultDateFormat="%c";
	this.defaultNumberFormat="#,##0";
	this.data = new tacos.collections.QuickSort();
	//this.links= new tacos.collections.QuickSort();;
	this.footerData=[];
	this.selected=[];		//	always an array to handle multiple selections.
	this.columns=[];
	
	this.sortIndex=-1;		//	index of the column sorted on, default is none -1
	this.sortDirection=0;	//	0==asc, 1==desc
	this.sortIndexLast=-1;		//	index of the column sorted on, first is the default.
	this.sortDirectionLast=0;	//	0==asc, 1==desc
	this.valueField="Id";	//	if a JSON structure is parsed and there is a field of this name,
							//	a value attribute will be added to the row (tr value="{Id}")

    this.showAll=false;
	this.maxRows=0;	//	0 means ignore.
	this.pageStart=0;
	this.drillDown=false; // Allow drill down when only data passed
	this.showFilter=false; // show the filter boxes
	this.title="";
	this.titleNode; // title dome node
	this.info="";
	this.infoNode; // info dome node
	this.filter; // filter dom node
	this.filterLast; // string of last filter
	this.filterDefault=0; // column index
	this.navigator; // navigator
	this.table; // table dom node for easy access
	this.onRowClicked = function(row, value){}; // row click event handler
    this.currentType=null;
    this.filterCollapsed=true;
    this.useToggleFilterButton=true;

	this.language = "en";


};
dojo.inherits(tacos.widget.ReportGrid, dojo.widget.Widget);
//dojo.requireAfterIf("html", "dojo.widget.html.ReportGrid");


tacos.widget.html.ReportGrid=function(){
	//	summary
	//	Constructor for the SortableTable widget
	dojo.widget.HtmlWidget.call(this);
	tacos.widget.ReportGrid.call(this);

	this.containerClass="";
	this.tableClass="";
	this.theadClass="";
	this.tbodyClass="";
	this.tfootClass="";
	this.headerClass="";
	this.headerSortUpClass="selected";
	this.headerSortDownClass="selected";
	this.rowClass="";
	this.border=0;
	this.cellSpacing=1;
	this.cellPadding=2;
	this.rowAlternateClass="alt";
	this.rowSelectedClass="selected";
	this.columnSelected="sorted-column";


};
dojo.inherits(tacos.widget.html.ReportGrid, dojo.widget.HtmlWidget);


dojo.lang.extend(tacos.widget.html.ReportGrid, {
	templatePath:null,
	templateCssPath:null,

	getTypeFromString:function(/* string */ s){
		//	summary
		//	Find the constructor that matches param s by searching through the entire object tree.
		var parts=s.split("."),i=0,obj=dj_global;
		do{obj=obj[parts[i++]];}while(i<parts.length&&obj);
		return(obj!=dj_global)?obj:null;	//	function
	},
	compare:function(/* object */ o1, /* object */ o2){
		//	summary
		//	Compare two objects using a shallow property compare
		for(var p in o1){
			if(!o2[p]) return false;	//	boolean
			if(o1[p].valueOf()!=o2[p].valueOf()) return false;	//	boolean
		}
		return true;	// boolean
	},
	isSelected:function(/* object */ o){
		//	summary
		//	checked to see if the passed object is in the current selection.
		for(var i=0;i<this.selected.length;i++){
			if(this.compare(this.selected[i],o)){
				return true; // boolean
			}
		}
		return false;	// boolean
	},
	removeFromSelected:function(/* object */ o){
		//	summary
		//	remove the passed object from the current selection.
		var idx=-1;
		for(var i=0;i<this.selected.length;i++){
			if(this.compare(this.selected[i],o)){
				idx=i;
				break;
			}
		}
		if(idx>=0){
			this.selected.splice(idx,1);
		}
	},
	getSelection:function(){
		//	summary
		//	return the array of currently selected objects (JSON format)
		return this.selected;	//	array
	},
	getValue:function(){
		//	summary
		//	return a comma-delimited list of selected valueFields.
		var a=[];
		for(var i=0;i<this.selected.length;i++){
			if (this.selected[i][this.valueField]){
				a.push(this.selected[i][this.valueField]);
			}
		}
		return a.join();	//	string
	},
	reset:function(){
		//	summary
		//	completely resets the internal representations.
		this.columns=[];
		this.data.clear();
		this.filter.value.value = "";
		this.createFilterColumns();
		this.resetSelections(this.table.getElementsByTagName("tbody")[0]);
	},
	resetSelections:function(/* HTMLTableBodyElement */ body){
		this.selected=[];
		var idx=0;
		var rows=body.getElementsByTagName("tr");
		for(var i=0; i<rows.length; i++){
			if(rows[i].parentNode==body){
				rows[i].removeAttribute("selected");
				if(this.enableAlternateRows&&idx%2==1){
					rows[i].className=this.rowAlternateClass;
				}else{
					rows[i].className=this.rowClass;
				}
				idx++;
			}
		}
	},

	getObjectFromRow:function(/* HTMLTableRowElement */ row){
		//	summary
		//	creates a JSON object based on the passed row
		var cells=row.getElementsByTagName("td");
		var o={};
		for(var i=0; i<this.columns.length;i++){
			if(this.columns[i].sortType=="__markup__"){
				//	FIXME: should we parse this instead?  Because if the user may not get back the markup they put in...
				o[this.columns[i].getField()]=cells[i].innerHTML;
			}else{
				var text=dojo.html.renderedTextContent(cells[i]);
				var val=new (this.columns[i].getType())(text);
				o[this.columns[i].getField()]=val;
			}
		}
		if(dojo.html.hasAttribute(row,"value")){
			o[this.valueField]=dojo.html.getAttribute(row,"value");
		}
		return o;	//	object
	},
	setSelectionByRow:function(/* HTMLTableElementRow */ row){
		//	summary
		//	create the selection object based on the passed row, makes sure it's unique.
		//	note that you need to call render manually (because of multi-select operations)
		var o=this.getObjectFromRow(row);
		var b=false;
		for(var i=0;i<this.selected.length;i++){
			if(this.compare(this.selected[i], o)){
				b=true;
				break;
			}
		}
		if(!b){
			this.selected.push(o);
		}
	},

	parseColumns:function(/* array of columns */ columns){
		//alert("!!!");
		//	summary
		//	parses the passed element to create column objects
		this.reset();

		for(var i=0; i<columns.length; i++){
			var o={
				field:null,
				format:null,
				noSort:false,
				hidden:false,
				sortType:"String",
				dataType:"String",
				sortFunction:null,
				label:null,
				align:"left",
				valign:"middle",
				cls:null,
				filtCaseIns:false,
				getField:function(){ return this.field||this.label; },
				getLabel:function(){ return this.label||this.field; },
				getType:function(){ return this.sortType; },
                getTotal:function() {}

            };
			var column = dojo.lang.mixin(o,columns[i]);

			column.sortType = this.getTypeFromString(column.dataType);

			if(column.dataType.toLowerCase()=="html"||column.dataType.toLowerCase()=="markup"){
				column.dataType="__markup__";	//	always convert to "__markup__"
				column.noSort=true;
			}

			this.columns.push(o);
		}

		// add the column headers
		var head=this.table.getElementsByTagName("thead")[0];
		while(head.childNodes.length>0) head.removeChild(head.childNodes[0]);
		var row=document.createElement("tr");
		for(var j=0;j<this.columns.length;j++){
			var cell=document.createElement("th");
			cell.setAttribute("align", this.columns[j].align);
			cell.setAttribute("valign", this.columns[j].valign);
    		cell.className=this.headerClass;
			cell.appendChild(document.createTextNode(this.columns[j].getLabel()));
			if (this.columns[j].hidden)
				cell.style.display="none";
			row.appendChild(cell);
		}
		head.appendChild(row);
		this.attachHeaders();

		this.createFilterColumns();

	},

	parseColumnsFromTable:function(/* HTMLTableHeadElement */ node){
		//	summary
		//	parses the passed element to create column objects
		this.reset();
		var row=node.getElementsByTagName("tr")[0];
		if (row==null) return;

		var cells=row.getElementsByTagName("td");
		if (cells.length==0) cells=row.getElementsByTagName("th");
		for(var i=0; i<cells.length; i++){
			var o={
				field:null,
				format:null,
				noSort:false,
				hidden:false,
				sortType:String,
				dataType:"String",
				sortFunction:null,
				label:null,
				align:"left",
				valign:"middle",
         		getField:function(){ return this.field||this.label; },
				getLabel:function(){ return this.label||this.field; },
				getType:function(){ return this.sortType; }
			};
			//	presentation attributes
			if(dojo.html.hasAttribute(cells[i], "align")){
				o.align=dojo.html.getAttribute(cells[i],"align");
			}
			if(dojo.html.hasAttribute(cells[i], "valign")){
				o.valign=dojo.html.getAttribute(cells[i],"valign");
			}

			//	hidden
			if(dojo.html.hasAttribute(cells[i], "hidden")){
				o.noSort=dojo.html.getAttribute(cells[i],"hidden")=="true";
			}

			//	sorting features.
			if(dojo.html.hasAttribute(cells[i], "nosort")){
				o.noSort=dojo.html.getAttribute(cells[i],"nosort")=="true";
			}

			if(dojo.html.hasAttribute(cells[i], "sortusing")){
				var trans=dojo.html.getAttribute(cells[i],"sortusing");
				var f=this.getTypeFromString(trans);
				if (f!=null && f!=window && typeof(f)=="function")
					o.sortFunction=f;
			}

			if(dojo.html.hasAttribute(cells[i], "field")){
				o.field=dojo.html.getAttribute(cells[i],"field");
			}
			if(dojo.html.hasAttribute(cells[i], "format")){
				o.format=dojo.html.getAttribute(cells[i],"format");
			}
			if(dojo.html.hasAttribute(cells[i], "dataType")){
				var dataType=dojo.html.getAttribute(cells[i],"dataType");
				if(dataType.toLowerCase()=="html"||dataType.toLowerCase()=="markup"){
					o.dataType="__markup__";	//	always convert to "__markup__"
					o.noSort=true;
				}else{
					var type=this.getTypeFromString(dataType);
					if(type){
						o.sortType=type;
						o.dataType=dataType;
					}
				}
			}
			o.label=dojo.html.renderedTextContent(cells[i]);
			this.columns.push(o);


			//	check to see if there's a default sort, and set the properties necessary
			if(dojo.html.hasAttribute(cells[i], "sort")){
				this.sortIndex=i;
				var dir=dojo.html.getAttribute(cells[i], "sort");
				if(!isNaN(parseInt(dir))){
					dir=parseInt(dir);
					this.sortDirection=(dir!=0)?1:0;
				}else{
					this.sortDirection=(dir.toLowerCase()=="desc")?1:0;
				}
			}
		}

		this.createFilterColumns();

	},

    parseData:function(/* array */ data) {
        if (this.onStartProcessing 
            && typeof this.onStartProcessing == "function" 
            && this.onStopProcessing 
            && typeof this.onStopProcessing == "function") {

            this.onStartProcessing();
            dojo.lang.setTimeout(this, "_parseData", 50, data);
        } else {
            this._actualParseData(data);
        }
    },

    _parseData:function(/* array */ data) {
        this._actualParseData(data);
        if (this.onStopProcessing 
            && typeof this.onStopProcessing == "function") {

            this.onStopProcessing();
        }
    },

	_actualParseData:function(/* array */ data){
		//	summary
		//	Parse the passed JSON data structure, and cast based on columns.
		this.filter.value.value = "";
		this.data.clear();
		this.selected=[];

		// reset the sorting
        /*
        this.sortIndex=-1;
		this.sortDirection=0;
		this.sortIndexLast=-1;
		this.sortDirectionLast=0;
        */

        // reset the Paging variables
		this.pageStart = 0;

		var length = data.length;
		var cols = this.columns.length;

		var dateCols =[];

		// Get the dates to convert
		for(var j=0; j<this.columns.length; j++){
			if (this.columns[j].dataType == "Date")
				dateCols.push(this.columns[j].getField());
		}


		for(var i=0;i<length;i++){
			// convert the dates to Date object
			for(var j=0;j<dateCols.length;j++){
				var value = data[i][dateCols[j]];
				if (value)
					data[i][dateCols[j]] = dojo.date.fromIso8601(value);
			}
			this.data.add(data[i]);
		}

		delete data;

	},

	//parseLinks:function(data){
	//	this.links = data;
    //	delete data;
	//},

	parseFooterData:function(data){
		for(var j=0; j<this.columns.length; j++){
			var value = data[this.columns[j].field];
			this.footerData[j]=value;
		}
    },

	parseDataFromTable:function(/* HTMLTableBodyElement */ tbody){
		//	summary
		//	parses the data in the tbody of a table to create a set of objects.
		//	Will add objects to this.selected if an attribute 'selected="true"' is present on the row.
		this.data.clear();
		this.filter.value.value = "";
		this.selected=[];

		// reset the Paging variables
		this.pageStart = 0;

		var rows=tbody.getElementsByTagName("tr");
		for(var i=0; i<rows.length; i++){
			if(dojo.html.getAttribute(rows[i],"ignoreIfParsed")=="true"){
				continue;
			}
			var o={};	//	new data object.
			var cells=rows[i].getElementsByTagName("td");
			for(var j=0; j<this.columns.length; j++){
				var field=this.columns[j].getField();
				if(this.columns[j].sortType=="__markup__"){
					//	FIXME: parse this?
					o[field]=cells[j].innerHTML;
				}else{
					var type=this.columns[j].getType();
					var val=dojo.html.renderedTextContent(cells[j]); //	should be the same index as the column.
					if (val) o[field]=new type(val);
					else o[field]=new type();	//	let it use the default.
				}
			}
			if(dojo.html.hasAttribute(rows[i],"value")&&!o[this.valueField]){
				o[this.valueField]=dojo.html.getAttribute(rows[i],"value");
			}
			//	FIXME: add code to preserve row attributes in __metadata__ field?
			this.data.add(o);

			//	add it to the selections if selected="true" is present.
			if(dojo.html.getAttribute(rows[i],"selected")=="true"){
				this.selected.push(o);
			}
		}

	},


	parseFooter:function(/* object */ footer){
		var tfoot = this.table.getElementsByTagName("tfoot")[0];
		while(tfoot.childNodes.length>0) tfoot.removeChild(tfoot.childNodes[0]);

		var row=document.createElement("tr");
		for(var j=0;j<this.columns.length;j++){
			var cell=document.createElement("td");
			cell.setAttribute("align", this.columns[j].align);
			cell.setAttribute("valign", this.columns[j].valign);
			cell.className = this.tfootClass;

			var value = footer[this.columns[j].getField()];

			if (value==null || value == "")
				cell.appendChild(document.createTextNode(""));
			else {
				if(this.columns[j].sortType=="__markup__"){
					cell.innerHTML=value;
				}else{
					if(this.columns[j].getType()==Date){
						var format=this.defaultDateFormat;
						if(this.columns[j].format) format=this.columns[j].format;
						cell.appendChild(document.createTextNode(dojo.date.format(value, format)));
					} else if (this.columns[j].getType()==Number){
						var format=this.defaultNumberFormat;
						if(this.columns[j].format && this.columns[j].format!="") format=this.columns[j].format;
						cell.appendChild(document.createTextNode(MochiKit.Format.numberFormatter(format)(value)));
					}
					else{
						cell.appendChild(document.createTextNode(value));
					}
				}
			}
			if (this.columns[j].hidden)
				cell.style.display="none";
			row.appendChild(cell);
		}
		tfoot.appendChild(row);

	},

    render:function() {
        if (this.onStartProcessing 
            && typeof this.onStartProcessing == "function" 
            && this.onStopProcessing 
            && typeof this.onStopProcessing == "function") {

            this.onStartProcessing();
            dojo.lang.setTimeout(this, "_render", 50);
        } else {
            this._actualRender();
        }
    },

    _render:function() {
        this._actualRender();
        if (this.onStopProcessing 
            && typeof this.onStopProcessing == "function") {

            this.onStopProcessing();
        }
    },

    americanDate:function (d) {
         /***
             Converts a MM/DD/YYYY OR MM-DD-YYYY or date to a Date object
         ***/
            d = d + "";
            if (typeof(d) != "string" || d.length == 0) {
                return null;
            }
            var a = d.split('/');
            if (a.length <= 1) {
                a = d.split('-');
            }
            if (a.length <= 1) {
                 alert("Value must be a valid date (mm/dd/yyyy or mm-dd-yyyy)");
                 return null;
            }

            var year = (a[2].length==2 ? "20" : "" ) + a[2];
            var month = (a[0].length==1 ? "0" : "" ) + a[0];
            var day = (a[1].length==1 ? "0" : "" ) + a[1];
            var dateStr = year + '-' + month + '-' + day;

            return new dojo.date.fromIso8601(dateStr);
        },


        europeanDate:function (d) {
         /***
             Converts a DD/MM/YYYY OR DD-MM-YYYY or date to a Date object
         ***/
             d = d + "";
             if (typeof(d) != "string" || d.length == 0) {
                 return null;
             }
             var a = d.split('/');
             if (a.length <= 1) {
                 a = d.split('-');
             }
             if (a.length <= 1) {
                 alert("Value must be a valid date (dd/mm/yyyy or dd-mm-yyyy)");

                 return null;
            }

            var year = (a[2].length==2 ? "20" : "" ) + a[2];
            var month = (a[1].length==1 ? "0" : "" ) + a[1];
            var day = (a[0].length==1 ? "0" : "" ) + a[0];
            var dateStr = year + '-' + month + '-' + day;

             return new dojo.date.fromIso8601(dateStr);
         },


    _actualRender:function(){
		//	summary
		//	renders the table to the browser
		
		var body=this.table.getElementsByTagName("tbody")[0];
		while(body.childNodes.length>0) body.removeChild(body.childNodes[0]);

		if(this.data.length==0)
			return;


		var filterColumn = this.filter.columns.value;
		var filterType = this.filter.filterType.value;
		var val = this.filter.value.value;
		
		
		var filterString = val + filterColumn + filterType;

		if (val == "") {
			this.data.filterClear();
			if (this.filterLast)
				this.sortIndexLast = -1;
			this.filterLast = null;
        } else {
			if (filterString != this.filterLast) {
			
				this.sortIndex = filterColumn;
				// Commented by Alexey
				// If we doing filtering sortDirection remains the same
				//this.sortDirection = 0;

				var dataType=this.columns[this.sortIndex].dataType.toLowerCase();
                if (dataType == "number") {
		            val = val.replace(/[, ]/g,'');
		        }
				 
				//if (dataType == "number" && isNaN(val)){
				if (dataType == "number" &&  (!val.match(/^[+-]?[0-9\.\-eE]{1,}$/)) ) {
        		    alert("Filter value must be a number");
				}
				else if (dataType == "date"){
					var dataFormatter = this.defaultDateFormat;
                    if (this.columns[this.sortIndex].format) dataFormatter = this.columns[this.sortIndex].format;

                    var sortValue = new Date();

                    if ((dataFormatter == "%m/%d/%Y") || (dataFormatter == "%m-%d-%Y")) {
                        sortValue = this.americanDate(val);
                    }
                    else {
                        sortValue = this.europeanDate(val);
                    }
                    /*
                    var parts = val.split("/");
					if (parts.length <= 1)
						parts = val.split("-");

					if (parts.length <= 1)
						alert("Value must be a valid date (dd/mm/yyyy or dd-mm-yyyy)");
					else{
						try{
							var d = new Date();
							var day = parts[0];
							var month = parts[1];
							if (parts.lenth == 2)
								var year = d.getFullYear();
							else
								var year =parts[2];
							var sortValue = new Date(year,month-1,day);
							//var sortValue = year + "-" + month + "-" + day;
						}catch(ex){
							alert("Value must be a valid date (dd/mm/yyyy or dd-mm-yyyy)");
						}
					}
					*/
				}
				else {
					// convert the value to the appropriate type
					var type=this.columns[this.sortIndex].getType();
					if(val)
						var sortValue=new type(val);
					else
						var sortValue=new type();	//	let it use the default.
				}

				if(sortValue){
					var filter;
                    //var slowFilter;
					var useSlowFilter = false;
					var fci = this.columns[this.sortIndex].filtCaseIns;
					//alert("Filter case insens = " + fci);
					
					if (fci) {
						sortValue = sortValue.toUpperCase();
						useSlowFilter = true;
					}
                    switch(filterType){
						case "starts":
                            filter = function(value){
                                if (fci) {
									value = value.toUpperCase();
								}
								if(value.indexOf(sortValue)==0) return 0;
                                if(value<sortValue) return -1;
                                return 1;
                            };
							break;
                        case "contains":
                            useSlowFilter = true;
                            filter = function(value){
                                if(value.toString().indexOf(sortValue.toString())>-1) {
                                    return 0;
                                }
                                return 1;
                            };
                            break;
                        case "endswith":
                            filter = function(value){
								if (fci) {
									value = value.toUpperCase();
								}
                                if (  value.toString().lastIndexOf(sortValue.toString()) > -1 &&
                                      value.toString().lastIndexOf(sortValue.toString()) == (value.toString().length - sortValue.toString().length)) {
                                    return 0;
                                }
                                //if(value.toString()<sortValue.toString()) return -1;
                                return 1;
                            };
                            break;
						case "greaterequal":
							filter = function(value){
								if (fci) {
									value = value.toUpperCase();
								}
								if(value>=sortValue) return 0;
								return -1;
							};
							break;
						case "greater":
							filter = function(value){
								if (fci) {
									value = value.toUpperCase();
								}
								if(value>sortValue) return 0;
								return -1;
							};
							break;
						case "less":
							filter = function(value){
								if (fci) {
									value = value.toUpperCase();
								}
								if(value<sortValue) return 0;
								return 1;
							};
							break;
						case "lessequal":
							filter = function(value){
								if (fci) {
									value = value.toUpperCase();
								}
								if(value<=sortValue) return 0;
								return 1;
							};
							break;
						case "notequal":
                            useSlowFilter = true;
							if (dataType == "date"){
				               filter = function(value){
                                     if(value.getTime()!=sortValue.getTime()) return 0;
                                     return 1;
                               };
                            }
                 		    else {
                              filter = function(value){
								if (fci) {
									value = value.toUpperCase();
								}
                                if(value!=sortValue) return 0;
                                return 1;
                              };
                            }

                            break;
                        default:
                            //equals
							useSlowFilter = true;
                            if (dataType == "date"){
				              filter = function(value){
                                    if(value.getTime()>sortValue.getTime()) return 1;
                                    if(value.getTime()<sortValue.getTime()) return -1;
                                    return 0;
                              };
                            }
                 		    else {
                              filter = function(value){
								if (fci) {
									value = value.toUpperCase();
								}
                                if(value>sortValue) return 1;
                                if(value<sortValue) return -1;
                                return 0;
                              };
                            }
					}

					this.filterLast = filterString;
					this.pageStart = 0;
					this.sortDirectionLast = 0;
					this.sortIndexLast=-1;
				}
				else{
					this.sortIndex = this.sortIndexLast;
					this.sortDirection = this.sortDirectionLast;
				}
			}
		}


		// format the headers
		var head=this.table.getElementsByTagName("thead")[0];
		var tr=head.getElementsByTagName("tr")[0];
		var cellTag="td";
		if(tr.getElementsByTagName(cellTag).length==0) cellTag="th";
		var headers=tr.getElementsByTagName(cellTag);
		for(var i=0; i<headers.length; i++){
			if(i==this.sortIndex){
				if(i!=this.sortIndexLast){
					headers[i].className=this.headerSortDownClass
				}else{
					if(this.sortDirection==0){
						headers[i].className=this.headerSortDownClass;
					}else{
						headers[i].className=this.headerSortUpClass;
					}
				}
			}else{
				//	reset the header class.
				//alert(this.headerClass);
				headers[i].className=this.headerClass;
			}
		}


		var col;

		// Only sort if it has changed
		if (this.sortIndex != this.sortIndexLast)
			col=this.columns[this.sortIndex];

		if(col!=null && !col.noSort){
			var field=col.getField();
			if(col.sortFunction){
				var sort=col.sortFunction;
			}
			// sort the data
			this.data.sortField(field,sort);
			//if (filter)
			//	this.data.filterField(field, filter);
            //else
            //    if(slowFilter)
            //        this.data.slowFilterField(field, slowFilter);
			if (filter) {
				if (useSlowFilter) {
					this.data.slowFilterField(field, filter);
				}
				else {
					this.data.filterField(field, filter);
				}
			}
            if(this.sortDirection!=0)
				this.data.reverse();

			this.sortIndexLast = this.sortIndex;
			this.sortDirectionLast = this.sortDirection;
		}

		if(this.sortDirection != this.sortDirectionLast){
			this.data.reverse();
			this.sortDirectionLast = this.sortDirection;
		}

		var end = this.data.count;

		// Check to see if paging is required
		if (this.maxRows > 0) {
			end = this.pageStart + this.maxRows;
			if (end > this.data.count)
				end = this.data.count;
		}
         
		var eend = this.data.count;
		if (!this.showAll) {
			eend = end;
		}


		for(var i=this.pageStart; i<eend;i++){

			var row=document.createElement("tr");
			if (this.data.get(i)!=null && this.data.get(i)[this.valueField]){
				row.setAttribute("value",this.data.get(i)[this.valueField]);
			}
			if(this.isSelected(this.data.get(i))){
				row.className=this.rowSelectedClass;
				row.setAttribute("selected","true");
			} else {
				row.className=this.rowClass;
				if(this.enableAlternateRows&&i%2==1){
					row.className=this.rowAlternateClass;
				}
			}
			for(var j=0;j<this.columns.length;j++){

				var cell=document.createElement("td");
				cell.setAttribute("align", this.columns[j].align);
				cell.setAttribute("valign", this.columns[j].valign);
    			if (this.columns[j].cls!=null) {
					cell.className = this.columns[j].cls;
				}
				if(this.sortIndex==j){
					//cell.className=this.columnSelected;
					cell.className=this.columns[j].cls + "Selected";
				}
				
				var value = null;
				var value_link = null;
				
				if (this.data.get(i)!=null) {
				   value = this.data.get(i)[this.columns[j].getField()];
				   value_link = this.data.get(i)[this.columns[j].getField() + '_link'];
				}
				
                //if (value==null || value == "")
                if (value==null) {
   				       cell.appendChild(document.createTextNode("-"));
				}
				else {
					if(this.columns[j].sortType=="__markup__"){
						cell.innerHTML=value;
					}else{
						var linkEl = cell;
     					if (value_link!=null) {
						    linkEl = cell.appendChild(document.createElement("a"));
							linkEl.setAttribute("href",value_link);
						}

						if(this.columns[j].getType()==Date && value!=""){
							var format=this.defaultDateFormat;
							if(this.columns[j].format) format=this.columns[j].format;
							linkEl.appendChild(document.createTextNode(dojo.date.format(value, format)));
						} else if (this.columns[j].getType()==Number){
							var format=this.defaultNumberFormat;
							if(this.columns[j].format && this.columns[j].format!="") format=this.columns[j].format;
							linkEl.appendChild(document.createTextNode(MochiKit.Format.numberFormatter(format)(value)));
						}
						else{
							linkEl.appendChild(document.createTextNode(value));
						}
						linkEl = null;

					}
				}
				if (this.columns[j].hidden)
					cell.style.display="none";
				row.appendChild(cell);
			}
			body.appendChild(row);

			//!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//dojo.event.connect(row, "onclick", this, "onUISelect");
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!


			dojo.event.connect(row, "onmouseover", this, "onMouseOver");
			dojo.event.connect(row, "onmouseout", this, "onMouseOut");
		}

		//	if minRows exist.
		var minRows=parseInt(this.minRows);
		if (!isNaN(minRows) && minRows>0 && this.data.count<minRows){
			var mod=0;
			if(this.data.count%2==0) mod=1;
			var nRows=minRows-data.length;
			for(var i=0; i<nRows; i++){
				var row=document.createElement("tr");
				row.setAttribute("ignoreIfParsed","true");
				row.className=this.rowClass
				if(this.enableAlternateRows&&i%2==mod){
					row.className=this.rowAlternateClass;
				}
				for(var j=0;j<this.columns.length;j++){
					var cell=document.createElement("td");
					cell.appendChild(document.createTextNode("\u00A0"));
					row.appendChild(cell);
				}
				body.appendChild(row);
			}
		}




	// Added for FOOTER ROW
	if (this.hasFooter) {
	    var row=document.createElement("tr");
    	    row.className=this.tfootClass;
            for(var j=0;j<this.columns.length;j++){
				var cell=document.createElement("td");
				cell.setAttribute("align", this.columns[j].align);
				cell.setAttribute("valign", this.columns[j].valign);
				//if(this.sortIndex==j){
				//	cell.className=this.columnSelected;
				//}

                cell.className=this.tfootClass;

                //var value = this.footerData[j];

                var value = this.columns[j].getTotal(this.data);

                if (value==null || value == "")
					cell.appendChild(document.createTextNode("-"));
				else {
					if(this.columns[j].sortType=="__markup__"){
						cell.innerHTML=value;
					}else{
						if(this.columns[j].getType()==Date){
							var format=this.defaultDateFormat;
							if(this.columns[j].format) format=this.columns[j].format;
							cell.appendChild(document.createTextNode(dojo.date.format(value, format)));
						} else if (this.columns[j].getType()==Number){
							var format=this.defaultNumberFormat;
							if(this.columns[j].format && this.columns[j].format!="") format=this.columns[j].format;
							cell.appendChild(document.createTextNode(MochiKit.Format.numberFormatter(format)(value)));
						}
						else{
							cell.appendChild(document.createTextNode(value));
						}
					}
				}
				if (this.columns[j].hidden)
					cell.style.display="none";

                row.appendChild(cell);
			}
			body.appendChild(row);
	}
    // Added for FOOTER ROW







		// add the title
		this.titleNode.innerHTML = this.title;
		if (this.title=="")
			this.titleNode.style.display = "none";
		else{
			this.titleNode.style.display = "inline";
		}
        
		// add additional String
		this.infoNode.innerHTML = this.info;
		if (this.info=="")
			this.infoNode.style.display = "none";
		//else{
		//	this.infoNode.style.display = "inline";
		//}
		
		this.navigator.showAll = this.showAll;
        this.navigator.language = this.language;
        this.navigator.setParameters(this.maxRows,this.pageStart,end,this.data.count);
		
		// set parameters for the navigator

		if (this.showFilter){
			this.filter.style.display = "inline";
            if(this.useToggleFilterButton)
            {
                this.filter.toggleFilterPanel.style.display = "block";
                if(!this.filterCollapsed)
                {
                    this.filter.filterTable.style.display = "block";
                }
                else
                {
                    this.filter.filterTable.style.display = "none";
                }
            }
            else
            {
              this.filter.toggleFilterPanel.style.display = "none";
            }
        }
		else
			this.filter.style.display = "none";

		this.show();


	},
	showSelections:function(){
		var body=this.table.getElementsByTagName("tbody")[0];
		var rows=body.getElementsByTagName("tr");
		var idx=0;
		for(var i=0; i<rows.length; i++){
			if(rows[i].parentNode==body){
				if(dojo.html.getAttribute(rows[i],"selected")=="true"){
					rows[i].className=this.rowSelectedClass;
				} else {
					if(this.enableAlternateRows&&idx%2==1){
						rows[i].className=this.rowAlternateClass;
					}else{
						rows[i].className=this.rowClass;
					}
				}
				idx++;
			}
		}
	},
	// Can be overriden to show progress
	onDataParse:function(o, row, total){

	},
	//	the following the user can override.
	onSelect:function(/* DomEvent */ e){
		//	summary
		//	empty function for the user to attach code to, fired by onUISelect
	},
	onUISelect:function(/* DomEvent */ e){
		//	summary
		//	fired when a user selects a row
		var row=dojo.html.getParentByType(e.target,"tr");
		var body=dojo.html.getParentByType(row,"tbody");
		if(this.enableMultipleSelect){
			if(e.metaKey||e.ctrlKey){
				if(this.isSelected(this.getObjectFromRow(row))){
					this.removeFromSelected(this.getObjectFromRow(row));
					row.removeAttribute("selected");
				}else{
					//	push onto the selection stack.
					this.setSelectionByRow(row);
					row.setAttribute("selected","true");
				}
			}else if(e.shiftKey){
				//	the tricky one.  We need to figure out the *last* selected row above,
				//	and select all the rows in between.
				var startRow;
				var rows=body.getElementsByTagName("tr");
				//	if there's a selection above, we go with that first.
				for(var i=0;i<rows.length;i++){
					if(rows[i].parentNode==body){
						if(rows[i]==row) break;
						if(dojo.html.getAttribute(rows[i],"selected")=="true"){
							startRow=rows[i];
						}
					}
				}
				//	if there isn't a selection above, we continue with a selection below.
				if(!startRow){
					startRow=row;
					for(;i<rows.length;i++){
						if(dojo.html.getAttribute(rows[i],"selected")=="true"){
							row=rows[i];
							break;
						}
					}
				}
				this.resetSelections(body);
				if(startRow==row){
					//	this is the only selection
					row.setAttribute("selected","true");
					this.setSelectionByRow(row);
				}else{
					var doSelect=false;
					for(var i=0; i<rows.length; i++){
						if(rows[i].parentNode==body){
							rows[i].removeAttribute("selected");
							if(rows[i]==startRow){
								doSelect=true;
							}
							if(doSelect){
								this.setSelectionByRow(rows[i]);
								rows[i].setAttribute("selected","true");
							}
							if(rows[i]==row){
								doSelect=false;
							}
						}
					}
				}
			}else{
				//	reset the selection
				this.resetSelections(body);
				row.setAttribute("selected","true");
				this.setSelectionByRow(row);
			}
		}else{
			//	reset the data selection and go.
			this.resetSelections(body);
			row.setAttribute("selected","true");
			this.setSelectionByRow(row);
		}
		this.showSelections();
		this.onSelect(e);
		e.stopPropagation();
		e.preventDefault();
	},
	onMouseOver:function(/* DomEvent */ e){
		var row=dojo.html.getParentByType(e.target,"tr");
		row.setAttribute("defaultClass",row.className);
		row.className = this.rowSelectedClass;
	},
	onMouseOut:function(/* DomEvent */ e){
		var row=dojo.html.getParentByType(e.target,"tr");
		row.className = row.getAttribute("defaultClass");
		row.removeAttribute("defaultClass");
	},
    onStartProcessing:function() {
		//	summary
		//	empty function for the user to attach code to, 
        //  fired by render,parseData, and onHeaderClick
    },
    onStopProcessing:function() {
		//	summary
		//	empty function for the user to attach code to, 
        //  fired by render,parseData, and onHeaderClick
    },
	onHeaderClick:function(/* DomEvent */ e){
		//	summary
		//	Main handler function for each header column click.
		var oldIndex=this.sortIndex;
		var oldDirection=this.sortDirection;
		var source=e.target;
		var row=dojo.html.getParentByType(source,"tr");
		var cellTag="td";
		if(row.getElementsByTagName(cellTag).length==0) cellTag="th";

		var headers=row.getElementsByTagName(cellTag);
		var header=dojo.html.getParentByType(source,cellTag);



		for(var i=0; i<headers.length; i++){
			if(headers[i]==header){
				if(i!=oldIndex){
                    if (this.filterLast) {
     					 if (confirm('Reset filter?')) this.filter.value.value = "";
						 else return;
					 }
                    //	new col.
					this.sortIndex=i;
					this.sortDirection=0;
				}else{
					this.sortDirection=(oldDirection==0)?1:0;
				}
			}else{
				//	reset the header class.
				headers[i].className=this.headerClass;
			}
		}
        // reset the paging
		this.pageStart=0;
        this.render();
	},

	onMove:function(/* int */ pageStart){
		this.pageStart = pageStart;
        if (this.navigator.navPageSelect.value==-1) {
			//this.maxRows=this.data.count+1;
            this.pageStart=0; 
			this.showAll = true;
        }
		else {
			this.showAll = false;
		}
        this.render();
	},

	onFilter:function(/* DomEvent */ e){

		this.render();

	},

	onFilter:function(/* DomEvent */ e){

		this.render();

	},

	onFilterClear:function(/* DomEvent */ e){

		this.filter.value.value = "";
        this.render();
	},

    onFilterTypeColumn:function(/* DomEvent */ e){
        this.currentType = this.filter.filterType.value;
    },

    onFilterColumn:function(/* DomEvent */ e){
        // it is better to not rebuild filterTypes
        // if datatype is the same as old.

        var dataType = this.columns[this.filter.columns.value].dataType.toLowerCase();
		this.filter.filterType.options.length = 0;

		var i = 0;

       

//* text based filters: Starts with, Ends with, Equals, Not Equals 
//* number based filters: Greater than or equal to, Less than or equal to, Equals, Not Equals 



		if (dataType == "string") {
			      this.filter.filterType[i] = new Option(translate(this.language, "starts"),"starts");i++;
            this.filter.filterType[i] = new Option(translate(this.language, "contains"),"contains");i++;
            this.filter.filterType[i] = new Option(translate(this.language, "endswith"),"endswith");i++;
		}
        if (dataType == "number" || dataType == "date") {
            this.filter.filterType[i] = new Option(translate(this.language, "greaterequal"), "greaterequal");i++;
      //      this.filter.filterType[i] = new Option("Greater than", "greater");i++;
            this.filter.filterType[i] = new Option(translate(this.language, "lessequal"), "lessequal");i++;
      //      this.filter.filterType[i] = new Option("Less than", "less");i++;
        }

        this.filter.filterType[i] = new Option( translate(this.language, "equal") ,"equal");i++;
        this.filter.filterType[i] = new Option( translate(this.language, "notequal") ,"notequal");i++;

        if(this.currentType != null) {
            this.filter.filterType.value= this.currentType;
        }
	},

	postCreate:function(){
		// 	summary
		//	overridden from HtmlWidget, initializes and renders the widget.
		var p=this.domNode.parentNode;
		var next = dojo.dom.nextElement(this.domNode);
		p.removeChild(this.domNode);

		var isDiv = this.domNode.tagName == "DIV";
		var div;

		if (isDiv)
			div = this.domNode;
		else
			div = document.createElement("div");


		var table;
		if(isDiv){
			table = document.createElement("table");
			table.border = this.border;
			table.cellPadding = this.cellPadding;
			table.cellSpacing = this.cellSpacing;
		}
		else
			table = this.domNode;

		table.width="100%";
		this.table = table;

		div.appendChild(table);
		this.domNode=div;

		if(this.containerClass.length>0){
	       div.className=this.containerClass;
		}

		// create the title
		this.titleNode=document.createElement("div");
		dojo.dom.insertBefore(this.titleNode,table);
        
		
       

        // Create the filter
		this.filter=document.createElement("div");
		dojo.dom.insertBefore(this.filter,table);
		this.filter.appendChild(this.createFilter());
		this.filter.style.display = "none";
        
		// Create the information string 
        this.infoNode=document.createElement("div");
		dojo.dom.insertBefore(this.infoNode,this.filter);

		// create the navigator
		this.navigator = dojo.widget.createWidget("Navigator");
		this.navigator.widgetId = dojo.dom.getUniqueId();
		this.navigator.domNode.id = this.navigator.widgetId;
		dojo.event.connect(this.navigator, "onMove", this, "onMove");
		dojo.dom.insertAfter(this.navigator.domNode,table);


		var tbody=table.getElementsByTagName("tbody")[0];
		var thead=table.getElementsByTagName("thead")[0];
		var tfoot=table.getElementsByTagName("tfoot")[0];
		if (thead==null){
			thead = document.createElement("thead");
			if (tbody!=null)
				dojo.dom.insertBefore(thead,tbody);
			else
				table.appendChild(thead);
		}

		if (tbody==null){
			tbody = document.createElement("tbody");
			dojo.dom.insertAfter(tbody,thead);
		}

		if (tfoot==null){
			tfoot = document.createElement("tfoot");
			dojo.dom.insertAfter(tfoot,tbody);
		}

		if(this.theadClass.length>0){
			thead.className=this.theadClass;
		}

		if (next == null)
			p.appendChild(div);
		else
			dojo.dom.insertBefore(div,next);

		//	disable selections
		// !!!!!!!!!!!!!!!!!!!!!!!!!!
		// Commented By Alex - as we want to Allow users to do selection
		//dojo.html.disableSelection(table);

		// set the table properties
		if (this.tableClass.length>0) {
			table.className=this.tableClass;
		}

		//	parse the columns.
		this.parseColumnsFromTable(thead);



		//	parse the tbody element and re-render it.
//		if (this.tbodyClass.length>0) {
//			tbody.className=this.tbodyClass;
//		}



		this.parseDataFromTable(tbody);

		this.attachHeaders();

		this.hide();

		this.render();
	},

	attachHeaders:function(){
		//	attach header handlers.
		var thead=this.domNode.getElementsByTagName("thead")[0];
		var header="td";
		if(thead.getElementsByTagName(header).length==0) header="th";
		var headers=thead.getElementsByTagName(header);
		for(var i=0; i<headers.length; i++){
			if(!this.columns[i].noSort){
				dojo.event.connect(headers[i], "onclick", this, "onHeaderClick");
			}
			if(this.sortIndex==i){
				if(this.sortDirection==0){
					headers[i].className=this.headerSortDownClass;
				}else{
					headers[i].className=this.headerSortUpClass;
				}
			}
		}
	},


    //!!!!please do not delete comments!!!!!
    onToggleFilterPanelClick:function(/* DomEvent */ e){
        if(this.filterCollapsed)
        {
//            dojo.html.removeClass(this.filter.toggleFilterPanel, "collapsed");
            this.filter.filterTable.style.display = "inline";
            this.filterCollapsed = false;
//            this.filter.showFilterButton2.style.display="inline";
//            this.filter.showFilterButton1.style.display="none";
        }
        else
        {
//            dojo.html.addClass(this.filter.toggleFilterPanel, "collapsed");
            this.filter.filterTable.style.display = "none";
            this.filterCollapsed = true;
//            this.filter.showFilterButton1.style.display="inline";
//            this.filter.showFilterButton2.style.display="none";
        }
    },

    filterValueKeyPressed:function(e){
        if(e.keyCode == 13)
        {
           this.onFilter();
        }

    },

    createFilter:function(){
        // create element and use it as panel
        var filterPanel = document.createElement("div");

        this.filter.toggleFilterPanel = document.createElement("div");
        dojo.html.addClass(this.filter.toggleFilterPanel, "dojoTogglePanel");
        //dojo.html.addClass(this.filter.toggleFilterPanel, "dojoTogglePanel.collapsed");
        //this.filter.toggleFilterPanel.name="meganame";
        dojo.event.connect(this.filter.toggleFilterPanel, "onclick", this, "onToggleFilterPanelClick");
        filterPanel.appendChild(this.filter.toggleFilterPanel);

        //!!!!please do not delete comments!!!!!
        /*
        this.filter.showFilterButton1 = document.createElement("img");
        this.filter.showFilterButton1.src = "./js/tacos/widget/templates/images/triangle.gif";
        dojo.html.addClass(this.filter.showFilterButton, "dojoNavButton");
        this.filter.toggleFilterPanel.appendChild(this.filter.showFilterButton1);

        this.filter.showFilterButton2 = document.createElement("img");
        this.filter.showFilterButton2.src = "./js/tacos/widget/templates/images/opentriangle.gif";
        dojo.html.addClass(this.filter.showFilterButton, "dojoNavButton");

        this.filter.showFilterButton2.style.display="none";
        this.filter.toggleFilterPanel.appendChild(this.filter.showFilterButton2);

        var toggleFilterText = document.createElement("spin");
        toggleFilterText.innerHTML = "<strong>Filter</strong>";
        this.filter.toggleFilterPanel.appendChild(toggleFilterText);
        */

        this.filter.showFilterButton = document.createElement("img");
        this.filter.showFilterButton.src = dojo.uri.dojoUri("../tacos/widget/templates/images/filter.gif");
        dojo.html.addClass(this.filter.showFilterButton, "dojoTogglePanel");
        this.filter.toggleFilterPanel.appendChild(this.filter.showFilterButton);

        this.filter.filterTable = document.createElement("table");
		this.filter.filterTable.border = 0;
		this.filter.filterTable.cellPadding=5;
		this.filter.filterTable.cellSpacing=0;

        filterPanel.appendChild(this.filter.filterTable);

        var body = document.createElement("tbody");
		this.filter.filterTable.appendChild(body);

		var tr = document.createElement("tr");
		body.appendChild(tr);
        /*
		var td = document.createElement("td");
		td.innerHTML = "<strong>Filter</strong>";
		tr.appendChild(td);
        */
		td = document.createElement("td");
		this.filter.columns = document.createElement("select");
		dojo.event.connect(this.filter.columns, "onchange", this, "onFilterColumn");
		td.appendChild(this.filter.columns);
		tr.appendChild(td);

		td = document.createElement("td");
		this.filter.filterType = document.createElement("select");
        dojo.event.connect(this.filter.filterType, "onchange", this, "onFilterTypeColumn");
		this.filter.filterType[0] = new Option("Equals","equal");
		td.appendChild(this.filter.filterType);
		tr.appendChild(td);

		td = document.createElement("td");
		this.filter.value = document.createElement("input");
		this.filter.value.type = "text";
        dojo.event.connect(this.filter.value, "onkeypress", this, "filterValueKeyPressed");
		td.appendChild(this.filter.value);
		tr.appendChild(td);

		td = document.createElement("td");
		var ok = document.createElement("input");
		ok.type = "button";
		ok.value = translate(this.language,"filter");
		dojo.event.connect(ok, "onclick", this, "onFilter");
		td.appendChild(ok);
		tr.appendChild(td);

		td = document.createElement("td");
		var clear = document.createElement("input");
		clear.type = "button";
		clear.value = translate(this.language,"clear");
		dojo.event.connect(clear, "onclick", this, "onFilterClear");
		td.appendChild(clear);
		tr.appendChild(td);

        return filterPanel;  /* return table dom element */
	},

	
	createFilterColumns:function(){
			// clear columns
			this.filter.columns.options.length = 0;
			var hiddenColumns = 0;
			
			for(i=0;i<this.columns.length;i++){
				if (this.columns[i].hidden == false){
					var name, value;
					name = this.columns[i].getLabel();
					value = i;
					this.filter.columns[i-hiddenColumns] = new Option(name,value);
				}
				else
					hiddenColumns++;
			}
			
			if(this.columns.length>0){
				this.filter.columns.selectedIndex=this.filterDefault-hiddenColumns;
				this.onFilterColumn();
			}
	
	}
	
	
});


