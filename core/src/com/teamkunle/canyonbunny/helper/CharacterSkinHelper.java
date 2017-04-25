package com.teamkunle.canyonbunny.helper;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by EngineerKunle on 25/04/2017.
 */

public enum CharacterSkinHelper {

    WHITE("WHITE", 1.0f, 1.0f, 1.0f),
    GRAY("GRAY", 7.0f, 7.0f, 7.0f),
    BROWN("BROWN",  0.7f, 0.5f, 0.3f);

    private String name;
    private Color color = new Color();

    private CharacterSkinHelper(String name, float r, float g, float b){
        this.name = name;
        color.set(r, g, b, 1.0f);
    }

    @Override
    public String toString() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
