function WSNHistory(myZCloudID, myZCloudKey) {
	var thiz = this;
	thiz.uid = myZCloudID;
	thiz.key = myZCloudKey;
	thiz.saddr = "zhiyun360.com:8080";
	
	thiz.setIdKey = function(uid, key) {
		thiz.uid = uid;
		thiz.key = key;		
	};
	
	thiz.initZCloud = function(uid, key) {
		thiz.uid = uid;
		thiz.key = key;		
	};
	
	thiz.setServerAddr = function(addr) {
		thiz.saddr = addr;
	};
	thiz.query = function(channel, start, end, interval, cb) {
		var url,q;
		if (arguments.length == 1) {
		    url = "http://"+thiz.saddr+"/v2/feeds/"+thiz.uid;
		   cb = arguments[0];
		} else {
		   q = "start="+start+"&end="+end+"&interval="+interval;
		   url = "http://"+thiz.saddr+"/v2/feeds/"+thiz.uid+"/datastreams/"+channel+"?"+q;
		}
		//console.log(url);
		$.ajax({
			type: "GET",
	    	url: url,
	    	dataType:"json",
	    	beforeSend: function( xhr ) {
				xhr.setRequestHeader("X-ApiKey", thiz.key);
			},
	    	success: function(data) {
	        	cb(data);
	    	}
    	});
	};
	thiz.queryLast = function(channel, cb, pa) {
		var url = "http://"+thiz.saddr+"/v2/feeds/"+thiz.uid+"/datastreams/"+channel;
		if (pa) {
			url += "?"+pa;
		}
		$.ajax({
			type: "GET",
	    	url: url,
	    	dataType:"json",
	    	beforeSend: function( xhr ) {
				xhr.setRequestHeader("X-ApiKey", thiz.key);
			},
	    	success: function(data) {
	        	cb(data);
	    	}
    	});
	};
	thiz.queryLast1H = function(channel, cb) {
		thiz.queryLast(channel, cb, "duration=1hour");
	};
	thiz.queryLast6H = function(channel, cb) {
		thiz.queryLast(channel, cb, "duration=6hours");
	};
	thiz.queryLast12H = function(channel, cb) {
		thiz.queryLast(channel, cb, "duration=12hours");
	};
	thiz.queryLast1D = function(channel, cb) {
		thiz.queryLast(channel, cb, "duration=1day");
	};
	thiz.queryLast5D = function(channel, cb) {
		thiz.queryLast(channel, cb, "duration=5days");
	};
	thiz.queryLast14D = function(channel, cb) {
		thiz.queryLast(channel, cb, "duration=14days");
	};
	thiz.queryLast1M = function(channel, cb) {
		thiz.queryLast(channel, cb, "duration=1month");
	};
	thiz.queryLast3M = function(channel, cb) {
		thiz.queryLast(channel, cb, "duration=3months");
	};
	thiz.queryLast6M = function(channel, cb) {
		thiz.queryLast(channel, cb, "duration=6months");
	};
	thiz.queryLast1Y = function(channel, cb) {
		thiz.queryLast(channel, cb, "duration=1year");
	};
}

