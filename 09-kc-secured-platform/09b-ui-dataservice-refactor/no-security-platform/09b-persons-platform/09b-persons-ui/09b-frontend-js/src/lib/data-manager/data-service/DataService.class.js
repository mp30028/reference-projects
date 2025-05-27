import getLogger from 'lib/logger';


//class DataServiceError extends Error {
//
//  constructor(message='Something went wrong', data='', code='', method='', url='') {
//    super();
//    this.message = message;
//    this.data = data;
//    this.code = code;
//    this.method = method;
//    this.url = url;
//  };
//  
//   dataServiceError = {
//      type: 'Error',
//      message: this.message || 'Something went wrong',
//      data: this.data || '',
//      code: this.code || '',
//      method: this.method || '',
//      url: this.url || '' 
//    };
//
//}

export default class DataService{
	MODULE = "DataService";	
			
	constructor(baseUrl) {
		this.baseUrl = baseUrl;
		this.LOGGER = getLogger(this.MODULE);
	};

	invokeRequest = async (httpMethod, fetchUrl, body) => {
		const MARKER = "[invokeRequest]";
		const msToUtcString = (ms) => {const dt = new Date(ms); return (dt.toUTCString());}
		const startTime = Date.now();
				
		this.LOGGER.info(MARKER, "requestStarted",{httpMethod}, {fetchUrl},{body}, {startTime: msToUtcString(startTime)});						
		const response = await fetch(
			fetchUrl, 
			{	method: httpMethod,
				body: (body ? JSON.stringify(body) : null),				
				headers: {
					'Content-Type': 'application/json;charset=UTF-8',
					'Accept': 'application/json, text/plain'
				}			
			}
		);
		const finishTime = Date.now();
		this.LOGGER.info(MARKER,"requestFinished",{httpMethod}, {fetchUrl}, {finishTime: msToUtcString(finishTime)}, {timeTakenMs: finishTime - startTime},{response});			
		if (!response.ok){			
			this.LOGGER.error(MARKER,"could not complete the " + httpMethod +" request to " + fetchUrl +" due to an error", {response_status: response.status} );
			throw (new Error("could not complete the " + httpMethod +" request to " + fetchUrl +" due to an error. returned-response-code= " + response.status) );
		}
		const responseJson = await response.json();	
		return responseJson;	
	};
			
	fetchAll = async () => {
		const fetchUrl = this.baseUrl;
		const response = await this.invokeRequest('GET', fetchUrl);
		return response;
	};

	update = async (data) => {
		const fetchUrl = this.baseUrl + "/" + data.id;
		const response = await this.invokeRequest('PUT', fetchUrl, data);
		return response;		
	};

	addNew = async (data) => {
		const fetchUrl = this.baseUrl;
		const response = await this.invokeRequest('POST', fetchUrl, data);
		return response;
	};
	
	deleteById = async (id) => {
		const MARKER = "[deleteById]";
		const msToUtcString = (ms) => {const dt = new Date(ms); return (dt.toUTCString());}
		const startTime = Date.now();
		const fetchUrl = this.baseUrl + "/" + id;
		var response;
		//const response = await this.invokeRequest('DELETE', fetchUrl);
//		try{
			response = await fetch(
				fetchUrl, 
				{	method: 'DELETE',
					body: (null),				
					headers: {
						'Content-Type': 'application/json;charset=UTF-8',
						'Accept': 'application/json, text/plain'
					}			
				}
			);
		const finishTime = Date.now();
		this.LOGGER.info(MARKER,"requestFinished",{status:"REQUEST-FAILED"}, {httpMethod: "DELETE"}, {fetchUrl}, {finishTime: msToUtcString(finishTime)}, {timeTakenMs: finishTime - startTime},{response});
		if (!response.ok){			
			this.LOGGER.error(MARKER,"could not commit the delete due to an error", {response_status: response.status} );
			throw (new Error("could not commit the delete due to an error. returned-response-code= " + response.status) );
		}
		return {deleteStatus: true};			
	};
}