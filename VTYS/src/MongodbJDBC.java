import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient; 
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.util.Scanner;
import java.io.FilterOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongodbJDBC {

	public static void main(String[] args) throws ClassNotFoundException {
		
	  Scanner scanner = new Scanner(System.in);
	  Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
      mongoLogger.setLevel(Level.SEVERE);
      // Creating a Mongo client 
      MongoClient mongo = new MongoClient( "localhost" , 27017 ); 

      // Creating Credentials 
      MongoCredential credential; 
      credential = MongoCredential.createCredential("sampleUser", "vtys", 
         "password".toCharArray()); 
      System.out.println("Connected to the database successfully");

      // Accessing the database 
      MongoDatabase database = mongo.getDatabase("vtys"); 
      MongoCollection<Document> collection = database.getCollection("Person"); 
      
      
      int secenek = -1;
      int inputId;	        
      
	    while(secenek != 0) { 
		   System.out.println("MongoDB Menu");
		   System.out.println("0: Çýkýþ");
		   System.out.println("1: Soy Aðacý Sorgula");
		   System.out.println("2: Soyundan Gelenleri Sorgula");
		   secenek = scanner.nextInt();
		   
		   switch(secenek) {
		   
		   case 1 :
	  			 System.out.println("ID ? ");
	  			 inputId = scanner.nextInt();
	  			 System.out.println("Soy Aðacý");
	  			 soyAgaci(inputId, collection);		
			     break;
			     
		   case 2 :
		  		 System.out.println("ID ? ");
	  			 inputId = scanner.nextInt();
	  			 System.out.println("Soyundan Gelenler");
	  			 soyundanGelenler(inputId, collection);		
				 break;
		   }
	    }
  }
	
	  public static void soyAgaci(int id, MongoCollection<Document> coll) throws ClassNotFoundException{
	       
	       try{

	    	     Bson bsonFilter = Filters.eq("id", id);
	    	     FindIterable<Document> findIt = coll.find(bsonFilter);
	    	    
	    	     for (Document document : findIt) {
	    	    	 	
		    			Person p = new Person(document.getInteger("id"),document.getString("name"),document.getString("gender"),document.getInteger("parentId") == null ? 0 : document.getInteger("parentId") );
		    			System.out.println(p.name);
		    			 if(document.getInteger("parentId") != null) {
		    				 soyAgaci(p.parentId,coll);
		    			 }
	    	     }
	       }catch(Exception e){
	           System.out.println(e.getMessage());
	       }
	   }
	
	public static void soyundanGelenler(int id, MongoCollection<Document> coll) throws ClassNotFoundException{

	       try{

	    	     Bson bsonFilter = Filters.in("parentId", id);
	    	     FindIterable<Document> findIt = coll.find(bsonFilter);
	    	     for (Document document : findIt) {
	    			Person p = new Person(document.getInteger("id"),document.getString("name"),document.getString("gender"),document.getInteger("parentId"));
	    			System.out.println(p.name);
	    			soyundanGelenler(p.id,coll);
	    	     }
	       }catch(Exception e){
	           System.out.println(e.getMessage());
	       }
	   }
}