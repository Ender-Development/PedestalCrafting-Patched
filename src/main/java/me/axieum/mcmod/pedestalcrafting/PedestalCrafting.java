package me.axieum.mcmod.pedestalcrafting;

import me.axieum.mcmod.pedestalcrafting.block.BlockPedestalCore;
import me.axieum.mcmod.pedestalcrafting.block.ModBlocks;
import me.axieum.mcmod.pedestalcrafting.proxy.CommonProxy;
import me.axieum.mcmod.pedestalcrafting.recipe.ModRecipes;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies = PedestalCrafting.MOD_DEPENDENCIES,
        useMetadata = true
)
public class PedestalCrafting
{
    public static final String MOD_DEPENDENCIES = "after: crafttweaker; after: jei; after: theoneprobe; after:waila;";

    @Mod.Instance(Tags.MOD_ID)
    public static PedestalCrafting instance;

    @SidedProxy(
            clientSide = "me.axieum.mcmod.pedestalcrafting.proxy.ClientProxy",
            serverSide = "me.axieum.mcmod.pedestalcrafting.proxy.ServerProxy"
    )
    public static CommonProxy proxy;

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(Tags.MOD_ID)
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.PEDESTAL_CORE, 1, 0);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event)
        {
            ModBlocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
            ModBlocks.registerItems(event.getRegistry());
        }

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event)
        {
            ModBlocks.registerModels();
        }

        @SubscribeEvent
        public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
        {
            ModRecipes.init();
        }
    }
}
