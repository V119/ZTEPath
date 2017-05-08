package xjtu.se.hmbb.dataStruct;

import java.io.*;
import java.util.*;

/**
 * Created by haoyang on 2017/5/6.
 */
public class AMGraph {
    private List<String> vertexList;                 //点的集合
    private int [][] edges;                         //存储边的矩阵
    private int numOfEdges;                         //边的个数
    private int start;                              //开始节点的下标
    private int end;                                //结束节点的下标
    private List<Integer> fruitRoomList;            //水果间，必经过的节点下标
    private List<int[]> anteaterPath;           //食蚁兽所在的边
    private List<int[]> rewardPath;              //带有奖励的边

    public AMGraph(File file) {
        this.numOfEdges = 0;
        this.vertexList = new ArrayList<>();
        this.fruitRoomList = new ArrayList<>();
        this.anteaterPath = new ArrayList<>();
        this.rewardPath = new ArrayList<>();

        int sentry = 0;

        Map<String, Integer> nodeToIndex = new HashMap<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line = "";
            while((line = reader.readLine()) != null) {
                if(line.trim().equals("")) {
                    sentry++;
                    continue;
                }

                String[] charList = line.trim().split(" ");
                // 如果哨兵值为0，获取节点个数,并初始化权重的二维数组
                if(sentry == 0) {
                    int numOfIndex = Integer.parseInt(charList[0]);
                    this.edges = new int[numOfIndex][numOfIndex];
                    for(int i = 0; i < numOfIndex; i++) {
                        for(int j = 0; j < numOfIndex; j++) {
                            edges[i][j] = Integer.MAX_VALUE;
                        }
                    }
                } else if(sentry == 1) {  // 如果哨兵值为0，读取边和节点
                    int startIndex = 0;
                    int endIndex = 0;

                    if(nodeToIndex.containsKey(charList[0])) {
                        startIndex = nodeToIndex.get(charList[0]);
                    } else {
                        startIndex = this.vertexList.size();

                        //获取初始节点和结束节点的下标
                        if (charList[0].equals("S")) {
                            this.start = startIndex;
                        } else if (charList[0].equals("E")) {
                            this.end = startIndex;
                        }

                        nodeToIndex.put(charList[0], startIndex);
                        this.vertexList.add(charList[0]);
                    }

                    if(nodeToIndex.containsKey(charList[1])) {
                        endIndex = nodeToIndex.get(charList[1]);
                    } else {
                        endIndex = this.vertexList.size();

                        if (charList[1].equals("S")) {
                            this.start = endIndex;
                        } else if (charList[1].equals("E")) {
                            this.end = endIndex;
                        }

                        nodeToIndex.put(charList[1], endIndex);
                        this.vertexList.add(charList[1]);
                    }

                    this.numOfEdges++;

                    //将边的权重加入edge中
                    int weight = Integer.parseInt(charList[2]);
                    this.edges[startIndex][endIndex] = weight;
                    this.edges[endIndex][startIndex] = weight;
                } else if(sentry == 2) { // 如果哨兵值为1，读取食蚁兽路径
                    int[] anteaterPathList = new int[2];
                    anteaterPathList[0] = nodeToIndex.get(charList[0]);
                    anteaterPathList[1] = nodeToIndex.get(charList[1]);
                    this.anteaterPath.add(anteaterPathList);
                } else if(sentry == 3) { //如果哨兵值为2，读取奖励的必过路径
                    int[] rewardPathList = new int[2];
                    rewardPathList[0] = nodeToIndex.get(charList[0]);
                    rewardPathList[1] = nodeToIndex.get(charList[1]);
                    this.rewardPath.add(rewardPathList);
                } else if(sentry == 4) { //如果哨兵值为3， 读取水果间
                    for(String node: charList) {
                        this.fruitRoomList.add(nodeToIndex.get(node));
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
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

    public List<int[]> getAnteaterPath() {
        return anteaterPath;
    }

    public void setAnteaterPath(List<int[]> anteaterPath) {
        this.anteaterPath = anteaterPath;
    }

    public List<int[]> getRewardPath() {
        return rewardPath;
    }

    public void setRewardPath(List<int[]> rewardPath) {
        this.rewardPath = rewardPath;
    }
}
