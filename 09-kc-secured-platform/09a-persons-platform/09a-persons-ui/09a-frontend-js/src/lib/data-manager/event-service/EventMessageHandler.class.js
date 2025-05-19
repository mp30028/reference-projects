import getLogger from 'lib/logger';
import BasicMergeHandlers from './BasicMergeHandlers.class';
import {uniqBy} from 'lodash';
export default class EventMessageHandler{
	MODULE = "EventMessageHandler";
	LOGGER = getLogger(this.MODULE);
	
	constructor(getCurrentItems, setCurrentItems, mergeHandlers){
		this.getCurrentItems = getCurrentItems;
		this.setCurrentItems = setCurrentItems;
		if (mergeHandlers){
			this.mergeHandlers = mergeHandlers;
		}else{
			this.mergeHandlers = new BasicMergeHandlers();;
		}
	}
	
	messageHandler = (event) => {	
		const MARKER = "[messageHandler]";				
		const eventData = JSON.parse(event.data);
		const eventDataBody = eventData.body;
		const operationType = eventData.operationType.toString().toUpperCase();
		this.LOGGER.debug(MARKER, {event}, {operationType}, {eventDataBody});
		var refreshedItems = null;
		switch(operationType){
			case 'REPLACE':
				this.LOGGER.debug(MARKER,'Doing REPLACE');
				refreshedItems = this.mergeHandlers.mergeUpdate(this.getCurrentItems(), eventDataBody);
				break;					
			case 'UPDATE':
				this.LOGGER.debug(MARKER,'Doing UPDATE');
				refreshedItems = this.mergeHandlers.mergeUpdate(this.getCurrentItems(), eventDataBody);
				break;
			case 'DELETE':
				const id = eventData.documentKey;
				this.LOGGER.debug(MARKER,'Doing DELETE', {id});
				refreshedItems = this.mergeHandlers.mergeDelete(this.getCurrentItems(), id);
				break;
			case 'INSERT':
				this.LOGGER.debug(MARKER,'Doing INSERT');
				const currentItems = this.getCurrentItems();
				refreshedItems = this.mergeHandlers.mergeInsert(currentItems, eventDataBody);
				break;
			default:
				this.LOGGER.debug(MARKER, '<default>', 'do-nothing');
				refreshedItems = this.getCurrentItems();
				break;
		}
		const dedupedRefreshedItems = uniqBy(refreshedItems, 'data.id');
		this.LOGGER.debug(MARKER,{refreshedItems}, {dedupedRefreshedItems});
		this.setCurrentItems(dedupedRefreshedItems);
	};	
}
