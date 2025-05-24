class ApiConfigs{
		
	constructor(apiConfigsUrl){
		this.fetchApiConfigs(apiConfigsUrl);
	}
				

	fetchApiConfigs(apiConfigsUrl) {
   		const xhr = new XMLHttpRequest();
   		xhr.open('GET', apiConfigsUrl, false);
   		xhr.send(null);
   		if (xhr.status === 200) {
			   try{
					this.apiConfigs = JSON.parse(xhr.responseText);	   
			   }catch(err) {
				   this.apiConfigs = '';
			   }
      		
   		} else {
      		throw new Error('Request failed: ' + xhr.statusText);
   		}
	}
		
	getConfigs = () =>{
		return this.apiConfigs;
	}

}

const apiConfigsUrl = process.env.REACT_APP_ENV_API_CONFIGS_URL;
const apiConfigs = (new ApiConfigs(apiConfigsUrl)).getConfigs();

const getApiConfigs = () =>{
	return apiConfigs;
};

export default getApiConfigs;
