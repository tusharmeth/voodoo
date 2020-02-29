package voodoo.demo;

import voodoo.engine.GameEngine;
import voodoo.engine.IGameLogic;

public class Main {

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
