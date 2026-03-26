package dev.sterner.guardvillagers.client.model;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class GuardPlayerRenderState extends PlayerEntityRenderState {
    public int kickTicks;
    public ItemStack mainHandStack;
    public ItemStack offHandStack;
    public boolean isEating;
    public int itemUseTimeLeft;
    public Hand activeHand;

    public GuardPlayerRenderState() {
        this.mainHandStack = ItemStack.EMPTY;
        this.offHandStack = ItemStack.EMPTY;
    }

    public ItemStack getStackInHand(Hand hand) {
        return hand == Hand.MAIN_HAND ? this.mainHandStack : this.offHandStack;
    }
}
