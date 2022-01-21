package org.sid.web;

import org.springframework.core.convert.converter.Converter;

public class StringToDouble implements Converter<String, Double> {

@Override
public Double convert(String str) {
 
try {
	if(str.equals("")) {
		str = null;
	 }
	return Double.parseDouble(str);
	
  }  catch(NumberFormatException ex) {
      
	  str = null;
	  return Double.parseDouble(str);
  }
}
}