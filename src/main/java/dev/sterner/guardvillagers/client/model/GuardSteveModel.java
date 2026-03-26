package dev.sterner.guardvillagers.client.model;


import dev.sterner.guardvillagers.common.entity.GuardEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class GuardSteveModel extends PlayerEntityModel {
    public GuardSteveModel(ModelPart root) {
        super(root, false);
    }

    @Override
    public void setAngles(PlayerEntityRenderState playerEntityRenderState) {
        super.setAngles(playerEntityRenderState);

        if (playerEntityRenderState instanceof GuardPlayerRenderState state) {
            if (state.kickTicks > 0) {
                float f1 = 1.0F - (float) MathHelper.abs(10 - 2 * state.kickTicks) / 10.0F;
                this.rightLeg.pitch = MathHelper.lerp(f1, this.rightLeg.pitch, -1.40F);
            }

            var ageInTicks = state.age;
            if (state.mainArm == Arm.RIGHT) {
                this.eatingAnimationRightHand(Hand.MAIN_HAND, state, ageInTicks);
                this.eatingAnimationLeftHand(Hand.OFF_HAND, state, ageInTicks);
            } else {
                this.eatingAnimationRightHand(Hand.OFF_HAND, state, ageInTicks);
                this.eatingAnimationLeftHand(Hand.MAIN_HAND, state, ageInTicks);
            }
        }
    }

    public static TexturedModelData createMesh() {
        ModelData meshdefinition = PlayerEntityModel.getTexturedModelData(Dilation.NONE, false);
        return TexturedModelData.of(meshdefinition, 64, 64);
    }

    public void eatingAnimationRightHand(Hand hand, GuardPlayerRenderState entity, float ageInTicks) {
        ItemStack itemstack = entity.getStackInHand(hand);
        boolean drinkingoreating = itemstack.getUseAction() == UseAction.EAT
                || itemstack.getUseAction() == UseAction.DRINK;
        if (entity.isEating && drinkingoreating
                || entity.itemUseTimeLeft > 0 && drinkingoreating && entity.activeHand == hand) {
            this.rightArm.yaw = -0.5F;
            this.rightArm.pitch = -1.3F;
            this.rightArm.roll = MathHelper.cos(ageInTicks) * 0.1F;
            this.head.pitch = MathHelper.cos(ageInTicks) * 0.2F;
            this.head.yaw = 0.0F;
            this.hat.setTransform(head.getTransform());
        }
    }

    public void eatingAnimationLeftHand(Hand hand, GuardPlayerRenderState entity, float ageInTicks) {
        ItemStack itemstack = entity.getStackInHand(hand);
        boolean drinkingoreating = itemstack.getUseAction() == UseAction.EAT
                || itemstack.getUseAction() == UseAction.DRINK;
        if (entity.isEating && drinkingoreating
                || entity.itemUseTimeLeft > 0 && drinkingoreating && entity.activeHand == hand) {
            this.leftArm.yaw = 0.5F;
            this.leftArm.pitch = -1.3F;
            this.leftArm.roll = MathHelper.cos(ageInTicks) * 0.1F;
            this.head.pitch = MathHelper.cos(ageInTicks) * 0.2F;
            this.head.yaw = 0.0F;
            this.hat.setTransform(head.getTransform());
        }
    }
}