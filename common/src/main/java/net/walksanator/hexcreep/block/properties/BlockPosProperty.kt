package net.walksanator.hexcreep.block.properties

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.properties.Property
import net.walksanator.hexcreep.ExampleMod
import java.util.*

class BlockPosProperty(string: String) : Property<BlockPos>(string, BlockPos::class.java ) {

    override fun getName(pos: BlockPos): String = "(%s, %s, %s)".format(pos.x,pos.y,pos.z)

    override fun getPossibleValues(): Collection<BlockPos> = FalseCollection()

    override fun getValue(string: String): Optional<BlockPos> {
        if (!string.startsWith("(") && !string.endsWith(")")) {
            return Optional.empty()
        }
        val ints = try {
            string.trim('(', ')').split(',').map { it.toInt() }
        } catch (e: NumberFormatException) {
            return Optional.empty()
        }
        if (ints.size != 3) {
            return Optional.empty()
        }
        return Optional.of(
            BlockPos(
                ints[0],
                ints[1],
                ints[2]
            )
        )
    }
}

class FalseCollection : Collection<BlockPos> {
    override val size: Int = Int.MAX_VALUE
    override fun isEmpty(): Boolean = false

    override fun iterator(): Iterator<BlockPos> = FalseIterator()

    override fun containsAll(elements: Collection<BlockPos>): Boolean = true
    override fun contains(element: BlockPos): Boolean = true
}

class FalseIterator : Iterator<BlockPos> {
    init {
        ExampleMod.LOGGER.warn("SOMEONE IS USING THE ITERATOR OF ALL BLOCK POSITIONS OHNO!")
    }
    var x = Int.MIN_VALUE
    var y = Int.MIN_VALUE
    var z = Int.MIN_VALUE

    override fun hasNext(): Boolean = (z == Int.MAX_VALUE)

    override fun next(): BlockPos {
        if (x == Int.MAX_VALUE) {
            x = Int.MIN_VALUE
            y += 1
        }
        if (y == Int.MAX_VALUE) {
            y = Int.MIN_VALUE
            z += 1
        }
        return BlockPos(x,y,z)
    }

}