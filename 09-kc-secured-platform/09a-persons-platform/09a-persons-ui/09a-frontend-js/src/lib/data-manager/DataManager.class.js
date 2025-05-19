import getLogger from 'lib/logger';
import DataService from './data-service'
import EventService, {EventMessageHandler, MergeHandlers} from './event-service';
import {DATA_STATUS} from './DataStatus';
import { cloneDeep } from 'lodash';

export default class DataManager{
	MODULE = "DataManager";
	LOGGER = getLogger(this.MODULE);
		
	constructor(dataUrl, eventsUrl, getData, setData){
		this.dataUrl = (dataUrl ? dataUrl : null);
		this.eventsUrl = (eventsUrl ? eventsUrl : null);
		this.getData = getData;
		this.setData = setData;		
		this.dataStateHelper = new DataStateHelper(getData, setData);
		this.commitHelper = new CommitHelper(dataUrl,getData, setData);
	};
	
	fetchSeedData = () =>{
		const MARKER = "[fetchSeedData]";
		this.LOGGER.debug(MARKER,"triggered",{dataUrl: this.dataUrl});
		this.dataUrl && (()=>{			
			const dsConfigurer = new DataServiceConfigurer(this.dataUrl, this.setData);
			dsConfigurer.setup();
			this.LOGGER.debug(MARKER,"setup completed");
		})();
	};
	
	startEventListener = () => {
		const MARKER = "[startEventListener]";
		this.LOGGER.debug(MARKER,"triggered");						
		this.eventsUrl && (()=>{			
			this.listener = new EventServiceConfigurer(this.eventsUrl, this.setData, this.getData, new MergeHandlers());
			this.listener.setup();
			this.LOGGER.debug(MARKER,"setup completed");
		})();				
	};
	
	change = (changedItem) =>{
		const MARKER = "[change]";
		this.LOGGER.debug(MARKER,"triggered", {changedItem});
		this.dataStateHelper.change(changedItem);				
	};
	
	cancel = () => {
		const MARKER = "[cancel]";
		this.LOGGER.debug(MARKER, "triggered");
		this.dataStateHelper.cancel();
	};

	addNew = (emptyItem) =>{
		const MARKER = "[addNew]";
		this.LOGGER.debug(MARKER, "triggered", {emptyItem});
		this.dataStateHelper.addNew(emptyItem);
	};
	
	delete = (itemToDelete) =>{
		const MARKER = "[deleteItem]";
		this.LOGGER.debug(MARKER, "triggered", {itemToDelete});
		this.dataStateHelper.delete(itemToDelete);
	};
	
	commit = () => {
		const MARKER = "[commit]";
		this.LOGGER.debug(MARKER, "triggered");
		this.commitHelper.commit();
	}
		
};

class CommitHelper{
	MODULE = "CommitHelper";
	LOGGER = getLogger(this.MODULE);

	constructor(url, getData, setData){
		this.getData = getData;
		this.setData = setData;
		this.dataService = new DataService(url);
	};

	commit = () => {
		const MARKER = "[commit]";
		this.LOGGER.debug(MARKER, "triggered");
		const pendingItems = this.getData()
			.filter(item => item.status !== DATA_STATUS.FETCHED)
			.map(item => cloneDeep(item));
		const refreshedItems = this.getData()
			.filter(item => item.status !== DATA_STATUS.DELETED)
//			.filter(item => item.status !== DATA_STATUS.INSERTED)
			.map(item => {
				if (item.status !== DATA_STATUS.FETCHED){
					item.status = DATA_STATUS.COMMITTED;
				}
				return item;
			});
		this.setData(refreshedItems);
		pendingItems.forEach(currentItem => {
				switch(currentItem.status){
					case DATA_STATUS.UPDATED:
						this.dataService.update(currentItem.data);
						break;
					case DATA_STATUS.INSERTED:
						this.dataService.addNew(currentItem.data);
						break;											
					case DATA_STATUS.DELETED:
						this.dataService.deleteById(currentItem.data.id);
						break;
					default:
						//do nothing
						break;
				};			
		});
	}
}


class DataStateHelper {
	MODULE = "DataStateHelper";
	LOGGER = getLogger(this.MODULE);
	
	constructor(getData, setData){
		this.getData = getData;
		this.setData = setData;
	};
	
	
	takeBackup = (currentItem, newItem) =>{
		const MARKER = "[takeBackup]";
		this.LOGGER.debug(MARKER,  "triggered", {currentItem}, {newItem});
		if(newItem.status === DATA_STATUS.INSERTED){
			// do nothing
		}else{
			if(currentItem.backup) {
				newItem.backup = currentItem.backup;
			}else{
				newItem.backup = currentItem.data;
			};			
		};

	};
	
	restoreBackup = (item) => {
		const MARKER = "[restoreBackup]";	
		this.LOGGER.debug(MARKER,{item});
		if(item.status === DATA_STATUS.INSERTED){
			item.data = null;
		}else{
			(item.backup) && (()=>{				
				item.data = item.backup;
				item.backup = undefined;
			})();			
		};
		item.status = DATA_STATUS.FETCHED;
	};
		
	change = (changedItem) =>{
		const MARKER = "[change]";	
		this.LOGGER.debug(MARKER,{changedItem});				
		const dataWithChange = this.getData().map((item)=>{
			if (item.data.id === changedItem.data.id){
				this.takeBackup(item, changedItem);
				return changedItem;
			}else{
				return item;
			};
		});
		this.setData(dataWithChange);
		this.LOGGER.debug(MARKER, {changedItem}, {dataWithChange});
	};
	
	cancel = () => {
		const MARKER = "[cancel]";
		this.LOGGER.debug(MARKER, "triggered");
		const resetData = this.getData()
			.map((item)=>{
				this.restoreBackup(item);
				this.LOGGER.debug(MARKER, {restoredItem: item})
				return item;
			})
			.filter(item => item.data !== null);
		this.LOGGER.debug(MARKER, {resetData});
		this.setData(resetData);
	};

	addNew = (emptyItem) =>{
		const MARKER = "[addNew]";
		//const DUMMY = {status: DATA_STATUS.INSERTED , data: {id: generateTempId() , moniker: "", firstname: "", lastname: "", gender: "MALE", otherNames: []}};
		this.LOGGER.debug(MARKER, "triggered", {emptyItem});
		this.setData([...this.getData(), emptyItem]);
	};
	
	delete = (itemToDelete) =>{
		const MARKER = "[delete]";
		this.LOGGER.debug(MARKER, "triggered");
		if (itemToDelete.status === DATA_STATUS.INSERTED){
			const dataRemoved = this.getData().filter((item) => item.data.id !== itemToDelete.data.id);
			this.setData(dataRemoved);	
		}else{
			const dataWithDelete = this.getData().map((item)=>{
				if (item.data.id === itemToDelete.data.id){
					this.takeBackup(item, itemToDelete);
					return itemToDelete;
				}else{
					return item;
				};
			});
			this.setData(dataWithDelete);
		};
	};		
}


class DataServiceConfigurer {
	
	constructor(url, setData){
		this.url = url;
		this.setData = setData;
	};
	
	setup = () => {
		const dataService = new DataService(this.url);
		dataService
			.fetchAll()
			.then(
				(fetchedData) => this.setData(
					fetchedData.map(fetchedItem => {return {status: DATA_STATUS.FETCHED, data : fetchedItem};})
				)
			);		
	};
};

class EventServiceConfigurer {
	
	constructor(url, setData, getData, mergeHandlers){
		this.url = url;
		this.setData = setData;
		this.getData = getData;
		this.mergeHandlers = mergeHandlers;
	};
	
	setup = () => {
		const eventMessageHandler = new EventMessageHandler(this.getData, this.setData, this.mergeHandlers);		
		(new EventService(this.url)).listen(eventMessageHandler.messageHandler);				
	};
};

