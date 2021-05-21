package com.example.lonearmy;

import android.graphics.PointF;

public class Drone extends GameObject {

    final float MAX_X_VELOCITY = 3;
    final float MAX_Y_VELOCITY = 3;

    long lastWaypointSetTime;
    PointF currentWaypoint;

    public Drone(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 1;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);

        setBitmapName("drone");
        setMoves(true);

        setActive(true);
        setVisible(true);

        currentWaypoint = new PointF();

        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
        setFacing(RIGHT);
    }

    @Override
    public void update(long fps, float gravity) {
        if (currentWaypoint.x > getWorldLocation().x) {
            setxVelocity(MAX_X_VELOCITY);
        } else if (currentWaypoint.x < getWorldLocation().x) {
            setxVelocity(-MAX_X_VELOCITY);
        } else {
            setxVelocity(0);
        }

        if (currentWaypoint.y >= getWorldLocation().y) {
            setyVelocity(MAX_Y_VELOCITY);
        } else if (currentWaypoint.y < getWorldLocation().y) {
            setyVelocity(-MAX_Y_VELOCITY);
        } else {
            setyVelocity(0);
        }

        move(fps);

        //updating drone hitbox to new location
        setRectHitbox();
    }

    public void setWaypoint(Vector2D playerLoc) {
        if (System.currentTimeMillis() > lastWaypointSetTime + 2000) { // check if 2 seconds has ellapsed since last update
            lastWaypointSetTime = System.currentTimeMillis();
            currentWaypoint.x = playerLoc.x;
            currentWaypoint.y = playerLoc.y;
        }

    }

}
