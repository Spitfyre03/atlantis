package com.mystic.atlantis.blocks;

import com.mystic.atlantis.init.BlockInit;
import com.mystic.atlantis.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.Random;

import static com.mystic.atlantis.blocks.AtlanteanWoodDoors.WATERLOGGED;

public class AtlanteanFireMelonFruitSpiked extends HorizontalDirectionalBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    public static final IntegerProperty AGE_4 = IntegerProperty.create("age", 0, 4);
    public static final IntegerProperty AGE = AGE_4;
    public static final BooleanProperty SPIKED = BooleanProperty.create("spiked");

    protected static final VoxelShape[] EAST_AABB = new VoxelShape[]{Block.box(11.0D, 7.0D, 6.0D, 15.0D, 12.0D, 10.0D), Block.box(9.0D, 5.0D, 5.0D, 15.0D, 12.0D, 11.0D), Block.box(7.0D, 3.0D, 4.0D, 15.0D, 12.0D, 12.0D)};
    protected static final VoxelShape[] WEST_AABB = new VoxelShape[]{Block.box(1.0D, 7.0D, 6.0D, 5.0D, 12.0D, 10.0D), Block.box(1.0D, 5.0D, 5.0D, 7.0D, 12.0D, 11.0D), Block.box(1.0D, 3.0D, 4.0D, 9.0D, 12.0D, 12.0D)};
    protected static final VoxelShape[] NORTH_AABB = new VoxelShape[]{Block.box(6.0D, 7.0D, 1.0D, 10.0D, 12.0D, 5.0D), Block.box(5.0D, 5.0D, 1.0D, 11.0D, 12.0D, 7.0D), Block.box(4.0D, 3.0D, 1.0D, 12.0D, 12.0D, 9.0D)};
    protected static final VoxelShape[] SOUTH_AABB = new VoxelShape[]{Block.box(6.0D, 7.0D, 11.0D, 10.0D, 12.0D, 15.0D), Block.box(5.0D, 5.0D, 9.0D, 11.0D, 12.0D, 15.0D), Block.box(4.0D, 3.0D, 7.0D, 12.0D, 12.0D, 15.0D)};

    public AtlanteanFireMelonFruitSpiked(Properties arg) {
        super(arg);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, 0).setValue(SPIKED, true).setValue(WATERLOGGED, Boolean.TRUE));
    }

    @Override
    public boolean isRandomlyTicking(BlockState arg) {
        return arg.getValue(AGE) < 4;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource random) {
        int i = blockState.getValue(AGE);
        if (i < 4 && ForgeHooks.onCropsGrowPre(level, blockPos, blockState, level.random.nextInt(4) == 0)) {
            level.setBlock(blockPos, blockState.setValue(AGE, i + 1), 4);
            ForgeHooks.onCropsGrowPost(level, blockPos, blockState);
        }

    }

    public HolderSet<Block> getAir(){
        Holder<Block> airHolderSet = Holder.direct(Blocks.AIR);
        return HolderSet.direct(airHolderSet);
    }

    public boolean OnlyWater(LevelReader worldReader, BlockPos pos, BlockState state) {
        return !worldReader.getBlockState(pos).is(getAir());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public boolean canSurvive(BlockState arg, LevelReader arg2, BlockPos arg3) {
        BlockState state = arg2.getBlockState(arg3.relative(arg.getValue(FACING)));
        if(OnlyWater(arg2, arg3, arg)){
            return state.is(BlockInit.ATLANTEAN_FIRE_MELON_STEM.get());
        }else{
            return false;
        }
    }

    @Override
    public VoxelShape getShape(BlockState arg, BlockGetter arg2, BlockPos arg3, CollisionContext arg4) {
        int i = arg.getValue(AGE);
        switch(arg.getValue(FACING)) {
            case SOUTH:
                return SOUTH_AABB[0];
            case NORTH:
            default:
                return NORTH_AABB[0];
            case WEST:
                return WEST_AABB[0];
            case EAST:
                return EAST_AABB[0];
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        BlockState blockstate = this.defaultBlockState();
        LevelReader levelreader = arg.getLevel();
        BlockPos blockpos = arg.getClickedPos();
        Direction[] var5 = arg.getNearestLookingDirections();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Direction direction = var5[var7];
            if (direction.getAxis().isHorizontal()) {
                blockstate = blockstate.setValue(FACING, direction);
                if (blockstate.canSurvive(levelreader, blockpos)) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    @Override
    public BlockState updateShape(BlockState arg, Direction arg2, BlockState arg3, LevelAccessor arg4, BlockPos arg5, BlockPos arg6) {
        return arg2 == arg.getValue(FACING) && !arg.canSurvive(arg4, arg5) ? Blocks.WATER.defaultBlockState() : super.updateShape(arg, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter arg, BlockPos arg2, BlockState arg3, boolean bl) {
        return arg3.getValue(AGE) < 4;
    }

    @Override
    public boolean isBonemealSuccess(Level arg, RandomSource random, BlockPos arg2, BlockState arg3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel arg, RandomSource random, BlockPos arg2, BlockState arg3) {
        arg.setBlock(arg2, arg3.setValue(AGE, arg3.getValue(AGE) + 1), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(FACING, AGE, SPIKED, WATERLOGGED);
    }

    @Override
    public boolean isPathfindable(BlockState arg, BlockGetter arg2, BlockPos arg3, PathComputationType arg4) {
        return false;
    }

    @Override
    public InteractionResult use(BlockState arg, Level arg2, BlockPos arg3, Player arg4, InteractionHand arg5, BlockHitResult arg6) {
        ItemStack itemstack = arg4.getItemInHand(arg5);
        if (itemstack.canPerformAction(ToolActions.SHEARS_CARVE) && arg.getValue(SPIKED)) {
            if (!arg2.isClientSide) {
                Direction direction = arg6.getDirection();
                Direction direction1 = direction.getAxis() == Direction.Axis.Y ? arg4.getDirection().getOpposite() : direction;
                arg2.playSound(null, arg3, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                arg2.setBlock(arg3, arg.setValue(SPIKED, false), 11);
                ItemEntity itementity = new ItemEntity(
                        arg2,
                        (double)arg3.getX() + 0.5 + (double)direction1.getStepX() * 0.65,
                        (double)arg3.getY() + 0.1,
                        (double)arg3.getZ() + 0.5 + (double)direction1.getStepZ() * 0.65,
                        new ItemStack(ItemInit.ATLANTEAN_FIRE_MELON_SPIKE.get(), 1)
                );
                itementity.setDeltaMovement(
                        0.05 * (double)direction1.getStepX() + arg2.random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction1.getStepZ() + arg2.random.nextDouble() * 0.02
                );
                arg2.addFreshEntity(itementity);
                itemstack.hurtAndBreak(1, arg4, arg2x -> arg2x.broadcastBreakEvent(arg5));
                arg2.gameEvent(arg4, GameEvent.SHEAR, arg3);
                arg4.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            }

            return InteractionResult.sidedSuccess(arg2.isClientSide);
        } else {
            return super.use(arg, arg2, arg3, arg4, arg5, arg6);
        }
    }

}
