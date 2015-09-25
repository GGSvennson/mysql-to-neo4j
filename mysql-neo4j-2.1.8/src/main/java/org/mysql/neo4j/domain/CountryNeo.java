package org.mysql.neo4j.domain;

import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;

@NodeEntity
public class CountryNeo extends Place {
	
	@RelatedTo(type = "HAS_CITY", direction = Direction.OUTGOING)
	Set<CityNeo> cities;
	
	@Fetch @RelatedToVia(type = "IS_ROOT", direction = Direction.INCOMING)
    Iterable<RoleIsRoot> roles;

    public CountryNeo() {
    }

    public CountryNeo(String id, String name) {
        super(id, name);
    }

    public Set<CityNeo> getCities() {
        return cities;
    }
    
    public void hasCity(CityNeo city) {
    	if(cities == null)
    		cities = new HashSet<CityNeo>();
    	cities.add(city);
    }
    
    public RoleHasCity hasCity(CityNeo city, String roleName) {
        return new RoleHasCity(this, city, roleName);
    }
    
    public Collection<RoleIsRoot> getRoles() {
		Iterable<RoleIsRoot> allRoles = roles;
		return allRoles == null ? Collections.<RoleIsRoot>emptyList() : IteratorUtil.asCollection(allRoles);
    }
}
