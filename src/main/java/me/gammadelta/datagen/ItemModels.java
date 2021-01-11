package me.gammadelta.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static me.gammadelta.AxiomaticTeleportationMod.MOD_ID;

public class ItemModels extends ItemModelProvider {
    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture("labcoat", new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(MOD_ID, "labcoat_item"));
    }
}
