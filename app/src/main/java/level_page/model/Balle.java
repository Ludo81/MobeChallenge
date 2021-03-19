package level_page.model;

import android.graphics.Color;

public class Balle {

    private int radius;
    private int cx;
    private int cy;
    private int couleur;

    public Balle(int radius, int cx, int cy) {
        this.radius = radius;
        this.cx = cx;
        this.cy = cy;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getCx() {
        return cx;
    }

    public void setCx(int cx) {
        this.cx = cx;
    }

    public int getCy() {
        return cy;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }
}
