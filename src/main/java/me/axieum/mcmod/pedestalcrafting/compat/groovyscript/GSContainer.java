package me.axieum.mcmod.pedestalcrafting.compat.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;

public class GSContainer extends GroovyPropertyContainer {
    public final PedestalCrafting pedestalCrafting = new PedestalCrafting();
    public GSContainer() {
        addProperty(pedestalCrafting);
    }
}
