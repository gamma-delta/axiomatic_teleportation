package me.gammadelta;

import net.minecraft.client.renderer.entity.model.BipedModel;

public class LabcoatArmorModel extends BipedModel {
    protected LabcoatArmorModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);

        this.bipedHead.showModel = false;
        this.bipedHeadwear.showModel = false;
        // allow body
        this.bipedLeftArm.showModel = false;
        this.bipedRightArm.showModel = false;
        this.bipedLeftLeg.showModel = false;
        this.bipedRightLeg.showModel = false;
    }

    private static LabcoatArmorModel INSTANCE = null;

    public static LabcoatArmorModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LabcoatArmorModel(0.0625f, 0, 64, 32);
        }
        return INSTANCE;
    }
}
