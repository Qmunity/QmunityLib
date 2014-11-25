package uk.co.qmunity.lib.vec;

public class Rotation {
    
    private double x = 0, y = 0, z = 0;
    
    public Rotation(double x, double y, double z) {
    
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
    
        return x;
    }
    
    public double getY() {
    
        return y;
    }
    
    public double getZ() {
    
        return z;
    }
    
    public void add(double x, double y, double z) {
    
        x += x;
        y += y;
        z += z;
    }
    
    public void subtract(double x, double y, double z) {
    
        add(-x, -y, -z);
    }
    
}
