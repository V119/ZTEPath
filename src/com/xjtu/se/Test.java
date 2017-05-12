package com.xjtu.se;
import xjtu.se.hmbb.dataStruct.AMGraph;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by haoyang on 2017/5/6.
 */
public class Test {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		String filename = null;
//		filename = "graph.txt";
		System.out.println("请输入文件路径：");
		File file = null;
		filename = sc.nextLine();
		file = new File(filename);
		while (!file.exists()) {
			System.out.println("请检查路径，重新输入：");
			filename = sc.nextLine();
			file = new File(filename);
		}
		
		int maxnum =0;
		while(maxnum<2){
			System.out.println("最大点数（大于1的整数）：");
			maxnum = sc.nextInt();
		}
		Date date1 = new Date(); 
		AMGraph am = new AMGraph(file);
		Roadsearch roadsearch = new Roadsearch(am);
		System.out.println("计算"+maxnum+"点内"
				+roadsearch.converPathtoName(String.valueOf(am.getStart()))
				+"到"
				+roadsearch.converPathtoName(String.valueOf(am.getEnd()))
				+"的路径：");
		
		String findPath = roadsearch.searchroad_vnsk(maxnum);
		if (findPath==null) {
			System.out.println("can't reach within "+maxnum+" nodes");
			int mustnum = am.getFruitRoomList().size();
			int allnum = am.getEdges().length;
			maxnum = mustnum*allnum;
			roadsearch = new Roadsearch(am);
			findPath = roadsearch.searchroad_vnsk(maxnum);
			System.out.println();
			if(findPath!=null){
				System.out.println("去掉最多点的约束，可选路径为：");
				System.out.println(findPath);
			}
		}else{
			System.out.println();
			System.out.println(findPath);
		}
		Date date2 = new Date();
		long time = date2.getTime()-date1.getTime();
		System.out.println();
		System.out.println("用时："+time+"ms");
		System.out.println("键入Q以退出");
		while (sc.hasNextLine()) {
			String fString = sc.nextLine().toLowerCase();
			if(fString.equals("q")){
				break;
			}
		}
		System.out.println("退出。。。");
		sc.close();
	}
}
