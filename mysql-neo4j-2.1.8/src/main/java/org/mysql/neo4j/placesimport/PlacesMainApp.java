package org.mysql.neo4j.placesimport;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PlacesMainApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

		GraphDatabaseService graphDatabase = ctx.getBean("graphDatabaseService", GraphDatabaseService.class);
		PlacesDbImportController importer = ctx.getBean("importer", PlacesDbImportController.class);
		PlacesDbQueries queries = ctx.getBean("queries", PlacesDbQueries.class);
		
		PlacesImporter placesImporter = new PlacesImporter(graphDatabase, importer, queries);
		
		//placesImporter.createDatabase();
		//placesImporter.queryDbGetRoot();
		placesImporter.queryDbGetAllCountries();
		placesImporter.queryDbGetCountryById("1");
		placesImporter.queryDbGetCountryByName("Spain");

		ctx.close();
	}
}
