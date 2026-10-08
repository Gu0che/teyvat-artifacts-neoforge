package com.guoche.teyvat_artifacts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

public final class LeylineSpawnerGenerationPack {
    private static final String PACK_ID = "teyvat_artifacts/leyline_generation_config";
    private static final String TAG_DIRECTORY = "tags/worldgen/biome/has_structure/";

    private LeylineSpawnerGenerationPack() {
    }

    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) {
            return;
        }

        PackLocationInfo location = new PackLocationInfo(
                PACK_ID,
                Component.literal("Teyvat Artifacts Leyline Generation Config"),
                PackSource.BUILT_IN,
                Optional.empty()
        );
        Pack.ResourcesSupplier resources = new Pack.ResourcesSupplier() {
            @Override
            public PackResources openPrimary(PackLocationInfo packLocation) {
                return new DynamicResources(packLocation);
            }

            @Override
            public PackResources openFull(PackLocationInfo packLocation, Pack.Metadata metadata) {
                return new DynamicResources(packLocation);
            }
        };
        Pack pack = Pack.readMetaAndCreate(
                location,
                resources,
                PackType.SERVER_DATA,
                new PackSelectionConfig(true, Pack.Position.TOP, true)
        );
        if (pack != null) {
            event.addRepositorySource(consumer -> consumer.accept(pack));
        }
    }

    private static final class DynamicResources implements PackResources {
        private final PackLocationInfo location;

        private DynamicResources(PackLocationInfo location) {
            this.location = location;
        }

        @Override
        public IoSupplier<InputStream> getRootResource(String... elements) {
            if (elements.length == 1 && "pack.mcmeta".equals(elements[0])) {
                return bytes(LeylineSpawnerGenerationPack::createPackMetadata);
            }
            return null;
        }

        @Override
        public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
            TeyvatArtifactsConfig.LeylineSpawner spawner = findSpawner(type, location);
            return spawner == null ? null : bytes(() -> createBiomeTag(spawner));
        }

        @Override
        public void listResources(PackType type, String namespace, String path, ResourceOutput output) {
            if (type != PackType.SERVER_DATA || !TeyvatArtifacts.MODID.equals(namespace)) {
                return;
            }
            for (TeyvatArtifactsConfig.LeylineSpawner spawner : TeyvatArtifactsConfig.LeylineSpawner.values()) {
                ResourceLocation location = tagLocation(spawner);
                if (location.getPath().startsWith(path)) {
                    output.accept(location, bytes(() -> createBiomeTag(spawner)));
                }
            }
        }

        @Override
        public Set<String> getNamespaces(PackType type) {
            return type == PackType.SERVER_DATA ? Set.of(TeyvatArtifacts.MODID) : Set.of();
        }

        @Override
        public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) {
            if ("pack".equals(serializer.getMetadataSectionName())) {
                return serializer.fromJson(createPackSection());
            }
            return null;
        }

        @Override
        public PackLocationInfo location() {
            return location;
        }

        @Override
        public void close() {
        }
    }

    private static TeyvatArtifactsConfig.LeylineSpawner findSpawner(PackType type, ResourceLocation location) {
        if (type != PackType.SERVER_DATA || !TeyvatArtifacts.MODID.equals(location.getNamespace())) {
            return null;
        }
        String path = location.getPath();
        if (!path.startsWith(TAG_DIRECTORY) || !path.endsWith(".json")) {
            return null;
        }
        String id = path.substring(TAG_DIRECTORY.length(), path.length() - ".json".length());
        return TeyvatArtifactsConfig.LeylineSpawner.byId(id);
    }

    private static ResourceLocation tagLocation(TeyvatArtifactsConfig.LeylineSpawner spawner) {
        return ResourceLocation.fromNamespaceAndPath(
                TeyvatArtifacts.MODID,
                TAG_DIRECTORY + spawner.id() + ".json"
        );
    }

    private static byte[] createBiomeTag(TeyvatArtifactsConfig.LeylineSpawner spawner) {
        JsonObject root = new JsonObject();
        root.addProperty("replace", true);
        JsonArray values = new JsonArray();
        for (String selector : spawner.configuredBiomes()) {
            JsonObject entry = new JsonObject();
            entry.addProperty("id", selector);
            entry.addProperty("required", false);
            values.add(entry);
        }
        root.add("values", values);
        return root.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] createPackMetadata() {
        JsonObject root = new JsonObject();
        root.add("pack", createPackSection());
        return root.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static JsonObject createPackSection() {
        JsonObject pack = new JsonObject();
        pack.addProperty("description", "Teyvat Artifacts Leyline generation configuration");
        pack.addProperty("pack_format", SharedConstants.DATA_PACK_FORMAT);
        return pack;
    }

    private static IoSupplier<InputStream> bytes(java.util.function.Supplier<byte[]> supplier) {
        return () -> new ByteArrayInputStream(supplier.get());
    }
}
