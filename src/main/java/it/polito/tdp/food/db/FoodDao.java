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
import it.polito.tdp.food.model.Vertici;



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
	
	public List<Vertici> getVertici (int nCalorie) {
		String sql="SELECT p.portion_display_name AS nome, p.portion_id AS id "
				+ "FROM `portion` p "
				+ "WHERE p.calories<? "
				+ "GROUP BY p.portion_display_name";
	
	Connection conn= DBConnect.getConnection();
	List<Vertici> result = new ArrayList<Vertici>();
	try {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, nCalorie);
	
		ResultSet res=st.executeQuery();
		while (res.next()) {
		
			Vertici v= new Vertici(res.getInt("id"), res.getString("nome"));
			result.add(v);
		}
		conn.close();
		return result;
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}
	
	}
	public List<Adiacenza> getArchi() {
		String sql="SELECT p1.portion_id AS p1id, p2.portion_id AS p2id, p1.portion_display_name p1name, p2.portion_display_name p2name, (f.food_code) AS peso "
				+ "FROM `portion` p1, food f, `portion` p2 "
				+ "WHERE  f.food_code=p1.food_code AND f.food_code=p2.food_code AND p1.portion_id<>p2.portion_id "
				+ "GROUP BY p1.food_code "
				+ "HAVING peso >=2";
		Connection conn= DBConnect.getConnection();
		List<Adiacenza> result= new ArrayList<Adiacenza>();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res=st.executeQuery();
			while (res.next()) {
				Vertici v1= new Vertici (res.getInt("p1id"), res.getString("p1name"));
				Vertici v2= new Vertici (res.getInt("p2id"), res.getString("p2name"));
				if (v1!=null && v2!=null) {
					result.add(new Adiacenza(v1, v2, res.getInt("peso")));
				}
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
