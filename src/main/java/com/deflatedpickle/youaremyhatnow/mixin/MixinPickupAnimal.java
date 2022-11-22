/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.youaremyhatnow.mixin;

import java.util.Optional;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings({"UnusedMixin", "unused"})
@Mixin({AnimalEntity.class, TameableEntity.class})
public abstract class MixinPickupAnimal extends PassiveEntity {
  private static final TrackedData<Integer> PUT_DOWN_TICKS =
      DataTracker.registerData(PassiveEntity.class, TrackedDataHandlerRegistry.INTEGER);
  private static final int PUT_DOWN_TICKS_MAX = 10;
  private static final String NBT_KEY = "PutDown";

  protected MixinPickupAnimal(EntityType<? extends PassiveEntity> entityType, World world) {
    super(entityType, world);
  }

  public int getPutDownTicks() {
    return this.dataTracker.get(PUT_DOWN_TICKS);
  }

  public void setPutDownTicks(int ticks) {
    this.dataTracker.set(PUT_DOWN_TICKS, ticks);
  }

  @Override
  public void initDataTracker() {
    super.initDataTracker();
    this.dataTracker.startTracking(PUT_DOWN_TICKS, PUT_DOWN_TICKS_MAX);
  }

  public Optional<ActionResult> putdown(PlayerEntity player, Hand hand) {
    if (hand == Hand.MAIN_HAND
        && player.getStackInHand(hand).isEmpty() /*&& player.isSneaking()*/) {
      this.startRiding(player, true);
      setPutDownTicks(PUT_DOWN_TICKS_MAX);
      return Optional.of(ActionResult.SUCCESS);
    }

    return Optional.empty();
  }

  @Override
  public ActionResult interactMob(PlayerEntity player, Hand hand) {
    return putdown(player, hand).orElse(super.interactMob(player, hand));
  }

  @Override
  public void tickRiding() {
    super.tickRiding();

    if (this.hasVehicle() && vehicle instanceof PlayerEntity) {
      if (getPutDownTicks() > 0) {
        setPutDownTicks(Math.max(0, getPutDownTicks() - 1));
      } else if (((PlayerEntity) vehicle).shouldDismount()) {
        this.dismountVehicle();
      }
    }
  }

  @Override
  public void writeCustomDataToNbt(NbtCompound nbt) {
    super.writeCustomDataToNbt(nbt);
    nbt.putInt(NBT_KEY, this.getPutDownTicks());
  }

  @Override
  public void readCustomDataFromNbt(NbtCompound nbt) {
    super.readCustomDataFromNbt(nbt);
    this.setPutDownTicks(nbt.getInt(NBT_KEY));
  }
}
