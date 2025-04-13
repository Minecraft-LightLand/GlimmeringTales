package dev.xkmc.glimmeringtales.content.research.logic;

public abstract class AbstractHex {

	public static final double WIDTH = 2, HEIGHT = Math.sqrt(3);

	/**
	 * get the X position of a cell relative to the center
	 */
	public abstract double getX(int row, int cell);

	/**
	 * get the Y position of a cell relative to the center
	 */
	public abstract double getY(int row, int cell);

}
