package net.walksanator.hexcreep.mixin;

import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.common.casting.actions.spells.OpMakePackagedSpell;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.walksanator.hexcreep.ExampleMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OpMakePackagedSpell.class)
public class MixinOpMakePackagedSpell {

    @ModifyExpressionValue(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/OperatorUtils;getList(Ljava/util/List;II)Lat/petrak/hexcasting/api/casting/SpellList;"),remap = false)
    private SpellList throwOnWriting(SpellList original) throws MishapInvalidIota {
        for (Iota i : original) {
            if (ExampleMod.checkRecursive(i, ExampleMod.STORE_CHECK)) {
                throw MishapInvalidIota.ofType(i,1,"storable");
            }
        }
        return original;
    }

}
