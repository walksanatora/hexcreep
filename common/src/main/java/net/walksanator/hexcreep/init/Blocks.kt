package net.walksanator.hexcreep.init

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.walksanator.hexcreep.ExampleMod
import net.walksanator.hexcreep.block.CreepBlock

object Blocks {
    private val BLOCKS = DeferredRegister.create(ExampleMod.MOD_ID, Registries.BLOCK)
    private val ITEMS = DeferredRegister.create(ExampleMod.MOD_ID, Registries.ITEM)

    val CREEP_BLOCK = BLOCKS.register("creep") { CreepBlock(BlockBehaviour.Properties.of().randomTicks()) }
    val CREEP_BLOCK_ITEM = ITEMS.register("creep") {BlockItem(CREEP_BLOCK.get(), Item.Properties())}

    fun register() {
        BLOCKS.register()
        ITEMS.register()
    }
}