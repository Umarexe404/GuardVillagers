package dev.sterner.guardvillagers.client.renderer;

import dev.sterner.guardvillagers.GuardVillagers;
import dev.sterner.guardvillagers.GuardVillagersClient;
import dev.sterner.guardvillagers.GuardVillagersConfig;
import dev.sterner.guardvillagers.client.model.*;
import dev.sterner.guardvillagers.common.entity.GuardEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class GuardRenderer extends BipedEntityRenderer<GuardEntity, GuardBipedRenderState, BipedEntityModel<GuardBipedRenderState>> {

    private final BipedEntityModel<GuardBipedRenderState> normal = this.getModel();

    public GuardRenderer(EntityRendererFactory.Context context) {
        super(context, new GuardVillagerModel(context.getPart(GuardVillagersClient.GUARD)), 0.5F);

        BipedEntityModel<GuardBipedRenderState> steve = new BipedEntityModel(context.getPart(EntityModelLayers.PLAYER));
        if (GuardVillagersConfig.useSteveModel)
            this.model = steve;
        else
            this.model = normal;

        GuardArmorModel headOuter = new GuardArmorModel(context.getPart(GuardVillagersClient.GUARD_ARMOR_OUTER_HEAD));
        GuardArmorModel chestOuter = new GuardArmorModel(context.getPart(GuardVillagersClient.GUARD_ARMOR_OUTER_CHEST));
        GuardArmorModel feetOuter = new GuardArmorModel(context.getPart(GuardVillagersClient.GUARD_ARMOR_OUTER_FEET));
        GuardArmorModel legsInner = new GuardArmorModel(context.getPart(GuardVillagersClient.GUARD_ARMOR_INNER_LEGS));
        refreshVisibility(headOuter, EquipmentSlot.HEAD);
        refreshVisibility(chestOuter, EquipmentSlot.CHEST);
        refreshVisibility(legsInner, EquipmentSlot.LEGS);
        refreshVisibility(feetOuter, EquipmentSlot.FEET);
        EquipmentModelData<BipedEntityModel<GuardBipedRenderState>> adult = new EquipmentModelData(headOuter, chestOuter, legsInner, feetOuter);
        this.addFeature(new ArmorFeatureRenderer(this, adult, adult, context.getEquipmentRenderer()));

    }

    private static void refreshVisibility(BipedEntityModel<?> m, EquipmentSlot slot) {
        m.head.visible = m.hat.visible = false;
        m.body.visible = m.rightArm.visible = m.leftArm.visible = false;
        m.rightLeg.visible = m.leftLeg.visible = false;
        switch (slot) {
            case HEAD:
                m.head.visible = true;
                m.hat.visible = true;
                break;
            case CHEST:
                m.body.visible = true;
                m.rightArm.visible = true;
                m.leftArm.visible = true;
                break;
            case LEGS:
            case FEET:
                m.rightLeg.visible = true;
                m.leftLeg.visible = true;
        }

    }

    @Override
    public GuardBipedRenderState createRenderState() {
        return new GuardBipedRenderState();
    }

    @Override
    public void updateRenderState(GuardEntity entity, GuardBipedRenderState state, float f) {
        super.updateRenderState(entity, state, f);
        state.guardVariant = entity.getGuardVariant();
        state.sneaking = entity.isSneaking();
        state.mainArm = entity.getMainArm();
        state.mainHandStack = entity.getMainHandStack();
        state.offHandStack = entity.getOffHandStack();
        state.hasRangedWeapon = isRanged(state.mainHandStack) || isRanged(state.offHandStack);
        BipedEntityModel.ArmPose mainPose = this.getArmPose(entity, state.mainHandStack, state.offHandStack, Hand.MAIN_HAND);
        BipedEntityModel.ArmPose offPose = this.getArmPose(entity, state.mainHandStack, state.offHandStack, Hand.OFF_HAND);
        if (state.mainArm == Arm.RIGHT) {
            state.rightArmPose = mainPose;
            state.leftArmPose = offPose;
        } else {
            state.rightArmPose = offPose;
            state.leftArmPose = mainPose;
        }
    }

    private static boolean isRanged(ItemStack s) {
        if (s != null && !s.isEmpty()) {
            Item it = s.getItem();
            return it instanceof BowItem || it instanceof CrossbowItem;
        } else {
            return false;
        }
    }

    private BipedEntityModel.ArmPose getArmPose(GuardEntity entityIn, ItemStack itemStackMain, ItemStack itemStackOff, Hand handIn) {
        BipedEntityModel.ArmPose bipedmodel$armpose = BipedEntityModel.ArmPose.EMPTY;
        ItemStack itemstack = handIn == Hand.MAIN_HAND ? itemStackMain : itemStackOff;
        if (!itemstack.isEmpty()) {
            bipedmodel$armpose = BipedEntityModel.ArmPose.ITEM;
            if (entityIn.getItemUseTimeLeft() > 0) {
                UseAction useaction = itemstack.getUseAction();
                switch (useaction) {
                    case BLOCK:
                        bipedmodel$armpose = BipedEntityModel.ArmPose.BLOCK;
                        break;
                    case BOW:
                        bipedmodel$armpose = BipedEntityModel.ArmPose.BOW_AND_ARROW;
                        break;
                    case SPEAR:
                        bipedmodel$armpose = BipedEntityModel.ArmPose.THROW_TRIDENT;
                        break;
                    case CROSSBOW:
                        if (handIn == entityIn.getActiveHand()) {
                            bipedmodel$armpose = BipedEntityModel.ArmPose.CROSSBOW_CHARGE;
                        }
                        break;
                    default:
                        bipedmodel$armpose = BipedEntityModel.ArmPose.EMPTY;
                        break;
                }
            } else {
                boolean flag1 = itemStackMain.getItem() instanceof CrossbowItem;
                boolean flag2 = itemStackOff.getItem() instanceof CrossbowItem;
                if (flag1 && entityIn.isAttacking()) {
                    bipedmodel$armpose = BipedEntityModel.ArmPose.CROSSBOW_HOLD;
                }

                if (flag2 && itemStackMain.getItem().getUseAction(itemStackMain) == UseAction.NONE
                        && entityIn.isAttacking()) {
                    bipedmodel$armpose = BipedEntityModel.ArmPose.CROSSBOW_HOLD;
                }
            }
        }
        return bipedmodel$armpose;
    }

    @Override
    protected void scale(GuardBipedRenderState state, MatrixStack matrices) {
        matrices.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    public Identifier getTexture(GuardBipedRenderState state) {
        return !GuardVillagersConfig.useSteveModel
                ? GuardVillagers.id(
                "textures/entity/guard/guard_" + state.guardVariant + ".png")
                : GuardVillagers.id(
                "textures/entity/guard/guard_steve_" + state.guardVariant + ".png");
    }
}