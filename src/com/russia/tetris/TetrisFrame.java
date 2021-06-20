package com.russia.tetris;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TetrisFrame extends JFrame{
	//����
	public final static int WIDTH=538;
	public final static int HEIGHT=610;
	String username;
	int MaxScore;
	//����
	TetrisPanel panel;
	public TetrisFrame(String username,int MaxScore) {
		this.username = username;
		this.MaxScore = MaxScore;
		this.setTitle("����˹����");
		this.setBounds(500,190,WIDTH,HEIGHT);
		//�������
		panel = new TetrisPanel(username, MaxScore);
		panel.action();
		this.addKeyListener(panel);  //����ע������¼�
		//����װ��������
		this.add(panel);
		this.setVisible(true);
		//���ô��ڹر�ʱ������֮�ر�
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		JMenuBar menu = new JMenuBar();
		this.setJMenuBar(menu);
		JMenu gameMenu = new JMenu("��Ϸ");
		JMenuItem newGameItem = gameMenu.add("����Ϸ");
		newGameItem.addActionListener(this.NewGameAction);
		JMenuItem pauseItem = gameMenu.add("��ͣ");
		pauseItem.addActionListener(this.PauseAction);
		JMenuItem continueItem = gameMenu.add("����");
		continueItem.addActionListener(this.ContinueAction);
		JMenuItem exitItem = gameMenu.add("�˳�");
		exitItem.addActionListener(this.ExitAction);

		JMenu rankMenu = new JMenu("�鿴");
		JMenuItem rankItem = rankMenu.add("���а�");
		rankItem.addActionListener(this.RankAction);

		JMenu modeMenu = new JMenu("ģʽ");
		JMenuItem esayItem = modeMenu.add("��");
		esayItem.addActionListener(this.esayAction);
		JMenuItem comItem = modeMenu.add("��ͨ");
		comItem.addActionListener(this.comAction);
		JMenuItem hardItem = modeMenu.add("����");
		hardItem.addActionListener(this.hardAction);

		JMenu helpMenu = new JMenu("����");
		JMenuItem aboutItem = helpMenu.add("����");
		aboutItem.addActionListener(this.AboutAction);
		menu.add(gameMenu);
		menu.add(modeMenu);
		menu.add(rankMenu);
		menu.add(helpMenu);
	}
	//����
	ActionListener esayAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			panel.setVersion(1);

		}
	};

	ActionListener comAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			panel.setVersion(10);

		}
	};

	ActionListener hardAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			panel.setVersion(20);

		}
	};

	ActionListener NewGameAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			panel.newGame();

		}
	};


	ActionListener PauseAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			panel.setGamePause();
		}
	};

	ActionListener ContinueAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			panel.setGameRun();
		}
	};

	ActionListener ExitAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			System.exit(0);
		}
	};

	ActionListener AboutAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			JOptionPane.showMessageDialog(TetrisFrame.this, "����˹���� 1.0", "����", JOptionPane.WARNING_MESSAGE);
		}
	};

	ActionListener RankAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			Rank rank = new Rank();
		}
	};

}
