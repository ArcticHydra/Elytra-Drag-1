package net.bl4st.elytradrag.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ElytraEntityModel.class)
public class ElytraDragAnimation {
    @Shadow
    private ModelPart rightWing;

    @Shadow
    private ModelPart leftWing;
    /**
     * Scuffed way to handle some kind of animation,
     * it just works™
     */

    @Inject(method = "setAngles", at = @At("TAIL"))
    private void SetAngles(LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!livingEntity.isSneaking() || !livingEntity.isGliding())
            return;
        long FLAPPING_SPEED = 3L;
        float progressCycle = (float)(System.currentTimeMillis() * FLAPPING_SPEED % 1000L) / 1000.0F;
        float progress = ((float)Math.sin(progressCycle * Math.PI * 2.0D) + 1.0F) / 2.0F;
        float animSpeedCoef = GetSpeedAnimationCoef((float)livingEntity.getVelocity().length() * 20.0F);
        this.rightWing.yaw = -1.5f * animSpeedCoef;
        this.leftWing.yaw = 1.5f * animSpeedCoef;
        this.rightWing.pitch = progress * (1f - animSpeedCoef);
        this.leftWing.pitch = progress * (1f - animSpeedCoef);

        this.rightWing.roll = 1f;
        this.leftWing.roll = -1f;
    }

    private float GetSpeedAnimationCoef(float value) {
        value = Math.max(2.0F, Math.min(45.0F, value));
        return (float)Math.pow(((value - 2.0F) / 45.0F), 0.25D);
    }
}
