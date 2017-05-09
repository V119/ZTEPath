package com.xjtu.se;
import xjtu.se.hmbb.dataStruct.AMGraph;

import java.io.File;

/**
 * Created by haoyang on 2017/5/6.
 */
public class Test {
	
    public static void main(String[] args) {
        File file = new File("/Users/haoyang/IdeaProjects/ZTEPath/src/graph.txt");
        AMGraph am = new AMGraph(file);
        
        //int[][] edge = am.getEdges();
        Roadsearch roadsearch = new Roadsearch(am);
        roadsearch.searchroad_vn();
        //roadsearch.searchroad();
        
    }
}
