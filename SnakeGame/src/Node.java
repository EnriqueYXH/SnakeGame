import java.util.Random;

/*
 * 节点类：每一条蛇是由若干个节点组成的，每一个节点有横纵坐标来确定位置
 */
public class Node {
    private int x;
    private int y;

    public Node() {
    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {return x;}
    public int getY() {return y;}

    //随机生成位置的方法
    public void random(){
        //创建Random对象 并随机生成横纵坐标
        Random seed = new Random();
        this.x = seed.nextInt(MainFrame.width);
        this.y = seed.nextInt(MainFrame.height);
    }
}
