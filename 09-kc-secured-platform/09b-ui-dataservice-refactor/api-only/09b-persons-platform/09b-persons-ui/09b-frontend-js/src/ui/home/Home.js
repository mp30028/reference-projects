import * as React from 'react';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import getLogger from 'lib/logger';
//import Demo from 'ui/home/demo';
//import {Fetch, Update} from 'ui/home/demo/api-integration';
import MainApp from 'ui/home/main-app';
import About from 'ui/home/about';
//import {BootstrapInlineEditDemo} from 'ui/home/demo/general-stuff';
//import PersonsUnplugged from 'ui/home/demo/persons-unplugged';


function Home() {
	const MODULE = 'Home'; 
	const LOGGER = getLogger(MODULE);		
 
	
	LOGGER.debug("Test from Home");
	
	return (
		<Tabs>
			<Tab eventKey={1} title="Main App">
				<MainApp /><br/>
			</Tab>
			<Tab eventKey={2} title="About">
				<About /><br/>
			</Tab>
			
{/*			
			<Tab eventKey={2} title="Demos, examples etc">
				<Tabs>
					<Tab eventKey={1} title="Persons Unplugged">
						<PersonsUnplugged />
					</Tab>
					<Tab eventKey={2} title="Bootstrap Edit Demo">
						<BootstrapInlineEditDemo />
					</Tab>
					<Tab eventKey={2} title="Sayings">
						<Demo />
					</Tab>

					
					
					<Tab eventKey={3} title="API Integation">
						<Tabs>
							<Tab eventKey={1} title="Fetch [GET]">
								<Fetch />
							</Tab>
							<Tab eventKey={2} title="Update [PUT]">
								<Update />
							</Tab>							
						</Tabs>
					</Tab>
					
				</Tabs>
			</Tab>
*/}			
		</Tabs>
	);
};

export default Home;

