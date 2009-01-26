var ConversationModerator = Class.create();

function notImplemented(notImplemented) {
	alert("Not yet implemented " + notImplemented);
}

ConversationModerator.prototype = {
	initialize: function(endUrl, endIfIdleUrl, secondsBeforeBecomesIdle, warnIfIdleUrl, warnSecondsBeforeEnd) {
		this.endUrl = endUrl;
		this.endIfIdleUrl = endIfIdleUrl;
		this.warnIfIdleUrl = warnIfIdleUrl;
		if (warnIfIdleUrl != null) notImplemented('warnIfIdleUrl');
		this.warnSecondsBeforeEnd = warnSecondsBeforeEnd;
		if (warnSecondsBeforeEnd != null) notImplemented('warnIfIdleUrl');
		this.checkEndId = null;
		this.warnCheckId = null;
		if (endIfIdleUrl != null) this.checkEnd(secondsBeforeBecomesIdle);
	},
        
	endIdle: function() {
		new Ajax.Request(this.endIfIdleUrl, {
			method: 'get',
			evalJSON:true,
			onSuccess: this.handleCheckEnd.bind(this)
		});
	},

	handleCheckEnd: function(transport) {
		var newTimeout = transport.responseJSON.newTimeout;
		if (newTimeout > 0) this.checkEnd(newTimeout);
		// else alert("Conversation has ended");
	},
	
	checkEnd: function(timeout) {
		if (this.checkEndId != null) clearTimeout(this.checkEndId);
		this.checkEndId = setTimeout(this.endIdle.bind(this), timeout * 1000);
	}
}

Event.observe(window, 'unload', function() {
	/*
	new Ajax.Request(this.endUrl, {
		method: 'get'
	});
	*/

	// alert("ending conversation");
});