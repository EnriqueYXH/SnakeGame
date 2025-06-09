import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class MainFrame extends JFrame {
    private Snake snake; //蛇
    private JPanel jPanel; //游戏棋盘
    private Timer timer; //定时器，在规定的时间内调用蛇移动的方法
    public static Node food; //食物
    public static int width;
    public static int height;
    public static int GRID_SIZE;
    public static int period;
    public static int x0;
    public static int y0;
    public static int choice;

    public MainFrame() throws HeadlessException {
        //初始化窗体参数
        initFrame();
        //初始化游戏棋盘
        initGamePanel();
        //初始化蛇
        initSnake();
        //初始化食物
        initFood();
        //初始化定时器
        initTimer();
        //设置键盘监听，让蛇随着上下左右方向移动
        setKeyListener();
    }

    //初始化窗体参数
    private void initFrame() {
        // 使用对话框让用户选择棋盘大小
        String[] options = {"小 (10x10)", "中 (30x30)"};
        choice = JOptionPane.showOptionDialog(
                this,
                "请选择棋盘大小:",
                "棋盘大小选择",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]  // 默认选中"中"
        );
        // 根据用户选择设置棋盘大小
        switch (choice) {
            case 0: // 小
                width = 10;
                height = 10;
                GRID_SIZE = 60;
                period = 200;
                x0 = 550;
                y0 = 200;
                break;
            case 1: // 中
                width = 30;
                height = 30;
                GRID_SIZE = 30;
                period = 100;
                x0 = 450;
                y0 = 50;
                break;
            default: // 用户关闭对话框或没有选择，使用默认值
                width = 30;
                height = 30;
                GRID_SIZE = 30;
                period = 100;
                x0 = 450;
                y0 = 50;
        }
        int wid = 0, het = 0;
        if (choice == 0) {
            wid = (width + 1) * GRID_SIZE - 46;
            het = (height + 1) * GRID_SIZE - 25;
        }
        if (choice == 1) {
            wid = (width + 1) * GRID_SIZE - 16;
            het = (height + 1) * GRID_SIZE + 2;
        }
        setSize(wid,het);
        //设置窗体的位置
        setLocation(x0, y0);
        //设置关闭按钮的作用（退出程序）
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗体大小不可变
        setResizable(false);
    }
    //初始化食物
    private void initFood() {
        food = new Node();
        food.random();
    }

    //设置键盘监听
    private void setKeyListener() {
        addKeyListener(new KeyAdapter() {
            //当键盘按下时，会自动掉此方法
            @Override
            public void keyPressed(KeyEvent e) {
                //键盘中的一个键都有一个编号
                switch (e.getKeyCode()){

                    case KeyEvent.VK_UP: //上键
                        if(snake.getDirection()!=Direction.DOWN){
                            snake.setDirection(Direction.UP);
                        }
                        break;
                    case KeyEvent.VK_DOWN: //下键
                        if(snake.getDirection()!=Direction.UP){
                            snake.setDirection(Direction.DOWN);
                        }
                        break;
                    case KeyEvent.VK_LEFT: //左键
                        if(snake.getDirection()!=Direction.RIGHT){
                            snake.setDirection(Direction.LEFT);
                        }
                        break;
                    case KeyEvent.VK_RIGHT: //右键
                        if(snake.getDirection()!=Direction.LEFT){
                            snake.setDirection(Direction.RIGHT);
                        }
                        break;
                }
            }
        });
    }

    //初始化定时器
    private void initTimer() {
        //创建定时器对象
        timer=new Timer();
        //初始化定时任务
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                snake.move();/*
                //判断蛇头是否和食物重合
                Node head = snake.getBody().getFirst();
                if(head.getX() == food.getX() && head.getY() == food.getY()){
                    snake.eat(food);
                    food.random();
                }
                */
                //重绘游戏棋盘
                jPanel.repaint();
            }
        };
        //每period毫秒，执行一次定时任务
        timer.scheduleAtFixedRate(timerTask,0,period);
    }

    private void initSnake() {
        snake = new Snake();
    }

    //初始化游戏棋盘
    private void initGamePanel() {
        jPanel = new JPanel(){
            // 加载蛇头图片
            Image headImage = new ImageIcon(getClass().getResource("/snake_head.png")).getImage();

            @Override
            public void paint(Graphics g) {
                //清空棋盘
                g.clearRect(0,0,width * GRID_SIZE,height * GRID_SIZE);
                //绘制横线
                for (int i = 0; i <= width; i++) {
                    g.drawLine(0,i * GRID_SIZE,height * GRID_SIZE,i * GRID_SIZE);
                }
                //绘制竖线
                for (int i = 0; i <= height; i++) {
                    g.drawLine(i * GRID_SIZE,0,i * GRID_SIZE,width * GRID_SIZE);
                }
                //绘制蛇
                LinkedList<Node> body = snake.getBody();
                for (int i = 0; i < body.size(); i++) {
                    Node node = body.get(i);
                    if (i == 0) {
                        // 蛇头使用图片绘制
                        // 根据方向旋转图片
                        Image rotatedHead = rotateHeadImage(headImage, snake.getDirection());
                        g.drawImage(rotatedHead,
                                node.getX() * GRID_SIZE,
                                node.getY() * GRID_SIZE,
                                GRID_SIZE, GRID_SIZE, null);
                    } else {
                        // 身体部分仍然用矩形绘制
                        g.fillRect(node.getX() * GRID_SIZE,
                                node.getY() * GRID_SIZE,
                                GRID_SIZE, GRID_SIZE);
                    }
                }
                //绘制食物
                g.fillRect(food.getX() * GRID_SIZE,food.getY() * GRID_SIZE, GRID_SIZE,GRID_SIZE);
            }

            // 根据蛇的方向旋转图片
            private Image rotateHeadImage(Image original, Direction direction) {
                BufferedImage rotated = new BufferedImage(GRID_SIZE, GRID_SIZE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = rotated.createGraphics();

                // 计算旋转角度
                double angle = 0;
                switch(direction) {
                    case UP: angle = Math.toRadians(0); break;
                    case RIGHT: angle = Math.toRadians(90); break;
                    case DOWN: angle = Math.toRadians(180); break;
                    case LEFT: angle = Math.toRadians(270); break;
                }

                // 旋转并绘制图像
                g2d.translate(GRID_SIZE/2, GRID_SIZE/2);
                g2d.rotate(angle);
                g2d.drawImage(original, -GRID_SIZE/2, -GRID_SIZE/2, GRID_SIZE, GRID_SIZE, null);
                g2d.dispose();

                return rotated;
            }
        };
        add(jPanel);
    }
    public static void main(String[] args) {
        //创建窗体对象，并显示
        new MainFrame().setVisible(true);
    }
}
