package com.xjtu.se;
import xjtu.se.hmbb.dataStruct.AMGraph;

import java.io.File;
import java.util.Scanner;

/**
 * Created by haoyang on 2017/5/6.
 */
public class Test {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String filename = null;
		File file = new File("C:\\Users\\chensharp\\workspace\\ZTEPath\\src\\graph.txt");
		//File file = new File("/graph.txt");
		if (!file.exists()) {
			System.out.println("找不到文件graph.txt");
			System.out.println("键入Q以退出");
			while (sc.hasNextLine()) {
				String fString = sc.nextLine().toLowerCase();
				if(fString.equals("q")){
					break;
				}
			}
			System.out.println("退出。。。");
			return;
		}
//		File file = null;
//		while (file == null||!file.exists()) {
//			System.out.println("请输入地图文件路径：");
//			filename = sc.nextLine();
//			file = new File(filename);
//		}

		int maxnum =0;
		while(maxnum<2){
			System.out.println("最大点数（大于1的整数）：");
			maxnum = sc.nextInt();
		}

		AMGraph am = new AMGraph(file);

		Roadsearch roadsearch = new Roadsearch(am);
		roadsearch.searchroad_vnsk(maxnum);
		
		System.out.println("键入Q以退出");
		while (sc.hasNextLine()) {
			String fString = sc.nextLine().toLowerCase();
			if(fString.equals("q")){
				break;
			}
		}
		System.out.println("退出。。。");
	}
}
