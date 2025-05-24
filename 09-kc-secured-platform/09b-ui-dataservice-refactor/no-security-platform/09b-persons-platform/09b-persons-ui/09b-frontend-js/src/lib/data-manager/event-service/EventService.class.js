import getLogger from 'lib/logger';

export default class EventService{
	MODULE = "EventService";
	
	static INITIAL_WAIT_SECONDS = 1;
	static MAX_WAIT_SECONDS = 64;
	
	constructor(listeningUrl) {
		this.listeningUrl = listeningUrl;
		this.LOGGER = getLogger(this.MODULE);
	}

	listen = (messageHandler) => {
		const MARKER = "[listen]";
		var retryAfter = EventService.INITIAL_WAIT_SECONDS;
		var eventSource = null;
		
		const errorHandler = () =>{
			const MARKER2 = "[errorHandler]"; 
		    retryAfter = retryAfter * 2;
		    if (retryAfter > EventService.MAX_WAIT_SECONDS) {
		        retryAfter = EventService.MAX_WAIT_SECONDS;
		    }
		    this.LOGGER.debug(MARKER, MARKER2, "(pre-setup)", {retryAfter});
			setTimeout(doSetup, (retryAfter * 1000));
			this.LOGGER.debug(MARKER, MARKER2, "(post-setup)", {retryAfter});
		};
		
		const openHandler = () =>{
			const MARKER2 = "[openHandler]";
			this.LOGGER.debug(MARKER, MARKER2,"(triggered)");
		};		
		
		const resetEventSource = () =>{
			const MARKER2 = "[resetEventSource]";
			this.LOGGER.debug(MARKER, MARKER2,"pre-reset", {eventSource})
			if (eventSource){				
				eventSource.close();
				eventSource = null;
			};
			this.LOGGER.debug(MARKER, MARKER2,"post-reset", {eventSource})			
		}
		
		const doSetup = () => {
			const MARKER2 = "[doSetup]";
			resetEventSource(eventSource);
			this.LOGGER.debug(MARKER, MARKER2, "pre-setup", {retryAfter}, {eventSourceIsNull: (eventSource ? false : true)})		
			eventSource = new EventSource(this.listeningUrl);
			eventSource.onmessage = messageHandler;
			eventSource.onopen = openHandler;
			eventSource.onerror = errorHandler;
			this.LOGGER.debug(MARKER, MARKER2,"post-setup", {retryAfter}, {eventSourceIsNull: (eventSource ? false : true)});
		};				
		doSetup();
	};	
}