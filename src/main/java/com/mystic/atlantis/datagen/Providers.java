package com.mystic.atlantis.datagen;

import com.mystic.atlantis.Atlantis;
import com.mystic.atlantis.TagsInit;
import com.mystic.atlantis.blocks.LinguisticGlyph;
import com.mystic.atlantis.entities.AtlantisEntities;
import com.mystic.atlantis.init.*;
import com.mystic.atlantis.itemgroup.AtlantisGroup;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class Providers {
    public static void init(IEventBus bus) {
        bus.addListener(Providers::dataGather);
    }

    public static void dataGather(GatherDataEvent event) {
        if(event.includeServer()) {
            event.getGenerator().addProvider(true, new AtlantisLootTables(event.getGenerator()));
            event.getGenerator().addProvider(true, new RecipeProvider(event.getGenerator()) {
                @Override
                protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
                    ShapelessRecipeBuilder.shapeless(ItemInit.LINGUISTIC_GLYPH_SCROLL.get(), 1).requires(Items.INK_SAC).requires(Items.PAPER).requires(ItemInit.ATLANTEAN_FIRE_MELON_SPIKE.get()).unlockedBy("has_fire_melon_spike", RecipeProvider.has(ItemInit.ATLANTEAN_FIRE_MELON_SPIKE.get())).save(consumer);
                    ShapedRecipeBuilder.shaped(BlockInit.getLinguisticBlock(LinguisticGlyph.BLANK, null).get(), 2).pattern("XX").pattern("XX").define('X', BlockInit.ATLANTEAN_PRISMARINE.get()).unlockedBy("has_atlantean_prismarine", RecipeProvider.has(BlockInit.ATLANTEAN_PRISMARINE.get())).save(consumer);
                    ShapedRecipeBuilder.shaped(BlockInit.WRITING_BLOCK.get())
                            .pattern("XXX")
                            .pattern("YZY")
                            .pattern("XXX")
                            .define('X', ItemInit.ATLANTEAN_FIRE_MELON_SPIKE.get())
                            .define('Y', Items.BOOK)
                            .define('Z', Blocks.CRAFTING_TABLE)
                            .unlockedBy("has_fire_melon_spike", RecipeProvider.has(ItemInit.ATLANTEAN_FIRE_MELON_SPIKE.get()))
                            .save(consumer);

                    ShapedRecipeBuilder.shaped(BlockInit.LINGUISTIC_BLOCK.get())
                            .pattern("XYX")
                            .pattern("YZY")
                            .pattern("XYX")
                            .define('X', ItemInit.AQUAMARINE_GEM.get())
                            .define('Y', BlockInit.ATLANTEAN_PLANKS.get())
                            .define('Z', Blocks.CRAFTING_TABLE)
                            .unlockedBy("has_atlantean_planks", RecipeProvider.has(BlockInit.ATLANTEAN_PLANKS.get()))
                            .save(consumer);
                    Ingredient ingredient = Ingredient.of(Stream.of(
                            LinguisticGlyph.values())
                            .map(ItemInit::getScroll)
                            .map(RegistryObject::get)
                            .map(ItemStack::new));

                    for(LinguisticGlyph glyph : LinguisticGlyph.values()) {
                        writing(ingredient, ItemInit.getScroll(glyph).get())
//                                .group("glyph_scroll")
                                .unlockedBy("has_fire_melon_spike", RecipeProvider.has(ItemInit.ATLANTEAN_FIRE_MELON_SPIKE.get()))
                                .save(consumer, "atlantis:" + ItemInit.getScroll(glyph).get().getName(ItemInit.getScroll(glyph).get().getDefaultInstance()).getString() + "_writing");
                    }

                    ShapelessRecipeBuilder.shapeless(ItemInit.ORICHALCUM_BLEND.get(), 3)
                            .requires(Items.RAW_COPPER, 6)
                            .requires(Items.RAW_GOLD)
                            .requires(ItemInit.AQUAMARINE_GEM.get())
                            .unlockedBy("has_aquamarine_gem", RecipeProvider.has(ItemInit.AQUAMARINE_GEM.get())).save(consumer);

                    ShapedRecipeBuilder.shaped(BlockInit.ORICHALCUM_BLOCK.get())
                            .pattern("XX")
                            .pattern("XX")
                            .define('X', ItemInit.ORICHALCUM_IGNOT.get())
                            .unlockedBy("has_orichalcum_ignot", RecipeProvider.has(ItemInit.ORICHALCUM_IGNOT.get()))
                            .save(consumer);

                    orichalcumUpgrade(ItemInit.AQUAMARINE_HELMET, ItemInit.ORICHALCUM_HELMET, consumer);
                    orichalcumUpgrade(ItemInit.AQUAMARINE_CHESTPLATE, ItemInit.ORICHALCUM_CHESTPLATE, consumer);
                    orichalcumUpgrade(ItemInit.AQUAMARINE_LEGGINGS, ItemInit.ORICHALCUM_LEGGINGS, consumer);
                    orichalcumUpgrade(ItemInit.AQUAMARINE_BOOTS, ItemInit.ORICHALCUM_BOOTS, consumer);
                    orichalcumUpgrade(ItemInit.AXE_AQUAMARINE, ItemInit.ORICHALCUM_AXE, consumer);
                    orichalcumUpgrade(ItemInit.PICKAXE_AQUAMARINE, ItemInit.ORICHALCUM_PICKAXE, consumer);
                    orichalcumUpgrade(ItemInit.HOE_AQUAMARINE, ItemInit.ORICHALCUM_HOE, consumer);
                    orichalcumUpgrade(ItemInit.SHOVEL_AQUAMARINE, ItemInit.ORICHALCUM_SHOVEL, consumer);
                    orichalcumUpgrade(ItemInit.SWORD_AQUAMARINE, ItemInit.ORICHALCUM_SWORD, consumer);

                    SimpleCookingRecipeBuilder.smelting(ItemInit.ORICHALCUM_BLEND.lazyMap(Ingredient::of).get(), ItemInit.ORICHALCUM_IGNOT.get(), 0.7f, 200).group("orichalcum_ignot").unlockedBy("has_orichalcum_blend", ItemInit.ORICHALCUM_BLEND.map(RecipeProvider::has).get()).save(consumer, "orichalcum_ignot_from_smelting");
                    SimpleCookingRecipeBuilder.blasting(ItemInit.ORICHALCUM_BLEND.lazyMap(Ingredient::of).get(), ItemInit.ORICHALCUM_IGNOT.get(), 0.7f, 100).group("orichalcum_ignot").unlockedBy("has_orichalcum_blend", ItemInit.ORICHALCUM_BLEND.map(RecipeProvider::has).get()).save(consumer, "orichalcum_ignot_from_blasting");
                }

                public void orichalcumUpgrade(RegistryObject<Item> base, RegistryObject<Item> result, Consumer<FinishedRecipe> consumer) {
                    UpgradeRecipeBuilder.smithing(
                            base.lazyMap(Ingredient::of).get(),
                                    ItemInit.ORICHALCUM_IGNOT.lazyMap(Ingredient::of).get(),
                                    result.lazyMap(ItemLike::asItem).get())
                            .unlocks("has_orichalcum_ignot", RecipeProvider.has(ItemInit.ORICHALCUM_IGNOT.get()))
                            .save(consumer, result.getId());
                }

                public static SingleItemRecipeBuilder writing(Ingredient ingredient, ItemLike result) {
                    return new SingleItemRecipeBuilder(RecipesInit.Serializers.WRITING_SERIALIZER.get(), ingredient, result, 1);
                }
            });
        }

        if(event.includeClient()) {
            event.getGenerator().addProvider(true, new BlockModelProvider(event.getGenerator(), "atlantis", event.getExistingFileHelper()) {
                @Override
                protected void registerModels() {
                    this.cubeAll(BlockInit.ORICHALCUM_BLOCK);
                    this.cubeBottomTop("writing_block", Atlantis.id("block/writing_table_side"), Atlantis.id("block/atlantean_planks"), Atlantis.id("block/writing_table_top"));
                }

                private void cubeAll(RegistryObject<Block> block) {
                    this.cubeAll(block.getId().getPath(), blockTexture(block.getId()));
                }

                private ResourceLocation blockTexture(ResourceLocation loc) {
                    return new ResourceLocation(loc.getNamespace(), "block/" + loc.getPath());
                }

            });

            event.getGenerator().addProvider(true, new BlockStateProvider(event.getGenerator(), "atlantis", event.getExistingFileHelper()) {
                @Override
                protected void registerStatesAndModels() {
                    this.horizontalBlock(BlockInit.WRITING_BLOCK.get(), new ModelFile.ExistingModelFile(Atlantis.id("block/writing_block"), event.getExistingFileHelper()));
                    this.simpleBlock(BlockInit.ORICHALCUM_BLOCK.get());
                }
            });

            event.getGenerator().addProvider(true, new ItemModelProvider(event.getGenerator(), "atlantis", event.getExistingFileHelper()) {

                @Override
                protected void registerModels() {
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_A);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_B);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_C);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_D);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_E);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_F);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_G);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_H);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_I);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_J);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_K);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_L);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_M);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_N);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_O);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_P);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_Q);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_R);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_S);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_T);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_U);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_V);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_W);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_X);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_Y);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_Z);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_0);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_1);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_2);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_3);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_4);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_5);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_6);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_7);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_8);
                    item(ItemInit.LINGUISTIC_GLYPH_SCROLL_9);

                    for(LinguisticGlyph glyph : LinguisticGlyph.values()) {
                        for(DyeColor color : DyeColor.values()) {
                            withParent(BlockInit.getLinguisticBlock(glyph, color), glyph);
                        }

                        withParent(BlockInit.getLinguisticBlock(glyph, null), glyph);
                    }

                    block(BlockInit.WRITING_BLOCK);
                    block(BlockInit.ORICHALCUM_BLOCK);

                    item(ItemInit.ORICHALCUM_IGNOT);
                    item(ItemInit.ORICHALCUM_BLEND);

                    item(ItemInit.ORICHALCUM_HELMET);
                    item(ItemInit.ORICHALCUM_CHESTPLATE);
                    item(ItemInit.ORICHALCUM_LEGGINGS);
                    item(ItemInit.ORICHALCUM_BOOTS);
                    itemTool(ItemInit.ORICHALCUM_AXE);
                    itemTool(ItemInit.ORICHALCUM_PICKAXE);
                    itemTool(ItemInit.ORICHALCUM_SHOVEL);
                    itemTool(ItemInit.ORICHALCUM_SWORD);
                    itemTool(ItemInit.ORICHALCUM_HOE);
                }

                private void itemTool(RegistryObject<Item> tool) {
                    getBuilder(tool.getId().getPath())
                            .parent(getExistingFile(mcLoc("item/handheld")))
                            .texture("layer0", items(tool.getId()));
                }

                private void withParent(RegistryObject<Block> block, LinguisticGlyph glyph) {
                    withExistingParent(block.getId().getPath(), block(Atlantis.id("linguistic_" + glyph.name().toLowerCase())));
                }

                private void block(RegistryObject<Block> block) {
                    withExistingParent(block.getId().getPath(), block(block.getId()));
                }

                private void item(RegistryObject<Item> block) {
                    try {
                        getBuilder(block.getId().getPath())
                                .parent(getExistingFile(mcLoc("item/generated")))
                                .texture("layer0", items(block.getId()));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                private ResourceLocation block(ResourceLocation location) {
                    return new ResourceLocation(location.getNamespace(), "block/" + location.getPath());
                }


                private ResourceLocation items(ResourceLocation location) {
                    return new ResourceLocation(location.getNamespace(), "items/" + location.getPath());
                }
            });


            //TODO Convert lang to pure data gen in the future. This will complain about duplicates if run without removing the existing en_us.json first
            event.getGenerator().addProvider(true, new LanguageProvider(event.getGenerator(), "atlantis", "en_us") {
                @Override
                protected void addTranslations() {
                    this.add(AtlantisGroup.MAIN, "Atlantis");
                    this.add(AtlantisGroup.GLYPH, "Altantean Glyphs");

                    add("container.linguistic", "Linguistic");
                    add("container.writing", "Writing");

                    addItem(ItemInit.AQUAMARINE_GEM, "Aquamarine Gem");
                    addItem(ItemInit.ORB_OF_ATLANTIS, "Orb Of Atlantis");
                    addItem(ItemInit.ATLANTEAN_CRYSTAL, "Atlantean Crystal");
                    addItem(ItemInit.OCEAN_STONE, "Ocean Stone");
                    addItem(ItemInit.DROP_OF_ATLANTIS, "Drop Of Atlantis");
                    addItem(ItemInit.BROWN_WROUGHT_PATCHES, "Brown Wrought Patches");
                    addItem(ItemInit.AXE_AQUAMARINE, "Axe Aquamarine");
                    addItem(ItemInit.PICKAXE_AQUAMARINE, "Pickaxe Aquamarine");
                    addItem(ItemInit.SHOVEL_AQUAMARINE, "Shovel Aquamarine");
                    addItem(ItemInit.HOE_AQUAMARINE, "Hoe Aquamarine");
                    addItem(ItemInit.SWORD_AQUAMARINE, "Sword Aquamarine");
                    addItem(ItemInit.AQUAMARINE_HELMET, "Aquamarine Helmet");
                    addItem(ItemInit.AQUAMARINE_CHESTPLATE, "Aquamarine Chestplate");
                    addItem(ItemInit.AQUAMARINE_LEGGINGS, "Aquamarine Leggings");
                    addItem(ItemInit.AQUAMARINE_BOOTS, "Aquamarine Boots");
                    addItem(ItemInit.BROWN_WROUGHT_HELMET, "Brown Wrought Helmet");
                    addItem(ItemInit.BROWN_WROUGHT_CHESTPLATE, "Brown Wrought Chestplate");
                    addItem(ItemInit.BROWN_WROUGHT_LEGGINGS, "Brown Wrought Leggings");
                    addItem(ItemInit.BROWN_WROUGHT_BOOTS, "Brown Wrought Boots");
                    addRecord(ItemInit.PANBEE, "LudoCrypt - panbee");
                    addRecord(ItemInit.COLUMN_CAVITATION, "Firel - column cavitation");
                    addItem(ItemInit.ATLANTEAN_CRAB_EGG, "Atlantean Crab Spawn Egg");
                    addItem(ItemInit.CRAB_LEGS, "Crab Legs");
                    addItem(ItemInit.CRAB_BUCKET, "Crab in a Bucket");
                    addItem(ItemInit.JELLYFISH_BUCKET, "Jellyfish Var. 1 in a Bucket");
                    addItem(ItemInit.ATLANTEAN_JELLYFISH_EGG, "Atlantean Jellyfish Var. 1 Spawn Egg");
                    addItem(ItemInit.ATLANTEAN_POWER_DUST, "Atlantean Power Dust");
                    addItem(ItemInit.ATLANTEAN_STRING, "Atlantean String");
                    addItem(ItemInit.JELLYFISH_2_BUCKET, "Jellyfish Var. 2 in a Bucket");
                    addItem(ItemInit.ATLANTEAN_JELLYFISH_2_EGG, "Atlantean Jellyfish Var. 2 Spawn Egg");
                    addItem(ItemInit.JETSTREAM_WATER_BUCKET, "Jetstream Water Bucket");
                    addItem(ItemInit.SALTY_SEA_WATER_BUCKET, "Salty Sea Water Bucket");
                    addItem(ItemInit.SUBMARINE, "Atlantean Submarine");
                    addItem(ItemInit.SHRIMP_BUCKET, "Shrimp in a Bucket");
                    addItem(ItemInit.SHRIMP, "Shrimp");
                    addItem(ItemInit.COOKED_SHRIMP, "Cooked Shrimp");
                    addItem(ItemInit.ATLANTEAN_SHRIMP_EGG, "Atlantean Shrimp Spawn Egg");
                    addItem(ItemInit.WATER_PILL, "Water Pill");
                    addItem(ItemInit.FIRE_MELON_JELLY_BOTTLE, "Atlantean Fire Melon Jelly in a Bottle");
                    addItem(ItemInit.JELLY_BOTTLE, "Jellyfish Jelly in a Bottle");
                    addItem(ItemInit.ATLANTEAN_FIRE_MELON_SEEDS, "Atlantean Fire Melon Seeds");
                    addItem(ItemInit.ATLANTEAN_FIRE_MELON_SPIKE, "Atlantean Fire Melon Spike");
                    addItem(ItemInit.ATLANTEAN_BOAT, "Atlantean Wood Boat");

                    addItem(ItemInit.ORICHALCUM_HELMET);
                    addItem(ItemInit.ORICHALCUM_CHESTPLATE);
                    addItem(ItemInit.ORICHALCUM_LEGGINGS);
                    addItem(ItemInit.ORICHALCUM_BOOTS);
                    addItem(ItemInit.ORICHALCUM_AXE);
                    addItem(ItemInit.ORICHALCUM_PICKAXE);
                    addItem(ItemInit.ORICHALCUM_SHOVEL);
                    addItem(ItemInit.ORICHALCUM_SWORD);
                    addItem(ItemInit.ORICHALCUM_HOE);

                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_A);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_B);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_C);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_D);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_E);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_F);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_G);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_H);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_I);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_J);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_K);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_L);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_M);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_N);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_O);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_P);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_Q);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_R);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_S);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_T);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_U);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_V);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_W);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_X);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_Y);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_Z);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_0);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_1);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_2);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_3);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_4);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_5);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_6);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_7);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_8);
                    addItem(ItemInit.LINGUISTIC_GLYPH_SCROLL_9);

                    addBlock(BlockInit.ATLANTEAN_POWER_LAMP, "Atlantean Power Lamp");
                    addBlock(BlockInit.ATLANTEAN_POWER_TORCH, "Atlantean Power Torch");
                    addBlock(BlockInit.ATLANTEAN_POWER_STONE, "Atlantean Power Stone");
                    addBlock(BlockInit.ATLANTEAN_POWER_REPEATER, "Atlantean Power Repeater");
                    addBlock(BlockInit.ATLANTEAN_TRIPWIRE_HOOK, "Atlantean Tripwire Hook");
                    addBlock(BlockInit.ATLANTEAN_POWER_LEVER, "Atlantean Power Lever");
                    addBlock(BlockInit.ANCIENT_ACACIA_WOOD_MOSS_DOOR, "Ancient Acacia Wood Moss Door");
                    addBlock(BlockInit.ANCIENT_BIRCH_WOOD_MOSS_DOOR, "Ancient Birch Wood Moss Door");
                    addBlock(BlockInit.ANCIENT_DARK_OAK_WOOD_MOSS_DOOR, "Ancient Dark Oak Wood Moss Door");
                    addBlock(BlockInit.ANCIENT_JUNGLE_WOOD_MOSS_DOOR, "Ancient Jungle Wood Moss Door");
                    addBlock(BlockInit.ANCIENT_OAK_WOOD_MOSS_DOOR, "Ancient Oak Wood Moss Door");
                    addBlock(BlockInit.ANCIENT_SPRUCE_WOOD_MOSS_DOOR, "Ancient Spruce Wood Moss Door");
                    addBlock(BlockInit.ANCIENT_ACACIA_WOOD_MOSS_TRAPDOOR, "Ancient Acacia Wood Moss Trapdoor");
                    addBlock(BlockInit.ANCIENT_BIRCH_WOOD_MOSS_TRAPDOOR, "Ancient Birch Wood Moss Trapdoor");
                    addBlock(BlockInit.ANCIENT_DARK_OAK_WOOD_MOSS_TRAPDOOR, "Ancient Dark Oak Wood Moss Trapdoor");
                    addBlock(BlockInit.ANCIENT_JUNGLE_WOOD_MOSS_TRAPDOOR, "Ancient Jungle Wood Moss Trapdoor");
                    addBlock(BlockInit.ANCIENT_OAK_WOOD_MOSS_TRAPDOOR, "Ancient Oak Wood Moss Trapdoor");
                    addBlock(BlockInit.ANCIENT_SPRUCE_WOOD_MOSS_TRAPDOOR, "Ancient Spruce Wood Moss Trapdoor");
                    addBlock(BlockInit.ANCIENT_ACACIA_WOOD_MOSS_FENCE, "Ancient Acacia Wood Moss Fence");
                    addBlock(BlockInit.ANCIENT_BIRCH_WOOD_MOSS_FENCE, "Ancient Birch Wood Moss Fence");
                    addBlock(BlockInit.ANCIENT_DARK_OAK_WOOD_MOSS_FENCE, "Ancient Dark Oak Wood Moss Fence");
                    addBlock(BlockInit.ANCIENT_JUNGLE_WOOD_MOSS_FENCE, "Ancient Jungle Wood Moss Fence");
                    addBlock(BlockInit.ANCIENT_OAK_WOOD_MOSS_FENCE, "Ancient Oak Wood Moss Fence");
                    addBlock(BlockInit.ANCIENT_SPRUCE_WOOD_MOSS_FENCE, "Ancient Spruce Wood Moss Fence");
                    addBlock(BlockInit.ANCIENT_ACACIA_WOOD_MOSS_STAIRS, "Ancient Acacia Wood Moss Stairs");
                    addBlock(BlockInit.ANCIENT_BIRCH_WOOD_MOSS_STAIRS, "Ancient Birch Wood Moss Stairs");
                    addBlock(BlockInit.ANCIENT_DARK_OAK_WOOD_MOSS_STAIRS, "Ancient Dark Oak Wood Moss Stairs");
                    addBlock(BlockInit.ANCIENT_JUNGLE_WOOD_MOSS_STAIRS, "Ancient Jungle Wood Moss Stairs");
                    addBlock(BlockInit.ANCIENT_OAK_WOOD_MOSS_STAIRS, "Ancient Oak Wood Moss Stairs");
                    addBlock(BlockInit.ANCIENT_SPRUCE_WOOD_MOSS_STAIRS, "Ancient Spruce Wood Moss Stairs");
                    addBlock(BlockInit.ANCIENT_ACACIA_WOOD_MOSS_SLAB, "Ancient Acacia Wood Moss Slab");
                    addBlock(BlockInit.ANCIENT_BIRCH_WOOD_MOSS_SLAB, "Ancient Birch Wood Moss Slab");
                    addBlock(BlockInit.ANCIENT_DARK_OAK_WOOD_MOSS_SLAB, "Ancient Dark Oak Wood Moss Slab");
                    addBlock(BlockInit.ANCIENT_JUNGLE_WOOD_MOSS_SLAB, "Ancient Jungle Wood Moss Slab");
                    addBlock(BlockInit.ANCIENT_OAK_WOOD_MOSS_SLAB, "Ancient Oak Wood Moss Slab");
                    addBlock(BlockInit.ANCIENT_SPRUCE_WOOD_MOSS_SLAB, "Ancient Spruce Wood Moss Slab");
                    addBlock(BlockInit.YELLOW_GLOWING_MUSHROOM, "Yellow Glowing Mushroom");
                    addBlock(BlockInit.PURPLE_GLOWING_MUSHROOM, "Purple Glowing Mushroom");
                    addBlock(BlockInit.CALCITE_BLOCK, "Hardened Calcite Block");
                    addBlock(BlockInit.CHISELED_AQUAMARINE, "Chiseled Aquamarine");
                    addBlock(BlockInit.ALGAE_BLOCK, "Algae Block");
                    addBlock(BlockInit.ATLANTEAN_POWER_COMPARATOR, "Atlantean Power Comparator");
                    addBlock(BlockInit.ATLANTEAN_LEAVES, "Atlantean Leaves");
                    addBlock(BlockInit.ATLANTEAN_LOGS, "Atlantean Log");
                    addBlock(BlockInit.ATLANTEAN_SAPLING, "Atlantean Sapling");
                    addBlock(BlockInit.UNDERWATER_SHROOM_BLOCK, "Atlantean Underwater Shroom");
                    addBlock(BlockInit.TUBER_UP_BLOCK, "Atlantean Tuber");
                    addBlock(BlockInit.BLUE_LILY_BLOCK, "Blue Lily Flower");
                    addBlock(BlockInit.BURNT_DEEP_BLOCK, "Burnt Deep Flower");
                    addBlock(BlockInit.ENENMOMY_BLOCK, "Atlantean Anemone");
                    addBlock(BlockInit.ATLANTEAN_SIGNS, "Atlantean Sign");
                    addBlock(BlockInit.ATLANTEAN_DOOR, "Atlantean Door");
                    addBlock(BlockInit.ATLANTEAN_FENCE, "Atlantean Fence");
                    addBlock(BlockInit.ATLANTEAN_FENCE_GATE, "Atlantean Fence Gate");
                    addBlock(BlockInit.ATLANTEAN_PLANKS, "Atlantean Planks");
                    addBlock(BlockInit.ATLANTEAN_PRESSURE_PLATE, "Atlantean Pressure Plate");
                    addBlock(BlockInit.ATLANTEAN_STAIRS, "Atlantean Stairs");
                    addBlock(BlockInit.ATLANTEAN_BUTTON, "Atlantean Button");
                    addBlock(BlockInit.ATLANTEAN_TRAPDOOR, "Atlantean Trapdoor");
                    addBlock(BlockInit.ATLANTEAN_SLAB, "Atlantean Slab");
                    addBlock(BlockInit.BUBBLE_MAGMA, "Bubble Magma");
                    addBlock(BlockInit.ATLANTEAN_PRISMARINE, "Atlantean Prismarine");
                    addBlock(BlockInit.ANCIENT_ACACIA_WOOD_MOSS, "Ancient Acacia Wood Moss ");
                    addBlock(BlockInit.ANCIENT_JUNGLE_WOOD_MOSS, "Ancient Jungle Wood Moss ");
                    addBlock(BlockInit.ANCIENT_DARK_OAK_WOOD_MOSS, "Ancient Dark Oak Wood Moss");
                    addBlock(BlockInit.ANCIENT_OAK_WOOD_MOSS, "Ancient Oak Wood Moss ");
                    addBlock(BlockInit.ANCIENT_SPRUCE_WOOD_MOSS, "Ancient Spruce Wood Moss ");
                    addBlock(BlockInit.ANCIENT_BIRCH_WOOD_MOSS, "Ancient Birch Wood Moss ");
                    addBlock(BlockInit.AQUAMARINE_ORE, "Aquamarine Ore");
                    addBlock(BlockInit.ATLANTEAN_CORE, "Atlantean Core");
                    addBlock(BlockInit.BLOCK_OF_AQUAMARINE, "Block Of Aquamarine");
                    addBlock(BlockInit.CHISELED_GOLDEN_BLOCK, "Chiseled Golden Block");
                    addBlock(BlockInit.CHISELED_GOLDEN_AQUAMARINE, "Chiseled Golden Aquamarine");
                    addBlock(BlockInit.RED_PEARL_BLOCK, "Red Pearl Block");
                    addBlock(BlockInit.YELLOW_PEARL_BLOCK, "Yellow Pearl Block");
                    addBlock(BlockInit.GRAY_PEARL_BLOCK, "Gray Pearl Block");
                    addBlock(BlockInit.BLACK_PEARL_BLOCK, "Black Pearl Block");
                    addBlock(BlockInit.BLUE_PEARL_BLOCK, "Blue Pearl Block");
                    addBlock(BlockInit.MAGENTA_PEARL_BLOCK, "Magenta Pearl Block");
                    addBlock(BlockInit.BROWN_PEARL_BLOCK, "Brown Pearl Block");
                    addBlock(BlockInit.WHITE_PEARL_BLOCK, "White Pearl Block");
                    addBlock(BlockInit.LIGHT_GRAY_PEARL_BLOCK, "Light Gray Pearl Block ");
                    addBlock(BlockInit.PURPLE_PEARL_BLOCK, "Purple Pearl Block");
                    addBlock(BlockInit.CYAN_PEARL_BLOCK, "Cyan Pearl Block");
                    addBlock(BlockInit.ORANGE_PEARL_BLOCK, "Orange Pearl Block");
                    addBlock(BlockInit.LIME_PEARL_BLOCK, "Lime Pearl Block");
                    addBlock(BlockInit.PINK_PEARL_BLOCK, "Pink Pearl Block");
                    addBlock(BlockInit.LIGHT_BLUE_PEARL_BLOCK, "Light Blue Pearl Block ");
                    addBlock(BlockInit.GREEN_PEARL_BLOCK, "Green Pearl Block");
                    addBlock(BlockInit.RED_COLORED_SHELL_BLOCK, "Red Colored Shell Block ");
                    addBlock(BlockInit.YELLOW_COLORED_SHELL_BLOCK, "Yellow Colored Shell Block ");
                    addBlock(BlockInit.GRAY_COLORED_SHELL_BLOCK, "Gray Colored Shell Block ");
                    addBlock(BlockInit.BLACK_COLORED_SHELL_BLOCK, "Black Colored Shell Block ");
                    addBlock(BlockInit.BLUE_COLORED_SHELL_BLOCK, "Blue Colored Shell Block ");
                    addBlock(BlockInit.MAGENTA_COLORED_SHELL_BLOCK, "Magenta Colored Shell Block ");
                    addBlock(BlockInit.BROWN_COLORED_SHELL_BLOCK, "Brown Colored Shell Block ");
                    addBlock(BlockInit.WHITE_COLORED_SHELL_BLOCK, "White Colored Shell Block ");
                    addBlock(BlockInit.LIGHT_GRAY_COLORED_SHELL_BLOCK, "Light Gray Colored Shell Block");
                    addBlock(BlockInit.PURPLE_COLORED_SHELL_BLOCK, "Purple Colored Shell Block ");
                    addBlock(BlockInit.CYAN_COLORED_SHELL_BLOCK, "Cyan Colored Shell Block ");
                    addBlock(BlockInit.ORANGE_COLORED_SHELL_BLOCK, "Orange Colored Shell Block ");
                    addBlock(BlockInit.LIME_COLORED_SHELL_BLOCK, "Lime Colored Shell Block ");
                    addBlock(BlockInit.PINK_COLORED_SHELL_BLOCK, "Pink Colored Shell Block ");
                    addBlock(BlockInit.LIGHT_BLUE_COLORED_SHELL_BLOCK, "Light Blue Colored Shell Block");
                    addBlock(BlockInit.GREEN_COLORED_SHELL_BLOCK, "Green Colored Shell Block ");
                    addBlock(BlockInit.OYSTER_SHELL_BLOCK, "Oyster Shell Block ");
                    addBlock(BlockInit.ATLANTIS_PORTAL, "Legacy Atlantean Portal Block (Danger will kill you in survival if used wrong!!!, Creative only!!!)");
                    addBlock(BlockInit.UNDERWATER_FLOWER, "Blue Atlantean Flower");
                    addBlock(BlockInit.RED_UNDERWATER_FLOWER, "Red Atlantean Flower");
                    addBlock(BlockInit.YELLOW_UNDERWATER_FLOWER, "Yellow Atlantean Flower");
                    addBlock(BlockInit.ALGAE, "Algae");
                    addBlock(BlockInit.OCEAN_LANTERN, "Atlantean Sea Lantern");
                    addBlock(BlockInit.ATLANTIS_CLEAR_PORTAL, "Atlantean Portal");

                    for (LinguisticGlyph glyph : LinguisticGlyph.values()) {
                        for (DyeColor color : DyeColor.values()) {
                            addBlock(BlockInit.getLinguisticBlock(glyph, color));
                        }

                        addBlock(BlockInit.getLinguisticBlock(glyph, null));
                    }

                    add(EffectsInit.SPIKES.get(), "Spikes");

                    add(AtlantisEntities.CRAB.get(), "Atlantean Crab");
                    add(AtlantisEntities.JELLYFISH2.get(), "Atlantean Jellyfish Var. 2");
                    add(AtlantisEntities.SHRIMP.get(), "Atlantean Shrimp");
                    add(AtlantisEntities.SUBMARINE.get(), "Atlantean Submarine");
                    add(AtlantisEntities.JELLYFISH.get(), "Atlantean Jellyfish Var. 1");

                    add("text.autoconfig.atlantis.title", "Atlantean Config");
                    add("text.autoconfig.atlantis.option.minCrabSpawnHeight", "Minimum Crab Spawn Height");
                    add("text.autoconfig.atlantis.option.minCrabSpawnHeight.@Tooltip", "The Minimum Height The Atlantean Crab Will Spawn At");
                    add("text.autoconfig.atlantis.option.maxCrabSpawnHeight", "Maximum Crab Spawn Height");
                    add("text.autoconfig.atlantis.option.maxCrabSpawnHeight.@Tooltip", "The Maximum Height The Atlantean Crab Will Spawn At");
                    add("text.autoconfig.atlantis.option.islandsOn", "Are islands on?");
                    add("text.autoconfig.atlantis.option.volcanoesOn", "Are volcanoes on?");
                    add("text.autoconfig.atlantis.option.calciteAcceleration", "Calcite Acceleration");
                    add("text.autoconfig.atlantis.option.calciteThreshold", "Calcite Threshold");
                    add("text.autoconfig.atlantis.option.general.calciteAcceleration", "Calcite Acceleration");
                    add("text.autoconfig.atlantis.option.general.calciteAcceleration.@Tooltip", "How fast you start to speed up at. Must reload world for it to work!");
                    add("text.autoconfig.atlantis.option.general.calciteThreshold", "Calcite Threshold");
                    add("text.autoconfig.atlantis.option.general.calciteThreshold.@Tooltip", "The maximum speed you can be at. Must reload world for it to work!");

                    addBlock(BlockInit.ORICHALCUM_BLOCK);
                    addItem(ItemInit.ORICHALCUM_BLEND);
                    addItem(ItemInit.ORICHALCUM_IGNOT);

                    addBlock(BlockInit.LINGUISTIC_BLOCK);
                    addBlock(BlockInit.WRITING_BLOCK);
                }

                private void addRecord(RegistryObject<Item> record, String s) {
                    addItem(record, "Music Disc");
                    add("item." + record.getId().getNamespace() + "." + record.getId().getPath() + ".desc", s);
                }

                private void addItem(RegistryObject<Item> registryObject) {
                    addItem(registryObject, WordUtils.capitalizeFully(registryObject.getId().getPath().replace("_", " ")));
                }

                private void addBlock(RegistryObject<Block> registryObject) {
                    addBlock(registryObject, WordUtils.capitalizeFully(registryObject.getId().getPath().replace("_", " ")));
                }


                private void add(CreativeModeTab tab, String entry) {
                    this.add("itemGroup." + tab.getRecipeFolderName(), entry);
                }
            });

            BlockTagsProvider blockTagsProvider = new BlockTagsProvider(event.getGenerator(), "atlantis", event.getExistingFileHelper()) {
                @Override
                protected void addTags() {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockInit.ORICHALCUM_BLOCK.get());
                    tag(BlockTags.NEEDS_IRON_TOOL).add(BlockInit.ORICHALCUM_BLOCK.get());
                }
            };

            event.getGenerator().addProvider(true, blockTagsProvider);

            event.getGenerator().addProvider(true, new ItemTagsProvider(event.getGenerator(), blockTagsProvider,"atlantis", event.getExistingFileHelper()) {
                @Override
                public void addTags() {
                    TagAppender<Item> tag = tag(TagsInit.Item.CAN_ITEM_SINK);
                    TagsInit.Item.getItemsThatCanSink().forEach(itemSupplier -> (tag).add(itemSupplier.get()));
                }
            });
        }
    }
}
