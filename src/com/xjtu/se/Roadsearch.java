package com.xjtu.se;

import java.util.*;


import xjtu.se.hmbb.dataStruct.AMGraph;
import xjtu.se.hmbb.dataStruct.Path;

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
		public int weight = Integer.MAX_VALUE;
		public virtualNode pnext;//指向下一个vn，用作生成路径。

		//保存到任一 key= 虚拟点id 的dij最短路径。integer = real nodeid
		public Map<String, List<Integer> > path2vn = new HashMap<String, List<Integer> >();
		//保存到任一 key= 虚拟点id 的dij最短路径的代价。integer = cost
		public Map<String, Integer > cost2vn = new HashMap<String, Integer >();
		//保存到任一key= 虚拟店id 的maxnode点内dij最短路径集合
		public Map<String,Path[]> path2vnMap = new HashMap<String,Path[]>() ;

		public virtualNode() {
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
			//合法
			return 1;
		}

		/**
		 * 求解maxnum点内到达的解集合
		 * @param canvisit  任务数组
		 * @param maxnum   最大访问数目
		 * @return
		 */
		public Path[] VNSK( List<Integer> canvisit , int maxnum){
			if(canvisit.size()==0){//完成任务后 退出  
				Path[] paths2e = path2vnMap.get(String.valueOf(_am.getEnd()));
				for (int i = 0; i < paths2e.length; i++) {
					if(paths2e[i].count>maxnum){
						paths2e[i].weight = Integer.MAX_VALUE;
					}
				}
				return paths2e;
			}else if(this.real_id1>=0&&canvisit.contains(this.real_id2)){//检测当前点是一条未访问过的虚拟边
				canvisit.remove((Integer)this.real_id2);
				//canvisit.remove((Integer)this.real_id1);
				Path[] pathsid22idt = vnallMap.get((Integer)this.real_id2).VNSK(canvisit, maxnum-1);
				Path[] pathsid12id2 = new Path[maxnum+1];
				Path pathid12id2 = new Path(this.real_id1, this.real_id2,this.weight);
				for(int i=0;i<pathsid12id2.length;i++){
					try {
						pathsid12id2[i] = new Path(pathid12id2, pathsid22idt[i]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						pathsid12id2[i] = new Path();
					}
				}
				return pathsid12id2;
			}
			//path[i]为i步到终点
			Path[] shortpathSet = new Path[maxnum+1];

			for (Integer node : canvisit) {

				//到中间节点node的路径集合
				Path[] paths = path2vnMap.get(node.toString());

				//fn=min(D+f(n-1)) D到中间节点的距离，fn-1是迭代得到的中间到最终
				//对每一条开始start到中间node的路径循环
				Path lastpath = null;
				for (int i = maxnum ; i >0 ; i--) {
					Path path = paths[i];//开始到中间
					if(path==null||(lastpath!=null&&path.count==lastpath.count)){
						//上一个path跟现在一样
						continue;
					}
					lastpath = path;
					//下一次迭代可以访问的节点
					if (path.weight<Integer.MAX_VALUE) {
						//路径存在
						int D = path.weight;
						Path[] pathnode2t = vnallMap.get((Integer)node).path2vnMap.get(String.valueOf(_am.getEnd()));//中间到结束

						int nextcount = maxnum-path.count+1;
						//i是path的count，node到end的count应该为maxnum-i+1
						Path path2t = pathnode2t[nextcount];
						if(path2t.weight==Integer.MAX_VALUE||D==Integer.MAX_VALUE){
							continue;
						}

						List<Integer> canvisit2 = new ArrayList<Integer>();
						canvisit2.addAll(canvisit);
						canvisit2.remove(node);//移出这次的node
						List<Integer> pastNodes = new ArrayList<Integer>();
						pastNodes.addAll(path.nodes);
						for (int j = 1; j < pastNodes.size()-1; j++) {
							virtualNode pastNode = vnallMap.get((Integer)pastNodes.get(j));
							if(pastNode!=null&&pastNode.real_id2>=0&&pastNodes.get(j+1)==pastNode.real_id2){
								for(int ci=0;ci<canvisit2.size();ci++){
									if(canvisit2.get(ci)==pastNodes.get(j)){
										canvisit2.remove(ci);
										break;
									}
								}
								for(int ci=0;ci<canvisit2.size();ci++){
									if(canvisit2.get(ci)==pastNode.real_id2){
										canvisit2.remove(ci);
										break;
									}
								}
							}else if (pastNode!=null&&pastNode.real_id2<0&&canvisit.contains(pastNodes.get(j))) {
								for(int ci=0;ci<canvisit2.size();ci++){
									if(canvisit2.get(ci)==pastNodes.get(j)){
										canvisit2.remove(ci);
										break;
									}
								}
							}
						}


						//对当前id1到node路径求解合适值
						//node到end的路径集合
						Path[] subPathSet = vnallMap.get(node).VNSK(canvisit2,nextcount);
						Path lastsubPath = null;
						for(int j = nextcount ; j > 0 ; j--){
							Path subPath = subPathSet[j];//中间到end
							//上一次的subPath跟这一次一样
							if(subPath==null||(lastsubPath!=null&&subPath.count==lastsubPath.count)){
								continue;
							}
							lastsubPath = subPathSet[j];
							
							if(subPath.weight<Integer.MAX_VALUE){
								//路径存在
								int f = subPath.weight;
								int fn = D+f;
								int counttotalpath = path.count+subPath.count-1;
								//
								if(shortpathSet[counttotalpath]==null){
									boolean insert = true;
									for(int bIndex = 1;bIndex<counttotalpath;bIndex++){
										if(shortpathSet[bIndex]!=null&&shortpathSet[bIndex].weight<fn){
											insert = false;
											break;
										}
									}
									if(insert){
										try {
											shortpathSet[counttotalpath] = new Path(path, subPath);
										} catch (Exception e) {
										}
									}
								}else if(counttotalpath<=maxnum&&fn<shortpathSet[counttotalpath].weight){
									//如果小于当前解index节点的权值
									try {
										shortpathSet[counttotalpath] = new Path(path, subPath);
									} catch (Exception e) {
									}
									int index = counttotalpath;
									index++;
									for(;index<maxnum+1;index++){
										if(shortpathSet[index]==null){
											shortpathSet[index]=null;
										}else if(fn<=shortpathSet[index].weight){
											shortpathSet[index]=null;
										}
									}
								}
							}
						}
					}
				}
			}

			return shortpathSet;
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
	 * 入口，测试使用
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
	 * 入口-virtualnode法---停止使用
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

		int epoch = vnallMap.get(_am.getStart()).search(1,isvisited2);
		System.out.println("end ! epoch size = "+epoch);

		/*List<Integer> canvisit = new ArrayList<Integer>();
		for (virtualNode allnode : vnallList) {
			canvisit.add(allnode.real_id1);
		}
		canvisit.remove((Integer)_am.getStart());
		canvisit.remove((Integer)_am.getEnd());
		String pathRoute = converPathtoName2(vnallMap.get(_am.getStart()).SK66(canvisit));
		System.out.println();
		System.out.println(pathRoute);*/

	}

	
	/**
	 * 入口，测试DijS
	 */
	public void searchroad_DijS() {
		int start = _am.getStart();
		int end = _am.getEnd();
		int maxnum = 9;
		Path[][] shortPath2 = DijS(_am.getEdges(), start,maxnum);
		//printMatrix(_am.getEdges(), "edges");

		int vertexcount = _am.getEdges().length;
		for (int i = 0; i < vertexcount; i++) {
			//String reString = _am.getVertexList().get(Integer.parseInt(shortPath2[i]));
			System.out.println("from " + converPathtoName(String.valueOf(start)) 
			+ " to " + converPathtoName(String.valueOf(i)) + " route= ");
			for(int max = 1;max<maxnum;max++){
				Path p = shortPath2[i][max];
				if (p!=null&&p.weight<Integer.MAX_VALUE) {
					System.out.println("\t"+p.count+"nodes "+"\t"+p.weight+"length "+ converPathtoName2(p.nodes));
				}
			}
		}
	}
	
	/**
	 * 算法入口
	 * @param maxnum:最长搜索节点数目
	 */
	public void searchroad_vnsk(int maxnum) {
		// 必过点添加到vs vnall集合
		for (int i = 0; i < _am.getFruitRoomList().size(); i++) {
			virtualNode _vNode = new virtualNode();
			_vNode.real_id1 = _am.getFruitRoomList().get(i);

			vnallList.add(_vNode);
			vsList.add(_vNode);

			vnallMap.put(_vNode.real_id1, _vNode);
			vsMap.put(_vNode.real_id1, _vNode);
		}
		//add vs-arc 边集合
		for (int i = 0; i < _am.getRewardPath().size(); i++) {
			int[] temp_line = _am.getRewardPath().get(i);//获得一条必过边
			int s1 = temp_line[0];
			int s2 = temp_line[1];//一条必过边的两点拆成两个虚拟点
			virtualNode _vNode1 = new virtualNode();
			_vNode1.real_id1 = s1;//以S1-S2的顺序插入一次
			_vNode1.real_id2 = s2;
			_vNode1.weight = _am.getEdges()[s1][s2];
			
			vnallList.add(_vNode1);
			vsList.add(_vNode1);
			vnallMap.put(_vNode1.real_id1, _vNode1);
			vsMap.put(_vNode1.real_id1, _vNode1);

			virtualNode _vNode2 = new virtualNode();
			_vNode2.real_id1 = s2;//以S2-S1的顺序再插入一次
			_vNode2.real_id2 = s1;
			_vNode2.weight = _am.getEdges()[s2][s1];

			vnallList.add(_vNode2);
			vsList.add(_vNode2);
			vnallMap.put(_vNode2.real_id1, _vNode2);
			vsMap.put(_vNode2.real_id1, _vNode2);
		}
		//add source target
		virtualNode _vNode_s = new virtualNode();
		_vNode_s.real_id1 = _am.getStart();
		vnallList.add(_vNode_s);
		vnallMap.put(_vNode_s.real_id1, _vNode_s);

		virtualNode _vNode_t = new virtualNode();
		_vNode_t.real_id1 = _am.getEnd();
		vnallList.add(_vNode_t);
		vnallMap.put(_vNode_t.real_id1, _vNode_t);

		//List<Integer> mustNodeIds = new ArrayList<>();//声明必过节点
		//for (virtualNode n : vnallList) { //
		//	mustNodeIds.add(n.real_id1);//复制到必过节点
		//}

		//遍历所有vnall
		int vnid1,vnid2;
		for (virtualNode vn1 : vnallList) {
			vnid1 = vn1.real_id1;
			//maxnum节点内能到的路径集合
			//get dij path
			Path[][] shortPathSet = DijS(_am.getEdges(),vnid1,maxnum);
			
			for (virtualNode vn2 : vnallList) {//拷贝进入vn1节点自带的路径数组path2vnmap里，供后面调用。
				vnid2 = vn2.real_id1;
				vn1.path2vnMap.put(String.valueOf(vnid2), shortPathSet[vnid2]);
			}
		}
		
		List<Integer> canvisit = new ArrayList<Integer>();//声明任务节点集合
		for (virtualNode allnode : vnallList) {
			canvisit.add(allnode.real_id1);//赋值
		}
		canvisit.remove((Integer)_am.getStart());//移除开始和结尾节点
		canvisit.remove((Integer)_am.getEnd());

		Path[] realroute = vnallMap.get(_am.getStart()).VNSK(canvisit,maxnum);//调用开始节点的VNSK方法递归寻路返回路径数组
		
		
		
		System.out.println("-----------------算法计算结果start：--------------------------------");
		System.out.println("计算最多 "+maxnum+" 点内"
				+converPathtoName(String.valueOf(_am.getStart()))
				+" 到 "
				+converPathtoName(String.valueOf(_am.getEnd()))
				+" 的路径：");
		
		boolean find = false;
		for (int i = maxnum ; i>0 ; i--) {//从高到低检查数组是否含有路径生成，realroute[i]的下标i代表最大经过i点的最短路径
			Path path = realroute[i];
			if (path != null) {//有路径
				String pathRoute = converPathtoName2(path.nodes);
				System.out.println("长度："+path.weight+"\t节点个数："+path.count);
				System.out.println("优化路径="+pathRoute);
				find = true;
				break;
			}
		}
		if (!find) {//未生成路径
			System.out.println("can't reach within "+maxnum+" nodes 建议更换更小的数目限制尝试");
		}
		System.out.println("-----------------算法计算结果end--------------------------------");

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
	 * 原生DIJ算法
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

	/**
	 * 返回全部中间结果的DIJ算法
	 * @param weight1
	 * @param start
	 * @param maxnodenum
	 * @return
	 */
	private Path[][] DijS(int[][] weight1, int start,int maxnodenum) {
		int[][] weight =arraysCopy(weight1);
		int n = weight.length; // 顶点个数
		Path[][] pathSet = new Path[n][maxnodenum+1];
		for (int i = 0; i < pathSet.length; i++) {
			Path[] paths = pathSet[i];
			for (int j = 0; j < paths.length; j++) {
				paths[j] = new Path();

			}
		}
		int times = Integer.min(n, maxnodenum);
		for(int max = 1;max<=times;max++){
			// 接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）
			// 返回一个int[] 数组，表示从start到它的最短路径长度
			ArrayList<Integer> hasvisited = new ArrayList<Integer>();
			ArrayList<Integer> novisited = new ArrayList<Integer>();
			for (int i = 0; i < n; i++) {
				novisited.add(i);
			}
			// 初始化，第一个顶点求出
			hasvisited.add(start);
			novisited.remove(start);

			pathSet[start][max] = new Path(start, start,  0);

			for(int i=0;i<n;i++){
				if ( weight[start][i] < Integer.MAX_VALUE && max>1) {
					pathSet[i][max] = new Path(start, i,  weight[start][i]);
				}else if(i!=start){
					pathSet[i][max] = new Path(start, i,  Integer.MAX_VALUE);
				}
			}
			// 要加入n-1个顶点
			for (int count = 1; count <= n-1; count++) {
				int k = -1; // 选出一个距离初始顶点start最近的未标记顶点
				int dmin = Integer.MAX_VALUE;//寻找最小的节点i
				for (Integer i : novisited) {
					if (pathSet[i][max].weight < dmin&&pathSet[i][max].count<max) {
						dmin = pathSet[i][max].weight ;
						k = i;
					}
				}

				if (k<0) {
					break;
				}

				// 将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin
				hasvisited.add((Integer)k);
				novisited.remove((Integer)k);
				// 以k为中间点，修正从start到未访问各点的距离
				for (Integer i : novisited) {
					Path temp = new Path(pathSet[k][max],i,weight[k][i]);
					if ((temp.weight < pathSet[i][max].weight) ){
						pathSet[i][max] = new Path(pathSet[k][max],i,weight[k][i]);
					}
				}
			}
		}

		if(maxnodenum>n){
			for (int i = 0; i < pathSet.length; i++) {
				for (int j = n+1; j < maxnodenum+1; j++) {
					pathSet[i][j] = pathSet[i][n];
				}
			}
		}
		return pathSet;
	}





	/* ***********私有接口 end *************************/

}
