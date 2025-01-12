package me.axieum.mcmod.pedestalcrafting.tile;

import me.axieum.mcmod.pedestalcrafting.Settings;
import me.axieum.mcmod.pedestalcrafting.block.BlockPedestal;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipe;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipeManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TilePedestalCore extends TileEntity implements ITickable {
    public int elapsed = 0;
    public int pedestalCount = 0;

    public final int RADIUS_HORIZONTAL = Settings.horizontalRadius;
    public final int RADIUS_VERTICAL = Settings.verticalRadius;

    public ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            TilePedestalCore.this.markDirty();

            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    };

    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        ArrayList<BlockPos> pedestalLocs = this.findPedestals();
        PedestalRecipe recipe = this.getRecipe(pedestalLocs);
        if (recipe != null) {
            ArrayList<TilePedestal> pedestals = this.getActivePedestals(recipe, pedestalLocs);
            int actualPedestals = pedestals != null ? pedestals.size() : 0;
            int requiredPedestals = recipe.getInput().size();
            if (this.process(recipe) && requiredPedestals <= actualPedestals) {
                if (requiredPedestals > 0) {
                    pedestals.forEach(pedestal -> {
                        ItemStack pedestalItem = pedestal.inventory.getStackInSlot(0).getItem().getContainerItem(pedestal.inventory.getStackInSlot(0));
                        pedestal.inventory.setStackInSlot(0, pedestalItem);
                        pedestal.markDirty();
                        recipe.getPostCraftPedestalParticles().forEach((particle, count) -> {
                            ((WorldServer) this.getWorld()).spawnParticle(
                                    particle,
                                    false,
                                    pedestal.getPos().getX() + 0.5D,
                                    pedestal.getPos().getY() + 1.5D,
                                    pedestal.getPos().getZ() + 0.5D,
                                    count,
                                    0,
                                    0,
                                    0, 0.1D
                            );
                        });
                    });
                }

                ItemStack container = this.inventory.getStackInSlot(0).getItem().getContainerItem(this.inventory.getStackInSlot(0));
                if (!container.isEmpty()) {
                    EntityItem item = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, container);
                    item.setNoPickupDelay();
                    world.spawnEntity(item);
                }

                this.inventory.setStackInSlot(0, recipe.getOutput().copy());
                this.elapsed = 0;
                this.markDirty();

                recipe.getPostCraftCoreParticles().forEach((particle, count) -> {
                    ((WorldServer) this.getWorld()).spawnParticle(
                            particle,
                            false,
                            this.getPos().getX() + 0.5D,
                            this.getPos().getY() + 1.5D,
                            this.getPos().getZ() + 0.5D,
                            count,
                            0,
                            0,
                            0, 0.1D
                    );
                });

            } else {
                recipe.getCraftingParticles().forEach((particle, count) -> {
                    ((WorldServer) this.getWorld()).spawnParticle(
                            particle,
                            false,
                            this.getPos().getX() + 0.5D,
                            this.getPos().getY() + 1.5D,
                            this.getPos().getZ() + 0.5D,
                            count,
                            0,
                            0,
                            0, 0.1D
                    );
                });
            }
        } else {
            this.elapsed = 0;
        }
    }

    private boolean process(PedestalRecipe recipe) {
        this.elapsed += 1;

        if (this.elapsed % 5 == 0) {
            this.markDirty();

            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }

        return this.elapsed >= recipe.getTicks();
    }

    public PedestalRecipe getRecipe() {
        return getRecipe(findPedestals());
    }

    private PedestalRecipe getRecipe(ArrayList<BlockPos> locs) {
        ArrayList<PedestalRecipe> recipes = PedestalRecipeManager.getInstance().getValidRecipes(this.inventory.getStackInSlot(0));

        if (!recipes.isEmpty()) {
            for (PedestalRecipe recipe : recipes) {
                if (recipe.getInput().isEmpty() || this.getActivePedestals(recipe, locs) != null) {
                    return recipe;
                }
            }
        }

        return null;
    }

    private ArrayList<BlockPos> findPedestals() {
        ArrayList<BlockPos> pedestalLocs = new ArrayList<>();
        Iterable<BlockPos> blocksToCheck = BlockPos.getAllInBox(
                this.getPos().add(-RADIUS_HORIZONTAL, -RADIUS_VERTICAL, -RADIUS_HORIZONTAL),
                this.getPos().add(RADIUS_HORIZONTAL, RADIUS_VERTICAL, RADIUS_HORIZONTAL)
        );

        for (BlockPos blockPos : blocksToCheck)
            if (this.getWorld().getBlockState(blockPos).getBlock() instanceof BlockPedestal) {
                pedestalLocs.add(blockPos);
            }

        this.pedestalCount = pedestalLocs.size();
        return pedestalLocs;
    }

    private ArrayList<TilePedestal> getActivePedestals(PedestalRecipe recipe, ArrayList<BlockPos> locs) {
        if (locs.isEmpty()) {
            return null;
        }

        ArrayList<Ingredient> remainingItems = new ArrayList<>(recipe.getInput());
        ArrayList<TilePedestal> pedestals = new ArrayList<>();

        for (BlockPos pos : locs) {
            TileEntity tileEntity = this.getWorld().getTileEntity(pos);

            if (!(tileEntity instanceof TilePedestal)) {
                break;
            }

            TilePedestal pedestal = (TilePedestal) tileEntity;

            for (Ingredient ingredient : remainingItems) {
                boolean isMatch = false;
                ItemStack pedestalItem = pedestal.inventory.getStackInSlot(0);

                if (ingredient.apply(pedestalItem)) {
                    isMatch = true;
                }

                if (isMatch) {
                    pedestals.add(pedestal);
                    remainingItems.remove(ingredient);
                    break;
                }
            }
        }

        if (!remainingItems.isEmpty() || pedestals.size() != recipe.getInput().size()) {
            return null;
        }

        return pedestals;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());

        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setInteger("elapsed", elapsed);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        elapsed = compound.getInteger("elapsed");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory : super.getCapability(
                capability,
                facing
        );
    }
}
