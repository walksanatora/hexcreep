package net.walksanator.hexcreep.mixin;

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import net.walksanator.hexcreep.ExampleMod;
import net.walksanator.hexcreep.api.IEatIotas;
import net.walksanator.hexcreep.duck.IGetRealStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(CastingImage.class)
public abstract class MixinCastingImage implements IGetRealStack {

    @Unique
    boolean hexcreep$escapeHatch = false;

    @Shadow(remap = false)
    public abstract List<Iota> getStack();

    @Override
    public List<Iota> getReal() {
        hexcreep$escapeHatch = true;
        List<Iota> real = getStack();
        hexcreep$escapeHatch = false;
        return real;
    }

    @Inject(method = "getStack()Ljava/util/List;", at = @At("RETURN"), cancellable = true, remap = false)
    private void untaintStack(CallbackInfoReturnable<List<Iota>> cir) {
        if (!hexcreep$escapeHatch) {
            List<Iota> old = cir.getReturnValue();
            List<Iota> remap = new ArrayList<Iota>(old.size());
            for (Iota i : old) {
                if (i instanceof DoubleIota d) {
                    double n = d.getDouble();
                    remap.add(new DoubleIota(n));
                } else {remap.add(i);}
            }
            cir.setReturnValue(remap);
            cir.cancel();
        }
    }

    @ModifyArg(method = "copy", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/vm/CastingImage;<init>(Ljava/util/List;ILjava/util/List;ZJLnet/minecraft/nbt/CompoundTag;)V"), index = 0)
    private List<Iota> remapCatchCase(List<? extends Iota> stack) {
        List<Iota> remap = new LinkedList<>();
        Set<UUID> seen = new HashSet<>();
        for (Iota i : stack) {
            remap.add(
                    ExampleMod.mapRecursive(i, (iota) -> {
                        if (iota instanceof IEatIotas ui && ui.preventDupe()) {
                            if (seen.add(ui.getUUID())) {
                                return iota;
                            } else {
                                return new NullIota();
                            }
                        }
                        return iota;
                    })
            );
        }
        return remap;
    }

}
