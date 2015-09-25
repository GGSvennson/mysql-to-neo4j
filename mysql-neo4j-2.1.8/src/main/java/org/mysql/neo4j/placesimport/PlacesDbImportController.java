package org.mysql.neo4j.placesimport;

import org.mysql.neo4j.entity.*;
import org.mysql.neo4j.repository.AddressNeoRepository;
import org.mysql.neo4j.repository.CityNeoRepository;
import org.mysql.neo4j.repository.CountryNeoRepository;
import org.mysql.neo4j.repository.RootRepository;
import org.mysql.neo4j.domain.AddressNeo;
import org.mysql.neo4j.domain.CityNeo;
import org.mysql.neo4j.domain.CountryNeo;
import org.mysql.neo4j.domain.RoleHasAddress;
import org.mysql.neo4j.domain.RoleHasCity;
import org.mysql.neo4j.domain.RoleIsRoot;
import org.mysql.neo4j.domain.RootNeo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
//import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    
    //@Autowired
    //Neo4jTemplate template;
    
	@Autowired
    PlacesDbApiClient client;
	
	public PlacesDbImportController() {
    	
    }

	@Transactional
    public RootNeo createGraphDb() {
		RootNeo root = doImportRoot();
        return root;
	}
	
	@Transactional
	public RootNeo importRoot() {
		return doImportRoot();
	}
	
	private RootNeo doImportRoot() {
		logger.debug("Importing root");
		
		RootNeo root = new RootNeo("1", "Root");
		root.addLabel("_Root");
		//root.addLabel("Root");
		System.out.println("root created " + root);
				
		List<Country> data = client.readAllCountries();
		if (data.isEmpty()) throw new RuntimeException("Data for Root not found.");
		
		Map<CountryNeo, RoleIsRoot> roles = relateCountriesToRoot(root, data);
		//template.save(root);
		Set<CountryNeo> set = roles.keySet();
		for(Iterator<CountryNeo> it = set.iterator(); it.hasNext();) {
			root.isRoot(it.next());
		}
		rootRepo.save(root);
		
		/*
		for(RoleIsRoot role: roles){
        	template.save(role);
        }
		*/
		
		return root;
	}

	private Map<CountryNeo, RoleIsRoot> relateCountriesToRoot(RootNeo root, List<Country> data) {
		Map<CountryNeo, RoleIsRoot> roles = new HashMap<CountryNeo, RoleIsRoot>();
		for (Country country : data) {
			CountryNeo countryNeo = doImportCountryNeo(country);
            RoleIsRoot role = root.isRoot(countryNeo, "IS_ROOT_OF");
            System.out.println("RoleIsRoot: " + role);
            roles.put(countryNeo, role);
        }
		
		return roles;
	}

    @Transactional
    public CountryNeo importCountryNeo(Country country) {
        return doImportCountryNeo(country);
    }

    private CountryNeo doImportCountryNeo(Country country) {
        logger.debug("Importing countryNeo");

        CountryNeo countryNeo = new CountryNeo(generateIndex(country.getCountryId()), country.getCountry());
        countryNeo.addLabel("_Country");
        //countryNeo.addLabel("Country");
        System.out.println("new country: " + countryNeo);

        List<City> data = client.readAllCitiesByCountry(country);
        if (data.isEmpty()) throw new RuntimeException("Data for Country not found.");
        
        Map<CityNeo, RoleHasCity> roles = relateCitiesToCountry(countryNeo, data);
        Set<CityNeo> set = roles.keySet();
        for(Iterator<CityNeo> it = set.iterator(); it.hasNext();) {
        	countryNeo.hasCity(it.next());
        }
        countryRepo.save(countryNeo);
        
        /*
        template.save(countryNeo);
        for(RoleHasCity role: roles){
        	template.save(role);
        }
        */
        
        return countryNeo;
    }

    private Map<CityNeo, RoleHasCity> relateCitiesToCountry(CountryNeo countryNeo, List<City> data) {
    	Map<CityNeo, RoleHasCity> roles = new HashMap<CityNeo, RoleHasCity>();
    	for (City city : data) {
            CityNeo cityNeo = doImportCityNeo(city);
            RoleHasCity role = countryNeo.hasCity(cityNeo, "IS_CITY_FROM");
            System.out.println("RoleHasCity: " + role);
            roles.put(cityNeo, role);
        }
    	
    	return roles;
    }

    @Transactional
    public CityNeo importCityNeo(City city) {
        return doImportCityNeo(city);
    }

    private CityNeo doImportCityNeo(City city) {
    	logger.debug("Importing cityNeo");
    	
    	CityNeo cityNeo = new CityNeo(generateIndex(city.getCityId()), city.getCity());
    	cityNeo.addLabel("_City");
    	//cityNeo.addLabel("City");
    	System.out.println("new city: " + cityNeo);

        List<Address> data = client.readAllAddressesByCity(city);
        if (data.isEmpty()) throw new RuntimeException("Data for City not found.");
        
        Map<AddressNeo, RoleHasAddress> roles = relateAddressesToCity(cityNeo, data);
    	Set<AddressNeo> set = roles.keySet();
    	for(Iterator<AddressNeo> it = set.iterator(); it.hasNext();) {
    		cityNeo.hasAddress(it.next());
    	}
    	cityRepo.save(cityNeo);
    	
        /*
        template.save(cityNeo);
        for(RoleHasAddress role: roles){
    		template.save(role);
    	}
        */
        
        return cityNeo;
    }

    private Map<AddressNeo, RoleHasAddress> relateAddressesToCity(CityNeo cityNeo, List<Address> data) {
    	Map<AddressNeo, RoleHasAddress> roles = new HashMap<AddressNeo, RoleHasAddress>();
    	for(Address address: data) {
    		AddressNeo addressNeo = doImportAddressNeo(address);
    		RoleHasAddress role = cityNeo.hasAddress(addressNeo, "IS_ADDRESS_IN");
    		System.out.println("RoleHasAddress: " + role);
    		roles.put(addressNeo, role);
    	}
    	
    	return roles;
    }

    @Transactional
    public AddressNeo importAddressNeo(Address address) {
        return doImportAddressNeo(address);
    }

    private AddressNeo doImportAddressNeo(Address address) {
    	logger.debug("Importing addressNeo");

    	if (address == null) throw new RuntimeException("Address not found.");
    	
		AddressNeo addressNeo = new AddressNeo(generateIndex(address.getAddressId()), address.getAddress());
		addressNeo.addLabel("_Address");
		//addressNeo.addLabel("Address");
		System.out.println("new address: " + addressNeo);
		
		addressNeo.setPostalCode(address.getPostalCode());
		addressRepo.save(addressNeo);
		
		/*
		template.save(addressNeo);
		*/
		
    	return addressNeo;
    }
    
    private String generateIndex(int index) {
    	return String.valueOf(index);
    }
}
