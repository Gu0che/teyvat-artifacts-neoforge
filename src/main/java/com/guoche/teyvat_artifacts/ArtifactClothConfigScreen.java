package com.guoche.teyvat_artifacts;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/** Loaded only through the optional client config-screen factory. */
public final class ArtifactClothConfigScreen {
    private ArtifactClothConfigScreen() {}

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.teyvat_artifacts.title"))
                .setEditable(Minecraft.getInstance().level == null)
                .setDoesConfirmSave(true);
        addConfig(builder, TeyvatArtifactsConfig.SPEC, "common");
        addConfig(builder, ArtifactSetConfig.SPEC, "sets");
        builder.setSavingRunnable(() -> {
            TeyvatArtifactsConfig.SPEC.save();
            ArtifactSetConfig.SPEC.save();
            ArtifactStat.invalidateCache();
        });
        return builder.build();
    }

    private static void addConfig(ConfigBuilder builder, ModConfigSpec spec, String name) {
        ConfigCategory category = builder.getOrCreateCategory(label(name));
        ConfigEntryBuilder entries = builder.entryBuilder();
        category.addEntry(entries.startTextDescription(
                Component.translatable("config.teyvat_artifacts.notice")).build());
        if (!spec.isLoaded()) {
            category.addEntry(entries.startTextDescription(
                    Component.translatable("config.teyvat_artifacts.unloaded")).build());
            return;
        }
        populate(entries, spec, spec.getValues(), List.of(), category::addEntry);
    }

    private static void populate(ConfigEntryBuilder entries, ModConfigSpec spec,
                                 UnmodifiableConfig node, List<String> path,
                                 Consumer<AbstractConfigListEntry> output) {
        // Sort by key rather than relying on NightConfig's map iteration order.
        node.valueMap().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            List<String> childPath = new ArrayList<>(path);
            childPath.add(entry.getKey());
            Object value = entry.getValue();
            if (value instanceof UnmodifiableConfig child) {
                var group = entries.startSubCategory(label(entry.getKey())).setExpanded(false);
                String comment = spec.getLevelComment(childPath);
                if (comment != null) group.setTooltip(tooltip(childPath, comment));
                populate(entries, spec, child, childPath, group::add);
                output.accept(group.build());
            } else if (value instanceof ModConfigSpec.ConfigValue<?> config) {
                output.accept(field(entries, spec, childPath, config));
            } else {
                throw new IllegalStateException("Unsupported config node: " + childPath);
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static AbstractConfigListEntry field(ConfigEntryBuilder entries, ModConfigSpec spec,
                                                  List<String> path, ModConfigSpec.ConfigValue config) {
        ModConfigSpec.ValueSpec metadata = spec.getSpec().get(path);
        Object current = config.get();
        Object defaults = metadata.getDefault();
        String key = path.get(path.size() - 1);
        Component title = label(key);
        Component[] tips = tooltip(path, metadata.getComment());
        if (current instanceof Boolean value) {
            return entries.startBooleanToggle(title, value).setDefaultValue((Boolean) defaults)
                    .setTooltip(tips).requireRestart().setSaveConsumer(config::set).build();
        }
        if (current instanceof Integer value) {
            return entries.startIntField(title, value).setDefaultValue((Integer) defaults)
                    .setTooltip(tips).requireRestart().setErrorSupplier(v -> error(metadata, v, path))
                    .setSaveConsumer(config::set).build();
        }
        if (current instanceof Double value) {
            return entries.startDoubleField(title, value).setDefaultValue((Double) defaults)
                    .setTooltip(tips).requireRestart().setErrorSupplier(v -> error(metadata, v, path))
                    .setSaveConsumer(config::set).build();
        }
        if (current instanceof String value) {
            return entries.startStrField(title, value).setDefaultValue((String) defaults)
                    .setTooltip(tips).requireRestart().setErrorSupplier(v -> error(metadata, v, path))
                    .setSaveConsumer(config::set).build();
        }
        if (current instanceof List<?> values && defaults instanceof List<?> defaultList) {
            // Config defaults describe the element type even when the edited list is empty.
            if (!defaultList.isEmpty() && defaultList.get(0) instanceof Number) {
                if (key.equals("countsByStar") || key.equals("extraCountsByStar")) {
                    return entries.startIntList(title, values.stream().map(v -> ((Number) v).intValue()).toList())
                            .setDefaultValue(defaultList.stream().map(v -> ((Number) v).intValue()).toList())
                            .setTooltip(tips).requireRestart().setErrorSupplier(v -> error(metadata, v, path))
                            .setSaveConsumer(v -> config.set(List.copyOf(v))).build();
                }
                return entries.startDoubleList(title, values.stream().map(v -> ((Number) v).doubleValue()).toList())
                        .setDefaultValue(defaultList.stream().map(v -> ((Number) v).doubleValue()).toList())
                        .setTooltip(tips).requireRestart().setErrorSupplier(v -> error(metadata, v, path))
                        .setSaveConsumer(v -> config.set(List.copyOf(v))).build();
            }
            return entries.startStrList(title, values.stream().map(String.class::cast).toList())
                    .setDefaultValue(defaultList.stream().map(String.class::cast).toList())
                    .setTooltip(tips).requireRestart().setErrorSupplier(v -> error(metadata, v, path))
                    .setSaveConsumer(v -> config.set(List.copyOf(v))).build();
        }
        throw new IllegalStateException("Unsupported config value: " + path);
    }

    private static Optional<Component> error(ModConfigSpec.ValueSpec metadata, Object value, List<String> path) {
        boolean fiveStars = path.get(0).equals("artifactSubstats")
                && path.get(path.size() - 1).endsWith("ByStar");
        if (fiveStars && value instanceof List<?> list && list.size() != 5) {
            return Optional.of(Component.translatable("config.teyvat_artifacts.five_values"));
        }
        // List specifications may accept a list but repair invalid individual entries.
        if ((value instanceof Number n && !Double.isFinite(n.doubleValue()))
                || !metadata.test(value) || !Objects.equals(metadata.correct(value), value)) {
            return Optional.of(Component.translatable("config.teyvat_artifacts.invalid"));
        }
        return Optional.empty();
    }

    private static Component[] tooltip(List<String> path, String comment) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal(String.join(".", path)));
        if (comment != null) {
            boolean chinese = Minecraft.getInstance().getLanguageManager().getSelected().startsWith("zh");
            comment.lines().filter(line -> !line.isBlank())
                    .filter(line -> line.codePoints().anyMatch(cp -> cp >= 0x4e00 && cp <= 0x9fff) == chinese)
                    .map(Component::literal).forEach(lines::add);
        }
        return lines.toArray(Component[]::new);
    }

    private static Component label(String key) {
        String translation = "config.teyvat_artifacts." + key;
        if (I18n.exists(translation)) return Component.translatable(translation);
        for (ArtifactSet set : ArtifactSet.values()) {
            if (set.id().equals(key)) return Component.translatable(set.nameKey());
        }
        String block = "block.teyvat_artifacts." + key;
        if (I18n.exists(block)) return Component.translatable(block);
        return Component.literal(key.replaceAll("([a-z])([A-Z])", "$1 $2").replace('_', ' '));
    }
}

