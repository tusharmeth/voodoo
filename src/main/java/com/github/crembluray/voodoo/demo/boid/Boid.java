package com.github.crembluray.voodoo.demo.boid;

import com.github.crembluray.voodoo.engine.gameObject.GameObject;
import com.github.crembluray.voodoo.engine.model.Mesh;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Boid extends GameObject {

    private static List<Boid> boids = new ArrayList<>(0);
    private float maxSpeed = 0.1f;
    private Vector3f velocity;
    private Vector3f acceleration;
    private final Boundary boundary;

    public Boid(Mesh mesh, Boundary boundary, Vector3f position) {
        this.boundary = boundary;
        this.setPosition(position.x, position.y, position.z);
        this.velocity = new Vector3f(
                (float)(Math.random() - 0.5),
                (float)(Math.random() - 0.5),
                (float)(Math.random() - 0.5)
        );
        this.acceleration = new Vector3f(0);
        setMesh(mesh);
        boids.add(this);
    }

    public void update() {
        flock();
        move();
        rotate();

        // Check if boid is inside boundary
        if(!boundary.isPointInsideBoundary(getPosition())) {
            if(getPosition().x < boundary.getMin().x + boundary.getPosition().x)
                setPosition(boundary.getMax().x + boundary.getPosition().x, getPosition().y, getPosition().z);
            if(getPosition().y < boundary.getMin().y + boundary.getPosition().y)
                setPosition(getPosition().x, boundary.getMax().y + boundary.getPosition().y, getPosition().z);
            if(getPosition().z < boundary.getMin().z + boundary.getPosition().z)
                setPosition(getPosition().z, getPosition().y, boundary.getMax().z + boundary.getPosition().z);
            if(getPosition().x > boundary.getMax().x + boundary.getPosition().x)
                setPosition(boundary.getMin().x + boundary.getPosition().x, getPosition().y, getPosition().z);
            if(getPosition().y > boundary.getMax().y + boundary.getPosition().y)
                setPosition(getPosition().x, boundary.getMin().y + boundary.getPosition().y, getPosition().z);
            if(getPosition().z > boundary.getMax().z + boundary.getPosition().z)
                setPosition(getPosition().x, getPosition().y, boundary.getMin().z + boundary.getPosition().z);
        }
    }

    // Flock the boids
    private void flock() {
        Vector3f alignment = new Vector3f();
        Vector3f cohesion = new Vector3f();
        Vector3f separation = new Vector3f();

        for(Boid boid : boids) {
            float distance = getPosition().distance(boid.getPosition());
            if(distance < 8f && boid != this) {
                // Align the boids
                alignment.add(boid.getVelocity());
                alignment.normalize();

                // Move the boids together
                if(distance < 6f) {
                    cohesion.add(boid.getPosition().sub(getPosition(), new Vector3f()));
                    cohesion.normalize();
                }

                // Keep the boids from getting to close
                if(distance < 4f) {
                    separation.add(getPosition().sub(boid.getPosition(), new Vector3f()));
                    separation.normalize();
                }
                limitVector(getAcceleration(), new Vector3f(maxSpeed));
            }
        }
        getAcceleration().add(alignment);
        getAcceleration().add(cohesion);
        getAcceleration().add(separation);
        getAcceleration().normalize();

        // Make the acceleration smaller
        getAcceleration().div(300);
    }

    // Update boid velocity
    private void move() {
        getVelocity().add(getAcceleration());
        limitVector(getVelocity(), new Vector3f(maxSpeed));
        getPosition().add(getVelocity());
    }

    // Update the boid rotation BROKEN // TODO
    private void rotate() {
        setRotation(
                getVelocity().x * 200,
                getVelocity().y * 200,
                getVelocity().z * 200
        );
    }

    //Limit the vector size
    private void limitVector(Vector3f vector, Vector3f limit) {
        if (vector.x > limit.x)
            vector.x = limit.x;
        else if (vector.x < -limit.x)
            vector.x = -limit.x;
        if (vector.y > limit.y)
            vector.y = limit.y;
        else if (vector.y < -limit.y)
            vector.y = -limit.y;
        if (vector.z > limit.z)
            vector.z = limit.z;
        else if (vector.z < -limit.z)
            vector.z = -limit.z;
    }

    public static List<Boid> getBoids() {
        return boids;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public Vector3f getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector3f acceleration) {
        this.acceleration = acceleration;
    }
}
