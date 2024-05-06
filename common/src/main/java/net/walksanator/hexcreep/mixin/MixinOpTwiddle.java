package net.walksanator.hexcreep.mixin;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.common.casting.actions.stack.OpTwiddling;
import net.walksanator.hexcreep.ExampleMod;
import net.walksanator.hexcreep.api.IEatIotas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;

@Mixin(OpTwiddling.class)
public abstract class MixinOpTwiddle {
    @Shadow(remap = false)
    public abstract int[] getLookup();

    @Unique
    private Boolean hexcreep$doesDuplicate = null;

    @Inject(method = "execute", at = @At("RETURN"), remap = false)
    private void hexcreep$noDuplicate(CallbackInfoReturnable<List<Iota>> cir) throws MishapInvalidIota {
        if (hexcreep$doesDuplicate == null) {
            hexcreep$doesDuplicate = false;
            HashSet<Integer> set = new HashSet<>();
            for (int num : getLookup()) {
                if (!set.add(num)) {
                    hexcreep$doesDuplicate = true; // Duplicate found
                    break;
                }
            }
        }

        if (hexcreep$doesDuplicate) {
            List<Iota> myList = cir.getReturnValue();
            for (int i = 0; i < myList.size(); i++) {
                Iota i2 = myList.get(i);
                boolean check = ExampleMod.checkRecursive(i2, ExampleMod.DUPE_CHECK);
                if (check) {
                    throw MishapInvalidIota.ofType(i2, i, "duplicate");
                }
            }
        }

    }
}
