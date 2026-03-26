package dev.sterner.guardvillagers.client.model;


import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class GuardBipedRenderState extends BipedEntityRenderState {
    public int kickTicks;
    public boolean hasRangedWeapon;
    public int guardVariant;
    public ItemStack mainHandStack;
    public ItemStack offHandStack;
    public boolean isEating;
    public int itemUseTimeLeft;
    public Hand activeHand;

    public GuardBipedRenderState() {
        this.mainHandStack = ItemStack.EMPTY;
        this.offHandStack = ItemStack.EMPTY;
    }

    public ItemStack getStackInHand(Hand hand) {
        return hand == Hand.MAIN_HAND ? this.mainHandStack : this.offHandStack;
    }
}
