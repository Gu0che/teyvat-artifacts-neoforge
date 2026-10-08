package com.guoche.teyvat_artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

public class FontaineWaterfallWenLeylineSpawnerBlockEntity extends BlockEntity implements LeylineTrialSession {
    private static final String PARTICIPANTS_KEY = "Participants";
    private static final String CLAIMED_KEY = "Claimed";
    private static final String SPAWNED_MOBS_KEY = "SpawnedMobs";
    private static final String TRIAL_TICKS_KEY = "TrialTicks";
    private static final String REWARD_TICKS_KEY = "RewardTicks";
    private static final String WAVES_SPAWNED_KEY = "WavesSpawned";
    private static final String NEXT_WAVE_TICKS_KEY = "NextWaveTicks";
    private static final int PARTICIPANT_RADIUS = 24;
    private static final int TRIAL_DURATION_TICKS = 90 * 20;
    private static final int REWARD_WINDOW_TICKS = 20 * 20;
    private static final int WAVE_COUNT = 2;
    private static final int MOBS_PER_WAVE = 5;
    private static final int WAVE_DELAY_TICKS = 20;
    private static final int MAX_SPAWN_ATTEMPTS = 40;
    private static final int SPAWN_RADIUS = 5;
    private static final int REWARD_EJECT_INTERVAL_TICKS = 6;

    private final Set<UUID> participants = new LinkedHashSet<>();
    private final Set<UUID> claimedParticipants = new LinkedHashSet<>();
    private final List<UUID> spawnedMobIds = new ArrayList<>();
    private final Queue<ItemStack> pendingRewardEjections = new ArrayDeque<>();
    private int trialTicksRemaining;
    private int rewardTicksRemaining;
    private int wavesSpawned;
    private int nextWaveTicksRemaining = -1;
    private int rewardEjectTicks;

    public FontaineWaterfallWenLeylineSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(LeylineSpawnerContent.FONTAINE_WATERFALL_WEN_LEYLINE_SPAWNER_BLOCK_ENTITY.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FontaineWaterfallWenLeylineSpawnerBlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            blockEntity.serverTick(serverLevel);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, FontaineWaterfallWenLeylineSpawnerBlockEntity blockEntity) {
        MondstadtLeylineSpawnerPhase phase = state.getValue(FontaineWaterfallWenLeylineSpawnerBlock.PHASE);
        RandomSource random = level.getRandom();
        Vec3 center = Vec3.atCenterOf(pos);

        switch (phase) {
            case ACTIVE -> {
                Vec3 particlePos = center.offsetRandom(random, 1.0F);
                level.addParticle(ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
                level.addParticle(ParticleTypes.FLAME, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
                if (random.nextFloat() <= 0.02F) {
                    level.playLocalSound(
                            pos,
                            SoundEvents.TRIAL_SPAWNER_AMBIENT,
                            SoundSource.BLOCKS,
                            random.nextFloat() * 0.25F + 0.75F,
                            random.nextFloat() + 0.5F,
                            false
                    );
                }
            }
            case REWARD -> {
                Vec3 particlePos = center.offsetRandom(random, 0.9F);
                if (random.nextInt(2) == 0) {
                    level.addParticle(ParticleTypes.SMALL_FLAME, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
                }
            }
            default -> {
            }
        }
    }

    public boolean handleInteraction(Player player, ItemStack heldStack) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return true;
        }

        return switch (getBlockState().getValue(FontaineWaterfallWenLeylineSpawnerBlock.PHASE)) {
            case READY -> LeylineTrialAccess.tryStart(player, heldStack, () -> beginTrial(serverLevel, player));
            case ACTIVE -> {
                player.displayClientMessage(Component.translatable("message.teyvat_artifacts.leyline.trial_in_progress"), true);
                yield true;
            }
            case REWARD -> claimReward(serverLevel, player, heldStack);
        };
    }

    public void discardTrackedMobs(ServerLevel level) {
        for (UUID uuid : spawnedMobIds) {
            Entity entity = level.getEntity(uuid);
            if (entity != null) {
                entity.discard();
            }
        }
        spawnedMobIds.clear();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(TRIAL_TICKS_KEY, trialTicksRemaining);
        tag.putInt(REWARD_TICKS_KEY, rewardTicksRemaining);
        tag.putInt(WAVES_SPAWNED_KEY, wavesSpawned);
        tag.putInt(NEXT_WAVE_TICKS_KEY, nextWaveTicksRemaining);
        tag.put(PARTICIPANTS_KEY, writeUuidList(participants));
        tag.put(CLAIMED_KEY, writeUuidList(claimedParticipants));
        tag.put(SPAWNED_MOBS_KEY, writeUuidList(spawnedMobIds));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        trialTicksRemaining = tag.getInt(TRIAL_TICKS_KEY);
        rewardTicksRemaining = tag.getInt(REWARD_TICKS_KEY);
        wavesSpawned = tag.getInt(WAVES_SPAWNED_KEY);
        nextWaveTicksRemaining = tag.contains(NEXT_WAVE_TICKS_KEY, Tag.TAG_INT) ? tag.getInt(NEXT_WAVE_TICKS_KEY) : -1;
        participants.clear();
        participants.addAll(readUuidList(tag.getList(PARTICIPANTS_KEY, Tag.TAG_STRING)));
        claimedParticipants.clear();
        claimedParticipants.addAll(readUuidList(tag.getList(CLAIMED_KEY, Tag.TAG_STRING)));
        spawnedMobIds.clear();
        spawnedMobIds.addAll(readUuidList(tag.getList(SPAWNED_MOBS_KEY, Tag.TAG_STRING)));
    }

    private void serverTick(ServerLevel level) {
        MondstadtLeylineSpawnerPhase phase = getBlockState().getValue(FontaineWaterfallWenLeylineSpawnerBlock.PHASE);
        switch (phase) {
            case ACTIVE -> tickActive(level);
            case REWARD -> tickReward(level);
            default -> {
            }
        }
    }

    private void tickActive(ServerLevel level) {
        LeylineTrialSessions.register(this, participants);
        if (trialTicksRemaining > 0) {
            trialTicksRemaining--;
        }

        refreshTrackedMobs(level);
        LeylineTrialMonsterPools.recoverTrackedMobs(level, worldPosition, spawnedMobIds, SPAWN_RADIUS);
        if (trialTicksRemaining <= 0) {
            resetTrial(level);
            return;
        }

        if (spawnedMobIds.isEmpty()) {
            if (wavesSpawned >= WAVE_COUNT) {
                openRewardWindow(level);
                return;
            }

            if (nextWaveTicksRemaining < 0) {
                nextWaveTicksRemaining = WAVE_DELAY_TICKS;
            } else if (nextWaveTicksRemaining > 0) {
                nextWaveTicksRemaining--;
            } else if (spawnTrialWave(level)) {
                wavesSpawned++;
                nextWaveTicksRemaining = -1;
            } else {
                nextWaveTicksRemaining = WAVE_DELAY_TICKS;
            }
        }

        setChanged();
    }

    private void tickReward(ServerLevel level) {
        tickPendingRewardEjections(level);

        if (rewardTicksRemaining > 0) {
            rewardTicksRemaining--;
        }

        if ((rewardTicksRemaining <= 0 || participants.isEmpty() || claimedParticipants.size() >= participants.size())
                && pendingRewardEjections.isEmpty()) {
            resetTrial(level);
            return;
        }

        setChanged();
    }

    private boolean beginTrial(ServerLevel level, Player activator) {
        participants.clear();
        claimedParticipants.clear();
        spawnedMobIds.clear();
        pendingRewardEjections.clear();
        trialTicksRemaining = TRIAL_DURATION_TICKS;
        rewardTicksRemaining = 0;
        wavesSpawned = 0;
        nextWaveTicksRemaining = -1;
        rewardEjectTicks = 0;

        collectParticipants(level, activator);
        LeylineTrialSessions.register(this, participants);
        setPhase(level, MondstadtLeylineSpawnerPhase.ACTIVE);
        level.levelEvent(3013, worldPosition, participants.size());
        if (spawnTrialWave(level)) {
            wavesSpawned = 1;
        } else {
            nextWaveTicksRemaining = WAVE_DELAY_TICKS;
        }
        setChanged();

        activator.displayClientMessage(Component.translatable("message.teyvat_artifacts.leyline.started"), true);
        return true;
    }

    private boolean claimReward(ServerLevel level, Player player, ItemStack heldStack) {
        UUID playerId = player.getUUID();
        if (!participants.contains(playerId)) {
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.leyline.not_participant"), true);
            return true;
        }

        if (claimedParticipants.contains(playerId)) {
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.leyline.already_claimed"), true);
            return true;
        }

        if (rewardTicksRemaining <= 0) {
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.leyline.reward_expired"), true);
            return true;
        }

        boolean usingRewardDoubler = LeylineRewardHelper.isRewardDoubler(heldStack);
        int multiplier = LeylineRewardHelper.consumePayment(player, heldStack);
        if (multiplier <= 0) {
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.leyline.not_enough_xp_or_resin", LeylineRewardHelper.getRewardXpCost()), true);
            return true;
        }

        claimedParticipants.add(playerId);
        ejectRewards(level, LeylineRewardHelper.createWaterfallWenRewards(level.random, multiplier));

        String messageKey = usingRewardDoubler
                ? "message.teyvat_artifacts.leyline.claimed_with_condensed_resin"
                : "message.teyvat_artifacts.leyline.claimed";
        player.displayClientMessage(Component.translatable(messageKey), true);
        if (claimedParticipants.size() >= participants.size()) {
            if (pendingRewardEjections.isEmpty()) {
                resetTrial(level);
            } else {
                setChanged();
            }
        } else {
            setChanged();
        }
        return true;
    }

    private void openRewardWindow(ServerLevel level) {
        LeylineTrialSessions.unregister(this);
        trialTicksRemaining = 0;
        rewardTicksRemaining = REWARD_WINDOW_TICKS;
        nextWaveTicksRemaining = -1;
        setPhase(level, MondstadtLeylineSpawnerPhase.REWARD);
        level.playSound(null, worldPosition, SoundEvents.TRIAL_SPAWNER_OPEN_SHUTTER, SoundSource.BLOCKS, 1.0F, 1.0F);
        setChanged();
    }

    private void resetTrial(ServerLevel level) {
        LeylineTrialSessions.unregister(this);
        level.playSound(null, worldPosition, SoundEvents.TRIAL_SPAWNER_CLOSE_SHUTTER, SoundSource.BLOCKS, 1.0F, 1.0F);
        discardTrackedMobs(level);
        participants.clear();
        claimedParticipants.clear();
        spawnedMobIds.clear();
        pendingRewardEjections.clear();
        trialTicksRemaining = 0;
        rewardTicksRemaining = 0;
        wavesSpawned = 0;
        nextWaveTicksRemaining = -1;
        rewardEjectTicks = 0;
        setPhase(level, MondstadtLeylineSpawnerPhase.READY);
        setChanged();
    }

    private void setPhase(ServerLevel level, MondstadtLeylineSpawnerPhase phase) {
        BlockState state = getBlockState();
        if (state.hasProperty(FontaineWaterfallWenLeylineSpawnerBlock.PHASE) && state.getValue(FontaineWaterfallWenLeylineSpawnerBlock.PHASE) != phase) {
            level.setBlock(worldPosition, state.setValue(FontaineWaterfallWenLeylineSpawnerBlock.PHASE, phase), 3);
        }
    }

    private void collectParticipants(ServerLevel level, Player activator) {
        AABB area = AABB.ofSize(Vec3.atCenterOf(worldPosition), PARTICIPANT_RADIUS * 2.0D, PARTICIPANT_RADIUS * 2.0D, PARTICIPANT_RADIUS * 2.0D);
        for (Player player : level.getEntitiesOfClass(Player.class, area, candidate -> !candidate.isSpectator())) {
            participants.add(player.getUUID());
        }
        participants.add(activator.getUUID());
    }

    private boolean spawnTrialWave(ServerLevel level) {
        LeylineTrialMonsterPools.SpawnResult result = LeylineTrialMonsterPools.spawnWave(
                level,
                worldPosition,
                getBlockState(),
                SPAWN_RADIUS,
                MAX_SPAWN_ATTEMPTS
        );
        spawnedMobIds.addAll(result.entityIds());
        return result.spawned();
    }
    private void ejectRewards(ServerLevel level, List<ItemStack> rewards) {
        boolean wasEmpty = pendingRewardEjections.isEmpty();
        for (ItemStack reward : rewards) {
            if (!reward.isEmpty()) {
                pendingRewardEjections.add(reward.copy());
            }
        }

        if (wasEmpty) {
            rewardEjectTicks = 0;
            tickPendingRewardEjections(level);
        }

        setChanged();
    }

    private void tickPendingRewardEjections(ServerLevel level) {
        if (pendingRewardEjections.isEmpty()) {
            rewardEjectTicks = 0;
            return;
        }

        if (rewardEjectTicks > 0) {
            rewardEjectTicks--;
            return;
        }

        Vec3 dropPos = Vec3.atBottomCenterOf(worldPosition).relative(Direction.UP, 1.2D);
        DefaultDispenseItemBehavior.spawnItem(level, pendingRewardEjections.poll(), 2, Direction.UP, dropPos);
        level.levelEvent(3014, worldPosition, 0);
        rewardEjectTicks = REWARD_EJECT_INTERVAL_TICKS;
        setChanged();
    }

    @Override
    public void failForParticipantDeath() {
        if (level instanceof ServerLevel serverLevel && trialTicksRemaining > 0) {
            resetTrial(serverLevel);
        }
    }

    private void refreshTrackedMobs(ServerLevel level) {
        spawnedMobIds.removeIf(uuid -> {
            Entity entity = level.getEntity(uuid);
            return !(entity instanceof Mob mob) || !mob.isAlive();
        });
    }

    private ListTag writeUuidList(Iterable<UUID> uuids) {
        ListTag tag = new ListTag();
        for (UUID uuid : uuids) {
            tag.add(StringTag.valueOf(uuid.toString()));
        }
        return tag;
    }

    private Set<UUID> readUuidList(ListTag tag) {
        Set<UUID> uuids = new LinkedHashSet<>();
        for (int index = 0; index < tag.size(); index++) {
            String value = tag.getString(index);
            try {
                uuids.add(UUID.fromString(value));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return uuids;
    }
}
