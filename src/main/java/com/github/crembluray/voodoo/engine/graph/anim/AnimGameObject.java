package com.github.crembluray.voodoo.engine.graph.anim;

import java.util.Map;
import java.util.Optional;

import com.github.crembluray.voodoo.engine.items.GameObject;
import com.github.crembluray.voodoo.engine.graph.Mesh;

public class AnimGameObject extends GameObject {

    private Map<String, Animation> animations;

    private Animation currentAnimation;

    public AnimGameObject(Mesh[] meshes, Map<String, Animation> animations) {
        super(meshes);
        this.animations = animations;
        Optional<Map.Entry<String, Animation>> entry = animations.entrySet().stream().findFirst();
        currentAnimation = entry.isPresent() ? entry.get().getValue() : null;
    }

    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }
}
