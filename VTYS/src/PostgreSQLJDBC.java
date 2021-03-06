import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Scanner;
import Person;

//ANIL SAVAÞKURT
//1306170002

public class PostgreSQLJDBC {
	
   public static void main(String args[]) {
	   
	   Scanner scanner = new Scanner(System.in);
	   Connection c = null;
	   Statement stmt = null;
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection("jdbc:postgresql://localhost:5433/vtys",
	            "postgres", "1");
	         c.setAutoCommit(false);
	         System.out.println("Opened database successfully");
	        int secenek = -1;
	        int inputId;	        
	        
	  	    while(secenek != 0) { 
	  		   System.out.println("PostgreSQL Menu");
	  		   System.out.println("0: Çýkýþ");
	  		   System.out.println("1: Soy Aðacý Sorgula");
	  		   System.out.println("2: Soyundan Gelenleri Sorgula");
	  		   secenek = scanner.nextInt();
	  		   
	  		   switch(secenek) {
	  		   
	  		   case 1 :
		  			 System.out.println("ID ? ");
		  			 inputId = scanner.nextInt();
		  			 System.out.println("Soy Aðacý");
		  			 soyAgaci(inputId, c);		
	  			     break;
	  			     
	  		   case 2 :
			  		 System.out.println("ID ? ");
		  			 inputId = scanner.nextInt();
		  			 System.out.println("Soyundan Gelenler");
		  			 soyundanGelenler(inputId, c);		
					 break;
	  		   }
	  	    }
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
   }

   public static void soyAgaci(int id, Connection con) throws ClassNotFoundException{
       
       String sql = "Select * From \"Person\" Where id in (Select \"parentId\" from \"Person\" Where id = ?)"; 

       try{
    	   
           PreparedStatement ps = con.prepareStatement(sql);
           ps.setInt(1, id);
           ResultSet rs = ps.executeQuery();
           while(rs.next()){
        		Person person = new Person(rs.getInt("id"), rs.getString("name"), rs.getString("gender"), rs.getInt("parentId"));
        		System.out.println(person.id+" "+person.name+" "+person.gender+" "+person.parentId);
                soyAgaci(person.id,con);
           }
       }catch(SQLException e){
           System.out.println(e.getMessage());
       }
   }
   
   public static void soyundanGelenler(int id, Connection con) throws ClassNotFoundException{
       
       String sql = "Select * From \"Person\" Where \"parentId\" = ?"; 

       try{
    	   
           PreparedStatement ps = con.prepareStatement(sql);
           ps.setInt(1, id);
           ResultSet rs = ps.executeQuery();
           while(rs.next()){
        		Person person = new Person(rs.getInt("id"), rs.getString("name"), rs.getString("gender"), rs.getInt("parentId"));
        		System.out.println(person.id+" "+person.name+" "+person.gender+" "+person.parentId);
        		soyundanGelenler(person.id,con);
           }
       }catch(SQLException e){
           System.out.println(e.getMessage());
       }
   }

}