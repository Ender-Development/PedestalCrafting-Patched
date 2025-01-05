package me.axieum.mcmod.pedestalcrafting.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PedestalRecipe {
    private final Ingredient core;
    private final ItemStack output;
    private final ArrayList<Ingredient> input;
    private final int ticks;
    private final ArrayList<HashMap<EnumParticleTypes, Integer>> particles = new ArrayList<>(
            3);

    public PedestalRecipe(ItemStack output, int ticks, Ingredient core, ArrayList<Ingredient> inputs) {
        this.core = core;
        this.ticks = ticks > 0 ? ticks : 5;
        this.output = output;
        this.input = inputs;

        // Initialise default particle effects, in case not overriden
        this.initParticles();
        this.addDefaultParticles();
    }

    public void addDefaultParticles() {
        this.addParticleCrafting(EnumParticleTypes.END_ROD, 2);
        this.addParticlePostCraftCore(EnumParticleTypes.CLOUD, 25);
        this.addParticlePostCraftPedestal(EnumParticleTypes.SMOKE_NORMAL, 15);
    }

    public void initParticles() {
        this.particles.add(0, new HashMap<>());
        this.particles.add(1, new HashMap<>());
        this.particles.add(2, new HashMap<>());
    }

    public PedestalRecipe setParticles(@Nullable Map<EnumParticleTypes, Integer> particlesCrafting, @Nullable Map<EnumParticleTypes, Integer> particlesPostCraftCore, @Nullable Map<EnumParticleTypes, Integer> particlesPostCraftPedestal) {
        this.particles.set(0, (HashMap<EnumParticleTypes, Integer>) particlesCrafting);
        this.particles.set(1, (HashMap<EnumParticleTypes, Integer>) particlesPostCraftCore);
        this.particles.set(2, (HashMap<EnumParticleTypes, Integer>) particlesPostCraftPedestal);

        return this;
    }

    public PedestalRecipe addParticleCrafting(HashMap<EnumParticleTypes, Integer>... particles) {
        for (HashMap<EnumParticleTypes, Integer> particle : particles)
            this.particles.get(0).putAll(particle);

        return this;
    }

    public PedestalRecipe addParticlePostCraftCore(HashMap<EnumParticleTypes, Integer>... particles) {
        for (HashMap<EnumParticleTypes, Integer> particle : particles)
            this.particles.get(1).putAll(particle);

        return this;
    }

    public PedestalRecipe addParticlePostCraftPedestal(HashMap<EnumParticleTypes, Integer>... particles) {
        for (HashMap<EnumParticleTypes, Integer> particle : particles)
            this.particles.get(2).putAll(particle);

        return this;
    }

    public PedestalRecipe addParticleCrafting(EnumParticleTypes particle, int count) {
        this.particles.get(0).put(particle, count);
        return this;
    }

    public PedestalRecipe addParticlePostCraftCore(EnumParticleTypes particle, int count) {
        this.particles.get(1).put(particle, count);
        return this;
    }

    public PedestalRecipe addParticlePostCraftPedestal(EnumParticleTypes particle, int count) {
        this.particles.get(2).put(particle, count);
        return this;
    }

    public Ingredient getCore() {
        return core;
    }

    public ArrayList<Ingredient> getInput() {
        return input;
    }

    public int getTicks() {
        return ticks;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ArrayList<HashMap<EnumParticleTypes, Integer>> getParticles() {
        return particles;
    }

    public HashMap<EnumParticleTypes, Integer> getCraftingParticles() {
        return particles.get(0);
    }

    public HashMap<EnumParticleTypes, Integer> getPostCraftCoreParticles() {
        return particles.get(1);
    }

    public HashMap<EnumParticleTypes, Integer> getPostCraftPedestalParticles() {
        return particles.get(2);
    }
}
