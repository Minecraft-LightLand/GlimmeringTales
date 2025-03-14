package dev.xkmc.glimmeringtales.content.block.altar;

import dev.xkmc.glimmeringtales.init.data.GTConfigs;

public class PerformanceConstants {

	public static final int CHECK_INTERVAL = 10;

	public static int range() {
		return GTConfigs.SERVER.ritualRange.get();
	}

}
