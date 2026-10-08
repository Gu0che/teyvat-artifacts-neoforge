package com.guoche.teyvat_artifacts;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

final class LeylineTrialSessions {
    private static final Map<BlockEntity, Set<UUID>> ACTIVE = new WeakHashMap<>();

    private LeylineTrialSessions() {
    }

    static void register(BlockEntity spawner, Collection<UUID> participants) {
        ACTIVE.computeIfAbsent(spawner, ignored -> Set.copyOf(participants));
    }

    static void unregister(BlockEntity spawner) {
        ACTIVE.remove(spawner);
    }

    static void participantDied(ServerPlayer deadPlayer) {
        Map<LeylineTrialSession, Set<UUID>> failed = new HashMap<>();
        for (Map.Entry<BlockEntity, Set<UUID>> entry : new ArrayList<>(ACTIVE.entrySet())) {
            BlockEntity spawner = entry.getKey();
            if (spawner instanceof LeylineTrialSession session
                    && spawner.getLevel() instanceof ServerLevel level
                    && level.getServer() == deadPlayer.getServer()
                    && level.hasChunkAt(spawner.getBlockPos())
                    && level.getBlockEntity(spawner.getBlockPos()) == spawner
                    && entry.getValue().contains(deadPlayer.getUUID())) {
                failed.put(session, entry.getValue());
            }
        }

        for (Map.Entry<LeylineTrialSession, Set<UUID>> entry : failed.entrySet()) {
            entry.getKey().failForParticipantDeath();
            for (UUID participantId : entry.getValue()) {
                ServerPlayer participant = deadPlayer.getServer().getPlayerList().getPlayer(participantId);
                if (participant != null) {
                    participant.displayClientMessage(Component.translatable("message.teyvat_artifacts.leyline.failed_player_died"), true);
                }
            }
        }
    }
}
