package me.gammadelta;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class LabcoatItem extends ArmorItem {
    private int ticksToTeleport = 30;
    private float blocksToMove = 2f;

    private static final String TICKS_HELD_KEY = "ticks_held";

    public LabcoatItem() {
        super(new LabcoatMaterial(), EquipmentSlotType.CHEST, new Properties().group(ItemGroup.COMBAT));
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        CompoundNBT tag = stack.getOrCreateTag();
        int ticksActiveSoFar = tag.getInt(TICKS_HELD_KEY);

        Vector3d dest = player.getEyePosition(0f).add(player.getLookVec().scale(1.5));
        RayTraceResult raytrace = world.rayTraceBlocks(
                new RayTraceContext(player.getEyePosition(0f), dest, RayTraceContext.BlockMode.COLLIDER,
                        RayTraceContext.FluidMode.NONE, player));
        boolean success = raytrace.getType() == RayTraceResult.Type.BLOCK
                && player.collidedHorizontally;
        if (success) {
            ticksActiveSoFar++;
        } else if (ticksActiveSoFar > 0) {
            ticksActiveSoFar--;
        }
        tag.putInt(TICKS_HELD_KEY, ticksActiveSoFar);

        for (int i = 0; i < ticksActiveSoFar; i++) {
            world.addParticle(new RedstoneParticleData(0.2f, 0.9f, 0.5f, 1.0f),
                    player.getPositionVec().x + (world.rand.nextDouble() - 0.5),
                    player.getPositionVec().y + (world.rand.nextDouble() - 0.5) + 1,
                    player.getPositionVec().z + (world.rand.nextDouble() - 0.5),
                    0.1, 0.1, 0.1);
            world.addParticle(new RedstoneParticleData(0.2f, 0.9f, 0.5f, 1.0f),
                    dest.x + (world.rand.nextDouble() - 0.5),
                    dest.y + (world.rand.nextDouble() - 0.5),
                    dest.z + (world.rand.nextDouble() - 0.5),
                    0.1, 0.1, 0.1);
        }

        if (ticksActiveSoFar >= ticksToTeleport) {
            Vector3d oldPos = player.getPositionVec();
            Vector3d projectedPos = player.getLookVec().scale(blocksToMove).add(player.getPositionVec());
            // Only set the Y to where they are actually looking if they are REALLY looking away
            double y = player.getPosY();
            float pitch = player.rotationPitch;
            if (Math.abs(pitch) > 30) {
                // ok they really mean it
                y = projectedPos.y;
            }
            player.setPosition(projectedPos.x, y, projectedPos.z);
            tag.putInt(TICKS_HELD_KEY, 0);
            world.playSound(projectedPos.x, y, projectedPos.z, SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.PLAYERS, 1.0f, 0.9f, false);

            // Conjure particles in a sort of smear between the two
            Vector3d middle = oldPos.add(0, 1, 0).add(projectedPos.add(0, 1, 0)).scale(0.5);
            Vector3d spread = oldPos.subtract(projectedPos).normalize();
            // Get even farther than the destination so the player gets extra particles at their head to look at
            Vector3d projectedFarther = player.getEyePosition(0f).add(player.getLookVec().scale(blocksToMove + 0.7));

            float speedMod = 0.5f;
            float spreadMod = 2.0f;

            for (int i = 0; i < 100; i++) {
                world.addParticle(new RedstoneParticleData(0.2f, 0.9f, 0.5f, 1.0f),
                        middle.x + (world.rand.nextDouble() - 0.5) * (spread.x + 0.5) * spreadMod,
                        middle.y + (world.rand.nextDouble() - 0.5) * (spread.y + 1) * spreadMod,
                        middle.z + (world.rand.nextDouble() - 0.5) * (spread.z + 0.5) * spreadMod,
                        spread.x * speedMod, spread.y * speedMod, spread.z * speedMod);
                // Give the player something to look at too
                world.addParticle(new RedstoneParticleData(0.2f, 0.9f, 0.5f, 1.0f),
                        projectedFarther.x + (world.rand.nextDouble() - 0.5),
                        projectedFarther.y + (world.rand.nextDouble() - 0.5),
                        projectedFarther.z + (world.rand.nextDouble() - 0.5),
                        spread.x * speedMod, spread.y * speedMod, spread.z * speedMod);
            }
        }
    }

    @Override
    public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack,
            EquipmentSlotType armorSlot, BipedModel _default) {
        return LabcoatArmorModel.getInstance();
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "axiomatic_teleportation:textures/labcoat_armor.png";
    }

    public static class LabcoatMaterial implements IArmorMaterial {
        @Override
        public int getDurability(EquipmentSlotType slotIn) {
            return 300;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotIn) {
            return 3;
        }

        @Override
        public int getEnchantability() {
            return 15;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return null;
        }

        @Override
        public String getName() {
            return "labcoat";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.5f;
        }
    }
}
