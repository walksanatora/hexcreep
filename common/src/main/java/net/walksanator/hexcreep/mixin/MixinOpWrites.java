package net.walksanator.hexcreep.mixin;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.common.casting.actions.rw.OpTheCoolerWrite;
import at.petrak.hexcasting.common.casting.actions.rw.OpWrite;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.walksanator.hexcreep.ExampleMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin( { OpWrite.class, OpTheCoolerWrite.class} )
public abstract class MixinOpWrites {
    @Shadow(remap = false)
    public abstract int getArgc();

    @WrapOperation(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/addldata/ADIotaHolder;writeIota(Lat/petrak/hexcasting/api/casting/iota/Iota;Z)Z"), remap = false)
    private boolean dontAllowUnstorablesToBeStored(ADIotaHolder instance, Iota iota, boolean b, Operation<Boolean> original) throws MishapInvalidIota {
        boolean check = ExampleMod.checkRecursive(iota, ExampleMod.STORE_CHECK);
        if (check) {
            throw MishapInvalidIota.ofType(iota, getArgc()-1, "storable");
        }
        return original.call(instance, iota, b);
    }
}
