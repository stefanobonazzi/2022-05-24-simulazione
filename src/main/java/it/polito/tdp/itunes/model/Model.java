package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private Graph<Track, DefaultWeightedEdge> graph;
	private List<Track> vertices;
	List<Track> best;
	
	public Model() {
		this.dao = new ItunesDAO();
	}
	
	public String creaGrafo(Genre g) {
		this.graph = new SimpleWeightedGraph<Track, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.vertices = this.dao.getAllTracks(g);
		
		Graphs.addAllVertices(this.graph, this.vertices);
		
		for(Track t1: this.vertices) {
			for(Track t2: this.vertices) {
				if(!t1.equals(t2) && t1.getMediaTypeId() == t2.getMediaTypeId()) {
					DefaultWeightedEdge edge = this.graph.getEdge(t1, t2);
					
					if(edge == null) {
						edge = this.graph.addEdge(t1, t2);
						double delta = t1.getMilliseconds() - t2.getMilliseconds();
		
						this.graph.setEdgeWeight(edge, Math.abs(delta));
					}
				}
			}
		}
		
		String s = "Grafo creato!\n#VERTICI: "+this.graph.vertexSet().size()+"\n#ARCHI: "+this.graph.edgeSet().size();
		return s;
	}

	public String deltaMassimo() {
		List<DefaultWeightedEdge> result = new ArrayList<DefaultWeightedEdge>();
		double max = 0;
		
		for(DefaultWeightedEdge e: this.graph.edgeSet()) {
			double weight = this.graph.getEdgeWeight(e);
			if(weight > max) {
				result = new ArrayList<DefaultWeightedEdge>();
				result.add(e);
				max = weight;
			} else if(weight == max) 
				result.add(e);
			
		}
		
		String s = "COPPIA CANZONI DELTA MASSIMO:\n";
		
		for(DefaultWeightedEdge edge: result) 
			s += this.graph.getEdgeSource(edge)+" *** "+this.graph.getEdgeTarget(edge)+" --> "+max+"\n";
		
		return s;
	}
	
	public String creaLista(Track c, int m) {
		List<Track> connectedTracks;
		ConnectivityInspector<Track, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.graph);
		connectedTracks = new ArrayList<Track>(ci.connectedSetOf(c));
		
		best = new ArrayList<Track>();
		List<Track> parziale = new ArrayList<Track>();	
		parziale.add(c);
		
		this.ricorsiva(parziale, m, connectedTracks, c.getBytes());
		
		String s = "LISTA DI CANZONI:\n";
		
		for(Track t: this.best) 
			s += t+"\n";
		
		return s;
	}
	
	private void ricorsiva(List<Track> parziale, int m, List<Track> connectedTracks, int mem) {
		if(parziale.size() > this.best.size()) 
			this.best = new ArrayList<Track>(parziale);
		
		for(Track t: connectedTracks) {
			if(!parziale.contains(t) && mem+t.getBytes() <= m) {
				parziale.add(t);
				this.ricorsiva(parziale, m, connectedTracks, mem+t.getBytes());
				parziale.remove(t);
			}
		}
	
	}

	public List<Genre> getAllGenres() {
		return this.dao.getAllGenres();
	}

	public List<Track> getVertices() {
		return vertices;
	}

	public boolean grafoCreato() {
		if(this.graph == null)
			return false;
		else 
			return true;
	}
	
}
