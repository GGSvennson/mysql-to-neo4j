package org.mysql.neo4j.placesimport;

import java.util.ArrayList;
import java.util.List;

import org.mysql.neo4j.domain.CountryNeo;
import org.mysql.neo4j.domain.RootNeo;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class PlacesImporter {

	private GraphDatabaseService graphDatabase;
	private PlacesDbImportController importer;
	private PlacesDbQueries queries;
	
	public PlacesImporter(GraphDatabaseService graphDatabase,
			PlacesDbImportController importer,
			PlacesDbQueries queries) {
		this.graphDatabase = graphDatabase;
		this.importer = importer;
		this.queries = queries;
	}
	
	public RootNeo createDatabase() {
		Transaction tx = graphDatabase.beginTx();
		RootNeo root = null;
		try {
			System.out.println("Importing data...");
			final long start = System.currentTimeMillis();
	        root = importer.createGraphDb();
	        final long time = System.currentTimeMillis() - start;
	        System.out.println("Import places took " + time + " ms.");
	        tx.success();
		} finally {
			tx.close();
		}
		return root;
	}
	
	public List<CountryNeo> queryDbGetCountries() {
		Transaction tx = graphDatabase.beginTx();
		List<CountryNeo> countries = new ArrayList<CountryNeo>();
		try {
			System.out.println();System.out.println();
	        System.out.println("List of countries.-");
	        countries = queries.findAllCountries();
	        for(CountryNeo country: countries) {
	        	System.out.println(country);
	        }
	    	tx.success();
		} finally {
			tx.close();
		}
		return countries;
	}
	
	public List<String> queryDbGetCountryNames() {
		Transaction tx = graphDatabase.beginTx();
		List<String> names = new ArrayList<String>();
		try {
			System.out.println();System.out.println();
	        System.out.println("List of country names.-");
	        names = queries.findAllCountryNames();
	        for(String name: names) {
	        	System.out.println(name);
	        }
	    	tx.success();
		} finally {
			tx.close();
		}
		return names;
	}
}
