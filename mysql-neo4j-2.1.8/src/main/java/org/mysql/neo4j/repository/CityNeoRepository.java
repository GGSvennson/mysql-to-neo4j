package org.mysql.neo4j.repository;

import java.util.Collection;

import org.mysql.neo4j.domain.CityNeo;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.repository.GraphRepository; 

public interface CityNeoRepository extends GraphRepository<CityNeo> {
	
	@Query("MATCH ({country})-[:`HAS_CITY`]->(cities) WHERE  country.name={0} " +
			"RETURN COLLECT(city.name) AS 'cities'")
	CityData findCitiesOfCountry(String countryName);
	
	@QueryResult
    public class CityData {
        Collection<String> cities;
        
        public Collection<String> getCountries() {
        	return cities;
        }
    }
}
