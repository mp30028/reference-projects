import getLogger from 'lib/logger';

export default class BasicMergeHandlers {
	MODULE = "BasicMergeHandlers";
	LOGGER = getLogger(this.MODULE);
	
	mergeUpdate = (currentItems, updatedItem) => {
			const MARKER = "[mergeUpdate]";
			const refreshedItems = currentItems.map((currentItem) => {
				if (currentItem.id === updatedItem.id){
					this.LOGGER.debug(MARKER,"currentItem replaced by updatedItem", {currentItem}, {updatedItem});						
					return updatedItem;
				}else{						
					return currentItem;
				}					
			});
			return refreshedItems;
	}
	
	mergeDelete = (currentItems, itemId) => {
		const MARKER = "[mergeDelete]";
		const refreshedItems = currentItems.filter((item) => item.id !== itemId );
		this.LOGGER.debug(MARKER,{countOfItemsDeleted: currentItems.length - refreshedItems.length});
		return refreshedItems;
	}
		
	mergeInsert = (currentItems, newRecord) => {
		const MARKER = "[mergeInsert]";
		const refreshedItems = [...currentItems , newRecord];
		this.LOGGER.debug(MARKER,{countOfItemsInserted: refreshedItems.length - currentItems.length});
		return refreshedItems;
	}
						
}