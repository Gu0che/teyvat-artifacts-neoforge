package com.guoche.teyvat_artifacts;

public final class AlchemyTableMaterials {
    private AlchemyTableMaterials() {
    }

    public record Counts(int unction, int essence, int elixir) {
        public int value() {
            return unction + essence * 4 + elixir * 16;
        }
    }

    // Prefer higher tiers, but keep lower tiers when a higher output slot is full.
    public static Counts pack(int value, int unctionLimit, int essenceLimit, int elixirLimit) {
        if (value < 0 || unctionLimit < 0 || essenceLimit < 0 || elixirLimit < 0) {
            throw new IllegalArgumentException("Negative material value or capacity");
        }
        int elixir = Math.min(value / 16, elixirLimit);
        int remainder = value - elixir * 16;
        int essence = Math.min(remainder / 4, essenceLimit);
        int unction = remainder - essence * 4;
        return unction <= unctionLimit ? new Counts(unction, essence, elixir) : null;
    }
}
