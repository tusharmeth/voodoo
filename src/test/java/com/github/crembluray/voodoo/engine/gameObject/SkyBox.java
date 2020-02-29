package com.github.crembluray.voodoo.engine.gameObject;

import com.github.crembluray.voodoo.engine.model.Material;
import com.github.crembluray.voodoo.engine.model.Mesh;
import com.github.crembluray.voodoo.engine.model.Texture;
import com.github.crembluray.voodoo.engine.util.OBJLoader;

public class SkyBox extends GameObject {

    public SkyBox(String objModel, String textureFile) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        Texture skyBoxtexture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.0f));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }
}