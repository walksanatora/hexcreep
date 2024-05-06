package net.walksanator.hexcreep.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.common.casting.actions.stack.OpDuplicateN;
import net.walksanator.hexcreep.ExampleMod;
import net.walksanator.hexcreep.api.IEatIotas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(OpDuplicateN.class)
public abstract class MixinOpDuplicateN {
    @Inject(method = "execute", at = @At("HEAD"), remap = false)
    private void duplicateChecking(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) throws MishapInvalidIota {
        Iota check = args.get(0);
        boolean check2 = ExampleMod.checkRecursive(check,ExampleMod.DUPE_CHECK);
        if (check2) {
            throw MishapInvalidIota.of(check,1,"duplicate");
        }
    }

}
