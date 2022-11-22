/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.youaremyhatnow.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings({"unused", "UnusedMixin"})
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends Entity {
  public MixinPlayerEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  @Override
  public double getMountedHeightOffset() {
    if (this.isSneaking()) {
      return super.getMountedHeightOffset() - 0.2;
    }
    return super.getMountedHeightOffset();
  }

  @Override
  public void updatePassengerPosition(Entity passenger) {
    if (!(passenger instanceof AnimalEntity) || !this.hasPassenger(passenger)) return;

    if (passenger instanceof FoxEntity) {
      ((FoxEntity) passenger).setSleeping(true);
    } else if (passenger instanceof TameableEntity) {
      ((TameableEntity) passenger).setSitting(true);
    }

    passenger.setPosition(
        this.getX(),
        this.getY()
            + this.getMountedHeightOffset()
            + (passenger.getHeightOffset() + 0.3)
                * (this.getPassengerList().indexOf(passenger) + 1),
        this.getZ());
    passenger.setYaw(this.getYaw());
  }
}
