package com.shu.Pano.Objects;


public class Coordinates {
    private float xAxis;
    private float yAxis;
    private float zAxis;

    public Coordinates (float xAxis,float yAxis,float zAxis){
        setxAxis(xAxis);
        setyAxis(yAxis);
        setzAxis(zAxis);

    }
    public float getxAxis() {
        return xAxis;
    }

    public void setxAxis(float xAxis) {
        this.xAxis = xAxis;
    }

    public float getyAxis() {
        return yAxis;
    }

    public void setyAxis(float yAxis) {
        this.yAxis = yAxis;
    }

    public float getzAxis() {
        return zAxis;
    }

    public void setzAxis(float zAxis) {
        this.zAxis = zAxis;
    }
}
