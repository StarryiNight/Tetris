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
     * scores ����
     * lines ��ȥ����
     * speed �ٶ� ���100
     * version ѡ����Ѷ�(speed:��0 ��ͨ:25 ����:50)
     */
    int scores;
    int lines;
    double speed;
    int version;
    int index = 0;
    //�������� ��СΪ10*20
    Block[][] wall = new Block[20][10];
    TetrisBlock tetrisBlock;      //�����еĶ���˹����
    TetrisBlock nextOne;       //��һ������Ķ���˹����

    final static int RUNNING = 0;
    final static int PAUSE = 1;
    final static int GAME_OVER = 2;
    int state;//��Ϸ״̬ running��Ϸ��   pause��ͣ  game-over ��Ϸ����

    //����ͼƬ
    Image backgroundImage, gameOverImage, pauseImage;
    //��ͬ���͵Ķ���˹����
    static Image T, I, J, L, O, S, Z;
    //��½���û���
    String userName;
    //���û���������
    int MaxScore;

    //ÿ�����ӵı߳�
    public final static int CELL_SIZE = 26;

    //����ÿ�ַ����ͼƬ
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

    /*��Ϸ����ʱ�İ�������*/
    public void processGameOverKey(int key) {
        //S�����¿�ʼ   Q: ��Ϸ�˳�
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

    /*��Ϸ��ͣʱ�İ�������*/
    public void processPauseKey(int key) {
        //C������     Q���˳�
        if (key == KeyEvent.VK_S) {
            state = RUNNING;
        } else if (key == KeyEvent.VK_Q) {
            System.exit(0);
        }
    }

    //��ʼ�µ���Ϸ
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

    //���췽��,�����û�����������
    public TetrisPanel(String userName, int MaxScore) {
        this.userName = userName;
        this.MaxScore = MaxScore;
        backgroundImage = new ImageIcon("image/tetris.png").getImage();
        gameOverImage = new ImageIcon("image/game-over.png").getImage();
        pauseImage = new ImageIcon("image/pause.png").getImage();
    }

    //��дpaint����
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //�������ͼƬ
        g.drawImage(backgroundImage, 0, 0, null);
        //������ϵƽ��15 ����������
        g.translate(15, 15);
        //20*10�ı�������,ʹ��Ҹ�����۲췽������λ��
        paintWallBlock(g);
        //���������еĶ���˹����
        paintDowningTetris(g);
        //�����ϽǷ��������һ������
        paintNextTetris(g);
        //���Ʒ���
        paintScore(g);
        //������״̬
        paintState(g);
    }



    //ͨ����Ϸ״̬�����Ʋ�ͬ״̬�ı���
    public void paintState(Graphics g) {
        if (state == GAME_OVER) {
            g.drawImage(gameOverImage, -15, -15, null);
        } else if (state == PAUSE) {
            g.drawImage(pauseImage, -15, -15, null);
        }
    }

    //������һ������
    public void paintNextTetris(Graphics g) {
        Block[] blocks = nextOne.blocks;
        for (int i = 0; i < tetrisBlock.blocks.length; i++) {
            Block block = blocks[i];
            g.drawImage(block.image, block.column * CELL_SIZE + 260, block.row * CELL_SIZE, null);
        }
    }

    //����ǽ
    public void paintWallBlock(Graphics g) {
        /*
         *  ����wall��ά���飬�õ���ÿһ��cell�������cell����Ϊnull������
         *  ����cow��rol��λ�û�һ�������Σ����cell������null����������cow��
         *  rol��λ�û�cell.image���ͼƬ
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


    //������������ķ���
    public void paintDowningTetris(Graphics g) {
        /*
         * �������������ĸ�cell��Ҫ�õ�ÿһ��cell�����꣬Ȼ�������Ķ�����
         * ����cell.imaage
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
        //���ö�ʱ����ÿ��һ��ʱ���ִ��һ������
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        speed = 40 - (lines / 1) - version;
                        speed = Math.max(speed, 1);
                        //�����ٶ�,��ֹ����̫��
                        if (state == RUNNING && index++ % speed == 0) {
                            slowDropAction();
                        }
                        repaint();  //�ػ�
                    }
                }
                , 0, 10, TimeUnit.MILLISECONDS);

    }

    //������Ȼ��������
    public void slowDropAction() {
        //����û�з���
        if (canDrop()) {
            tetrisBlock.drop();
        } else {//����
            fixation();//���䵽������
            cleanLines();//�Ƿ�����������ķ���
            if (isGameOver()) {
                state = GAME_OVER;
                if (scores > MaxScore) {
                    int n = JOptionPane.showConfirmDialog(null, "�¼�¼!�Ƿ񱣴����?", "�¼�¼!", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        if (new DbConnection().saveScore(userName, scores)) {
                            JOptionPane.showMessageDialog(null, "����ɹ�", "�ɹ�", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "����ʧ��", "ʧ��", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } else {
                    int n = JOptionPane.showConfirmDialog(null, "��Ϸ����,�Ƿ����¿�ʼ", "��Ϸ����", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                            newGame();
                    }
                }
            } else {

                //��Ϸû�н���,������������,����һ���ķ��鸳ֵ�������е� ����ѭ��
                tetrisBlock = nextOne;
                nextOne = TetrisBlock.randomOne();
            }
        }
    }


    /*���������ȥ����*/
    public void cleanLines() {
        //ֻҪĳһ�еĸ������� �Ϳ������ ,��ĳһ�ж���Ϊ��
        for (int row = 0; row < wall.length; row++) {
            //ѭ������һ�е�Ԫ�������ƶ�,�����������
            if (IsCellsFull(row)) {
                deleteRow(row);
                //�����÷�����
                scores += Math.max(1, version / 2);
                lines++;
            }
        }
    }

    /*�жϸ����Ƿ��Ѿ�ȫ����*/
    public boolean IsCellsFull(int row) {
        //�����һ������û�� �ͷ���false
        for (int col = 0; col < wall[row].length; col++) {
            if (wall[row][col] == null) {
                return false;
            }
        }
        return true;
    }

    /*ɾ��ĳһ��*/
    public void deleteRow(int row) {
        //index�Ǵ����濪ʼ��,���԰�row-1��ֵ��row ��Ϊ�� ��һ�еĸ�ֵ������
        for (int i = row; i > 1; i--) {
            //����Arrays.copyof����
            wall[i] = Arrays.copyOf(wall[i - 1], 10);
        }
        //��һ�и�ֵΪ��
        Arrays.fill(wall[0], null);
    }

    /*�ж���Ϸ�Ƿ��Ѿ�������*/
    public boolean isGameOver() {
        Block[] blocks = nextOne.blocks;
        //�ж���һ�������·��Ƿ�Ϊ�� ��Ϊ������Ϸ����
        for (Block block : blocks) {
            if (wall[block.row][block.column] != null) {
                return true;
            }
        }
        return false;
    }

    /*�жϷ����Ƿ��������*/
    public boolean canDrop() {
        //������˵ײ����������Ѿ��з��� ���ܼ�������
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

    /*����̶�*/
    public void fixation() {
        /*��ǰ���ĸ������ֵ��ֵ����Ӧ��ǽ
         * */
        Block[] blocks = tetrisBlock.blocks;
        for (Block block : blocks) {
            wall[block.row][block.column] = block;
        }
    }

    //��д���̿��Ʒ���
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        // ���ݲ�ͬ��״̬������ͬ���߼�
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

    /*��������*/
    public void processRunningKey(int key) {
        //��������˵��: p:��ͣ q:�˳���Ϸ ���ҷ����:���Ʒ��� �Ϸ����:�任��̬ �·����:�������� �ո�:ֱ������
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

    /*������ת*/
    public void rotateRightAction() {
        tetrisBlock.rotateRight();
//        tetrisBlock.rotateRight();
        if (outOfBounds() || wardOff()) {
            rotateLeftAction();
        }
    }

    /*˳ʱ����ת*/
    public void rotateLeftAction() {
//        tetrisBlock.rotateRight();
          tetrisBlock.rotateLeft();
        if (outOfBounds() || wardOff()) {
            rotateRightAction();
        }
    }

    /*ֱ�ӵ�����ȥ*/
    public void directDropAction() {
        //���䵽��������Ϊֹ
        while (canDrop()) {
            tetrisBlock.drop();
        }
        //���� �ж����� �·������
        fixation();
        cleanLines();
        tetrisBlock = nextOne;
        nextOne = TetrisBlock.randomOne();
    }

    /*���Ʒ���  �÷�  ��ʷ��߷ֵȵ�*/
    public void paintScore(Graphics g) {
        //�����÷�
        int x = 290;
        int y = 160;
        g.setColor(Color.BLUE);
        g.setFont(new Font("", Font.BOLD, 25));
        g.drawString("SCORES��" + scores, x, y);
        y += 56;
        g.drawString("SPEED��" + (100 - speed * 2.5), x, y);
        y += 56;
        g.drawString("LINES��" + lines, x, y);
        y += 56;
        g.drawString("MAX��" + MaxScore, x, y);
    }

    /*�������̿���*/
    public void moveLeftAction() {
        //�ж��ܷ����ƣ����Խ���ˣ����߱���ĸ��ӵ����ˣ�������һ����������
        tetrisBlock.moveLeft();//������
        //���Խ����߱���
        if (outOfBounds() || wardOff()) {
            tetrisBlock.moveRight();
        }
    }

    /*�������̿���*/
    public void moveRightAction() {
        TetrisBlock a = tetrisBlock;
        a.moveRight();
//        a.moveLeft();
        //tetrisBlock.moveRight();//������
        if (outOfBounds() || wardOff()) {
            tetrisBlock.moveLeft();
        }
    }

    //�ж��Ƿ�Խ��
    public boolean outOfBounds() {
        //������ĸ�cell������һ��cell��col<0����col>9
        Block[] blocks = tetrisBlock.blocks;
        for (Block block : blocks) {
            if (block.column < 0 || block.column > 9) {
                return true;
            }
        }
        return false;
    }

    //�ж��Ƿ񱻵�
    public boolean wardOff() {
        //��һ�����Ӧǽ�ϲ�Ϊnull ������ס
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

