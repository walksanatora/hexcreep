package net.walksanator.hexcreep.mixin;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.common.casting.actions.local.OpPushLocal;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.walksanator.hexcreep.ExampleMod;
import net.walksanator.hexcreep.api.IEatIotas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(OpPushLocal.class)
public abstract class MixinOpPushLocal {

    @ModifyExpressionValue(method = "operate", at = @At(value = "INVOKE", target = "Lkotlin/collections/CollectionsKt;removeLast(Ljava/util/List;)Ljava/lang/Object;"),remap = false)
    private Object ravenmindIsCopy(Object original) throws MishapInvalidIota {
        if (ExampleMod.checkRecursive((Iota)original, ExampleMod.DUPE_CHECK)) {
            throw MishapInvalidIota.ofType((Iota)original, 0, "duplicate");
        }
        return original;
    }
}
