import React, {useState, useEffect, useRef} from 'react';
import getLogger from 'lib/logger';
import ListGroup from 'react-bootstrap/ListGroup';
import Stack from 'react-bootstrap/Stack';
import {cloneDeep} from 'lodash';
import Text, {Dropdown} from 'lib/inline-edit';
import {PlusSquare, DashSquare} from 'react-bootstrap-icons';
import Button from 'react-bootstrap/Button';
import DataManager, {DATA_STATUS} from 'lib/data-manager';
import generateObjectId from 'lib/object-id';
import getApiConfigs from 'configs';

const Persons = () =>{
	const MODULE = "Persons";
	const LOGGER = getLogger(MODULE);
	const apiConfigs = getApiConfigs();
	const API_URL = apiConfigs.protocol + "://" + apiConfigs.host + ":" + apiConfigs.port + "/" + apiConfigs.path;
	const EVENTS_URL = API_URL + "/events";	
	const [dataManager, setDataManager] = useState(null);	
	const currentPersons = useRef([]);
	const [persons, setPersons] = useState([]); //persons = array of {status, data, backup, hold} objects
	const [isCommitTriggered, setIsCommitTriggered] = useState(false);

	LOGGER.debug("triggered", {apiConfigs});
	
	useEffect(()=>{
		const MARKER = "[InitialisationHook]";
		
		const getPersons = () => currentPersons.current;
		
		const setCurrentPersons = ((p) => {
			setPersons(p);
			currentPersons.current = p;
		});
			
		LOGGER.debug(MARKER, "triggered");		
		setDataManager(new DataManager(API_URL, EVENTS_URL, getPersons, setCurrentPersons/*, tempIdCleanup*/));
	},[LOGGER, EVENTS_URL, API_URL]);

	useEffect(()=>{
		const MARKER = "[personsHook]";	
		LOGGER.debug(MARKER,{persons});
		currentPersons.current = persons;	
	},[LOGGER, persons]);

	useEffect(()=>{
		const MARKER = "[dataManagerHook]";	
		LOGGER.debug(MARKER,"triggered");		
		if (dataManager){
			dataManager.fetchSeedData();
			dataManager.startEventListener();
		}
	},[LOGGER, dataManager]);

	useEffect(()=>{
		const MARKER = "[commitTriggerHook]";	
		LOGGER.debug(MARKER,"triggered");
		if (isCommitTriggered){
			dataManager.commit();
			setIsCommitTriggered(false);
		};
	},[LOGGER, isCommitTriggered, dataManager])

	
	const generateId = () =>{
		const MARKER = "[generateId]";
		const generatedId  = generateObjectId(); 			
		LOGGER.debug(MARKER,"triggered", {generatedId});
		return generatedId;
	};
	
	const commit = () =>{
		const MARKER = "[commit]";	
		LOGGER.debug(MARKER,"triggered");		
		setIsCommitTriggered(true);
	};
	
	return (
		<span>
			<PersonsList 
				persons={persons}
				changeHandler={dataManager&& dataManager.change}
				cancelHandler={dataManager&& dataManager.cancel}
				addNewHandler={dataManager&& dataManager.addNew}
				deleteHandler={dataManager&& dataManager.delete}
				commitHandler={commit}
				generateId={generateId}
				roles={apiConfigs&& apiConfigs.authInfo.roles}
			/>
		</span>
	);

};

export default Persons;


/********************Component: PersonsList ****************************************************/
	const PersonsList = ({persons, changeHandler, commitHandler, cancelHandler, addNewHandler, deleteHandler, generateId, roles}) => {
		const MODULE = "Persons-PersonsList";
		const LOGGER = getLogger(MODULE);
		LOGGER.debug({persons});
		
		const isButtonsEnabled = (data) => {
			const MARKER = "[isButtonsEnabled]";
			const pending = data.find((item) => item.status !== DATA_STATUS.FETCHED);
			LOGGER.debug(MARKER,{pending},{data});
			if (pending){					
				return true;
			}else{
				return false;
			}
		};
		
		const addNew = () => {
			const MARKER = "[addNew]";
			const DUMMY = {status: DATA_STATUS.INSERTED , data: {id: generateId(), moniker: "", firstname: "", lastname: "", gender: "MALE", otherNames: []}};
			LOGGER.debug(MARKER,"triggered", {DUMMY});
			addNewHandler && addNewHandler(DUMMY);			
		}

		return (
			<>	
				<ListGroup>
					{(persons.filter(item => item.status !== DATA_STATUS.DELETED)).map((p) => 
						<ListGroup.Item  key={p.data.id}>
							<EditablePerson person={p} changeHandler={changeHandler} deleteHandler={deleteHandler} generateId={generateId} roles={roles} />
						</ListGroup.Item>
					)}					
				</ListGroup>
					<div>
						<Stack direction="horizontal" gap={2}>
							<AddNewButton displayLabel="Add Person" itemName="addNewPerson" suffix="person" addNewHandler={addNew} />
							<CommitCancelButtons commitHandler={commitHandler} cancelHandler={cancelHandler} isEnabled={isButtonsEnabled(persons)} />
						</Stack>
					</div>							
			</>		
		);		
	};


/********************Component: EditablePerson ****************************************************/
	const EditablePerson = ({person, changeHandler, deleteHandler, generateId, roles}) =>{
		const MODULE = "Persons-EditablePerson";
		const LOGGER = getLogger(MODULE);
				
		LOGGER.debug("triggered", {person, changeHandler, deleteHandler, generateId});
		
		const change = (itemName, updatedItem) => {
			const MARKER = "[save]";
			LOGGER.debug(MARKER, {updatedItem});
			changeHandler && (()=>{
				const clonedPerson = cloneDeep(person);
				clonedPerson.data[itemName] = updatedItem;
				if (clonedPerson.status === DATA_STATUS.FETCHED){
					clonedPerson.status = DATA_STATUS.UPDATED;
				};
				changeHandler(clonedPerson);
			})();
			LOGGER.debug(MARKER, {itemName},{updatedItem});
		};
		
		const deleteItem = (itemToDelete) =>{
			const MARKER = "[deleteItem]";
			LOGGER.debug(MARKER, {itemToDelete});
			if (itemToDelete.status === DATA_STATUS.INSERTED){
				// do nothing
			}else{
				itemToDelete.status = DATA_STATUS.DELETED;
			}			
			deleteHandler && deleteHandler(itemToDelete);			
		};
		
		const hasRole = (rolesToCheck) =>{
			return roles.some(role => rolesToCheck.includes(role))
		};
		
		return (					 
				<Stack>
					<Stack direction="horizontal" gap={4}>
						<span style={{width: "300px"}}>
							<pre>ID: {person.data.id} - {person.status}</pre>
						</span>
						<span>
							<DeleteButton deleteHandler={{handler: deleteItem, item: person}} displayLabel={"Delete Person"} itemName={"person"} suffix={person.data.id} isEnabled={hasRole(["PERSONS_PLATFORM_ADMIN"])}/>
						</span>
					</Stack>
					<span>
						<Text 
							id="moniker" 
							label="Moniker" 
							value={person.data.moniker}  
							changeHandler={change}
						/>
					</span>
					<span>
						<Text 
							id="firstname" 
							label="Firstname" 
							value={person.data.firstname}  
							changeHandler={change}
						/>
					</span>
					<span>
						<Text 
							id="lastname" 
							label="Lastname" 
							value={person.data.lastname}  
							changeHandler={change}
						/>
					</span>
					<span>
						<Dropdown 
							id="gender" 
							label="Gender" 
							value={person.data.gender}  
							changeHandler={change}
							dropdownList={[
  								{label: 'MALE', value: 'MALE'},
  								{label: 'FEMALE', value: 'FEMALE'}
  							]}							
						/>
					</span>
					<span>
						<Text 
							id="nationality" 
							label="Nationality" 
							value={person.data.nationality}  
							changeHandler={change}
						/>
					</span>
					<span>
						<Text 
							id="domicile" 
							label="Domicile" 
							value={person.data.domicile}  
							changeHandler={change}
						/>
					</span>										
					
					<span>
						<OtherNames 							 
							label="Other Names:" 
							otherNames={person.data.otherNames}
							id={person.data.id} 
							changeHandler={change}
							generateId={generateId}
						/> 
					</span>
				</Stack>
		);
	};


/********************Component: OtherNames *********************************************************/
	const OtherNames = ({label, id, otherNames, changeHandler, cancelHandler, generateId}) =>{
		const MODULE = "Persons-OtherNames";
		const LOGGER = getLogger(MODULE);	
		
		LOGGER.debug({otherNames});
		
		const change = (changedItem) => {
			const MARKER = "[change]";
			LOGGER.debug(MARKER, {updatedItem: changedItem});						
			changeHandler && (()=>{				
				const refreshedOtherNames = otherNames.map(on =>{
					if (on.id === changedItem.id){
						return changedItem;
					}else{
						return on;
					};
				});
				changeHandler("otherNames", refreshedOtherNames);				
			})();
			LOGGER.debug(MARKER,{updatedItem: changedItem});
		};
		
		const addNew = () =>{
			const MARKER = "[addNew]";			
			const newOtherName = {id: generateId() , value: "", nameType: "MIDDLE_NAME"}			
			LOGGER.debug(MARKER, "triggered", {newOtherName});
			const refreshedOtherNames = [...otherNames, newOtherName];
			changeHandler && changeHandler("otherNames", refreshedOtherNames);
		};

		const deleteItem = (id) =>{
			const MARKER = "[deleteItem]";
			const refreshedOtherNames = otherNames.filter((on) => on.id !== id);
			LOGGER.debug(MARKER, "triggered", {refreshedOtherNames});
			changeHandler && changeHandler("otherNames", refreshedOtherNames);
		};

		return (
			<Stack gap="2">
				<span>{label} 						
					{otherNames.map(on =>
							<OtherNameItem key={on.id} otherName={on} changeHandler={change} deleteHandler={deleteItem} />
					)}						
				</span>
				<span style={{width: "525px", textAlign: "right"}}>
					<AddNewSubitemButton itemName="AddNewOtherName" suffix={id} addNewHandler={addNew} />
				</span>
			</Stack>		
		);
	};
	
	
/********************Component: OtherNameItem *********************************************************/
	const OtherNameItem = ({otherName, changeHandler, deleteHandler, cancelHandler}) => {
		const MODULE = "Persons-OtherNameItem";
		const LOGGER = getLogger(MODULE);	
		LOGGER.debug("triggered", {otherName});
		
		const change = (itemName, updatedItem) => {
			const MARKER = "[save]";			
			LOGGER.debug(MARKER, {itemName}, {updatedItem});
			changeHandler && ( () =>{
				const clonedOtherName = cloneDeep(otherName);
				clonedOtherName[itemName] = updatedItem;
				changeHandler(clonedOtherName);				
			})();
			LOGGER.debug(MARKER, {isSaveHandlerProvided: (changeHandler)},{itemName},{updatedItem});			
		}

		const deleteItem = () =>{
			const MARKER = "[deleteItem]";						
			LOGGER.debug(MARKER, "triggered");
			deleteHandler && deleteHandler(otherName.id);
			LOGGER.debug(MARKER, {isDeleteHandler: (deleteHandler)},{otherName});
		};
		
		return(			
			<Stack direction="horizontal" gap={3}>		
				<span>
					<Text 
						id="value" 
						label="-" 
						value={otherName.value}  
						changeHandler={change}
						labelStyle={{width: "100px", textAlign: 'center'}}
						valueStyle={{width: "200px"}}						
					/>				
				</span>
				<span>
					<Dropdown 
						id="nameType" 
						label="" 
						value={otherName.nameType}  
						changeHandler={change}
						dropdownList={[
							{label: 'MIDDLE NAME', value: 'MIDDLE_NAME'},
							{label: 'NICKNAME', value: 'NICKNAME'},
							{label: 'ALIAS', value: 'ALIAS'}
						]}
						labelStyle={{width: "1px"}}
						valueStyle={{width: "200px"}}							
					/>
				</span>
				<span>
					<DeleteSubitemButton itemName="DeleteOtherName" suffix={otherName.id} deleteHandler={deleteItem}  />
				</span>				
			</Stack>						
		);
		
	};
	
	
/********************Component: SaveCancelButtons *********************************************************/
	const CommitCancelButtons = ({commitHandler, cancelHandler, isEnabled}) => {
		const MODULE = "Persons-SaveCancelButtons";
		const LOGGER = getLogger(MODULE);
		LOGGER.debug({isEnabled});
		
		const commit = (event) =>{
			const MARKER = "[save]"
			LOGGER.debug(MARKER, "triggered");
			commitHandler && commitHandler();
			event.stopPropagation();
		};
		
		return (
			<>
				{isEnabled &&
					<Stack direction="horizontal" gap={1}>
						<span >		
								 
							<Button variant="warning" id='commitButton' name= 'commitButton' onClick={commit}>Commit</Button>
						</span>
						<span>
							<Button variant="danger" id='cancelButton' name= 'cancelButton' onClick={cancelHandler}>Cancel</Button>
						</span>
					</Stack>
				}
			</>
		);
	};
	
	
/********************Component: AddNewItemButton *********************************************************/
	const AddNewSubitemButton = ({itemName, suffix, addNewHandler}) => {
		const MODULE = "Persons-AddNewSubitemButton";
		const LOGGER = getLogger(MODULE);
		const defaultIconStyle = {color: 'green', cursor: 'default'};
		const onHoverIconStyle = {color: 'blue', cursor: 'pointer'};
		const [iconStyle, setIconStyle] = useState(defaultIconStyle);  
		
		const hoverStart = (event) => {
			setIconStyle(onHoverIconStyle);
		};
		
		const hoverEnd = (event) => {
			setIconStyle(defaultIconStyle);
		};
		
		const addNew = (event) =>{
			const MARKER = "[addNew]"
			LOGGER.debug(MARKER, "triggered");
			addNewHandler && addNewHandler();
			event.stopPropagation();
		};
		
		return (
			<>				
				<PlusSquare style={iconStyle} 
					size="25"
					id={'addNewSubitemButton_' + itemName + '_' + suffix} 
					name={'addNewSubitemButton_' + itemName + '_' + suffix}  
					onClick={addNew}
					onMouseOver={hoverStart}
					onMouseOut={hoverEnd}
				/>
			</>
		);
	}


/********************Component: DeleteButton *********************************************************/
	const DeleteSubitemButton = ({itemName, suffix, deleteHandler}) => {
		const MODULE = "Persons-DeleteSubitemButton";
		const LOGGER = getLogger(MODULE);
		const defaultIconStyle = {color: 'red', cursor: 'default'};
		const onHoverIconStyle = {color: 'blue', cursor: 'pointer'};
		const [iconStyle, setIconStyle] = useState(defaultIconStyle); 		
		
		const hoverStart = (event) => {
			setIconStyle(onHoverIconStyle);
		};
		
		const hoverEnd = (event) => {
			setIconStyle(defaultIconStyle);
		};
		
		const deleteSubitem = () => {
			const MARKER = "[onDelete]";
			LOGGER.debug(MARKER,"triggered");
			deleteHandler && deleteHandler();
		};
		
		return (
			<DashSquare style={iconStyle} 
				size="25"
				id={'deleteSubitemButton_' + itemName + '_' + suffix} 
				name={'deleteSubitemButton_' + itemName + '_' + suffix}  
				onClick={deleteSubitem}
				onMouseOver={hoverStart}
				onMouseOut={hoverEnd}				
			/>
		);
	}
	
	
/********************Component: AddNewButton *********************************************************/
	const AddNewButton = ({displayLabel, itemName, suffix, addNewHandler}) => {
		const MODULE = "Persons-AddNewButton";
		const LOGGER = getLogger(MODULE);
		LOGGER.debug("triggered");
		
		const addNew = (event) =>{
			const MARKER = "[addNew]"
			LOGGER.debug(MARKER, "triggered");
			addNewHandler && addNewHandler();
			event.stopPropagation();
		};
		
		return (
			<span >				 
				<Button variant="success" 
						id={'addNewButton_' + itemName + '_' + suffix}
						name={'addNewButton_' + itemName + '_' + suffix} 
						onClick={addNew}
				>
					{displayLabel}
				</Button>
			</span>				
		);
	};
	
	
	
/********************Component: DeleteButton *********************************************************/
	const DeleteButton = ({displayLabel, itemName, suffix, deleteHandler, isEnabled}) => {
		const MODULE = "Persons-DeleteButton";
		const LOGGER = getLogger(MODULE);
		LOGGER.debug("triggered");
		
		const deleteItem = (event) =>{
			const MARKER = "[deleteItem]"
			LOGGER.debug(MARKER, "triggered");
			deleteHandler && 
			deleteHandler.handler && 
			deleteHandler.item && 
			deleteHandler.handler(deleteHandler.item);
			event.stopPropagation();
		};
		
		return (
			<span >
				{isEnabled &&
					<Button variant="outline-danger" 
							id={'deleteButton_' + itemName + '_' + suffix}
							name={'deleteButton_' + itemName + '_' + suffix} 
							onClick={deleteItem}
					>
						{displayLabel}
					</Button>
				}
			</span>				
		);
	};