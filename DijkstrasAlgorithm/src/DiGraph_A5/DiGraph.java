package DiGraph_A5;
import java.util.HashMap;
import java.util.PriorityQueue;
public class DiGraph implements DiGraphInterface {
	
	private HashMap<String, Node> nodeMap = new HashMap<String, Node>();
	private HashMap<Long, Node> idMap = new HashMap<Long, Node>();
	private HashMap<Long, Edge> edgeMap = new HashMap<Long, Edge>();
	private HashMap<String, Edge> elabelMap = new HashMap<String, Edge>();
	
	
  // in here go all your data and methods for the graph

  public DiGraph ( ) { // default constructor
    // explicitly include this
    // we need to have the default constructor
    // if you then write others, this one will still be there
  }
  
  

@Override
public boolean addNode(long idNum, String label) {
	// TODO Auto-generated method stub
	if (nodeMap.containsKey(label) || idMap.containsKey(idNum) || idNum < 0) {
		return false;
	} else {
		Node newNode = new Node(idNum, label);
		nodeMap.put(label, newNode);
		idMap.put(idNum, newNode);
	
		return true;
	}
}

@Override
public boolean addEdge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
	// TODO Auto-generated method stub
	String key = sLabel + "--" + dLabel;
	if (edgeMap.containsKey(idNum) || idNum < 0 || elabelMap.containsKey(key) || !nodeMap.containsKey(sLabel) || !nodeMap.containsKey(dLabel)) {
		return false;
	} else {
		Edge newEdge = new Edge (idNum, sLabel, dLabel, weight, eLabel);
		edgeMap.put(idNum, newEdge);
		nodeMap.get(sLabel).outEdges.add(newEdge);
		nodeMap.get(dLabel).inEdges.add(newEdge);
		elabelMap.put(newEdge.getELabel(), newEdge);
		return true;
	}

}

@Override 
public boolean delNode(String label) {
	// TODO Auto-generated method stub
	if (nodeMap.containsKey(label)) {
		Node delNode = nodeMap.get(label);
		for (Edge temp: delNode.inEdges) {
			nodeMap.get(temp.getSLabel()).outEdges.remove(temp);
			edgeMap.remove(temp.getIdNum());
			String tempy = temp.getSLabel() + "--" + temp.getDLabel();
			elabelMap.remove(tempy);
		}
		for (Edge temp: delNode.outEdges) {
			nodeMap.get(temp.getDLabel()).inEdges.remove(temp);
			edgeMap.remove(temp.getIdNum());
			String tempy = temp.getSLabel() + "--" + temp.getDLabel();
			elabelMap.remove(tempy);
		}
		idMap.remove(delNode.getIDNUM());
		nodeMap.remove(label);
		return true;
	}
	return false;
}

@Override
public boolean delEdge(String sLabel, String dLabel) {
	// TODO Auto-generated method stub
	String huzzah = sLabel + "--" + dLabel;
	if (!elabelMap.containsKey(huzzah)) {
		return false;
	}
	Edge delEdge = elabelMap.get(huzzah);
	edgeMap.remove(delEdge.getIdNum());
	elabelMap.remove(huzzah);
	nodeMap.get(sLabel).outEdges.remove(delEdge);
	nodeMap.get(dLabel).inEdges.remove(delEdge);
	return true;
}

@Override
public long numNodes() {
	// TODO Auto-generated method stub
	return nodeMap.size();
}

@Override
public long numEdges() {
	// TODO Auto-generated method stub
	return edgeMap.size();
}

public ShortestPathInfo[] shortestPath(String label) {
	if (!nodeMap.containsKey(label)) {
		return null;
	}
	ShortestPathInfo[] paths = new ShortestPathInfo[nodeMap.size()];
	Node start = nodeMap.get(label);
	PriorityQueue<QueuePair> pq = new PriorityQueue<QueuePair>();
	QueuePair qp = new QueuePair(start, (long) 0);
	pq.add(qp);
	int i = 0;
	
	while (pq.size() != 0) {
		
		QueuePair p = pq.poll();
		Node n = p.n;
		Long dist = p.distance;
		if (n.known) {
			continue;
		
		}
		n.known = true;
		paths[i] = new ShortestPathInfo(n.getLabel(), n.dv);
		i++;
		
		for (Edge edges : n.outEdges) {
			Node dest = nodeMap.get(edges.getDLabel());
			if (dest.pv == null) {
				dest.pv = n;
				dest.dv = dist + edges.getWeight();
				qp = new QueuePair(dest, dest.dv);
				pq.add(qp);
				continue;
			}
			if (dest.dv > dist + edges.getWeight()) {
				dest.dv = dist + edges.getWeight();
				dest.pv = n;
				qp = new QueuePair(dest, dest.dv);
				pq.add(qp);
			}
			
		}
		
	}
	
	for (Node n : nodeMap.values()) {
		if (n.known == false) {
			n.dv = -1;
			paths[i] = new ShortestPathInfo(n.getLabel(), n.dv);
			i++;
		}
	}	
	return paths;
}
  

}