package me.gammadelta;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(AxiomaticTeleportationMod.MOD_ID)
public class AxiomaticTeleportationMod {
    public static final String MOD_ID = "axiomatic_teleportation";

    private static AxiomaticTeleportationMod instance;
    private final SimpleChannel network;

    // Register stuff here; we have few enough things to register we can do it inline.
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public AxiomaticTeleportationMod() {
        instance = this;

        this.network = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "network"), () -> "1.0", s -> true,
                s -> true);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ITEMS.register("labcoat", LabcoatItem::new);
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static AxiomaticTeleportationMod getInstance() {
        return instance;
    }

    public SimpleChannel getNetwork() {
        return network;
    }
}
