package com.russia.tetris;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TetrisFrame extends JFrame{
	//属性
	public final static int WIDTH=538;
	public final static int HEIGHT=610;
	String username;
	int MaxScore;
	//构造
	TetrisPanel panel;
	public TetrisFrame(String username,int MaxScore) {
		this.username = username;
		this.MaxScore = MaxScore;
		this.setTitle("俄罗斯方块");
		this.setBounds(500,190,WIDTH,HEIGHT);
		//创建面板
		panel = new TetrisPanel(username, MaxScore);
		panel.action();
		this.addKeyListener(panel);  //监听注册键盘事件
		//面板封装到窗口上
		this.add(panel);
		this.setVisible(true);
		//设置窗口关闭时程序随之关闭
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		JMenuBar menu = new JMenuBar();
		this.setJMenuBar(menu);
		JMenu gameMenu = new JMenu("游戏");
		JMenuItem newGameItem = gameMenu.add("新游戏");
		newGameItem.addActionListener(this.NewGameAction);
		JMenuItem pauseItem = gameMenu.add("暂停");
		pauseItem.addActionListener(this.PauseAction);
		JMenuItem continueItem = gameMenu.add("继续");
		continueItem.addActionListener(this.ContinueAction);
		JMenuItem exitItem = gameMenu.add("退出");
		exitItem.addActionListener(this.ExitAction);

		JMenu rankMenu = new JMenu("查看");
		JMenuItem rankItem = rankMenu.add("排行榜");
		rankItem.addActionListener(this.RankAction);

		JMenu modeMenu = new JMenu("模式");
		JMenuItem esayItem = modeMenu.add("简单");
		esayItem.addActionListener(this.esayAction);
		JMenuItem comItem = modeMenu.add("普通");
		comItem.addActionListener(this.comAction);
		JMenuItem hardItem = modeMenu.add("困难");
		hardItem.addActionListener(this.hardAction);

		JMenu helpMenu = new JMenu("帮助");
		JMenuItem aboutItem = helpMenu.add("关于");
		aboutItem.addActionListener(this.AboutAction);
		menu.add(gameMenu);
		menu.add(modeMenu);
		menu.add(rankMenu);
		menu.add(helpMenu);
	}
	//方法
	ActionListener esayAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			panel.setVersion(1);

		}
	};

	ActionListener comAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			panel.setVersion(10);

		}
	};

	ActionListener hardAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			panel.setVersion(20);

		}
	};

	ActionListener NewGameAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			panel.newGame();

		}
	};


	ActionListener PauseAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			panel.setGamePause();
		}
	};

	ActionListener ContinueAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			panel.setGameRun();
		}
	};

	ActionListener ExitAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			System.exit(0);
		}
	};

	ActionListener AboutAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			JOptionPane.showMessageDialog(TetrisFrame.this, "俄罗斯方块 1.0", "关于", JOptionPane.WARNING_MESSAGE);
		}
	};

	ActionListener RankAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			Rank rank = new Rank();
		}
	};

}
