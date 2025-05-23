import React, {useState, useEffect} from "react";
import getLogger from 'lib/logger';
import getApiConfigs from 'configs';
import {pick} from 'lodash';

const About = () => {
	const MODULE = "About";
	const LOGGER = getLogger(MODULE);
	const [configs, setConfigs] = useState(null);  
//	const unsortedApiConfigs = getApiConfigs();
//	const apiConfigs = pick(unsortedApiConfigs,Object.keys(unsortedApiConfigs).sort());
	

	useEffect(()=>{
		const fetchedConfigs = getApiConfigs();
		if (fetchedConfigs === ''){ //ensures login triggered	
			setConfigs({state: "Something Wrong. Configs unavailable"});
		}else{
		  	const pickedConfigs = pick(fetchedConfigs,
		  		[
					"authInfo.id", 
		  			"authInfo.username", 
		  			"authInfo.fullname",
		  			"authInfo.email",
		  			"authInfo.roles",
		  			"authInfo.authenticationTime",
		  			"authInfo.issuer",
		  			"authInfo.tokenIssuedAt",
		  			"protocol", 
		  			"host", 
		  			"port", 
		  			"path"
		  		]
		  	);	
			setConfigs(pickedConfigs);
		}	
	},[])	
	
	
	
	LOGGER.debug("triggered", {configs});
	return(
		<span>
			<pre>
				{JSON.stringify(configs,null,4)}
			</pre>		
		</span>
	)
}

export default About;