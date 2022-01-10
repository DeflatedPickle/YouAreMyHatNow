/* Copyright (c) 2021-2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.axolotlhats

import net.fabricmc.api.ModInitializer
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import java.util.Optional

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
}
