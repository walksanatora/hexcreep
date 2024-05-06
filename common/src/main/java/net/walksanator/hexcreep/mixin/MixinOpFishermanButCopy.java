package net.walksanator.hexcreep.mixin;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.common.casting.actions.stack.OpFishermanButItCopies;
import com.llamalad7.mixinextras.sugar.Local;
import net.walksanator.hexcreep.ExampleMod;
import net.walksanator.hexcreep.api.IEatIotas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(OpFishermanButItCopies.class)
public abstract class MixinOpFishermanButCopy {

    @ModifyArg(method = "operate", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), index = 0, remap = false)
    private Object preventDuplicatefromTopDown(Object i, @Local int depth) throws MishapInvalidIota {
        boolean check = ExampleMod.checkRecursive((Iota)i, ExampleMod.DUPE_CHECK);
        if (check) {
            throw MishapInvalidIota.ofType((Iota)i, depth, "duplicate");
        }
        return i;
    }

    @ModifyArg(method = "operate", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V"), index = 1, remap = false)
    private Object preventDuplicatefromBottomUp(Object i, @Local int depth) throws MishapInvalidIota {
        boolean check = ExampleMod.checkRecursive((Iota)i, ExampleMod.DUPE_CHECK);
        if (check) {
            throw MishapInvalidIota.ofType((Iota)i, depth, "duplicate");
        }
        return i;
    }
}
