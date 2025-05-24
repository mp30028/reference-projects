import * as React from 'react';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import getLogger from 'lib/logger';


function Home() {
	const MODULE = 'Home'; 
	const LOGGER = getLogger(MODULE);		
 
	LOGGER.debug("Test from Home");
	
	return (
		<Tabs>
			<Tab eventKey={1} title="Main App">
				Hello World<br/>
			</Tab>
			<Tab eventKey={2} title="About">
				This is a test tab<br/>
			</Tab>	
		</Tabs>
	);
};

export default Home;

