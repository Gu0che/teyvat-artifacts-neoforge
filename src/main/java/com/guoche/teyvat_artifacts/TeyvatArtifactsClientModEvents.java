package com.guoche.teyvat_artifacts;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = TeyvatArtifacts.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class TeyvatArtifactsClientModEvents {
    private TeyvatArtifactsClientModEvents() {
    }

    @SubscribeEvent
    public static void registerConfigScreen(net.neoforged.fml.event.lifecycle.FMLClientSetupEvent event) {
        if (!net.neoforged.fml.ModList.get().isLoaded("cloth_config")) return;
        net.neoforged.fml.ModList.get().getModContainerById(TeyvatArtifacts.MODID).orElseThrow()
                .registerExtensionPoint(net.neoforged.neoforge.client.gui.IConfigScreenFactory.class,
                        (container, parent) -> ArtifactClothConfigScreen.create(parent));
    }

    @SubscribeEvent
    public static void registerAlchemyScreen(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
        event.register(AlchemyTableContent.MENU.get(), AlchemyTableScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.<Crystalfly>registerEntityRenderer(TeyvatArtifacts.ANEMO_CRYSTALFLY.get(), CrystalflyRenderer::new);
        event.<Crystalfly>registerEntityRenderer(TeyvatArtifacts.ICE_CRYSTALFLY.get(), CrystalflyRenderer::new);
        event.<Crystalfly>registerEntityRenderer(TeyvatArtifacts.GEO_CRYSTALFLY.get(), CrystalflyRenderer::new);
        event.<Crystalfly>registerEntityRenderer(TeyvatArtifacts.ELECTRO_CRYSTALFLY.get(), CrystalflyRenderer::new);
        event.<Crystalfly>registerEntityRenderer(TeyvatArtifacts.DENDRO_CRYSTALFLY.get(), CrystalflyRenderer::new);
        event.<Crystalfly>registerEntityRenderer(TeyvatArtifacts.HYDRO_CRYSTALFLY.get(), CrystalflyRenderer::new);
        event.<Crystalfly>registerEntityRenderer(TeyvatArtifacts.PYRO_CRYSTALFLY.get(), CrystalflyRenderer::new);
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.MONDSTADT_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                MondstadtLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.LIYUE_CLEAR_POOL_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                LiyueClearPoolLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.MONDSTADT_RIDGE_WATCH_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                MondstadtRidgeWatchLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.MONDSTADT_THORNY_CROWN_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                MondstadtThornyCrownLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.MONDSTADT_PEAK_VINDAGNYR_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                MondstadtPeakVindagnyrLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.MONDSTADT_VALLEY_REMEMBRANCE_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                MondstadtValleyRemembranceLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.LIYUE_DOMAIN_GUYUN_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                LiyueDomainGuyunLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.LIYUE_LOST_VALLEY_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                LiyueLostValleyLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.LIYUE_ZHOU_FORMULA_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                LiyueZhouFormulaLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.INAZUMA_MOMIJI_DYED_COURT_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                InazumaMomijiDyedCourtLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.INAZUMA_SLUMBERING_COURT_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                InazumaSlumberingCourtLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.SUMERU_MOLTEN_IRON_FORTRESS_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                SumeruMoltenIronFortressLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.SUMERU_SOLITARY_ENLIGHTENMENT_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                SumeruSolitaryEnlightenmentLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.SUMERU_CITY_GOLD_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                SumeruCityGoldLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.FONTAINE_WATERFALL_WEN_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                FontaineWaterfallWenLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.FONTAINE_DENOUEMENT_SIN_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                FontaineDenouementSinLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.FONTAINE_FADED_THEATER_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                FontaineFadedTheaterLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.NATLAN_DERELICT_MASONRY_DOCK_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                NatlanDerelictMasonryDockLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.NATLAN_RAINBOW_SANCTUM_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                NatlanRainbowSanctumLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.NOD_KRAI_MOONCHILDS_TREASURES_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                NodKraiMoonchildsTreasuresLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.NOD_KRAI_FROSTLADEN_MACHINERY_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                NodKraiFrostladenMachineryLeylineSpawnerRenderer::new
        );
        event.registerBlockEntityRenderer(
                LeylineSpawnerContent.SNEZHNAYA_INVERTED_GLACIER_LEYLINE_SPAWNER_BLOCK_ENTITY.get(),
                SnezhnayaInvertedGlacierLeylineSpawnerRenderer::new
        );
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CrystalflyModel.LAYER_LOCATION, CrystalflyModel::createBodyLayer);
    }
}
