package com.xjtu.se;

import java.util.*;

import com.xjtu.se.Roadsearch.virtualNode;

import xjtu.se.hmbb.dataStruct.AMGraph;

/**
 * 主程序
 * @author chensharp 
 * 2017/5/8
 */
public class Roadsearch {
	//data start
	private AMGraph _am; //图的所有数据
	public TreeSet<virtualNode> vnallList = new TreeSet<virtualNode>();//全部虚拟节点表 含s t ,还含 
	public TreeSet<virtualNode> vsList = new TreeSet<virtualNode>();//必过虚拟节点表
	
	public Map<Integer,virtualNode> vnallMap = new HashMap<Integer,virtualNode>();//全部虚拟节点表 含s t ,还含 
	public Map<Integer,virtualNode> vsMap = new HashMap<Integer,virtualNode>();//必过虚拟节点表

	
	
	//定义虚拟节点
	class virtualNode implements Comparable{
		
		public int real_id1 ;//真实nodeID，
		public int real_id2= -1 ;//真实点是边的才会用到id2，
		
		public virtualNode pnext;//指向下一个vn，用作生成路径。
		
		//保存到任一 key= 虚拟点id 的dij最短路径。integer = real nodeid
		public Map<String, List<Integer> > path2vn = new HashMap<String, List<Integer> >();
		//保存到任一 key= 虚拟点id 的dij最短路径的代价。integer = cost
		public Map<String, Integer > cost2vn = new HashMap<String, Integer >();
		
		
		
		public virtualNode() {
			//isVisited =0;
		}
		
		@Override
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			if(!(o instanceof virtualNode))  
	            throw new RuntimeException("This is not a instance of Class \"virtualNode\" ");  
	        
	        virtualNode s =(virtualNode)o;  
	        if(this.real_id1 > s.real_id1)  
	            return 1;  
	        else if(this.real_id1 ==s.real_id1)  
	            return 0;  
	        return -1;  
		}	
		
		//1 ok 0 illegal -1 noroad
		public int checklaw(int key){
			//
			if (key >=1000) {
				return -1;//没有路了
			}
			//检测是否是end节点
			
			//这里缺检测代码
			
			//合法
			return 1;
		}
		/**
		 * SK66算法
		 * 
		 */
		public List<Integer> SK66( List<Integer> canvisit){
			if(canvisit.size()==0){
				return path2vn.get(String.valueOf(_am.getEnd()));
			}
			List<Integer> shortpath = null;
			int minCost = Integer.MAX_VALUE;
			for (Integer node : canvisit) {
				int D = cost2vn.get(node.toString());
				if(D>=minCost){
					continue;
				}
				List<Integer> canvisit2 = new ArrayList<Integer>();
				canvisit2.addAll(canvisit);
				canvisit2.remove(node);
				List<Integer> subPath = vnallMap.get(node).SK66(canvisit2);
				if(subPath!=null){
					boolean flag = false;
					for (Integer subnode : subPath) {
						if(subnode.equals((Integer)_am.getEnd())){
							continue;
						}else if(!canvisit.contains(subnode)
								&&cost2vn.containsKey(subnode.toString())){
							flag = true;
							break;
						}else if(subnode.equals(this.real_id1)){
							flag = true;
							break;
						}
					}
					if(flag){
						continue;
					}
					int f = caculateCost(subPath);
					int fn = D+f;
					if(fn<minCost){
						minCost = fn;
						subPath.remove(0);
						subPath.addAll(0, path2vn.get(String.valueOf(node)));
						System.out.println(converPathtoName2(subPath));
						shortpath = subPath;
					}
				}
			}
			
			return shortpath;
		}
		/**
		 * 遍历
		 * @return
		 */
		public int search(int poch, List<Integer> isvisited) {
			//选择下一个vn，赋给pnext
			//选择cost2vn中最小的cost 的vn，要求不能是已经访问过的，也不能是end节点
			
			//置当前id 为已访问，
			if (isvisited.contains(this.real_id1) == false) {
				isvisited.add(this.real_id1);
				System.out.println(real_id1+" 已经访问");
			}
			
			//退出条件,当前是End节点
			if (this.real_id1==_am.getEnd() || poch > 15) {
				System.out.println("到达终点");
				System.out.println("path= " +( converPathtoName2(isvisited) ));
				return 0;
			}
			
			//找下一个合适的node id
			int minkey;
			for (;;) {
				minkey = Integer.parseInt( getmincost_Key(cost2vn,isvisited) );
				int res = checklaw(minkey);
				System.out.println("key="+minkey+" res=" +res);
				
				if (res==1) {//检测合法不
					break;

				}else {//-1
					//找不到合适的路径了
					System.out.println("找不到合适的路了");
					return 0;
				}
			}
			
			System.out.println("key="+minkey);
			pnext = vnallMap.get(minkey);//取出vnode

			return pnext.search(poch+1,isvisited);
		}
		

		/**
		 * 得到最小cost的virtualnode 的key
		 * @param map
		 * @return
		 */
		public String getmincost_Key(Map<String, Integer> map, List<Integer> isvisited) {
			if (map == null)
				return null;
			int min = 1000;
			String _key="";
			System.out.println("getmincost start id="+this.real_id1);

			System.out.println("isvisited="+isvisited);
			
			for (String key : map.keySet()) {
				int temp = map.get(key);//取出cost
				
				if (temp< min && (isvisited.contains(Integer.parseInt(key))==false )) { //
					min = temp;
					_key = key;
					
					System.out.println("getmincost key ="+key+" cost = "+temp);
				}
			}
			return _key;
		}
	}
	
	
	//data  end 
	/* ***********公有接口 start *************************/
	/**
	 * 传入AMGraph
	 * @param amgraph
	 */
	public Roadsearch(AMGraph amgraph) {
		// TODO Auto-generated constructor stub
		_am = amgraph;	
	}
	
	/**
	 * 入口，测试
	 */
	public void searchroad() {
		int start = _am.getStart();
		int end = _am.getEnd();
		String[] shortPath2 = Dij(_am.getEdges(), start);
		//printMatrix(_am.getEdges(), "edges");
		
		int vertexcount = _am.getEdges().length;
		for (int i = 0; i < vertexcount; i++) {
			//String reString = _am.getVertexList().get(Integer.parseInt(shortPath2[i]));
			String paths = converPathtoName(shortPath2[i]);
			System.out.println(" from " + start + " to " + i + " route= " + paths);
		}
	}
	
	/**
	 * 入口-virtualnode法
	 */
	public void searchroad_vn() {
		
		// 添加到vs集合
		//TreeSet<Integer> vs = new TreeSet<Integer>();
		for (int i = 0; i < _am.getFruitRoomList().size(); i++) {
			//vs.add(_am.getFruitRoomList().get(i));
			virtualNode _vNode = new virtualNode();
			_vNode.real_id1 = _am.getFruitRoomList().get(i);
			
			vnallList.add(_vNode);
			vsList.add(_vNode);
			
			vnallMap.put(_vNode.real_id1, _vNode);
			vsMap.put(_vNode.real_id1, _vNode);
		}
		//System.out.println("VS="+ vs);
		//add vs-arc
		for (int i = 0; i < _am.getRewardPath().size(); i++) {
			int[] temp_line = _am.getRewardPath().get(i);//获得一条必过边
			int s1 = temp_line[0];
			int s2 = temp_line[1];
			virtualNode _vNode1 = new virtualNode();
			_vNode1.real_id1 = s1;
			_vNode1.real_id2 = s2;
			vnallList.add(_vNode1);
			vsList.add(_vNode1);
			vnallMap.put(_vNode1.real_id1, _vNode1);
			vsMap.put(_vNode1.real_id1, _vNode1);
			
			virtualNode _vNode2 = new virtualNode();
			_vNode2.real_id1 = s2;
			_vNode2.real_id2 = s1;
			vnallList.add(_vNode2);
			vsList.add(_vNode2);
			vnallMap.put(_vNode2.real_id1, _vNode2);
			vsMap.put(_vNode2.real_id1, _vNode2);
			
			
		}
		System.out.println("VNALLlist.size="+ vnallList.size());
		//add source target
		virtualNode _vNode_s = new virtualNode();
		_vNode_s.real_id1 = _am.getStart();
		vnallList.add(_vNode_s);
		vnallMap.put(_vNode_s.real_id1, _vNode_s);
		
		virtualNode _vNode_t = new virtualNode();
		_vNode_t.real_id1 = _am.getEnd();
		vnallList.add(_vNode_t);
		vnallMap.put(_vNode_t.real_id1, _vNode_t);
		
		//遍历所有vnall
		int vnid1,vnid2;
		for (virtualNode vn1 : vnallList) {
			vnid1 = vn1.real_id1;
			
			//get dij path
			String[] shortPath = Dij(_am.getEdges(),vnid1);
			
			for (virtualNode vn2 : vnallList) {
				vnid2 = vn2.real_id1;
				if (vnid1!= vnid2 ) {//不是自己到自己
					System.out.print(vnid1+"-"+vnid2+ " ");
					//生成到其余顶点的dij路径
					List<Integer> pathtemp = new ArrayList<Integer>();
					String shortpath1 = shortPath[vnid2];//取当前vnid2为目的地的路径
					
					int[] pathint = converPathtoInt(shortpath1);
					for (int i = 0; i < pathint.length; i++) {
						pathtemp.add(pathint[i]);
					}
					int cost = caculateCost(pathtemp);
					vn1.path2vn.put(String.valueOf(vnid2),pathtemp );//add
					vn1.cost2vn.put(String.valueOf(vnid2), cost);
					
					System.out.println("path = "+converPathtoName(shortpath1) + " cost="+ cost);
					
				}
			}
		}
		//Map<Integer, Boolean> isvisited1 = new HashMap<Integer,Boolean>();
		List<Integer> isvisited2 = new ArrayList<Integer>();

//		int epoch = vnallMap.get(_am.getStart()).search(1,isvisited2);
//		System.out.println("end ! epoch size = "+epoch);
		
		List<Integer> canvisit = new ArrayList<Integer>();
		for (virtualNode allnode : vnallList) {
			canvisit.add(allnode.real_id1);
		}
		canvisit.remove((Integer)_am.getStart());
		canvisit.remove((Integer)_am.getEnd());
		String pathRoute = converPathtoName2(vnallMap.get(_am.getStart()).SK66(canvisit));
		System.out.println();
		System.out.println(pathRoute);
		
	}
	
	
	/**
	 * 法 入口1  该法废弃
	 */
	public void searchroad1() {
		//添加到vs集合
		TreeSet<Integer> vs = new TreeSet<Integer>();
		for (int i = 0; i < _am.getFruitRoomList().size(); i++) {
			vs.add(_am.getFruitRoomList().get(i));
		}
		//System.out.println("VS="+ vs);
		
	    //s-vs
		Map<String, List<Integer>> StoVs = new HashMap<String, List<Integer>>();	
		
		
		int start = 0;
		String[] shortPath2 = Dij(_am.getEdges(), start);
		//printMatrix(_am.getEdges(), "edges");
		
		int vertexcount = _am.getEdges().length;
		for (int i = 0; i < vertexcount; i++) {
			if (vs.contains(i)) {
				List<Integer> _temp = new ArrayList<Integer>();
				int[] _tempint = converPathtoInt( shortPath2[i] );
				
				for (int j = 0; j < _tempint.length; j++) {
					_temp.add(_tempint[j] );
				}
				
				StoVs.put(String.valueOf(i),_temp );
			}
		}
		System.out.println("StoVs" +StoVs);
		
		
		//vs-vs
		Map<String, Map<String, List<Integer>> > VstoVs = new HashMap<String,  Map<String, List<Integer>>  >();	
        
		List<Integer> vs_array = new ArrayList<Integer>();
		for(Integer vs_value : vs){
			vs_array.add(vs_value);
		}
		List<int[]> vspair = new ArrayList<int[]>(); 
		
		for (int i = 0; i < vs_array.size(); i++) {
			for (int j = 0; j < vs_array.size(); j++) {
				int s1 = vs_array.get(i);
				int s2 = vs_array.get(j);
				if (s1 !=s2) {
					//a good pair
					System.out.println(s1+"-"+s2);
					//save pair
					int[] temp = new int[2];
					temp[0]=s1;
					temp[1]=s2;
					vspair.add(temp);
				}
			}
		}
		//System.out.println();
		
		
		
		//
	}
	
	
	/* ***********公有接口 end *************************/
	
	/* ***********私有接口 start *************************/
	
	/**
	 * 打印矩阵
	 */
	private void printMatrix(int[][] mat, String name) {
		System.out.println("---------------开始打印矩阵:"+name+"----------------------------");
		// print 
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				if (mat[i][j]==Integer.MAX_VALUE) {
					System.out.print(" # ");
				}else{
					System.out.print(" "+ mat[i][j] + " ");
				}
			}
			System.out.print("\n");
		}
	}

	/**
	 * 过滤只求单个点到点路径
	 * @param weight
	 * @param start
	 * @param end
	 * @return
	 */
	private String getDIJPath(int[][] weight,int start,int end) {
		int[][] temp = arraysCopy(weight);//复制下数组
		
		String[] str = Dij(temp, start);
		return str[end];
	}
	
	/**
	 * 把Dij函数的返回值eg 1-3-4 做处理，转为实际名字 S -N1-N2-E 的路径
	 * @param _s
	 * @return
	 */
	private String converPathtoName(String _s) {
		int[] paths = converPathtoInt(_s);
		String str= "";
		for(int i=0;i<paths.length;i++){
			String reString = _am.getVertexList().get(paths[i]);
			if (i==0) {
				str = reString;
			}else{
				str =str+"-"+ reString;
			}
		}
		
		return str;
	}
	
	/**
	 * 把eg 1-3-4 做处理，转为实际名字 S -N1-N2-E 的路径
	 * @param _s
	 * @return
	 */
	private String converPathtoName2(List<Integer> _s) {
		//Object[] paths1 = _s.toArray();
		int[] paths = new int[_s.size()];
		for (int i = 0; i < paths.length; i++) {
			paths[i]= _s.get(i);
		}
		String str= "";
		for(int i=0;i<paths.length;i++){
			String reString = _am.getVertexList().get(paths[i]);
			if (i==0) {
				str = reString;
			}else{
				str =str+"-"+ reString;
			}
		}
		
		return str;
	}
	
	
	/**
	 * 把Dij函数的返回值eg 1-3-4 做处理，转为int[]
	 * @param _s
	 * @return
	 */
	private int[] converPathtoInt(String _s) {
		String[] strings = _s.split("-");
		int[] returnss = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			returnss[i] = Integer.parseInt(strings[i]);
		}
		return returnss;
	}
	
	/**
	 * 拷贝数组二维,非引用复制。
	 * @param newmat
	 * @return
	 */
	private int[][] arraysCopy(int[][] newmat) {
		int n = newmat.length;
		int[][] result= new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = newmat[i][j];
			}
		}
		return result;
	}
	
	/**
	 * 计算路径的代价
	 * @return
	 */
	private int caculateCost(List<Integer> _path) {
		
		int cost,node1 ,node2;
		int sum =0;
		for (int i = 0; i < _path.size() - 1; i++) {
			node1 = _path.get(i);
			node2 = _path.get(i+1);
			cost = _am.getEdges()[node1][node2];
			sum += cost;
			if (cost>=Integer.MAX_VALUE) {
				System.out.println("path-error");
			}
		}
		return sum;
	}
	 /**
     * 
     * @param weight1
     * @param start
     * @return  返回start点到所有点的最短路径的string[],其中【1】是start到1的路径，用-隔开。  
     */
	private String[] Dij(int[][] weight1, int start) {
		// 接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）
		// 返回一个int[] 数组，表示从start到它的最短路径长度
		int[][] weight =arraysCopy(weight1);
		
		int n = weight.length; // 顶点个数
		int[] shortPath = new int[n]; // 存放从start到其他各点的最短路径

		String[] path = new String[n]; // 存放从start到其他各点的最短路径的字符串表示
		for (int i = 0; i < n; i++) {
			path[i] = new String(start + "-" + i);
		}
		int[] visited = new int[n]; //标记当前该顶点的最短路径是否已经求出,1表示已求出

		// 初始化，第一个顶点求出
		shortPath[start] = 0;
		visited[start] = 1;

		for(int i=0;i<n;i++){
			if ( weight[start][i] < Integer.MAX_VALUE) {
				shortPath[i] = weight[start][i] ;
				path[i] = String.valueOf(start) + "-" + i;
//				visited[i] = 1;
			}else if(i!=start){
				shortPath[i] = Integer.MAX_VALUE ;
			}
		}
		// 要加入n-1个顶点
		for (int count = 1; count <= n-1; count++) {
			int k = -1; // 选出一个距离初始顶点start最近的未标记顶点
			int dmin = Integer.MAX_VALUE;//寻找最小的节点i
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && shortPath[i] < dmin) {
					dmin = shortPath[i] ;
					k = i;
				}
			}
			//System.out.println("k=" + k);

			// 将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin
//			shortPath[k] = dmin;
			visited[k] = 1;

			// 以k为中间点，修正从start到未访问各点的距离
			for (int i = 0; i < n; i++) {
				// System.out.println("k="+k);
				if (visited[i] == 0 && (shortPath[k] + weight[k][i] < shortPath[i]&&weight[k][i]!=Integer.MAX_VALUE)) {
					int res =0;
//					if(shortPath[k]==Integer.MAX_VALUE || weight[k][i]==Integer.MAX_VALUE){//如果二者有一个是无穷大，即为不可达，结果也为不可达
//						res = Integer.MAX_VALUE;
//					}else {
//						res = (shortPath[k] + weight[k][i]);
//					}
					res = (shortPath[k] + weight[k][i]);
					shortPath[i] = res;
					
					path[i] = path[k] + "-" + i;
				}
			}
		}
		return path;
	}
	
	
	
	
	
	
	
	/* ***********私有接口 end *************************/
	
}
