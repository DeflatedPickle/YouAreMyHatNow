package com.deflatedpickle.axolotlhats.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AxolotlEntity;
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
        if (!(passenger instanceof AxolotlEntity) || !this.hasPassenger(passenger)) return;

        passenger.setPosition(
                this.getX(),
                this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset() + 0.3,
                this.getZ()
        );
        passenger.setYaw(this.getYaw());
    }
}
