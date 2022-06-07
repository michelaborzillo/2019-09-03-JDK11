package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;


public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	
	public List<String> getVertici (double nCalorie) {
		String sql="SELECT DISTINCT portion_display_name "
				+ "FROM `portion` "
				+ "WHERE calories<?";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setDouble(1, nCalorie);
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					String s= res.getString("portion_display_name");
					
					list.add(s);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

		
	}
	public List<Adiacenza> getCoppie (Map<Integer, Portion> idMap) {
		String sql="SELECT p1.portion_id AS p1, p2.portion_id AS p2, COUNT(f.food_code) AS peso "
				+ "FROM `portion` p1, food f, `portion` p2 "
				+ "WHERE  f.food_code=p1.food_code AND f.food_code=p2.food_code AND p1.portion_id<>p2.portion_id "
				+ "GROUP BY p1.food_code "
				+"HAVING peso >=2";
		Connection conn= DBConnect.getConnection();
		List<Adiacenza> result= new ArrayList<Adiacenza>();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res=st.executeQuery();
			while (res.next()) {
				String p1=res.getString("p1");
				String p2= res.getString("p2");
//				idMap.put(p1.getPortion_id(), p1);
//				idMap.put(p2.getPortion_id(), p2);
				if (p1!=null && p2!=null)
				result.add(new Adiacenza(p1, p2, (int)res.getDouble("peso")));
				
			}conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public double getPeso (){
		String sql="SELECT p.food_code, COUNT(*) AS peso "
				+ "FROM `portion` p "
				+ "GROUP BY p.food_code "
				+ "HAVING peso >=2";
		Connection conn= DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res=st.executeQuery();
			double peso=0;
			if (res.next()) {
				
				peso=res.getDouble("peso");
			}
			conn.close();
			return peso;
		}catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
		
	}

}
