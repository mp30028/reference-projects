import React from 'react';
import getLogger from 'lib/logger';
import Persons from './persons';

const MainApp = () => {
	const MODULE = "MainApp";
	const LOGGER = getLogger(MODULE);
	
	LOGGER.debug("Test from MainApp");
	
	return (
		<>
			<Persons/>
		</>
	)
};

export default MainApp;