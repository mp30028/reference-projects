import * as React from 'react';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import getLogger from 'lib/logger';
import Persons from '../persons';

function Home() {
	const MODULE = 'Home'; 
	const LOGGER = getLogger(MODULE);		
 
	LOGGER.debug("Test from Home");
	
	return (
		<Tabs>
			<Tab eventKey={1} title="Hello World">
				Hello World<br/>
			</Tab>
			<Tab eventKey={2} title="Persons Data">
				<Persons />
			</Tab>	
		</Tabs>
	);
};

export default Home;

