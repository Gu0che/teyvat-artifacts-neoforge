package com.guoche.teyvat_artifacts;

import net.minecraft.util.StringRepresentable;

public enum MondstadtLeylineSpawnerPhase implements StringRepresentable {
    READY("ready", 0),
    ACTIVE("active", 8),
    REWARD("reward", 8);

    private final String serializedName;
    private final int lightLevel;

    MondstadtLeylineSpawnerPhase(String serializedName, int lightLevel) {
        this.serializedName = serializedName;
        this.lightLevel = lightLevel;
    }

    public int lightLevel() {
        return lightLevel;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }
}
