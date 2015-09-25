package org.mysql.neo4j.repository;

import java.util.Collection;
import java.util.Collections;

import org.mysql.neo4j.domain.CountryNeo;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface CountryNeoRepository extends GraphRepository<CountryNeo> {
	
	@Query("MATCH (n:CountryNeo) RETURN COLLECT(n.name) AS countries")
	CountryNeoData findAllCountryNames();
	
	@QueryResult
    public class CountryNeoData {
        Collection<String> countries = Collections.emptyList();
        
        public Collection<String> getCountries() {
        	return countries;
        }
    }
}
