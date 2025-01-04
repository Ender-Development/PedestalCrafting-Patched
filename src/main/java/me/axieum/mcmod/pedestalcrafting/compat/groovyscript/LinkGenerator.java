package me.axieum.mcmod.pedestalcrafting.compat.groovyscript;

import com.cleanroommc.groovyscript.documentation.linkgenerator.BasicLinkGenerator;
import me.axieum.mcmod.pedestalcrafting.Tags;

public class LinkGenerator extends BasicLinkGenerator {
    @Override
    public String id() {
        return Tags.MOD_ID;
    }

    @Override
    protected String domain() {
        return "https://github.com/Ender-Development/PedestalCrafting-Patched/";
    }
}
