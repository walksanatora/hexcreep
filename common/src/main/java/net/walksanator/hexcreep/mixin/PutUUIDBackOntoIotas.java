package net.walksanator.hexcreep.mixin;

import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.walksanator.hexcreep.api.IEatIotas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "at/petrak/hexcasting/api/casting/iota/GarbageIota$1")
public class PutUUIDBackOntoIotas {

    @Inject(method = "deserialize(Lnet/minecraft/nbt/Tag;Lnet/minecraft/server/level/ServerLevel;)Lat/petrak/hexcasting/api/casting/iota/GarbageIota;", at = @At("RETURN"), cancellable = true)
    private void putUUIDOnInstance(Tag tag, ServerLevel world, CallbackInfoReturnable<GarbageIota> cir) {
        IEatIotas gi = (IEatIotas)cir.getReturnValue();
        gi.setUUID(
                ((CompoundTag)tag).getUUID("hexcreep$uuid")
        );
        cir.setReturnValue((GarbageIota) gi);
    }
}
