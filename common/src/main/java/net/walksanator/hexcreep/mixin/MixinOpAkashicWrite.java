package net.walksanator.hexcreep.mixin;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.common.casting.actions.akashic.OpAkashicWrite;
import net.walksanator.hexcreep.ExampleMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(OpAkashicWrite.class)
public class MixinOpAkashicWrite {

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/mishaps/MishapOthersName$Companion;getTrueNameFromDatum(Lat/petrak/hexcasting/api/casting/iota/Iota;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/entity/player/Player;"), index = 0)
    private Iota writeCheck(Iota datum) throws MishapInvalidIota {
        boolean check = ExampleMod.checkRecursive(datum, ExampleMod.STORE_CHECK);
        if (check) {
            throw MishapInvalidIota.ofType(datum, 2, "storable");
        }
        return datum;
    }
}
