package dev.xkmc.glimmeringtales.content.research.logic;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class HexHandler {

	public record Data(byte[] data, byte[] subs, ArrayList<Data> cores) {

	}

	private record HalfResult(int row, int cell) {
	}

	public static final int CORE_LIMIT = 6;
	public static final double WIDTH = 2, HEIGHT = Math.sqrt(3);
	public final int radius;
	public final SubHex[] subhex;
	public final SubHexCore[] cores;
	final byte[][] cells;

	public HexHandler(int r) {
		radius = r;
		cells = new byte[getRowCount()][];
		for (int i = 0; i < getRowCount(); i++)
			cells[i] = new byte[getCellCount(i)];
		subhex = new SubHex[getArea()];
		cores = new SubHexCore[CORE_LIMIT];
	}

	public HexHandler(Data tag) {
		byte[] data = tag.data;
		byte[] subs = tag.subs;
		var list = tag.cores;
		radius = data[0] & 0xF;
		cells = new byte[getRowCount()][];
		for (int i = 0; i < getRowCount(); i++)
			cells[i] = new byte[getCellCount(i)];
		subhex = new SubHex[getArea()];
		cores = new SubHexCore[CORE_LIMIT];
		for (int i = 0; i < list.size(); i++)
			cores[i] = new SubHexCore(new HexHandler(list.get(i)));
		int k = 1, s = 0;
		HexCell cell = new HexCell(this, 0, 0);
		for (int i = 0; i < getRowCount(); i++)
			for (int j = 0; j < getCellCount(i); j++) {
				cell.row = i;
				cell.cell = j;
				int val = data[k >> 1] >> ((k & 1) * 4);
				int dir = (byte) (val & 7);
				for (int d = 0; d < 3; d++) {
					if ((HexDirection.values()[d].mask() & dir) != 0) {
						cell.toggle(HexDirection.values()[d]);
					}
				}
				if ((val & 8) != 0) {
					byte sval = subs[s++];
					SubHexCore core = cores[sval & 7];
					int rot = sval >> 3 & 7;
					boolean flip = (sval >> 6 & 1) != 0;
					subhex[getInd(i, j)] = new SubHex(core, rot, flip);
				}
				k++;
			}
	}

	private static HalfResult getCoordinate(double x, double y) {
		// row number relative to center in rectangular grid
		int row = (int) Math.floor(y / HEIGHT + 0.5);
		// relative y coordinate of the point in rectangular grid
		double rel_y = y - row * HEIGHT;
		// cell number relative to center in rectangular grid
		int cell = (int) Math.floor(x / WIDTH + 0.5 - Math.abs(row) * 0.5);
		// relative x coordinate of the point in rectangular grid
		double rel_x = x - (Math.abs(row) * 0.5 + cell) * WIDTH;

		double xoff = WIDTH / 4.0;
		double yoff = HEIGHT / 6.0;

		HexDirection dire = rel_y > 0 ?
				rel_x > 0 ? HexDirection.LOWER_RIGHT : HexDirection.LOWER_LEFT :
				rel_x > 0 ? HexDirection.UPPER_RIGHT : HexDirection.UPPER_LEFT;

		rel_x = Math.abs(rel_x) - xoff;
		rel_y = Math.abs(rel_y) - yoff * 2;
		if (rel_x > 0 && rel_y > 0 && rel_x / xoff + rel_y / yoff > 1) {
			cell += dire.getCellOffset(0, row, cell);
			row += dire.getRowOffset();
		}
		return new HalfResult(row, cell);
	}

	/**
	 * get the total cell count of the hexagon
	 */
	public int getArea() {
		return 3 * radius * (radius + 1) + 1;
	}

	public int getCellCount(int row) {
		return Math.min(row, radius * 2 - row) + radius + 1;
	}

	@Nullable
	public CellResult getCellOnHex(double x, double y) {
		HalfResult pos = getCoordinate(x, y);
		return CellResult.get(pos.row() + radius, pos.cell() + radius, this);
	}

	@Nullable
	public LocateResult getElementOnHex(double x, double y) {
		HalfResult pos = getCoordinate(x * 2, y * 2);
		int trow = Math.floorDiv(pos.row(), 2) + radius;
		int tcel = Math.floorDiv(pos.cell(), 2) + radius;
		if (pos.row() % 2 == 0 && pos.cell() % 2 == 0)
			return CellResult.get(trow, tcel, this);
		if (pos.row() % 2 == 0) {
			return ArrowResult.get(trow, tcel, HexDirection.RIGHT, this);
		}
		if (pos.row() < 0)
			return ArrowResult.get(trow, tcel, pos.cell() % 2 == 0 ? HexDirection.LOWER_LEFT : HexDirection.LOWER_RIGHT, this);
		if (pos.cell() % 2 == 0)
			return ArrowResult.get(trow, tcel, HexDirection.LOWER_RIGHT, this);
		return ArrowResult.get(trow, tcel + 1, HexDirection.LOWER_LEFT, this);

	}

	/**
	 * get the index of a cell in special cell array
	 */
	public int getInd(int row, int cell) {
		if (row <= radius)
			return (2 * radius + 1 + row) * row / 2 + cell;
		return getArea() - (4 * radius + 2 - row) * (2 * radius + 1 - row) / 2 + cell;
	}

	public FlowChart getMatrix(boolean withFlow) {
		return new HexCalc(this).getMatrix(withFlow);
	}

	public int getRowCount() {
		return radius * 2 + 1;
	}

	/**
	 * get the X position of a cell relative to the center
	 */
	public double getX(int row, int cell) {
		return (cell - radius) * WIDTH + (radius * 2 + 1 - getCellCount(row)) * WIDTH / 2;
	}

	/**
	 * get the Y position of a cell relative to the center
	 */
	public double getY(int row, int cell) {
		return (row - radius) * HEIGHT;
	}

	public boolean isInvalid() {
		HexCell cell = new HexCell(this, 0, 0);
		for (int r = 0; r < cells.length; r++)
			for (int c = 0; c < cells[r].length; c++) {
				cell.row = r;
				cell.cell = c;
				if (cell.isInvalid())
					return true;
			}
		return false;
	}

	public Data write() {
		int tot = getArea() * 4 + 4;
		int len = tot >> 3;
		byte[] data = new byte[len];
		SubHex[] sub = new SubHex[getArea()];
		data[0] |= (byte) (radius & 0xF);
		int k = 1, s = 0;
		for (int i = 0; i < getRowCount(); i++)
			for (int j = 0; j < getCellCount(i); j++) {
				data[k >> 1] |= (byte) ((cells[i][j] & 7) << ((k & 1) * 4));
				if (subhex[getInd(i, j)] != null) {
					data[k >> 1] |= (byte) (8 << ((k & 1) * 4));
					sub[s] = subhex[getInd(i, j)];
					s++;
				}
				k++;
			}
		byte[] subs = new byte[s];
		for (int i = 0; i < s; i++) {
			int rot = (sub[i].rotation + 6) % 6;
			int flip = sub[i].flip ? 1 : 0;
			int ans = sub[i].core.index | (rot << 3) | (flip << 6);
			subs[i] = (byte) ans;
		}
		ArrayList<Data> list = new ArrayList<>();
		for (SubHexCore core : cores)
			if (core != null)
				list.add(core.hex.write());
		return new Data(data, subs, list);
	}

	public class SubHexCore {

		public final HexHandler hex;
		public final Frac[][] otho;
		public final int exist, index;

		public SubHexCore(HexHandler hex) {
			this.hex = hex;
			this.otho = hex.getMatrix(false).matrix;
			int exi = 0;
			for (int i = 0; i < 6; i++) {
				HexDirection dir = HexDirection.values()[i];
				int dr = dir.getRowOffset();
				int dc = dir.getCellOffset(hex.radius, hex.radius, hex.radius);
				int r = (dr + 1) * hex.radius;
				int c = (dc + 1) * hex.radius;
				if (hex.cells[r][c] != 0)
					exi |= 1 << i;
			}
			this.exist = exi;
			for (int i = 0; i < cores.length; i++)
				if (cores[i] == null) {
					cores[i] = this;
					this.index = i;
					return;
				}
			throw new HexException("no core space");
		}
	}

}
