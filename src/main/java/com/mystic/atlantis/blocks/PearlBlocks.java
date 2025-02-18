package com.mystic.atlantis.blocks;


import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import static com.mystic.atlantis.blocks.AtlanteanWoodDoors.WATERLOGGED;

public class PearlBlocks extends HalfTransparentBlock implements SimpleWaterloggedBlock
{

    public PearlBlocks(BlockBehaviour.Properties properties) {
        super(properties
                .strength(2.0F, 5.0F)
//                .breakByTool(FabricToolTags.PICKAXES, 1) //TODO: Fix this
                .requiresCorrectToolForDrops()
                .lightLevel((state) -> 5)
                .noOcclusion()
                .sound(SoundType.STONE));
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, true));
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 125;
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(worldIn, player, pos, state, te, stack);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            worldIn.removeBlock(pos, false);
            return;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(WATERLOGGED);
    }
}
