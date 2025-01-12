package me.axieum.mcmod.pedestalcrafting.compat.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.google.common.collect.Lists;
import me.axieum.mcmod.pedestalcrafting.Tags;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipe;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipeManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumParticleTypes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class PedestalCrafting extends VirtualizedRegistry<PedestalRecipe> {
    @RecipeBuilderDescription(example = {
            @Example(".center(item('minecraft:lava_bucket')).output(item('minecraft:obsidian')).ticks(100)"),
            @Example(".center(ore('plankWood')).input(ore('stickWood'),item('minecraft:water_bucket'),ore('logWood')).output(item('minecraft:diamond')).ticks(100)"),
            @Example(".center(ore('oreGold')).input(item('minecraft:chest'),item('minecraft:piston')).output(item('minecraft:emerald')).ticks(100).addCraftingParticle('fireworkSpark', 10)"),
            @Example(".center(ore('oreDiamond')).input(item('minecraft:hopper'),item('minecraft:chest')).output(item('minecraft:stone')).ticks(100).addCraftingParticle('bubble', 10).addPostCraftCoreParticle('suspended', 10)"),
            @Example(".center(ore('oreRedstone')).input(item('minecraft:cobblestone'), ore('ingotGold')).output(item('minecraft:redstone')).ticks(100).addCraftingParticle('instantSpell', 10).addPostCraftCoreParticle('dripLava', 10).addPostCraftPedestalParticle('portal', 10)")
    })
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilder();
    }

    public void add(PedestalRecipe recipe) {
        if (recipe != null) {
            addScripted(recipe);
            PedestalRecipeManager.getInstance().getRecipes().add(recipe);
        }
    }

    public boolean remove(PedestalRecipe recipe) {
        if (PedestalRecipeManager.getInstance().getRecipes().removeIf(r -> r == recipe)) {
            addBackup(recipe);
            return true;
        }
        return false;
    }

    @Override
    @GroovyBlacklist
    public void onReload() {
        PedestalRecipeManager.getInstance().getRecipes().removeAll(removeScripted());
        PedestalRecipeManager.getInstance().getRecipes().addAll(restoreFromBackup());
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:stick')"))
    public boolean removeByOutput(IIngredient output) {
        return PedestalRecipeManager.getInstance().getRecipes().removeIf(r -> {
            if (output.test(r.getOutput())) {
                addBackup(r);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:redstone_block')"))
    public boolean removeByInput(IIngredient input) {
        return PedestalRecipeManager.getInstance().getRecipes().removeIf(r -> {
            if (r.getInput().stream().map(Ingredient::getMatchingStacks).anyMatch(s -> Arrays.stream(s).anyMatch(input))) {
                addBackup(r);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, description = "groovyscript.wiki.pedestalcrafting.pedestal_crafting.removebycenter", example = @Example("item('minecraft:wool')"))
    public boolean removeByCenter(IIngredient input) {
        return PedestalRecipeManager.getInstance().getRecipes().removeIf(r -> {
            if (Arrays.stream(r.getCore().getMatchingStacks()).anyMatch(input)) {
                addBackup(r);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<PedestalRecipe> streamRecipes() {
        return new SimpleObjectStream<>(PedestalRecipeManager.getInstance().getRecipes()).setRemover(this::remove);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
    public void removeAll() {
        PedestalRecipeManager.getInstance().getRecipes().forEach(this::addBackup);
        PedestalRecipeManager.getInstance().getRecipes().clear();
    }

    @Property(property = "input", comp = @Comp(gte = 0))
    @Property(property = "output", comp = @Comp(eq = 1))
    public static class RecipeBuilder extends AbstractRecipeBuilder<PedestalRecipe> {

        @Property
        private Map<EnumParticleTypes, Integer> particlesCrafting = new HashMap<>();

        @Property
        private Map<EnumParticleTypes, Integer> particlesPostCraftCore = new HashMap<>();

        @Property
        private Map<EnumParticleTypes, Integer> particlesPostCraftPedestal = new HashMap<>();

        @Property(comp = @Comp(not = "null"))
        private IIngredient center;

        @Property(comp = @Comp(gte = 0), defaultValue = "0")
        private int ticks = 0;

        @RecipeBuilderMethodDescription(field = "particlesCrafting")
        public RecipeBuilder addCraftingParticle(String particle, int count) {
            this.particlesCrafting.put(EnumParticleTypes.getByName(particle), count);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particlesCrafting")
        public RecipeBuilder addCraftingParticle(EnumParticleTypes particle, int count) {
            this.particlesCrafting.put(particle, count);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particlesPostCraftCore")
        public RecipeBuilder addPostCraftCoreParticle(String particle, int count) {
            this.particlesPostCraftCore.put(EnumParticleTypes.getByName(particle), count);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particlesPostCraftCore")
        public RecipeBuilder addPostCraftCoreParticle(EnumParticleTypes particle, int count) {
            this.particlesPostCraftCore.put(particle, count);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particlesPostCraftPedestal")
        public RecipeBuilder addPostCraftPedestalParticle(String particle, int count) {
            this.particlesPostCraftPedestal.put(EnumParticleTypes.getByName(particle), count);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particlesPostCraftPedestal")
        public RecipeBuilder addPostCraftPedestalParticle(EnumParticleTypes particle, int count) {
            this.particlesPostCraftPedestal.put(particle, count);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "center")
        public RecipeBuilder center(IIngredient center) {
            this.center = center;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "ticks")
        public RecipeBuilder ticks(int ticks) {
            this.ticks = ticks;
            return this;
        }

        @Override
        public String getErrorMsg() {
            return "Error adding" + Tags.MOD_NAME + " recipe";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateFluids(msg);
            validateItems(msg, 0, Integer.MAX_VALUE, 1, 1);
            msg.add(center == null, "core must be defined");
            msg.add(ticks < 0, "ticks must be a nonnegative integer, yet it was {}", ticks);
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable PedestalRecipe register() {
            if (!validate()) return null;
            ArrayList<Ingredient> outerIngredients = Lists.newArrayList();
            for (IIngredient i : input) outerIngredients.add(i.toMcIngredient());
            PedestalRecipe recipe = new PedestalRecipe(output.get(0), ticks, center.toMcIngredient(), outerIngredients);
            recipe.initParticles();
            recipe.setParticles(particlesCrafting, particlesPostCraftCore, particlesPostCraftPedestal);
            GSPlugin.instance.pedestalCrafting.add(recipe);
            return recipe;
        }
    }
}
