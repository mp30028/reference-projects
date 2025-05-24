import log from 'loglevel';
import prefix from 'loglevel-plugin-prefix';
import {loggerConfigs as configs} from 'configs';


const formatOptions = {
	template: '[%t] [%l] [%n]',
	levelFormatter(level) {
		return level.toUpperCase();
	},
	nameFormatter(name) {
		return name || 'global';
	},
	timestampFormatter(date) {
		return date.toISOString();		
	}
}

//const DEFAULT_LEVEL = levels.WARN;

const getLevelFromConfigs = (name) =>{
	const value = configs[name];
	if(value){
		return value;
	}else{
		return null;
	}
}

const checkLevel = (name, inLevel) => {
	if (inLevel){
		return inLevel;
	}else{
		const levelLookedup = getLevelFromConfigs(name);
		if (levelLookedup){
			return levelLookedup
		}else{
			return getLevelFromConfigs("DEFAULT");
		}
	}
};

export const getLogger = (name, level) =>{
	prefix.reg(log);
	prefix.apply(log,formatOptions);	
	const logger = log.getLogger(name);
	const actualLevel = checkLevel(name, level);
	logger.setLevel(actualLevel);
	logger.rebuild();
	return logger;
};