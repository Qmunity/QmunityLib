package uk.co.qmunity.lib.misc;

public class ShiftingBuffer<T> {

    private Object[][] data;
    private T def;

    public ShiftingBuffer(int buffers, int size, T def) {

        data = new Object[buffers][size];
        this.def = def;

        for (int i = 0; i < buffers; i++)
            for (int j = 0; j < size; j++)
                data[i][j] = def;
    }

    public T set(int buffer, T value) {

        data[buffer][0] = value;
        return value;
    }

    public T get(int buffer) {

        return get(buffer, data[buffer].length - 1);
    }

    @SuppressWarnings("unchecked")
    public T get(int buffer, int id) {

        return (T) data[buffer][id];
    }

    public Object[] getAll(int buffer) {

        return data[buffer];
    }

    public void shift() {

        shift(false);
    }

    public void shift(boolean reset0) {

        for (int i = 0; i < data.length; i++) {
            Object[] data = this.data[i];
            for (int j = data.length - 1; j > 0; j--)
                data[j] = data[j - 1];
            if (reset0)
                data[0] = def;
        }
    }

}
