package org.mysql.neo4j.placesimport;

import org.mysql.neo4j.repository.AddressNeoRepository;
import org.mysql.neo4j.repository.CityNeoRepository;
import org.mysql.neo4j.repository.CountryNeoRepository;
import org.mysql.neo4j.repository.RootRepository;
import org.mysql.neo4j.domain.AddressNeo;
import org.mysql.neo4j.domain.CityNeo;
import org.mysql.neo4j.domain.CountryNeo;
import org.mysql.neo4j.domain.RootNeo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller("importer")
public class PlacesDbImportController {

    private static final Logger logger = LoggerFactory.getLogger(PlacesDbImportController.class);
    
    @Autowired
    @Lazy
    AddressNeoRepository addressRepo;
	
    @Autowired
    @Lazy
    CityNeoRepository cityRepo;
	
    @Autowired
    @Lazy
    CountryNeoRepository countryRepo;
    
    @Autowired
    @Lazy
    RootRepository rootRepo;
    
	public PlacesDbImportController() {
    	
    }

	@Transactional
    public RootNeo createGraphDb() {
		RootNeo root = new RootNeo("1", "Root");
		root.addLabel("_Root");
		
		AddressNeo address = new AddressNeo("1", "391 Callao Drive");
		address.setPostalCode("34021");
		address.addLabel("_Address");
		addressRepo.save(address);
		
		CityNeo city = new CityNeo("1", "Toulouse");
		city.addLabel("_City");
		city.hasAddress(address);
		cityRepo.save(city);
		
		CountryNeo country = new CountryNeo("1", "France");
		country.addLabel("_Country");
		country.hasCity(city);
		countryRepo.save(country);
		
		root.isRoot(country);
		
		address = new AddressNeo("2", "939 Probolinggo Loop");
		address.setPostalCode("4166");
		address.addLabel("_Address");
		addressRepo.save(address);
		
		city = new CityNeo("2", "A Corua (La Corua)");
		city.addLabel("_City");
		city.hasAddress(address);
		cityRepo.save(city);
		
		country = new CountryNeo("2", "Spain");
		country.addLabel("_Country");
		country.hasCity(city);
		countryRepo.save(country);
		
		root.isRoot(country);
		
		rootRepo.save(root);
		
        return root;
	}
}
