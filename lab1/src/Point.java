/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cannonbait
 */
public class Point {
    private final int x, y;
    
    public Point (int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    
    @Override
    public boolean equals(Object rhs){
        if (this == rhs){
            return true;
        }else if (rhs == null || this.getClass() != rhs.getClass()){
            return false;
        } else {
            Point toCompare = (Point)rhs;
            return toCompare.getX() == this.getX() && toCompare.getY() == this.getY();
        }
    }
    
    @Override
    public int hashCode(){
        return x*3+y*5;
    }
}
