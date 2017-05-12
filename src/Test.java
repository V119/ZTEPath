import xjtu.se.hmbb.dataStruct.AMGraph;

import java.io.File;

import com.xjtu.se.Roadsearch;

/**
 * Created by haoyang on 2017/5/6.
 * 
 * 仅供功能测试
 */
public class Test {
    public static void main(String[] args) {
    	File file = new File("C:\\Users\\chensharp\\workspace\\ZTEPath\\src\\graph.txt");
        long startTime = System.currentTimeMillis();
        AMGraph am = new AMGraph(file);
        Roadsearch roadsearch = new Roadsearch(am);
		roadsearch.searchroad_DijS();
        
        
        
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}