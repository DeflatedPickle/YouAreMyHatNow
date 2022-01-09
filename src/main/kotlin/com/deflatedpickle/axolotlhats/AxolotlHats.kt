/* Copyright (c) 2021-2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.axolotlhats

import net.fabricmc.api.ModInitializer
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.AxolotlEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BucketItem
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand

@Suppress("UNUSED")
object AxolotlHats : ModInitializer {
    private const val MOD_ID = "$[id]"
    private const val NAME = "$[name]"
    private const val GROUP = "$[group]"
    private const val AUTHOR = "$[author]"
    private const val VERSION = "$[version]"

    override fun onInitialize() {
        println(listOf(MOD_ID, NAME, GROUP, AUTHOR, VERSION))
    }

    fun interact(axolotl: AxolotlEntity, playerEntity: PlayerEntity, hand: Hand): ActionResult {
        if (hand == Hand.MAIN_HAND && playerEntity.getStackInHand(hand).item !is BucketItem) {
            axolotl.startRiding(playerEntity, true)
            return ActionResult.success(playerEntity.world.isClient)
        }

        return ActionResult.PASS
    }

    fun tickRiding(axolotl: AxolotlEntity, mount: Entity) {
        if (mount is PlayerEntity && mount.shouldDismount()) {
            axolotl.dismountVehicle()
        }
    }
}
