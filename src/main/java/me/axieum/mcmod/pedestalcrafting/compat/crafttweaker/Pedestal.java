package me.axieum.mcmod.pedestalcrafting.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenDoc;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import me.axieum.mcmod.pedestalcrafting.Tags;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipe;
import me.axieum.mcmod.pedestalcrafting.recipe.PedestalRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

@ZenClass("mods." + Tags.MOD_ID + ".Pedestal")
public class Pedestal
{
    @ZenMethod
    @ZenDoc("Adds a new pedestal crafting recipe")
    public static void addRecipe(IItemStack output, int ticks, IItemStack core, IIngredient[] ingredients)
    {
        Ingredient coreIngredient = Ingredient.fromStacks((ItemStack) core.getInternal());
        ItemStack outputItem = (ItemStack) output.getInternal();
        CraftTweakerAPI.apply(new Add(new PedestalRecipe(
                outputItem,
                ticks,
                coreIngredient,
                toObjects(ingredients)
        )));
    }

    @ZenMethod
    @ZenDoc("Adds a new pedestal crafting recipe whilst overriding the default particles")
    public static void addRecipe(IItemStack output, int ticks, IItemStack core, IIngredient[] ingredients, String[][] particlesCrafting, String[][] particlesPostCraftCore, String[][] particlesPostCraftPedestal)
    {
        Ingredient coreIngredient = Ingredient.fromStacks((ItemStack) core.getInternal());
        ItemStack outputItem = (ItemStack) output.getInternal();
        PedestalRecipe recipe = new PedestalRecipe(
                outputItem,
                ticks,
                coreIngredient,
                toObjects(ingredients)
        );

        recipe.setParticles(
                toParticleArray(particlesCrafting),
                toParticleArray(particlesPostCraftCore),
                toParticleArray(particlesPostCraftPedestal)
        );

        CraftTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    @ZenDoc("Removes an existing pedestal crafting recipe")
    public static void remove(IItemStack target)
    {
        CraftTweakerAPI.apply(new Remove(toItemStack(target)));
    }

    private static ItemStack toItemStack(IItemStack itemStack)
    {
        if (itemStack == null)
            return ItemStack.EMPTY;

        Object internal = itemStack.getInternal();

        if (!(internal instanceof ItemStack))
            CraftTweakerAPI.getLogger().logError("Invalid item stack supplied: " + itemStack);

        return (ItemStack) internal;
    }

    private static ArrayList<Ingredient> toObjects(IIngredient[] ingredientList)
    {
        if (ingredientList == null)
            return null;

        return new ArrayList<Ingredient>(Arrays.asList(
                Arrays.stream(ingredientList)
                        .map(ingredient -> {
                            if (ingredient == null)
                                return null;

                            if (ingredient instanceof IOreDictEntry)
                                return Ingredient.fromStacks(OreDictionary.getOres(((IOreDictEntry) ingredient).getName()).toArray(new ItemStack[0]));

                            return Ingredient.fromStacks((ItemStack) ingredient.getInternal());
                        })
                        .filter(Objects::nonNull)
                        .toArray(Ingredient[]::new)
        ));
    }

    private static EnumParticleTypes toParticle(String name)
    {
        EnumParticleTypes particle = EnumParticleTypes.getByName(name);
        if (particle != null)
            return particle;
        else
            throw new RuntimeException("Invalid particle effect: " + name + "! Seek documentation for valid particle strings.");
    }

    private static HashMap<EnumParticleTypes, Integer> toParticleArray(String[]... particles)
    {
        HashMap<EnumParticleTypes, Integer> particleArray = new HashMap<EnumParticleTypes, Integer>();

        for (String[] particle : particles)
        {
            if (particle.length < 1)
                throw new RuntimeException("Empty particle list supplied: " + particle.toString());

            String particleString = particle[0];
            int particleCount = 5;

            if (particle.length > 1)
            {
                try
                {
                    particleCount = Integer.parseInt(particle[1]);
                } catch (NumberFormatException e)
                {
                    throw new RuntimeException("Invalid particle count number supplied: " + particle[1]);
                }
            }

            particleArray.put(toParticle(particleString), particleCount);
        }

        return particleArray;
    }

    private static class Add implements IAction
    {
        PedestalRecipe recipe;

        public Add(PedestalRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void apply()
        {
            PedestalRecipeManager.getInstance().addRecipe(recipe);
        }

        @Override
        public String describe()
        {
            return "Adding a Pedestal Crafting recipe for " + recipe.getOutput().getDisplayName();
        }
    }

    private static class Remove implements IAction
    {
        ItemStack target;

        public Remove(ItemStack target)
        {
            this.target = target;
        }

        @Override
        public void apply()
        {
            PedestalRecipeManager.getInstance().removeRecipe(target);
        }

        @Override
        public String describe()
        {
            return "Removing a Pedestal Crafting recipe for " + target.getDisplayName();
        }
    }
}
