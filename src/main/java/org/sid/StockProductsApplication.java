package org.sid;

import org.sid.dao.UserRepository;
import org.sid.entities.User;
import org.sid.sec.SecurityConfig;
import org.sid.web.StringToDouble;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class StockProductsApplication {
  
	public static void main(String[] args) {
		
		
		 ApplicationContext context = SpringApplication.run(StockProductsApplication.class, args);
		
		ConversionService conversionService = DefaultConversionService.getSharedInstance();
	    ConverterRegistry converters = (ConverterRegistry) conversionService;
	    converters.addConverter(new StringToDouble());
	    
//	    SecurityConfig sc = new SecurityConfig(); 
//	    
//	    PasswordEncoder encoder = sc.encoder();
//	    
//	    UserRepository userRep = context.getBean(UserRepository.class);	
	    /*
	    userRep.save(new User("ahmed" , "ahmed@g.com", encoder.encode("01234")));
	    userRep.save(new User("hassan" , "ahmed@g.com", encoder.encode("01234")));
	    userRep.save(new User("rachid" , "ahmed@g.com", encoder.encode("01234")));
	    */
	    
		 //EntreeRepository erep = context.getBean(EntreeRepository.class);		
		/*
		arep.save(new Produit("Test 00" , 700, 13));
		
		arep.save(new Produit("ord hp 442" , 2000, 50));
		arep.save(new Produit("imp 12" , 800, 24));
		*/
		//arep.findAll().forEach(p -> System.out.println(p.getDesignation()));
		
	}

}
