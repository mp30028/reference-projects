import React, {useState, useRef, useEffect} from 'react';
import getLogger from 'lib/logger';
import Form from 'react-bootstrap/Form';
import Stack from 'react-bootstrap/Stack';


const DEFAULT_LABEL_STYLE={width: "100px"}
const DEFAULT_VALUE_STYLE={width: "200px"}

/********************Component: Dropdown *********************************************************/
export const Dropdown = ({id, label, value, changeHandler, cancelHandler, dropdownList, labelStyle, valueStyle}) => {
	const MODULE = "Dropdown";
	const LOGGER = getLogger(MODULE);
	const isSaveNeeded = useRef(false);
	const currentValueBackup = useRef(null);
	const [currentValue, setCurrentValue]= useState(value);
	
		useEffect(()=>{
			setCurrentValue(value);			
		},[value]);						
	
		const change = () => {
			const MARKER = "[save]";
			LOGGER.debug(MARKER,"triggered");
			if (changeHandler){
				LOGGER.debug(MARKER,"changeHandler is set", {value}, {currentValue});
				changeHandler(id, currentValue);
			}else{
				LOGGER.debug(MARKER,"No changeHandler set", {value}, {currentValue});
			};
		};
	
		const cancel = () => {
			const MARKER = "[cancel]";
			LOGGER.debug(MARKER,"triggered");
			if (cancelHandler){
				LOGGER.debug(MARKER,"cancelHandler is set", {value}, {currentValue});
				cancelHandler();
			}else{
				LOGGER.debug(MARKER,"No cancelHandler set", {value}, {currentValue});
			}
		};			
		
		const handlers = new EditableComponentHandlers(isSaveNeeded, currentValueBackup, currentValue, setCurrentValue, cancel, change);
			
	return(
		<>
			<Stack direction="horizontal" gap={1}>
				<span style={labelStyle ? labelStyle : DEFAULT_LABEL_STYLE}>
					<Form.Label>{label}</Form.Label>
				</span>
				<span style={valueStyle ? valueStyle : DEFAULT_VALUE_STYLE}>
				    <Form.Select 					    	
				    	value={currentValue}
						onFocus={handlers.handleOnFocus} 
						onChange={handlers.handleOnChange} 
						onKeyDown={handlers.handleOnKeyPress} 
						onBlur={handlers.handleOnBlur}							
				    >
				    	{dropdownList && 
				    		dropdownList.map((item) =>
								<option key={item.value} value={item.value}>{item.label}</option>
							)
						}							
					</Form.Select>
				</span>
			</Stack>		
		</>		
	);
};


/********************Component: EditableText *********************************************************/
	const Text = ({id, label, value, changeHandler, cancelHandler, labelStyle, valueStyle}) => {
		const MODULE = "Text";
		const LOGGER = getLogger(MODULE);
		const isSaveNeeded = useRef(false);
		const currentValueBackup = useRef(null);
		const [currentValue, setCurrentValue]= useState(value);
		
		useEffect(()=>{
			setCurrentValue(value);			
		},[value]);
		
		
			const change = () => {
				const MARKER = "[save]";
				LOGGER.debug(MARKER,"triggered");
				if (changeHandler){
					LOGGER.debug(MARKER,"changeHandler is set", {value}, {currentValue});
					changeHandler(id, currentValue);
				}else{
					LOGGER.debug(MARKER,"No changeHandler set", {value}, {currentValue});
				};
			};
		
			const cancel = () => {
				const MARKER = "[cancel]";
				LOGGER.debug(MARKER,"triggered");
				if (cancelHandler){
					LOGGER.debug(MARKER,"cancelHandler is set", {value}, {currentValue});
					cancelHandler();
				}else{
					LOGGER.debug(MARKER,"No cancelHandler set", {value}, {currentValue});
				}
			};
			
			const handlers = new EditableComponentHandlers(isSaveNeeded, currentValueBackup, currentValue, setCurrentValue, cancel, change);
		
				
		return (
			<>
				<Stack direction="horizontal" gap={1}>
					<span style={labelStyle ? labelStyle : DEFAULT_LABEL_STYLE }><Form.Label>{label}</Form.Label></span>
					<span style={valueStyle ? valueStyle : DEFAULT_VALUE_STYLE }>
							<Form.Control 
								type="text" 
								value={currentValue} 
								onFocus={handlers.handleOnFocus} 
								onChange={handlers.handleOnChange} 
								onKeyDown={handlers.handleOnKeyPress} 
								onBlur={handlers.handleOnBlur}/>
					</span>
				</Stack>		
			</>
		);
	};
	
export default Text;

	
/********************Class: EditableComponentHandlers *********************************************************/	
	class EditableComponentHandlers{
		MODULE = "EditableComponentHandlers";	
				
		constructor(isSaveNeeded, currentValueBackup, currentValue, setCurrentValue, cancel, save) {			
			this.LOGGER = getLogger(this.MODULE);
			this.currentValueBackup = currentValueBackup;
			this.currentValue = currentValue;
			this.setCurrentValue = setCurrentValue;
			this.cancel = cancel;
			this.save = save;
			this.isSaveNeeded = isSaveNeeded;
		};
			takeCurrentValueBackup = () =>{
				this.currentValueBackup.current = this.currentValue;
			};
			
			resetCurrentValueBackup = () =>{
				this.currentValueBackup.current = null;
			};
			
			getCurrentValueBackup = () => {
				return this.currentValueBackup.current;
			}
			
			setIsSaveNeeded = (b) => {
				this.isSaveNeeded.current = b;
			};
			
			getIsSaveNeeded = () => {
				return this.isSaveNeeded.current;
			}
	
			handleOnFocus = (event) => {
				const MARKER = "[handleOnFocus]";						
				this.LOGGER.debug(MARKER,{currentValue: this.currentValue});
				this.takeCurrentValueBackup();
				this.setIsSaveNeeded(true);
			};
			
			handleOnBlur = (event) => {
				const MARKER = "[handleOnBlur]";
				this.LOGGER.debug(MARKER,{currentValue: this.currentValue}, {isSaveNeeded: this.isSaveNeeded});
				if (!this.getIsSaveNeeded()){
					this.LOGGER.debug(MARKER, "Save NOT to be done so will discard any changes");
					this.setCurrentValue(this.getCurrentValueBackup());
					this.cancel();
				}else{
					this.LOGGER.debug(MARKER, "Save to be done");
					this.setIsSaveNeeded(false);
					this.save()
				}
				this.resetCurrentValueBackup();
			};
			
			handleOnKeyPress = (event) =>{
				const MARKER = "[handleOnKeyPress]";
				const keyPressed = event.keyCode;
				if(keyPressed === 13){
					this.LOGGER.debug(MARKER,"enter pressed.");
					this.setIsSaveNeeded(true);
					event.target.blur();
				}else if (keyPressed ===27){
					this.LOGGER.debug(MARKER,"escape pressed.");
					this.setIsSaveNeeded(false);
					event.target.blur();
				};
			}
			
			handleOnChange = (event) => {
				const MARKER = "[handleOnChange]";
				const inputValue = event.target.value;
				this.LOGGER.debug(MARKER,"triggered", {event});
				this.setCurrentValue(inputValue);
			}		
	}
