package dev.sterner.guardvillagers;

import dev.sterner.guardvillagers.client.model.GuardArmorModel;
import dev.sterner.guardvillagers.client.model.GuardSteveModel;
import dev.sterner.guardvillagers.client.model.GuardVillagerModel;
import dev.sterner.guardvillagers.client.renderer.GuardRenderer;
import dev.sterner.guardvillagers.client.screen.GuardVillagerScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;

import static dev.sterner.guardvillagers.GuardVillagers.*;

public class GuardVillagersClient implements ClientModInitializer {

    public static ModelLayerLocation GUARD = new ModelLayerLocation(GuardVillagers.id( "guard"), "main");
    public static ModelLayerLocation GUARD_STEVE = new ModelLayerLocation(GuardVillagers.id( "guard_steve"), "main");

    public static final ModelLayerLocation GUARD_ARMOR_OUTER_HEAD = new ModelLayerLocation(GuardVillagers.id("guard_armor_outer_head"), "main");
    public static final ModelLayerLocation GUARD_ARMOR_OUTER_CHEST = new ModelLayerLocation(GuardVillagers.id("guard_armor_outer_chest"), "main");
    public static final ModelLayerLocation GUARD_ARMOR_OUTER_FEET = new ModelLayerLocation(GuardVillagers.id("guard_armor_outer_feet"), "main");
    public static final ModelLayerLocation GUARD_ARMOR_INNER_LEGS = new ModelLayerLocation(GuardVillagers.id("guard_armor_inner_legs"), "main");

    @Override
    public void onInitializeClient() {
        MenuScreens.register(GUARD_SCREEN_HANDLER, GuardVillagerScreen::new);
        EntityModelLayerRegistry.registerModelLayer(GUARD, GuardVillagerModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(GUARD_STEVE, GuardSteveModel::createMesh);
        EntityModelLayerRegistry.registerModelLayer(GUARD_ARMOR_OUTER_HEAD, GuardArmorModel::createOuterArmorLayer);
        EntityModelLayerRegistry.registerModelLayer(GUARD_ARMOR_OUTER_CHEST, GuardArmorModel::createOuterArmorLayer);
        EntityModelLayerRegistry.registerModelLayer(GUARD_ARMOR_OUTER_FEET, GuardArmorModel::createOuterArmorLayer);
        EntityModelLayerRegistry.registerModelLayer(GUARD_ARMOR_INNER_LEGS, GuardArmorModel::createInnerArmorLayer);
        EntityRendererRegistry.register(GUARD_VILLAGER, GuardRenderer::new);
    }
}
