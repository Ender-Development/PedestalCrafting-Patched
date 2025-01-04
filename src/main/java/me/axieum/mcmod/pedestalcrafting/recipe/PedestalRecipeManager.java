package me.axieum.mcmod.pedestalcrafting.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PedestalRecipeManager
{
    private static final PedestalRecipeManager INSTANCE = new PedestalRecipeManager();

    private final List<PedestalRecipe> recipes = new ArrayList<PedestalRecipe>();

    public static PedestalRecipeManager getInstance()
    {
        return INSTANCE;
    }

    public PedestalRecipe addRecipe(ItemStack output, int ticks, Ingredient core, ArrayList<Ingredient> inputs)
    {
        PedestalRecipe recipe = new PedestalRecipe(output, ticks, core, inputs);
        recipes.add(recipe);
        return recipe;
    }

    public PedestalRecipe addRecipe(PedestalRecipe recipe)
    {
        recipes.add(recipe);
        return recipe;
    }

    public List<PedestalRecipe> getRecipes()
    {
        return recipes;
    }

    public PedestalRecipe getRecipe(ItemStack itemStack)
    {
        for (PedestalRecipe recipe : recipes)
        {
            if (recipe.getOutput().isItemEqual(itemStack))
            {
                return recipe;
            }
        }
        return null;
    }

    public ArrayList<PedestalRecipe> getValidRecipes(ItemStack itemStack)
    {
        ArrayList<PedestalRecipe> validRecipes = new ArrayList<PedestalRecipe>();

        if (!itemStack.isEmpty())
        {
            for (PedestalRecipe recipe : this.getRecipes())
            {
                if (!(recipe.getCore().getMatchingStacks().length == 0) && Arrays.stream(recipe.getCore().getMatchingStacks()).anyMatch(itemStack::isItemEqual))
                    validRecipes.add(recipe);
            }
        }
        return validRecipes;
    }

    public boolean removeRecipe(ItemStack itemStack)
    {
        for (PedestalRecipe recipe : recipes)
        {
            if (recipe.getOutput().isItemEqual(itemStack))
            {
                recipes.remove(itemStack);
                return true;
            }
        }
        return false;
    }
}
