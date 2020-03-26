package com.github.crembluray.voodoo.game;

import com.github.crembluray.voodoo.engine.GameEngine;
import com.github.crembluray.voodoo.engine.IGameLogic;
import com.github.crembluray.voodoo.engine.Window;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            Window.WindowOptions opts = new Window.WindowOptions();
            opts.cullFace = false;
            opts.showFps = true;
            opts.compatibleProfile = true;
            opts.antialiasing = true;
            opts.frustumCulling = false;
            GameEngine gameEng = new GameEngine("VOODOO", vSync, opts, gameLogic);
            gameEng.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
