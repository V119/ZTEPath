package xjtu.se.hmbb.dataStruct;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;

public class Path {
	public List<Integer> nodes;
	public int weight;
	public int count;
	public Path(Path p, int nextNode , int weight2NextNode) {
			this.weight = p.weight<Integer.MAX_VALUE&&weight2NextNode<Integer.MAX_VALUE?p.weight+weight2NextNode:Integer.MAX_VALUE;
			this.nodes = new ArrayList<Integer>(p.nodes);
			this.nodes.add(nextNode);
			this.count = this.nodes.size();
	}
	public Path(int start,int target,int weight){
		this.nodes = new ArrayList<Integer>();
		this.nodes.add(start);
		this.nodes.add(target);
		this.weight = weight;
		this.count = start==target?1:2;
	}
	public Path(){
		this.weight = Integer.MAX_VALUE;
		this.count = 0;
		this.nodes = new ArrayList<Integer>();
	}
	public Path(Path p1,Path p2) throws Exception{
		if (p1==null||p2==null) {
			throw new Exception();
		}
		this.nodes = new ArrayList<Integer>();
		this.nodes.addAll(p1.nodes);
		if (p1.nodes.get(p1.nodes.size()-1)==p2.nodes.get(0)&&p1.weight<Integer.MAX_VALUE&&p2.weight<Integer.MAX_VALUE
				&&p1.count<Integer.MAX_VALUE&&p2.count<Integer.MAX_VALUE) {
			this.nodes.remove(p1.nodes.size()-1);
		}else{
			throw new Exception();
		}
		this.nodes.addAll(p2.nodes);
		this.weight = p1.weight+p2.weight;
		this.count = p1.count+p2.count-1;
	}
	
	public boolean isShorter(Path p){
		if (p==null) {
			return true;
		}
		return this.weight < p.weight;
	}
	public boolean isLess(Path p){
		if (p==null) {
			return true;
		}
		return this.count < p.count;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nodes.toString();
	}
}
