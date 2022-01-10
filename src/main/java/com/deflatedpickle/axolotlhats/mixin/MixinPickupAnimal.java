package com.deflatedpickle.axolotlhats.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@SuppressWarnings({"UnusedMixin", "unused"})
@Mixin({
        AnimalEntity.class,
        TameableEntity.class
})
abstract public class MixinPickupAnimal extends PassiveEntity {
    protected MixinPickupAnimal(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    public Optional<ActionResult> pickUp(PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND && player.getStackInHand(hand).isEmpty() && player.isSneaking()) {
            this.startRiding(player, true);
            return Optional.of(ActionResult.SUCCESS);
        }

        return Optional.empty();
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return pickUp(player, hand).orElse(super.interactMob(player, hand));
    }

    @Override
    public void tickRiding() {
        super.tickRiding();

        if (this.hasVehicle() && vehicle instanceof PlayerEntity && ((PlayerEntity) vehicle).shouldDismount()) {
            this.dismountVehicle();
        }
    }
}
