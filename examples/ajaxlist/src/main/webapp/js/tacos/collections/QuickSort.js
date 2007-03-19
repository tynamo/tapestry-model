
dojo.provide("tacos.collections.QuickSort");
dojo.require("dojo.collections.Collections");
dojo.require("dojo.collections.Dictionary");


tacos.collections.QuickSort = function(arr){

	
	var items = [];
	var indexes = new dojo.collections.Dictionary();
	this.field; // used to stored the field name
	this.ascending = true; // main index order
    this.useSlowFiltering = false;
    this

    if (arr) items = items.concat(arr);
	
	this.length = items.length;
	this.count = items.length;

	this.add = function(obj, pos){
		items.push(obj);
		this.length = items.length;
		this.count = items.length;
	};
	this.clear = function(){
		items.splice(0, items.length);
		indexes.clear();
		this.field = null;
		this.length = 0;
		this.count = 0;
	};
	this.item = function(k){
		return items[k];
	};
	this.remove = function(obj){
		var i = this.indexOf(obj);
		if (i >=0) {
			items.splice(i,1);
		}
		this.length = items.length;
		this.count = items.length;
	};
	this.reverse = function(){
		if (this.filterIndex)
			this.filterIndex.reverse();
		else if (this.mainIndex){
			this.mainIndex.reverse();
			this.acsending = !this.ascending;
		}
		else if (this.field)	
			this.index(this.field).reverse();
		else
			items.reverse();
	};
	
	this.get = function (i){
		if (this.filterIndex)
			return items[this.filterIndex[i]];
		else if (this.mainIndex)	
			return items[this.mainIndex[i]];
		else if (this.field)	
			return items[this.indexItem(this.field,i)];
		else
			return items[i];
	};	
	
	this.index = function(field){
        return indexes.entry(field).value;
	}
	
	this.indexItem = function(field, i){
        return indexes.entry(field).value.index[i];
	}
	
	// Copyright (c) 2003,2004  Henk Reints, http://henk-reints.nl
	this.sort = function (userCompareFunction, userFilterFunction)
	{	var N = this.length, index = [new Array(N), new Array(N)], iDst = 0
		for (var i = 0; i < N; index[iDst][i] = i++);
		if (arguments.length > 0)
		{	for (var L = 1; L < N; L += L)
			{	for (var iSrc = iDst, iDst = 1 - iSrc, i2 = 0, i3 = 0; i2 < N; )
				{	for (var i1 = i2, L1 = Math.min(i1 + L, N),
						i2 = L1, L2 = Math.min(i2 + L, N); i1 < L1 && i2 < L2; )
					{	if (userCompareFunction(items[index[iSrc][i1]],items[index[iSrc][i2]]) <= 0)
							index[iDst][i3++] = index[iSrc][i1++]
						else	index[iDst][i3++] = index[iSrc][i2++]
					}
					for (; i1 < L1; index[iDst][i3++] = index[iSrc][i1++] );
					for (; i2 < L2; index[iDst][i3++] = index[iSrc][i2++] );
			}	}
			this.userCompareFunction = userCompareFunction
		} else
		{	for (var L = 1; L < N; L += L)
			{	for (var iSrc = iDst, iDst = 1 - iSrc, i2 = 0, i3 = 0; i2 < N; )
				{	for (var i1 = i2, L1 = Math.min(i1 + L, N),
						i2 = L1, L2 = Math.min(i2 + L, N); i1 < L1 && i2 < L2; )
					{	if (items[index[iSrc][i1]] <= items[index[iSrc][i2]] )
							index[iDst][i3++] = index[iSrc][i1++]
						else	index[iDst][i3++] = index[iSrc][i2++]
					}
					for (; i1 < L1; index[iDst][i3++] = index[iSrc][i1++] );
					for (; i2 < L2; index[iDst][i3++] = index[iSrc][i2++] );
			}	}
			if (this.userCompareFunction) delete this.userCompareFunction
		}
		this.mainIndex = index[iDst];
		this.acsending = true;
		this.field = null;
		if(userFilterFunction)
			this.filter(userFilterFunction);
	};
	
	// Copyright (c) 2003,2004  Henk Reints, http://henk-reints.nl
	this.sortField = function ( field, userCompareFunction)
	{	
		this.field = field;
		delete this.mainIndex;

		if (indexes.contains(field)) // index already created
			return;
	
		var N = this.length, index = [new Array(N), new Array(N)], iDst = 0
		for (var i = 0; i < N; index[iDst][i] = i++);
		if (userCompareFunction)
		{	for (var L = 1; L < N; L += L)
			{	for (var iSrc = iDst, iDst = 1 - iSrc, i2 = 0, i3 = 0; i2 < N; )
				{	for (var i1 = i2, L1 = Math.min(i1 + L, N),
						i2 = L1, L2 = Math.min(i2 + L, N); i1 < L1 && i2 < L2; )
					{	if (userCompareFunction(items[index[iSrc][i1]][field],items[index[iSrc][i2]][field]) <= 0)
							index[iDst][i3++] = index[iSrc][i1++]
						else	index[iDst][i3++] = index[iSrc][i2++]
					}
					for (; i1 < L1; index[iDst][i3++] = index[iSrc][i1++] );
					for (; i2 < L2; index[iDst][i3++] = index[iSrc][i2++] );
			}	}
		} else
		{	for (var L = 1; L < N; L += L)
			{	for (var iSrc = iDst, iDst = 1 - iSrc, i2 = 0, i3 = 0; i2 < N; )
				{	for (var i1 = i2, L1 = Math.min(i1 + L, N),
						i2 = L1, L2 = Math.min(i2 + L, N); i1 < L1 && i2 < L2; )
					{	if (items[index[iSrc][i1]][field] <= items[index[iSrc][i2]][field] )
							index[iDst][i3++] = index[iSrc][i1++]
						else	index[iDst][i3++] = index[iSrc][i2++]
					}
					for (; i1 < L1; index[iDst][i3++] = index[iSrc][i1++] );
					for (; i2 < L2; index[iDst][i3++] = index[iSrc][i2++] );
			}	}		
		}
		
		var idx =	{	field:"", 
						ascending: true, 
						index: null, 
						reverse: function(){
							this.index.reverse();
							this.ascending=!this.ascending;
						}
					};
		
		idx.field = field;
		idx.index = index[iDst];
		indexes.add(field,idx);
	};

	
	// specified column must be sorted first for quick filtering
	this.filter = function(userFilterFunction){
		delete this.filterIndex;
		// sort the array
		this.sort();
		
		var count = this.length;
		if(count==0) return;
		var filterIndex = [];
		var lower = 0;
		var upper = count - 1;
		var middle = 0;
		
		// get the middle point
		if (count > 1)
			middle = parseInt(count/2);
		if (userFilterFunction(this.get(lower)) > 0){
			this.filterIndex = filterIndex;		
			this.count = this.filterIndex.length;
			return;
		}
		if (userFilterFunction(this.get(upper)) < 0){
			this.filterIndex = filterIndex;		
			this.count = this.filterIndex.length;
			return;
		}		
		
		var i = 0;
		while (lower<upper && i<count){
			i++;
			// if we have found a match move back until find the first 
			var result = userFilterFunction(this.get(middle)[field]);
			if (result==0){
				for(j=middle;j>=lower-1;j--){
					if (j==-1 || userFilterFunction(this.get(j)) != 0){
						j++;
						for(j;j<=upper;j++){
							if (userFilterFunction(this.get(j))== 0)
								filterIndex.push(this.mainIndex[j]);
							else
								break; // exit everything has been found
						}
						this.filterIndex = filterIndex;		
						this.count = this.filterIndex.length;
						return;
					}				
				}		
			} 
			else if (result>0) { // the item is greater
				// close down the upper
				upper = middle;
				var diff = parseInt(((upper - lower) / 2),0);
				//if (diff == 0) diff=-1;
			}	
			else { // the item is less than
				lower = middle;
				var diff = parseInt(((upper - lower) / 2),0);
				if (diff == 0) diff=1;
			}
			middle = lower + diff;

		}
	};
	
	// specified column must be sorted first for quick filtering
	this.filterField = function(field, userFilterFunction){
		delete this.filterIndex;
		// sort the field
		this.sortField(field);
		if(!this.index(this.field).ascending)
			this.index(this.field).reverse();
		
		var count = this.length;
		if(count==0) return;
		var filterIndex = [];
		var lower = 0;
		var upper = count - 1;
		var middle = 0;
		
		// get the middle point
		if (count > 1)
			middle = parseInt(count/2);
		if (userFilterFunction(this.get(lower)[field]) > 0){
			this.filterIndex = filterIndex;		
			this.count = this.filterIndex.length;
			return;
		}
		if (userFilterFunction(this.get(upper)[field]) < 0){
			this.filterIndex = filterIndex;		
			this.count = this.filterIndex.length;
			return;
		}		
		
		var i = 0;
		while (lower<upper && i<count){
			i++;
			// if we have found a match move back until find the first 
			var result = userFilterFunction(this.get(middle)[field]);
			if (result==0){
				for(j=middle;j>=lower-1;j--){
					if (j==-1 || userFilterFunction(this.get(j)[field]) != 0){
						j++;
						for(j;j<=upper;j++){
							if (userFilterFunction(this.get(j)[field])== 0)
								filterIndex.push(this.indexItem(field,j));
							else
								break; // exit everything has been found
						}
						this.filterIndex = filterIndex;		
						this.count = this.filterIndex.length;
						return;
					}				
				}		
			} 
			else if (result>0) { // the item is greater
				// close down the upper
				upper = middle;
				var diff = parseInt(((upper - lower) / 2),0);
				//if (diff == 0) diff=-1;
			}	
			else { // the item is less than
				lower = middle;
				var diff = parseInt(((upper - lower) / 2),0);
				if (diff == 0) diff=1;
			}
			middle = lower + diff;

		}		
	};

    this.slowFilterField  = function(field, userFilterFunction){
	    delete this.filterIndex;


        // sort the field
		this.sortField(field);
		if(!this.index(this.field).ascending)
			this.index(this.field).reverse();

		var count = this.length;
		if(count==0) return;
		var filterIndex = [];


		var i = 0;
		var j = 0;
		while (i<count){
			var result = userFilterFunction(this.get(i)[field]);
			if (result==0){
                filterIndex.push(this.indexItem(field,i));
    			j++;
			}
            i++;
		}
		this.count = j;
        if(filterIndex.length != 0)
            this.filterIndex = filterIndex;
    };

    this.filterClear = function(){
		delete this.filterIndex;
		this.count = items.length;
	}
	
	
};

