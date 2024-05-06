package net.walksanator.hexcreep.api;

import java.util.UUID;

/**
 * implement on a {@link at.petrak.hexcasting.api.casting.iota.Iota} to prevent it from being duplicated and written to items external storages
 */
public interface IEatIotas {
    /**
     * gets a stored UUID on the iota instance, used for dupe checking
     * should be created if the UUID does not exists
     * @return the uuid of the iota
     */
    UUID getUUID();

    /**
     * sets the stored UUID for eg: when loading from NBT
     * @param uuid the uuid to set uuid to
     */
    void setUUID(UUID uuid);

    /**
     * determines whether to prevent dupes, override to change/create conditions (gemini, fisherman 2)
     * @return whether to prevent dupes
     */
    default boolean preventDupe() {return true;}

    /**
     * determines whether to prevent storing in external storages (foci, akashic records, wisp)
     * @return whether to prevent storage
     */
    default boolean preventStore() {return true;}
}
