package me.axieum.mcmod.pedestalcrafting.render;

import me.axieum.mcmod.pedestalcrafting.block.ModBlocks;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenders
{
    public static void init()
    {
        // Pedestal
        ClientRegistry.bindTileEntitySpecialRenderer(
                ModBlocks.PEDESTAL.getTileEntityClass(),
                new RenderPedestal()
        );


        // Pedestal Core
        ClientRegistry.bindTileEntitySpecialRenderer(
                ModBlocks.PEDESTAL_CORE.getTileEntityClass(),
                new RenderPedestalCore()
        );
    }
}
