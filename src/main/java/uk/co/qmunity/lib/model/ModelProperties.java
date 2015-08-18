package uk.co.qmunity.lib.model;

import uk.co.qmunity.lib.Copyable;

public class ModelProperties implements Copyable<ModelProperties> {

    private boolean hasNormals = false, hasLighting = false, hasColors = true, hasTextures = false;

    public ModelProperties() {

    }

    public ModelProperties(boolean hasNormals, boolean hasLighting, boolean hasColors, boolean hasTextures) {

        set(hasNormals, hasLighting, hasColors, hasTextures);
    }

    public boolean hasNormals() {

        return hasNormals;
    }

    public boolean hasLighting() {

        return hasLighting;
    }

    public boolean hasColors() {

        return hasColors;
    }

    public boolean hasTextures() {

        return hasTextures;
    }

    public ModelProperties setHasNormals(boolean hasNormals) {

        this.hasNormals = hasNormals;
        return this;
    }

    public ModelProperties setHasLighting(boolean hasLighting) {

        this.hasLighting = hasLighting;
        return this;
    }

    public ModelProperties setHasColors(boolean hasColors) {

        this.hasColors = hasColors;
        return this;
    }

    public ModelProperties setHasTextures(boolean hasTextures) {

        this.hasTextures = hasTextures;
        return this;
    }

    public ModelProperties set(ModelProperties context) {

        return set(context.hasNormals(), context.hasLighting(), context.hasColors(), context.hasTextures());
    }

    public ModelProperties set(boolean hasNormals, boolean hasLighting, boolean hasColors, boolean hasTextures) {

        this.hasNormals = hasNormals;
        this.hasLighting = hasLighting;
        this.hasColors = hasColors;
        this.hasTextures = hasTextures;

        return this;
    }

    @Override
    public ModelProperties copy() {

        return new ModelProperties(hasNormals, hasLighting, hasColors, hasTextures);
    }

}
