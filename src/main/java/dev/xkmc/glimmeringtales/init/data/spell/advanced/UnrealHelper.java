package dev.xkmc.glimmeringtales.init.data.spell.advanced;

import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.modifier.SetPosModifier;
import dev.xkmc.l2magic.content.engine.predicate.OrPredicate;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;

import java.util.List;

public class UnrealHelper {

	public static ConfiguredEngine<?> cuboid(double x, double y, double z, double stepLen, ConfiguredEngine<?> child) {
		var xNum = (int) (x / stepLen) + 1;
		var yNum = (int) (y / stepLen) + 1;
		var zNum = (int) (z / stepLen) + 1;
		return new LoopIterator(
				IntVariable.of(Integer.toString(xNum)),
				new LoopIterator(
						IntVariable.of(Integer.toString(yNum)),
						new LoopIterator(
								IntVariable.of(Integer.toString(zNum)),
								new MoveEngine(
										List.of(
												new SetPosModifier(
														DoubleVariable.of("PosX+xInd*" + stepLen + "-" + (x / 2)),
														DoubleVariable.of("PosY+yInd*" + stepLen + "-" + (y / 2)),
														DoubleVariable.of("PosZ+zInd*" + stepLen + "-" + (z / 2))
												)
										),
										child
								),
								"zInd"
						),
						"yInd"
				),
				"xInd"
		);
	}

	public static ConfiguredEngine<?> cuboidSurface(double x, double y, double z, double stepLen, ConfiguredEngine<?> child) {
		var xNum = (int) (x / stepLen) + 1;
		var yNum = (int) (y / stepLen) + 1;
		var zNum = (int) (z / stepLen) + 1;
		return new LoopIterator(
				IntVariable.of(Integer.toString(xNum)),
				new LoopIterator(
						IntVariable.of(Integer.toString(yNum)),
						new LoopIterator(
								IntVariable.of(Integer.toString(zNum)),
								new PredicateLogic(
										new OrPredicate(List.of(
												BooleanVariable.of("xInd*yInd*zInd==0"),
												BooleanVariable.of("xInd==" + (xNum - 1)),
												BooleanVariable.of("yInd==" + (yNum - 1)),
												BooleanVariable.of("zInd==" + (zNum - 1))
										)),
										new MoveEngine(
												List.of(
														new SetPosModifier(
																DoubleVariable.of("PosX+xInd*" + stepLen + "-" + (x / 2)),
																DoubleVariable.of("PosY+yInd*" + stepLen + "-" + (y / 2)),
																DoubleVariable.of("PosZ+zInd*" + stepLen + "-" + (z / 2))
														)
												),
												child
										),
										null
								),
								"zInd"
						),
						"yInd"
				),
				"xInd"
		);
	}
}
