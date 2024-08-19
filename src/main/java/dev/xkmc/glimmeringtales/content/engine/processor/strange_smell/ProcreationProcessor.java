package dev.xkmc.glimmeringtales.content.engine.processor.strange_smell;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.collision.EntityStorageCache;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import java.util.Collection;


import static net.minecraft.world.entity.AgeableMob.getSpeedUpSecondsWhenFeeding;

public record ProcreationProcessor(
        IntVariable num
        ) implements EntityProcessor<ProcreationProcessor> {

    public static final MapCodec<ProcreationProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            IntVariable.codec("num", e -> e.num)
    ).apply(i, ProcreationProcessor::new));
    @Override
    public ProcessorType<ProcreationProcessor> type() {
        return GTEngine.PROCREATION.get();
    }

    @Override
    public void process(Collection<LivingEntity> le, EngineContext ctx) {
        if (!(ctx.user().level() instanceof ServerLevel sl)) return;
        for (var e : le) {
            if(e instanceof Animal animal)
                if(ctx.user().user() instanceof Player player){
                    if(!animal.isBaby()){
                        animal.setInLove(player);
                    }else{
                        animal.ageUp(getSpeedUpSecondsWhenFeeding(-animal.getAge()),true);
                    }
                }else{
                    if(!animal.isBaby()){
                        animal.setInLove(null);
                    }else{
                        animal.ageUp(getSpeedUpSecondsWhenFeeding(-animal.getAge()),true);
                    }
                }
        }
    }


}
