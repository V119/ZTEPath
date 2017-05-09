package com.xjtu.se;

import xjtu.se.hmbb.dataStruct.AMGraph;

/**
 * 主程序
 * @author chensharp 
 * 2017/5/8
 */
public class Roadsearch {
	//data start
	private AMGraph _am;//图的所有数据
	
	
	
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
	 * 入口
	 */
	public void searchroad() {
		int start = _am.getStart();
		int end = _am.getEnd();
		String[] shortPath2 = Dij(_am.getEdges(), start);
		
		printMatrix(_am.getEdges(), "edges");
		
		int count = _am.getEdges().length;
		for (int i = 0; i < count; i++) {
			//String reString = _am.getVertexList().get(Integer.parseInt(shortPath2[i]));
			String paths = converPathtoName(shortPath2[i]);
			System.out.println("从" + start + "出发到" + i + "的最短路径为：" + paths);
		}

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
     * 
     * @param weight
     * @param start
     * @return  返回start点到所有点的最短路径的string[],其中【1】是start到1的路径，用-隔开。  
     */
	private String[] Dij(int[][] weight, int start) {
		// 接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）
		// 返回一个int[] 数组，表示从start到它的最短路径长度
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

		// 要加入n-1个顶点
		for (int count = 1; count <= n-1; count++) {
			int k = -1; // 选出一个距离初始顶点start最近的未标记顶点
			int dmin = Integer.MAX_VALUE;//寻找最小的节点i
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][i] < dmin) {
					dmin = weight[start][i];
					k = i;
				}
			}
			//System.out.println("k=" + k);

			// 将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin
			shortPath[k] = dmin;
			visited[k] = 1;

			// 以k为中间点，修正从start到未访问各点的距离
			for (int i = 0; i < n; i++) {
				// System.out.println("k="+k);
				if (visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]) {
					int res =0;
					if(weight[start][k]==Integer.MAX_VALUE || weight[k][i]==Integer.MAX_VALUE){//如果二者有一个是无穷大，即为不可达，结果也为不可达
						res = Integer.MAX_VALUE;
					}else {
						res = (weight[start][k] + weight[k][i]);
					}
					weight[start][i] = res;
					
					path[i] = path[k] + "-" + i;
				}
			}
		}
		return path;
	}
	
	
	
	
	/* ***********私有接口 end *************************/
	
}
