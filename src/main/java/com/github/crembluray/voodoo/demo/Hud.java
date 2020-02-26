package com.github.crembluray.voodoo.demo;

import com.github.crembluray.voodoo.engine.Window;
import com.github.crembluray.voodoo.engine.gameObject.GameObject;
import com.github.crembluray.voodoo.hud.FontTexture;
import com.github.crembluray.voodoo.hud.IHud;
import com.github.crembluray.voodoo.hud.TextItem;
import org.joml.Vector4f;

import java.awt.Font;

public class Hud implements IHud {

    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);

    private static final String CHARSET = "ISO-8859-1";

    private final GameObject[] gameObjects;

    private final TextItem statusTextItem;

    public Hud(String statusText) throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        this.statusTextItem = new TextItem(statusText, fontTexture);
        this.statusTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 1));

        // Create list that holds the HUD
        gameObjects = new GameObject[]{statusTextItem};
    }

    public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }

    @Override
    public GameObject[] getGameObjects() {
        return gameObjects;
    }

    public void updateSize(Window window) {
        this.statusTextItem.setPosition(10f, window.getHeight() - 50f, 0);
    }
}
