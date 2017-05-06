package xjtu.se.hmbb.dataStruct;

import java.io.*;
import java.util.List;

/**
 * Created by haoyang on 2017/5/6.
 */
public class AMGraph {
    private List<String> vertexList;                //点的集合
    private int [][] edges;                         //存储边的矩阵
    private int numOfEdges;                         //边的个数
    private int start;                              //开始节点的下标
    private int end;                                //结束节点的下标
    private List<Integer> fruitRoomList;            //水果间，必经过的节点下标
    private List<Integer[]> anteaterPath;           //食蚁兽所在的边
    private List<Integer[]> rewardath;              //带有奖励的边

    public AMGraph(File file) throws IOException {
        this.numOfEdges = 0;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int sentry = 0;
        String line = "";
        while((line = reader.readLine()) != null) {
            if(line.trim().equals("")) {
                sentry++;
            }
            // 如果哨兵值为0，读取边和节点
            if(sentry == 0) {

            } else if(sentry == 1) { // 如果哨兵值为1，读取食蚁兽路径

            } else if(sentry == 2) { //如果哨兵值为2，读取奖励的必过路径

            } else if(sentry == 3) { //如果哨兵值为3， 读取水果间

            }
        }

    }

    public List<String> getVertexList() {
        return vertexList;
    }

    public void setVertexList(List<String> vertexList) {
        this.vertexList = vertexList;
    }

    public int[][] getEdges() {
        return edges;
    }

    public void setEdges(int[][] edges) {
        this.edges = edges;
    }

    public int getNumOfEdges() {
        return numOfEdges;
    }

    public void setNumOfEdges(int numOfEdges) {
        this.numOfEdges = numOfEdges;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<Integer> getFruitRoomList() {
        return fruitRoomList;
    }

    public void setFruitRoomList(List<Integer> fruitRoomList) {
        this.fruitRoomList = fruitRoomList;
    }

    public List<Integer[]> getAnteaterPath() {
        return anteaterPath;
    }

    public void setAnteaterPath(List<Integer[]> anteaterPath) {
        this.anteaterPath = anteaterPath;
    }

    public List<Integer[]> getRewardath() {
        return rewardath;
    }

    public void setRewardath(List<Integer[]> rewardath) {
        this.rewardath = rewardath;
    }
}
