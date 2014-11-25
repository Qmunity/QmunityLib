package uk.co.qmunity.lib.texture;

public interface ITextureTransform {

    public Texture transform(Texture tex);

    public static final class TransformableParameters {

        public static final int RED = 1;
        public static final int GREEN = 2;
        public static final int BLUE = 4;

        public static final int ALPHA = 8;
        public static final int COLOR = RED + GREEN + BLUE;

        public static final boolean contains(int value, int parameter) {

            return (value & parameter) > 0;
        }

    }

}
