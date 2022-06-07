package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	Graph<String, DefaultWeightedEdge> grafo;
	FoodDao dao;
	List<Portion> list;
	Map<Integer, Portion>idMap;
	public List<String> best;
	public double max=0;
	public Model () {
		dao= new FoodDao();
		idMap= new HashMap<Integer, Portion>();
	}
	
	public void creaGrafo(double nCalorie) {
		this.grafo= new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
//		List<Portion> port= dao.getVertici(nCalorie, idMap);
//		Graphs.addAllVertices(this.grafo, idMap.values());
//		for (Adiacenza ad: dao.getCoppie(idMap)) {
//			Graphs.addEdgeWithVertices(this.grafo, ad.getP1(), ad.getP2(), ad.getPeso());
//			
//		}
	
	List<String> port= dao.getVertici(nCalorie);
	Graphs.addAllVertices(this.grafo, port);
	for (Adiacenza ad: dao.getCoppie(idMap)) {
		Graphs.addEdgeWithVertices(this.grafo, ad.getP1(), ad.getP2(), ad.getPeso());
	}
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
		
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	
	}
	public List<String> getVertici (double nCalorie) {
		return dao.getVertici(nCalorie);
	}
	
	
	public List<Adiacenza> getAdiacenza(String vertice) {
		List<String> vicini= new LinkedList<String>(Graphs.neighborListOf(this.grafo, vertice));
		List<Adiacenza>result= new ArrayList<Adiacenza>();
		for (String p: vicini) {
			double peso= this.grafo.getEdgeWeight(this.grafo.getEdge(vertice, p));
			result.add(new Adiacenza(vertice, p, (int)peso));
		}
		return result;
	}
	
	
	//RICORSIONE CAMMINO SEMPLICE CON PESO MASSIMO
	public double pesoCammino(List<String> parziale) {
		double peso=0;
		
		for(int i=1; i<parziale.size(); i++) {
			double p = this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i-1), parziale.get(i))) ;
			peso += p ;
		}
		return peso ;
		
	}
	
	public void cercaLista(String partenza, int n) {
		this.best=null;
		this.max=0;
		List<String> parziale= new ArrayList<String>();
		parziale.add(partenza);
		ricorsione(parziale,  n, 1);
	}

	private void ricorsione(List<String> parziale, int n, int livello) {
		
		if (livello==n+1) {
			double peso= pesoCammino(parziale);
			if (peso>this.max) {
				this.max=peso;
				this.best= new ArrayList<String>(parziale);
			}
			return;
		}
		List<String> vicini= Graphs.neighborListOf(this.grafo, parziale.get(livello-1));
		for (String p: vicini) {
			if (!parziale.contains(p)) {
				parziale.add(p);
				ricorsione(parziale, n, livello+1);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
	}
	
}
