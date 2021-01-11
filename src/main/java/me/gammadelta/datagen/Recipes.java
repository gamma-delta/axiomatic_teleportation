package me.gammadelta.datagen;


import me.gammadelta.AxiomaticTeleportationMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(AxiomaticTeleportationMod.LABCOAT.get())
                .key('O', Tags.Items.ENDER_PEARLS).key('C', Items.LEATHER_CHESTPLATE).key('P', Items.PISTON)
                .key('D', Tags.Items.GEMS_DIAMOND).key('R', Tags.Items.DUSTS_REDSTONE)
                .patternLine(" D ").patternLine("OCO").patternLine("PRP")
                .addCriterion("has_pearl", hasItem(Items.ENDER_PEARL))
                .build(consumer);
    }
}
