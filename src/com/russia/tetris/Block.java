package com.russia.tetris;

import java.awt.Image;

public class Block {
	int column;
	int row;
	Image image;

	public Block(int row, int col, Image image) {
		this.row=row;
		this.column =col;
		this.image=image;
		
	}

	public void moveLeft() {
		column--;
	}
	public void moveRight() {
		column++;
	}
	public void drop() {
		row++;
	}
	
}
