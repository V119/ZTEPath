import xjtu.se.hmbb.dataStruct.AMGraph;

import java.io.File;

/**
 * Created by haoyang on 2017/5/6.
 */
public class Test {
    public static void main(String[] args) {
        File file = new File("/Users/haoyang/IdeaProjects/ZTEPath/src/graph.txt");
        long startTime = System.currentTimeMillis();
        AMGraph am = new AMGraph(file);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
