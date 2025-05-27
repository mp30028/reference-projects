import React, {useState, useEffect} from 'react';
import getLogger from 'lib/logger';
import getApiConfigs from 'configs';
import {DataService} from 'lib/data-manager';

const Persons = () => {
	const MODULE = "Persons";
	const LOGGER = getLogger(MODULE);
	const apiConfigs = getApiConfigs();
	const API_URL = apiConfigs.protocol + "://" + apiConfigs.host + ":" + apiConfigs.port + "/" + apiConfigs.path;
	const [personsData, setPersonsData]=useState(null);
	
	
	LOGGER.debug({API_URL});
	
	useEffect(()=>{
		const dataService = new DataService(API_URL);
		dataService
			.fetchAll()
			.then(
				(fetchedData) => setPersonsData(fetchedData)
			);		
	},[API_URL])
		
		
	return (
		<span>
			<pre>
				{JSON.stringify(personsData,null,4)}
			</pre>
		</span>
	)
}

export default Persons;