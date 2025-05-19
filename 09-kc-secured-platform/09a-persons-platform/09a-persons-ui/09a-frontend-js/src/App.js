import * as React from 'react';
import { Routes, Route, Outlet } from 'react-router-dom';
import Home from 'ui/home';
import getApiConfigs from 'configs';

const App = () => {	
const [loginErr, setLoginErr] = React.useState(true);
const [configs, setConfigs] = React.useState("");

React.useEffect(()=>{
	setConfigs(getApiConfigs());
	if (getApiConfigs() === ''){ //ensures login triggered	
		setLoginErr(true);
	}else{
	  	setLoginErr(false);
	}	
},[])
	return (
		<>
			{loginErr ?
				<h1>
					Access is only possible via secure gateway
				</h1>
			:
				<>
				<pre>User: {configs.authInfo.fullname}  [{configs.authInfo.username}]</pre>

				<Routes>
					<Route path="/" element={<Home />} /> 
					<Route path="*" element={<p>There's nothing here: 404!</p>} />			
				</Routes>
				<main>
					<Outlet />
				</main>
				</>			
			}


		</>
	);
};

export default App;