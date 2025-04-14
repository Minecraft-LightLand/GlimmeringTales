package dev.xkmc.glimmeringtales.content.research.client.base;

import net.minecraft.util.Mth;

public abstract class AbstractScalableGui {

	public final WindowBox box = new WindowBox();

	protected float magn = 14;
	protected double scrollX, scrollY;

	public void scroll(double dx, double dy) {
		scrollX += dx;
		scrollY += dy;
	}

	public boolean mouseScrolled(double mx, double my, double amount) {
		int min = Math.min(box.w, box.h);
		int max = Math.max(box.w, box.h);
		float m0 = magn;
		magn = Mth.clamp(magn + (float) amount, min / 50f, max / 10f);
		m0 = magn / m0;
		double x0 = box.x + box.w / 2d;
		double y0 = box.y + box.h / 2d;
		scrollX = mx - x0 - (mx - x0 - scrollX) * m0;
		scrollY = my - y0 - (my - y0 - scrollY) * m0;
		return true;
	}

	protected double getMX(double mx) {
		double x0 = box.x + box.w / 2d;
		return (mx - x0 - scrollX) / magn;
	}

	protected double getMY(double my) {
		double y0 = box.y + box.h / 2d;
		return (my - y0 - scrollY) / magn;
	}

	public abstract void initScale();

	public void scrollTo(double x, double y) {
		scrollX = -x * magn;
		scrollY = -y * magn;
	}

}
