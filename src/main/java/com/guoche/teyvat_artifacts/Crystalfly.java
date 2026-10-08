package com.guoche.teyvat_artifacts;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class Crystalfly extends Bat {
    private static final float SILK_TOUCH_DEATH_SOUND_VOLUME = 6.0F;
    private static final float BREAK_DEATH_SOUND_VOLUME = 0.3F;
    private static final double FLIGHT_SPEED = 0.25D;
    private static final double FLIGHT_ACCELERATION = 0.12D;
    private static final int FLIGHT_RADIUS = 7;
    private static final ResourceKey<Biome> FLOWER_FOREST = ResourceKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath("minecraft", "flower_forest")
    );
    private static final String FLOWER_FOREST_CLUSTER_CHECKED_TAG = "FlowerForestClusterChecked";

    @Nullable
    private BlockPos targetPosition;
    private boolean silkTouchDeath;
    private boolean flowerForestClusterChecked;

    public Crystalfly(EntityType<? extends Crystalfly> entityType, Level level) {
        super(entityType, level);
        this.setResting(false);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createCrystalflyAttributes() {
        return Bat.createAttributes().add(Attributes.MAX_HEALTH, 2.0D);
    }

    public static boolean checkCrystalflySpawnRules(EntityType<Crystalfly> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        BlockState state = level.getBlockState(pos);
        BlockState above = level.getBlockState(pos.above());
        BlockState below = level.getBlockState(pos.below());
        boolean hasSurface = below.isFaceSturdy(level, pos.below(), Direction.UP)
                || level.getFluidState(pos.below()).is(FluidTags.WATER);
        return hasSurface
                && state.getCollisionShape(level, pos).isEmpty()
                && above.getCollisionShape(level, pos.above()).isEmpty()
                && level.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public boolean isFlapping() {
        return this.tickCount % 20 == 0;
    }

    @Override
    public boolean isResting() {
        return false;
    }

    @Override
    public void setResting(boolean isResting) {
        super.setResting(false);
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public void tick() {
        this.setNoGravity(true);
        super.tick();
        if (!this.level().isClientSide && !this.flowerForestClusterChecked) {
            this.flowerForestClusterChecked = true;
            trySpawnFlowerForestCluster();
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        this.silkTouchDeath = isSilkTouchDamageSource(damageSource);
        if (!this.level().isClientSide) {
            SoundEvent sound = this.silkTouchDeath ? SoundEvents.AMETHYST_BLOCK_CHIME : SoundEvents.GLASS_BREAK;
            float volume = this.silkTouchDeath ? SILK_TOUCH_DEATH_SOUND_VOLUME : BREAK_DEATH_SOUND_VOLUME;
            this.level().playSound(
                    null,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    sound,
                    this.getSoundSource(),
                    volume,
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F
            );
        }
        super.die(damageSource);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        if (this.silkTouchDeath) {
            this.spawnAtLocation(new ItemStack(TeyvatArtifacts.CRYSTAL_CORE.get()));
        }
    }

    private boolean isSilkTouchDamageSource(DamageSource damageSource) {
        return hasSilkTouch(damageSource.getEntity()) || hasSilkTouch(damageSource.getDirectEntity());
    }

    private boolean hasSilkTouch(@Nullable Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }
        return hasSilkTouch(livingEntity.getMainHandItem()) || hasSilkTouch(livingEntity.getOffhandItem());
    }

    private boolean hasSilkTouch(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return this.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(Enchantments.SILK_TOUCH)
                .map(enchantment -> stack.getEnchantmentLevel(enchantment) > 0)
                .orElse(false);
    }

    @Override
    protected void customServerAiStep() {
        this.setNoGravity(true);
        if (this.targetPosition == null
                || this.random.nextInt(35) == 0
                || this.targetPosition.closerToCenterThan(this.position(), 1.4D)
                || !isFreeFlightTarget(this.targetPosition)) {
            chooseTargetPosition();
        }

        if (this.targetPosition == null) {
            return;
        }

        Vec3 target = Vec3.atCenterOf(this.targetPosition);
        Vec3 offset = target.subtract(this.position());
        if (offset.lengthSqr() < 0.2D) {
            this.targetPosition = null;
            return;
        }

        Vec3 desired = offset.normalize().scale(FLIGHT_SPEED);
        int surfaceY = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.getBlockX(), this.getBlockZ());
        if (this.getY() < surfaceY + 1.5D) {
            desired = new Vec3(desired.x, Math.max(desired.y, 0.18D), desired.z);
        }

        Vec3 motion = this.getDeltaMovement();
        Vec3 nextMotion = motion.add(
                (desired.x - motion.x) * FLIGHT_ACCELERATION,
                (desired.y - motion.y) * FLIGHT_ACCELERATION,
                (desired.z - motion.z) * FLIGHT_ACCELERATION
        );
        this.setDeltaMovement(nextMotion);
        if (nextMotion.horizontalDistanceSqr() > 1.0E-6D) {
            float yaw = (float) (Mth.atan2(nextMotion.z, nextMotion.x) * 180.0F / (float) Math.PI) - 90.0F;
            float wrappedYaw = Mth.wrapDegrees(yaw - this.getYRot());
            this.setYRot(this.getYRot() + wrappedYaw);
            this.yBodyRot = this.getYRot();
        }
        this.zza = 0.25F;
    }

    private void chooseTargetPosition() {
        for (int attempt = 0; attempt < 12; attempt++) {
            int x = this.getBlockX() + this.random.nextInt(FLIGHT_RADIUS * 2 + 1) - FLIGHT_RADIUS;
            int z = this.getBlockZ() + this.random.nextInt(FLIGHT_RADIUS * 2 + 1) - FLIGHT_RADIUS;
            int surfaceY = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            int minY = Math.max(surfaceY + 2, this.level().getMinBuildHeight() + 1);
            int maxY = Math.min(surfaceY + 5, this.level().getMaxBuildHeight() - 2);
            if (maxY < minY) {
                continue;
            }

            BlockPos candidate = new BlockPos(x, minY + this.random.nextInt(maxY - minY + 1), z);
            if (isFreeFlightTarget(candidate)) {
                this.targetPosition = candidate;
                return;
            }
        }

        BlockPos fallback = this.blockPosition().above(2 + this.random.nextInt(3));
        this.targetPosition = isFreeFlightTarget(fallback) ? fallback : null;
    }

    private boolean isFreeFlightTarget(BlockPos pos) {
        return pos.getY() > this.level().getMinBuildHeight()
                && pos.getY() < this.level().getMaxBuildHeight() - 1
                && this.level().getBlockState(pos).getCollisionShape(this.level(), pos).isEmpty()
                && this.level().getBlockState(pos.above()).getCollisionShape(this.level(), pos.above()).isEmpty();
    }

    private void trySpawnFlowerForestCluster() {
        if (!(this.level() instanceof ServerLevel serverLevel) || !serverLevel.getBiome(this.blockPosition()).is(FLOWER_FOREST)) {
            return;
        }

        int extraCount = this.random.nextInt(4);
        for (int i = 0; i < extraCount; i++) {
            EntityType<Crystalfly> type = randomCrystalflyType();
            Crystalfly crystalfly = type.create(serverLevel);
            if (crystalfly == null) {
                continue;
            }

            double x = this.getX() + (this.random.nextDouble() - 0.5D) * 5.0D;
            double z = this.getZ() + (this.random.nextDouble() - 0.5D) * 5.0D;
            int surfaceY = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor(x), Mth.floor(z));
            double y = Math.max(surfaceY + 2.0D, this.getY() + (this.random.nextDouble() - 0.5D) * 2.0D);
            crystalfly.moveTo(x, y, z, this.random.nextFloat() * 360.0F, 0.0F);
            crystalfly.flowerForestClusterChecked = true;
            crystalfly.setNoGravity(true);
            serverLevel.addFreshEntity(crystalfly);
        }
    }

    private EntityType<Crystalfly> randomCrystalflyType() {
        return switch (this.random.nextInt(7)) {
            case 0 -> TeyvatArtifacts.ANEMO_CRYSTALFLY.get();
            case 1 -> TeyvatArtifacts.ICE_CRYSTALFLY.get();
            case 2 -> TeyvatArtifacts.GEO_CRYSTALFLY.get();
            case 3 -> TeyvatArtifacts.ELECTRO_CRYSTALFLY.get();
            case 4 -> TeyvatArtifacts.DENDRO_CRYSTALFLY.get();
            case 5 -> TeyvatArtifacts.HYDRO_CRYSTALFLY.get();
            default -> TeyvatArtifacts.PYRO_CRYSTALFLY.get();
        };
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.flowerForestClusterChecked = compound.getBoolean(FLOWER_FOREST_CLUSTER_CHECKED_TAG);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean(FLOWER_FOREST_CLUSTER_CHECKED_TAG, this.flowerForestClusterChecked);
    }
}
