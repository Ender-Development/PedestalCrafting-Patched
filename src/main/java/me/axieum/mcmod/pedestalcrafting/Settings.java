package me.axieum.mcmod.pedestalcrafting;

import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID)
public class Settings
{
    @Config.LangKey("config.displaySpeed")
    @Config.RangeDouble
    public static double displaySpeed = 0.125D;

    @Config.LangKey("config.horizontalRadius")
    @Config.RangeInt(min = 1)
    public static int horizontalRadius = 3;

    @Config.LangKey("config.verticalRadius")
    @Config.RangeInt(min = 1)
    public static int verticalRadius = 1;
}
