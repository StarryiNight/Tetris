package com.russia.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TetrisPanel extends JPanel implements KeyListener {
    /**
     * scores 分数
     * lines 消去行数
     * speed 速度 最大100
     * version 选择的难度(speed:简单0 普通:25 困难:50)
     */
    int scores;
    int lines;
    double speed;
    int version;
    int index = 0;
    //背景格子 大小为10*20
    Block[][] wall = new Block[20][10];
    TetrisBlock tetrisBlock;      //下落中的俄罗斯方块
    TetrisBlock nextOne;       //下一个随机的俄罗斯方块

    final static int RUNNING = 0;
    final static int PAUSE = 1;
    final static int GAME_OVER = 2;
    int state;//游戏状态 running游戏中   pause暂停  game-over 游戏结束

    //背景图片
    Image backgroundImage, gameOverImage, pauseImage;
    //不同类型的俄罗斯方块
    static Image T, I, J, L, O, S, Z;
    //登陆的用户名
    String userName;
    //该用户的最大分数
    int MaxScore;

    //每个格子的边长
    public final static int CELL_SIZE = 26;

    //加载每种方块的图片
    static {
        T = new ImageIcon("image/T.png").getImage();
        I = new ImageIcon("image/I.png").getImage();
        J = new ImageIcon("image/J.png").getImage();
        L = new ImageIcon("image/L.png").getImage();
        O = new ImageIcon("image/O.png").getImage();
        S = new ImageIcon("image/S.png").getImage();
        Z = new ImageIcon("image/Z.png").getImage();
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setGameRun() {
        state = RUNNING;
    }

    public void setGameOver() {
        state = GAME_OVER;
    }

    public void setGamePause() {
        state = PAUSE;
    }

    /*游戏结束时的按键操作*/
    public void processGameOverKey(int key) {
        //S：重新开始   Q: 游戏退出
        switch (key) {
            case KeyEvent.VK_S:
                newGame();
                break;
            case KeyEvent.VK_Q:
                System.exit(0);
                break;
            default:
                break;

        }
    }

    /*游戏暂停时的按键操作*/
    public void processPauseKey(int key) {
        //C：继续     Q：退出
        if (key == KeyEvent.VK_S) {
            state = RUNNING;
        } else if (key == KeyEvent.VK_Q) {
            System.exit(0);
        }
    }

    //开始新的游戏
    public void newGame() {
        scores = 0;
        lines = 0;
        tetrisBlock = TetrisBlock.randomOne();
        nextOne = TetrisBlock.randomOne();
        state = RUNNING;
        index = 0;
        wall = new Block[20][10];
        MaxScore = new DbConnection().maxScore(userName);
    }

    //构造方法,传入用户名和最大分数
    public TetrisPanel(String userName, int MaxScore) {
        this.userName = userName;
        this.MaxScore = MaxScore;
        backgroundImage = new ImageIcon("image/tetris.png").getImage();
        gameOverImage = new ImageIcon("image/game-over.png").getImage();
        pauseImage = new ImageIcon("image/pause.png").getImage();
    }

    //重写paint方法
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘出背景图片
        g.drawImage(backgroundImage, 0, 0, null);
        //把坐标系平移15 方便后面计算
        g.translate(15, 15);
        //20*10的背景格子,使玩家更清楚观察方块下落位置
        paintWallBlock(g);
        //绘制下落中的俄罗斯方块
        paintDowningTetris(g);
        //在右上角方框绘制下一个方块
        paintNextTetris(g);
        //绘制分数
        paintScore(g);
        //最后绘制状态
        paintState(g);
    }



    //通过游戏状态来绘制不同状态的背景
    public void paintState(Graphics g) {
        if (state == GAME_OVER) {
            g.drawImage(gameOverImage, -15, -15, null);
        } else if (state == PAUSE) {
            g.drawImage(pauseImage, -15, -15, null);
        }
    }

    //绘制下一个方块
    public void paintNextTetris(Graphics g) {
        Block[] blocks = nextOne.blocks;
        for (int i = 0; i < tetrisBlock.blocks.length; i++) {
            Block block = blocks[i];
            g.drawImage(block.image, block.column * CELL_SIZE + 260, block.row * CELL_SIZE, null);
        }
    }

    //绘制墙
    public void paintWallBlock(Graphics g) {
        /*
         *  遍历wall二维数组，得到，每一个cell对象，如果cell对象为null，就在
         *  它的cow和rol的位置画一个正方形；如果cell对象不是null，就在它的cow和
         *  rol的位置画cell.image这个图片
         * */
        for (int row = 0; row < wall.length; row++) {
            for (int col = 0; col < wall[row].length; col++) {
                Block block = wall[row][col];
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                if (block == null) {
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    g.drawImage(block.image, x - 1, y - 1, null);
                }
            }
        }

    }


    //绘制正在下落的方块
    public void paintDowningTetris(Graphics g) {
        /*
         * 画出这个方块的四个cell，要得到每一个cell的坐标，然后用这四对坐标
         * 画出cell.imaage
         * */
        for (int i = 0; i < tetrisBlock.blocks.length; i++) {
            Block block = tetrisBlock.blocks[i];
            int x = block.column * CELL_SIZE;
            int y = block.row * CELL_SIZE;
            g.drawImage(block.image, x - 1, y - 1, null);
        }
    }

    public void action() {
        tetrisBlock = TetrisBlock.randomOne();
        nextOne = TetrisBlock.randomOne();
        state = 1;
        //利用定时器，每隔一段时间便执行一次任务
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        speed = 40 - (lines / 1) - version;
                        speed = Math.max(speed, 1);
                        //控制速度,防止下落太快
                        if (state == RUNNING && index++ % speed == 0) {
                            slowDropAction();
                        }
                        repaint();  //重绘
                    }
                }
                , 0, 10, TimeUnit.MILLISECONDS);

    }

    //正常自然慢速下落
    public void slowDropAction() {
        //下面没有方块
        if (canDrop()) {
            tetrisBlock.drop();
        } else {//被挡
            fixation();//下落到方块上
            cleanLines();//是否可以清楚下面的方块
            if (isGameOver()) {
                state = GAME_OVER;
                if (scores > MaxScore) {
                    int n = JOptionPane.showConfirmDialog(null, "新纪录!是否保存分数?", "新纪录!", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        if (new DbConnection().saveScore(userName, scores)) {
                            JOptionPane.showMessageDialog(null, "保存成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "保存失败", "失败", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } else {
                    int n = JOptionPane.showConfirmDialog(null, "游戏结束,是否重新开始", "游戏结束", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                            newGame();
                    }
                }
            } else {

                //游戏没有结束,继续产生方块,把下一个的方块赋值给下落中的 继续循环
                tetrisBlock = nextOne;
                nextOne = TetrisBlock.randomOne();
            }
        }
    }


    /*清除可以消去的行*/
    public void cleanLines() {
        //只要某一行的格子满了 就可以清除 ,即某一行都不为空
        for (int row = 0; row < wall.length; row++) {
            //循环把上一行的元素往下移动,即可清除该行
            if (IsCellsFull(row)) {
                deleteRow(row);
                //清除后得分增加
                scores += Math.max(1, version / 2);
                lines++;
            }
        }
    }

    /*判断改行是否已经全满了*/
    public boolean IsCellsFull(int row) {
        //如果有一个格子没满 就返回false
        for (int col = 0; col < wall[row].length; col++) {
            if (wall[row][col] == null) {
                return false;
            }
        }
        return true;
    }

    /*删除某一行*/
    public void deleteRow(int row) {
        //index是从上面开始的,所以把row-1赋值给row 即为把 上一行的赋值给该行
        for (int i = row; i > 1; i--) {
            //调用Arrays.copyof方法
            wall[i] = Arrays.copyOf(wall[i - 1], 10);
        }
        //第一行赋值为空
        Arrays.fill(wall[0], null);
    }

    /*判断游戏是否已经够结束*/
    public boolean isGameOver() {
        Block[] blocks = nextOne.blocks;
        //判断下一个方块下方是否为空 不为空则游戏结束
        for (Block block : blocks) {
            if (wall[block.row][block.column] != null) {
                return true;
            }
        }
        return false;
    }

    /*判断方块是否可以下落*/
    public boolean canDrop() {
        //如果到了底部或者下面已经有方块 则不能继续下落
        Block[] blocks = tetrisBlock.blocks;
        for (Block block : blocks) {
            if (block.row == 19) {
                return false;
            }
            if (wall[block.row + 1][block.column] != null) {
                return false;
            }
        }
        return true;
    }

    /*下落固定*/
    public void fixation() {
        /*当前的四个方块的值赋值给对应的墙
         * */
        Block[] blocks = tetrisBlock.blocks;
        for (Block block : blocks) {
            wall[block.row][block.column] = block;
        }
    }

    //重写键盘控制方法
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        // 根据不同的状态来处理不同的逻辑
        switch (state) {
            case RUNNING:
                processRunningKey(key);
                break;
            case GAME_OVER:
                processGameOverKey(key);
                break;
            case PAUSE:
                processPauseKey(key);
                break;
            default:
                break;
        }
    }

    /*按键操作*/
    public void processRunningKey(int key) {
        //按键操作说明: p:暂停 q:退出游戏 左右方向键:控制方向 上方向键:变换形态 下方向键:加速下落 空格:直接落下
        switch (key) {
            case KeyEvent.VK_P:
                state = PAUSE;
                break;
            case KeyEvent.VK_Q:
                System.exit(0);
                break;
            case KeyEvent.VK_LEFT:
                moveLeftAction();
                break;
            case KeyEvent.VK_RIGHT:
                moveRightAction();
                break;
            case KeyEvent.VK_UP:
                rotateRightAction();
                break;
            case KeyEvent.VK_DOWN:
                slowDropAction();
                break;
            case KeyEvent.VK_SPACE:
                directDropAction();
                break;
            case KeyEvent.VK_Z:
                rotateLeftAction();
                break;
            default:
                break;
        }
    }

    /*向右旋转*/
    public void rotateRightAction() {
        tetrisBlock.rotateRight();
//        tetrisBlock.rotateRight();
        if (outOfBounds() || wardOff()) {
            rotateLeftAction();
        }
    }

    /*顺时针旋转*/
    public void rotateLeftAction() {
//        tetrisBlock.rotateRight();
          tetrisBlock.rotateLeft();
        if (outOfBounds() || wardOff()) {
            rotateRightAction();
        }
    }

    /*直接掉落下去*/
    public void directDropAction() {
        //下落到不能下落为止
        while (canDrop()) {
            tetrisBlock.drop();
        }
        //下落 判定消除 新方块产生
        fixation();
        cleanLines();
        tetrisBlock = nextOne;
        nextOne = TetrisBlock.randomOne();
    }

    /*绘制分数  得分  历史最高分等等*/
    public void paintScore(Graphics g) {
        //消除得分
        int x = 290;
        int y = 160;
        g.setColor(Color.BLUE);
        g.setFont(new Font("", Font.BOLD, 25));
        g.drawString("SCORES：" + scores, x, y);
        y += 56;
        g.drawString("SPEED：" + (100 - speed * 2.5), x, y);
        y += 56;
        g.drawString("LINES：" + lines, x, y);
        y += 56;
        g.drawString("MAX：" + MaxScore, x, y);
    }

    /*左移流程控制*/
    public void moveLeftAction() {
        //判断能否左移，如果越界了，或者被别的格子挡道了，就右移一下修正回来
        tetrisBlock.moveLeft();//先左移
        //如果越界或者被挡
        if (outOfBounds() || wardOff()) {
            tetrisBlock.moveRight();
        }
    }

    /*右移流程控制*/
    public void moveRightAction() {
        TetrisBlock a = tetrisBlock;
        a.moveRight();
//        a.moveLeft();
        //tetrisBlock.moveRight();//先右移
        if (outOfBounds() || wardOff()) {
            tetrisBlock.moveLeft();
        }
    }

    //判断是否越界
    public boolean outOfBounds() {
        //方块的四个cell的任意一个cell的col<0或者col>9
        Block[] blocks = tetrisBlock.blocks;
        for (Block block : blocks) {
            if (block.column < 0 || block.column > 9) {
                return true;
            }
        }
        return false;
    }

    //判断是否被挡
    public boolean wardOff() {
        //任一方块对应墙上不为null 即被挡住
        Block[] blocks = tetrisBlock.blocks;
        for (Block block : blocks) {
            if (wall[block.row][block.column] != null) {
                return true;
            }
        }
        return false;
    }




    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}

