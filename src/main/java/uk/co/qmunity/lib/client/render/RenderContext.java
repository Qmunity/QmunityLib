package uk.co.qmunity.lib.client.render;

import uk.co.qmunity.lib.Copyable;

public class RenderContext implements Copyable<RenderContext> {

    private boolean useNormals = false, useLighting = false, useColors = true, useTextures = false;

    public RenderContext() {

    }

    public RenderContext(boolean useNormals, boolean useLighting, boolean useColors, boolean useTextures) {

        set(useNormals, useLighting, useColors, useTextures);
    }

    public boolean useNormals() {

        return useNormals;
    }

    public boolean useLighting() {

        return useLighting;
    }

    public boolean useColors() {

        return useColors;
    }

    public boolean useTextures() {

        return useTextures;
    }

    public RenderContext setUseNormals(boolean useNormals) {

        this.useNormals = useNormals;
        return this;
    }

    public RenderContext setUseLighting(boolean useLighting) {

        this.useLighting = useLighting;
        return this;
    }

    public RenderContext setUseColors(boolean useColors) {

        this.useColors = useColors;
        return this;
    }

    public RenderContext setUseTextures(boolean useTextures) {

        this.useTextures = useTextures;
        return this;
    }

    public RenderContext set(RenderContext context) {

        return set(context.useNormals(), context.useLighting(), context.useColors(), context.useTextures());
    }

    public RenderContext set(boolean useNormals, boolean useLighting, boolean useColors, boolean useTextures) {

        this.useNormals = useNormals;
        this.useLighting = useLighting;
        this.useColors = useColors;
        this.useTextures = useTextures;

        return this;
    }

    @Override
    public RenderContext copy() {

        return new RenderContext(useNormals, useLighting, useColors, useTextures);
    }

}
