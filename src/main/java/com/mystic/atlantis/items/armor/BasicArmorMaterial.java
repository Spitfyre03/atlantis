package com.mystic.atlantis.items.armor;

import com.mystic.atlantis.init.ItemInit;
import com.mystic.atlantis.util.Lazy;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class BasicArmorMaterial
{
    public static final net.minecraft.item.ArmorMaterial ARMOR_AQUAMARINE = new ArmorMaterial( "aquamarine", 24, new int[] {2, 6, 7, 3} , 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F, 0.0F, () -> Ingredient.ofItems(ItemInit.AQUAMARINE_GEM));
    public static final net.minecraft.item.ArmorMaterial ARMOR_BROWN_WROUGHT = new ArmorMaterial("wrought", 24, new int[] {3, 5, 5, 4} , 7, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F, 0.0F, () -> Ingredient.ofItems(ItemInit.BROWN_WROUGHT_PATCHES));

    private static class ArmorMaterial implements net.minecraft.item.ArmorMaterial{

        private static final int[] Max_Damage_Array = new int[] {13,15,16,11};
        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;
        private final SoundEvent soundEvent;
        private final float toughness;
        private final float knockbackResistance;
        private final Lazy<Ingredient> repairMaterial;

        public ArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, double toughness, float knockbackResistance, Supplier<Ingredient> supplier) {
            this.name = name;
            this.maxDamageFactor = maxDamageFactor;
            this.damageReductionAmountArray = damageReductionAmountArray;
            this.enchantability = enchantability;
            this.soundEvent = soundEvent;
            this.toughness = (float)toughness;
            this.knockbackResistance = knockbackResistance;
            this.repairMaterial = new Lazy<>(supplier);
        }

        @Override
        public int getDurability(EquipmentSlot slotIn) {
            return Max_Damage_Array[slotIn.getEntitySlotId()] * maxDamageFactor;
        }

        @Override
        public int getProtectionAmount(EquipmentSlot slotIn) {
            return damageReductionAmountArray[slotIn.getEntitySlotId()];
        }

        @Override
        public int getEnchantability() {
            return enchantability;
        }

        @Override
        public SoundEvent getEquipSound() {
            return soundEvent;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return repairMaterial.get();
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public String getName() {
            return name;
        }

        @Override
        public float getToughness() {
            return toughness;
        }

        @Override
        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }
    }
}
