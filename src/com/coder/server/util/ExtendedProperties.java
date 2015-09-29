package com.coder.server.util;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Properties;

public class ExtendedProperties extends Properties {
	private static final long serialVersionUID = -3871753522871012670L;

	private static final String START_CONST = "{";
	private static final String END_CONST = "}";

	private static final int MAX_SUBST_DEPTH = 5;

	public ExtendedProperties() {
		super();
	}
	
	public ExtendedProperties(Properties defaults) {
		super(defaults);
	}

	@Override
	public String getProperty(String key) {
		return getResolvedProperty(this, key, 0);
	}
	
	public String resolveString(String str){
		return resolveString(this, str, 0);
	}
	
	private String getUnresolvedProperty(String key){
		return super.getProperty(key);
	}

	private static String getResolvedProperty(ExtendedProperties properties, String key, int level) {
		String value = properties.getUnresolvedProperty(key);
		if (value != null && level < MAX_SUBST_DEPTH) {
			int beginIndex = 0;
			int startName = 0;	//pointing to the start of an embedded key reference
			int endName = 0;	//pointing to the end of an embedded key reference
			while ((startName = value.indexOf(START_CONST, beginIndex)) != -1) {
				endName = value.indexOf(END_CONST, startName);
				if (endName == -1) {
					return value;
				}
				String embededKeyName = value.substring(startName+1, endName);
				String embededKeyValue = getResolvedProperty(properties, embededKeyName, level+1);
				if (embededKeyValue == null) {
					embededKeyValue = "[" + embededKeyName + "=NULL]";
				}
				String newValue = value.substring(0, startName);
				newValue += embededKeyValue;
				beginIndex = newValue.length();
				newValue += value.substring(endName+1);
				value = newValue;
			}
		}
		return value;
	}
	
	private static String resolveString(ExtendedProperties properties, String str, int level){
		ExtendedProperties a = new ExtendedProperties();
		a.setProperty(str, str);
		ExtendedProperties b = ExtendedProperties.combine(properties, a);
		return getResolvedProperty(b, str , 0);
	}
	
	public String[] getAllKeysMatching(String regexp){
		LinkedList<String> matchingKey = new LinkedList<String>();
		Enumeration<Object> keys = this.keys();
		while(keys.hasMoreElements()){
			String key = (String)keys.nextElement();
			if(key.matches(regexp)){
				matchingKey.add(key);				
			}
		}
		return (String[])matchingKey.toArray();
	}
	
	public static ExtendedProperties combine(ExtendedProperties... properties){
		ExtendedProperties combined = new ExtendedProperties();
		for(ExtendedProperties prop : properties){
			if(prop != null){
				Enumeration<Object> keys = prop.keys();
				while(keys.hasMoreElements()){
					String key = (String)keys.nextElement();
					String value = prop.getUnresolvedProperty(key);
					if(combined.getProperty(key) != null){
						System.out.println("XProperties::combine: overwriting property with key \"" + key + "\" value before: \"" + combined.getProperty(key) + "\" after: \"" + value + "\"");
					}
					combined.setProperty(key, value);
				}
			}
		}
		return combined;
	}
	
}