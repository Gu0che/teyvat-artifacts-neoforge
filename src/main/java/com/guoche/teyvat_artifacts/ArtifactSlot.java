package com.guoche.teyvat_artifacts;

import net.minecraft.network.chat.Component;

public enum ArtifactSlot {
    FLOWER("flower", "curios.identifier.flower"),
    FEATHER("feather", "curios.identifier.feather"),
    SANDS("sands", "curios.identifier.sands"),
    GOBLET("goblet", "curios.identifier.goblet"),
    CIRCLET("circlet", "curios.identifier.circlet");

    private final String id;
    private final String translationKey;

    ArtifactSlot(String id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    public String id() {
        return id;
    }

    public String translationKey() {
        return translationKey;
    }

    public Component displayName() {
        return Component.translatable(translationKey);
    }
}
