import getLogger from 'lib/logger';
import {DATA_STATUS} from '../DataStatus';

export default class MergeHandlers {
	MODULE = "MergeHandlers";
	LOGGER = getLogger(this.MODULE);
	
	wrapData = (status, data) => {
		return ({status: status, data : data});
	};
			
	mergeUpdate = (currentWrappedItems, updatedItem) => {
			const MARKER = "[mergeUpdate]";
			const refreshedItems = currentWrappedItems.map((currentItem) => {
				this.LOGGER.debug(MARKER,{updatedItem});								
					if (currentItem.data.id === updatedItem.id){
						if ((currentItem.status === DATA_STATUS.COMMITTED) || (currentItem.status === DATA_STATUS.FETCHED)){
							const wrappedValue = this.wrapData(DATA_STATUS.FETCHED, updatedItem);
							this.LOGGER.debug(MARKER,"currentItem replaced by updatedItem", {wrappedValue},{currentItem}, {updatedItem});
							return wrappedValue;
						}else{
							this.LOGGER.debug(MARKER,"currentItem is in a dirty state so will not be replaced by updatedItem", {currentItem}, {updatedItem});
							currentItem.hold = updatedItem;
							return currentItem;
						}
					}else{					
						return currentItem;
					}
			});
			this.LOGGER.debug(MARKER,{refreshedItems});
			return refreshedItems;
	};
	
	mergeDelete = (currentItems, itemId) => {
		const MARKER = "[mergeDelete]";
		const refreshedItems = currentItems.filter((item) => item.data.id !== itemId );
		this.LOGGER.debug(MARKER,{countOfItemsDeleted: currentItems.length - refreshedItems.length});
		return refreshedItems;
	}
		
	mergeInsert = (currentItems, newRecord) => {
		const MARKER = "[mergeInsert]";
		var refreshedItems = this.mergeUpdate(currentItems, newRecord);
		this.LOGGER.debug(MARKER, {refreshedItems});
		const foundInRefreshedItems = refreshedItems.find(item => item.id === newRecord.id);
		if (!foundInRefreshedItems){
			refreshedItems = [...refreshedItems, this.wrapData(DATA_STATUS.FETCHED, newRecord)]
		}
		return refreshedItems;
	}
						
}