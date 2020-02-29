package com.github.crembluray.voodoo.demo;

import com.github.crembluray.voodoo.engine.GameEngine;
import com.github.crembluray.voodoo.engine.IGameLogic;

public class MainTest {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new Demo();
            GameEngine gameEng = new GameEngine("VOODOO", 600, 480, vSync, gameLogic);
            gameEng.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
