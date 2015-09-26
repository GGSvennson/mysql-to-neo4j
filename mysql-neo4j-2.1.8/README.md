# mysql to neo4j
This is a java project that convert a mysql database into a neo4j database.

I have created a hierarchical tree graph database as a result of converting a MySQL database into another Neo4j database. The transformation works fine.

On the top level exist a node labeled as Root, on the second level are the countries labeled as Country. Every country has cities that is the third level labeled as City. And down, for every city, there are a subsets of addresses labeled as Address. The relationships should be,

(root)-[:IS_ROOT]->(country)-[:HAS_CITY]->(city)-[:HAS_ADDRESS]->(address)

Later, using Spring Data Neo4j repositories, I tried to query the database with no success.

I use JDBC for MySQL database and Spring Data Neo4j for Neo4j database, as mentioned before.

The project includes corresponding JUnit unit test that so far and such it is implemented it returns success but you should understand that it is not valid.

So while this issue is corrected the project will not be completed successfully.