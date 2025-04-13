package dev.xkmc.glimmeringtales.content.research.logic;

import javax.annotation.Nullable;

public class CellResult extends LocateResult {

	private final int row, cell;
	private final AbstractHex hex;

	CellResult(int row, int cell, AbstractHex hex) {
		this.row = row;
		this.cell = cell;
		this.hex = hex;
	}

	@Nullable
	public static CellResult get(int row, int cell, HexHandler hex) {
		if (row < 0 || row >= hex.getRowCount())
			return null;
		if (cell < 0 || cell >= hex.getCellCount(row))
			return null;
		return new CellResult(row, cell, hex);
	}

	@Override
	public ResultType getType() {
		return ResultType.CELL;
	}

	@Override
	public double getX() {
		return hex.getX(row, cell);
	}

	@Override
	public double getY() {
		return hex.getY(row, cell);
	}

	public int getRow() {
		return row;
	}

	public int getCell() {
		return cell;
	}
}
