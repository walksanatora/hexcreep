package net.walksanator.hexcreep.mixin;

import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.walksanator.hexcreep.api.IEatIotas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(GarbageIota.class)
public class ImplementUndupOnIotas implements IEatIotas {
    @Unique
    private UUID hexcreep$storedUUID = null;

    @Override
    public UUID getUUID() {
        if (hexcreep$storedUUID == null) {
            hexcreep$storedUUID = UUID.randomUUID();
        }
        return hexcreep$storedUUID;
    }

    @Override
    public void setUUID(UUID uuid) {
        hexcreep$storedUUID = uuid;
    }

    @Inject(method = "serialize", at = @At("RETURN"), cancellable = true)
    private void addUUIDToData(CallbackInfoReturnable<Tag> cir) {
        Tag tag = cir.getReturnValue();
        if (tag instanceof CompoundTag ct) {
            ct.putUUID("hexcreep$uuid",hexcreep$storedUUID);
            cir.setReturnValue(ct);
            cir.cancel();
        }

    }

}
