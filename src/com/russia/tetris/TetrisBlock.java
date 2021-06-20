package com.russia.tetris;

public abstract class TetrisBlock {

    State[] states;
    Block[] blocks = new Block[4];

    //左移
    public void moveLeft() {
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].moveLeft();
        }
    }
    //右移
    public void moveRight() {
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].moveRight();
        }
    }
    //掉落
    public void drop() {
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].drop();
        }
    }

    int index = 1000;

    /*往左旋转*/
    public void rotateLeft() {
        index--;
        State s = states[index % states.length];
        Block block = blocks[0];//旋转轴
        int row = block.row;
        int col = block.column;
        blocks[1].row = row + s.row1;
        blocks[1].column = col + s.col1;
        blocks[2].row = row + s.row2;
        blocks[2].column = col + s.col2;
        blocks[3].row = row + s.row3;
        blocks[3].column = col + s.col3;
    }

    /*往右旋转*/
    public void rotateRight() {
        index++;
        State s = states[index % states.length];
        Block block = blocks[0];//旋转轴
        int row = block.row;
        int col = block.column;
        blocks[1].row = row + s.row1;
        blocks[1].column = col + s.col1;
        blocks[2].row = row + s.row2;
        blocks[2].column = col + s.col2;
        blocks[3].row = row + s.row3;
        blocks[3].column = col + s.col3;
    }

    /*随机产生一个方块*/
    public static TetrisBlock randomOne() {
        double type =  Math.random() * 7.0;

        if (type <=1) {
            return new T();
        } else if (type <=2) {
            return new O();
        } else if (type <=3) {
            return new L();
        } else if (type <=4) {
            return new J();
        } else if (type <=5) {
            return new S();
        } else if (type <=6) {
            return new I();
        }
        return new Z();
    }

}

class T extends TetrisBlock {
    //4个状态
    public T() {
        
        blocks[0] = new Block(0, 4, TetrisPanel.T);
        blocks[1] = new Block(0, 3, TetrisPanel.T);
        blocks[2] = new Block(0, 5, TetrisPanel.T);
        blocks[3] = new Block(1, 4, TetrisPanel.T);
        //分配空间
        states = new State[4];
        states[0] = new State(0, -1, 0, 1, 1, 0);
        states[1] = new State(-1, 0, 1, 0, 0, -1);
        states[2] = new State(0, 1, 0, -1, -1, 0);
        states[3] = new State(1, 0, -1, 0, 0, 1);
    }
}

class O extends TetrisBlock {

    public O() {
        
        blocks[0] = new Block(0, 4, TetrisPanel.O);
        blocks[1] = new Block(0, 5, TetrisPanel.O);
        blocks[2] = new Block(1, 4, TetrisPanel.O);
        blocks[3] = new Block(1, 5, TetrisPanel.O);

        states = new State[2];
        states[0] = new State(0, 1, 1, 0, 1, 1);
        states[1] = new State(0, 1, 1, 0, 1, 1);
    }
}

class I extends TetrisBlock {
    public I() {
        
        blocks[0] = new Block(1, 4, TetrisPanel.I);
        blocks[1] = new Block(0, 4, TetrisPanel.I);
        blocks[2] = new Block(2, 4, TetrisPanel.I);
        blocks[3] = new Block(3, 4, TetrisPanel.I);

        states = new State[2];
        states[0] = new State(-1, 0, 1, 0, 2, 0);
        states[1] = new State(0, -1, 0, 1, 0, 2);
    }
}

class J extends TetrisBlock {
    public J() {
        
        blocks[0] = new Block(1, 5, TetrisPanel.J);
        blocks[3] = new Block(2, 4, TetrisPanel.J);
        blocks[1] = new Block(0, 5, TetrisPanel.J);
        blocks[2] = new Block(2, 5, TetrisPanel.J);

        states = new State[4];
        states[1] = new State( 0, 1, 0, -1,-1, -1);
        states[2] = new State(1, 0,  -1, 0,-1, 1);
        states[3] = new State(0, -1, 0, 1, 1, 1);
        states[0] = new State(-1, 0,  1, 0,1, -1);
    }
}

class L extends TetrisBlock {
    public L() {
        
        blocks[0] = new Block(1, 4, TetrisPanel.L);
        blocks[1] = new Block(0, 4, TetrisPanel.L);
        blocks[2] = new Block(2, 4, TetrisPanel.L);
        blocks[3] = new Block(2, 5, TetrisPanel.L);

        states = new State[4];
        states[0] = new State(-1, 0, 1, 0, 1, 1);
        states[1] = new State(0, 1, 0, -1, 1, -1);
        states[2] = new State(1, 0, -1, 0, -1, -1);
        states[3] = new State(0, -1, 0, 1, -1, 1);
    }
}

class S extends TetrisBlock {
    public S() {
        
        blocks[0] = new Block(1, 4, TetrisPanel.S);
        blocks[1] = new Block(1, 3, TetrisPanel.S);
        blocks[2] = new Block(0, 4, TetrisPanel.S);
        blocks[3] = new Block(0, 5, TetrisPanel.S);

        states = new State[4];
        states[0] = new State(0, -1, -1, 0, -1, 1);
        states[1] = new State(-1, 0, 0, 1, 1, 1);
        states[2] = new State(0, 1, 1, 0, 1, -1);
        states[3] = new State(1, 0, 0, -1, -1, -1);
    }
}


class Z extends TetrisBlock {
    public Z() {
        
        blocks[0] = new Block(1, 4, TetrisPanel.Z);
        blocks[1] = new Block(0, 3, TetrisPanel.Z);
        blocks[2] = new Block(0, 4, TetrisPanel.Z);
        blocks[3] = new Block(1, 5, TetrisPanel.Z);

        states = new State[4];
        states[0] = new State(-1, -1, -1, 0, 0, 1);
        states[1] = new State(-1, 1, 0, 1, 1, 0);
        states[2] = new State(1, 1, 1, 0, 0, -1);
        states[3] = new State(1, -1, 0, -1, -1, 0);
    }
}