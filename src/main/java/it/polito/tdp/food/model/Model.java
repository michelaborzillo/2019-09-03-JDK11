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
	private FoodDao dao;
	Graph<Vertici, DefaultWeightedEdge> grafo;
	public List<Vertici> best;
	public double max=0;
	public Model () {
		dao=new FoodDao();
	}
	
	
	public void creaGrafo(int nCalorie) {
		this.grafo= new SimpleWeightedGraph<Vertici, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Vertici> port= dao.getVertici(nCalorie);
		Graphs.addAllVertices(this.grafo, port);
		for (Adiacenza a: dao.getArchi()) {
			Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
			
		}
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
		
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	
	}
	public List<Vertici> getVertici (int nCalorie) {
		return dao.getVertici(nCalorie);
	}


	public List<Adiacenza> getAdiacenza(Vertici vertice) {
		List<Vertici> vicini= new LinkedList<Vertici>(Graphs.neighborListOf(this.grafo, vertice));
		List<Adiacenza>result= new ArrayList<Adiacenza>();
		for (Vertici p: vicini) {
			int peso= (int) this.grafo.getEdgeWeight(this.grafo.getEdge(vertice, p));
			result.add(new Adiacenza(vertice, p, peso));
		}
		return result;
	}
	//RICORSIONE CAMMINO SEMPLICE CON PESO MASSIMO
		public double pesoCammino(List<Vertici> parziale) {
			double peso=0;
			
			for(int i=1; i<parziale.size(); i++) {
				double p = this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i-1), parziale.get(i))) ;
				peso += p ;
			}
			return peso ;
			
		}
		
		public void cercaLista(Vertici partenza, int n) {
			this.best=null;
			this.max=0;
			List<Vertici> parziale= new ArrayList<Vertici>();
			parziale.add(partenza);
			ricorsione(parziale,  n, 1);
		}

		private void ricorsione(List<Vertici> parziale, int n, int livello) {
			
			if (livello==n+1) {
				double peso= pesoCammino(parziale);
				if (peso>this.max) {
					this.max=peso;
					this.best= new ArrayList<Vertici>(parziale);
				}
				return;
			}
			List<Vertici> vicini= Graphs.neighborListOf(this.grafo, parziale.get(livello-1));
			for (Vertici p: vicini) {
				if (!parziale.contains(p)) {
					parziale.add(p);
					ricorsione(parziale, n, livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
			
			
		}	
}
