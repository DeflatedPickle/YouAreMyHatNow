package com.deflatedpickle.axolotlhats.mixin;

import com.deflatedpickle.axolotlhats.AxolotlHats;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings({"UnusedMixin", "unused"})
@Mixin(AxolotlEntity.class)
abstract public class MixinPickupAxolotl extends AnimalEntity {
    protected MixinPickupAxolotl(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return Bucketable.tryBucket(player, hand, (AxolotlEntity) (Object) this)
                .or(() -> java.util.Optional.of(AxolotlHats.INSTANCE.interact((AxolotlEntity) (Object) this, player, hand)))
                .orElse(super.interactMob(player, hand));
    }

    @Override
    public void tickRiding() {
        super.tickRiding();
        assert vehicle != null;
        AxolotlHats.INSTANCE.tickRiding((AxolotlEntity) (Object) this, vehicle);
    }
}
