package me.axieum.mcmod.pedestalcrafting.compat.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.google.common.collect.Lists;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import me.axieum.mcmod.pedestalcrafting.Tags;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipe;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipeManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumParticleTypes;
import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;

@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class PedestalCrafting extends VirtualizedRegistry<PedestalRecipe> {
    @RecipeBuilderDescription(example = {
            @Example(".input(ore('stickWood'),ore('plankWood'),ore('logWood'),item('minecraft:stick')).output(item('minecraft:diamond')).ticks(100)"),
            @Example(".input(item('minecraft:chest'),item('minecraft:piston')).output(item('minecraft:emerald')).ticks(100).addCraftingParticle('fireworkSpark', 10)"),
            @Example(".input(item('minecraft:hopper'),item('minecraft:chest')).output(item('minecraft:stone')).ticks(100).addCraftingParticle('bubble', 10).addPostCraftCoreParticle('suspended', 10)"),
            @Example(".input(item('minecraft:cobblestone'), ore('ingotGold')).output(item('minecraft:redstone')).ticks(100).addCraftingParticle('instantSpell', 10).addPostCraftCoreParticle('dripLava', 10).addPostCraftPedestalParticle('portal', 10)")
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

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<PedestalRecipe> streamRecipes() {
        return new SimpleObjectStream<>(PedestalRecipeManager.getInstance().getRecipes()).setRemover(this::remove);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
    public void removeAll() {
        PedestalRecipeManager.getInstance().getRecipes().forEach(this::addBackup);
        PedestalRecipeManager.getInstance().getRecipes().clear();
    }

    @Property(property = "input", comp = @Comp(gte = 2))
    @Property(property = "output", comp = @Comp(gte = 0, lte = 1))
    public static class RecipeBuilder extends AbstractRecipeBuilder<PedestalRecipe> {

        @Property
        private HashMap<EnumParticleTypes, Integer> particlesCrafting = new HashMap<>();

        @Property
        private HashMap<EnumParticleTypes, Integer> particlesPostCraftCore = new HashMap<>();

        @Property
        private HashMap<EnumParticleTypes, Integer> particlesPostCraftPedestal = new HashMap<>();

        @Property(comp = @Comp(gte = 5))
        private Integer ticks = 5;

        @RecipeBuilderMethodDescription(field = "particlesCrafting")
        public RecipeBuilder addCraftingParticle(String particle, int count) {
            this.particlesCrafting.put(EnumParticleTypes.getByName(particle), count);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particlesPostCraftCore")
        public RecipeBuilder addPostCraftCoreParticle(String particle, int count) {
            this.particlesPostCraftCore.put(EnumParticleTypes.getByName(particle), count);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particlesPostCraftPedestal")
        public RecipeBuilder addPostCraftPedestalParticle(String particle, int count) {
            this.particlesPostCraftPedestal.put(EnumParticleTypes.getByName(particle), count);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "ticks")
        public RecipeBuilder ticks(Integer ticks) {
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
            validateItems(msg, 2, Int.MaxValue(), 1, 1);
            msg.add(ticks < 5, "ticks must be greater or equal 5, yet it was {}", ticks);
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable PedestalRecipe register() {
            if (!validate()) return null;
            ArrayList<Ingredient> outerIngredients = Lists.newArrayList();
            for (int i = 1; i < input.size(); i++) {
                outerIngredients.add(input.get(i).toMcIngredient());
            }
            PedestalRecipe recipe = new PedestalRecipe(output.get(0), ticks, input.get(0).toMcIngredient(), outerIngredients);
            recipe.initParticles();
            if (particlesCrafting.isEmpty() && particlesPostCraftCore.isEmpty() && particlesPostCraftPedestal.isEmpty()) {
                recipe.addDefaultParticles();
            } else {
                recipe.setParticles(particlesCrafting, particlesPostCraftCore, particlesPostCraftPedestal);
            }
            GSPlugin.instance.pedestalCrafting.add(recipe);
            return recipe;
        }
    }
}
