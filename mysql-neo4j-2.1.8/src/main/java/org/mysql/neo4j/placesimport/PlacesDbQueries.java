package org.mysql.neo4j.placesimport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.mysql.neo4j.domain.CountryNeo;
import org.mysql.neo4j.repository.*;
import org.mysql.neo4j.repository.CountryNeoRepository.CountryNeoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlacesDbQueries {

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
    
    public PlacesDbQueries() {
    	
    }
    
    public List<CountryNeo> findAllCountries() {
    	Result<CountryNeo> result = countryRepo.findAll();
    	List<CountryNeo> list = new ArrayList<CountryNeo>();
    	for(Iterator<CountryNeo> it = result.iterator(); it.hasNext(); ) {
    		list.add(it.next());
    	}
    	return list;
    }
    
    public List<String> findAllCountryNames() {
    	CountryNeoData names = countryRepo.findAllCountryNames();
    	Collection<String> collection = names.getCountries();
    	
    	List<String> list = new ArrayList<String>();
    	for(Iterator<String> it = collection.iterator(); it.hasNext(); ) {
    		list.add(it.next());
    	}
    	return list;
    }
}
