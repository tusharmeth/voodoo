package com.github.crembluray.voodoo.engine.graph.particles;

import java.util.List;

import com.github.crembluray.voodoo.engine.items.GameObject;

public interface IParticleEmitter {

    void cleanup();
    
    Particle getBaseParticle();
    
    List<GameObject> getParticles();
}
