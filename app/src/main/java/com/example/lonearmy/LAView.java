package com.example.lonearmy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

public class LAView extends SurfaceView implements Runnable {

    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread;

    //For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder holder;

    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;

    // Game Engine
    private LevelManager lm;
    private Viewport vp;
    InputController ic;
    SoundManager sm;

    LAView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;

        holder = getHolder();
        paint = new Paint();

        vp = new Viewport(screenWidth, screenHeight);

        sm = new SoundManager();
        sm.loadSound(context);

        // Load the first level
        loadLevel("LevelGround", 15, 2);
    } // End of constructor

    @Override
    public void run() {

        while (running) {
            startFrameTime = currentTimeMillis();

            update();
            draw();

            timeThisFrame = currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000/timeThisFrame;
            }
        }
    }

    private void update() {
        for (GameObject go : lm.gameObjects) {
            if (go.isActive()) {
                // Clip anything off-screen
                if (!vp.clipObjects(go.getWorldLocation().x,
                        go.getWorldLocation().y,
                        go.getWidth(),
                        go.getHeight())) {
                    // Set visible flag to true
                    go.setVisible(true);

                    // check collisions with player
                    int hit = lm.player.checkCollisions(go.getHitbox());
                    if (hit > 0) {
                        //collision! Now deal with different types
                        switch (go.getType()) {
                            default:// Probably a regular tile
                                if (hit == 1) {// Left or right
                                    lm.player.setxVelocity(0);
                                    lm.player.setPressingRight(false);
                                }
                                if (hit == 2) {// Feet
                                    lm.player.isFalling = false;
                                }
                                break;
                        }
                    }

                    if (lm.isPlaying()) {
                        // Run any un-clipped updates
                        go.update(fps, lm.gravity);
                    }
                } else {

                    // Set visible flag to false
                    go.setVisible(false);
                    // Now draw() can ignore them

                }
            }
        }

        if (lm.isPlaying()) {
            //Reset the players location as the centre of the viewport
            vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex)
                            .getWorldLocation().x,
                    lm.gameObjects.get(lm.playerIndex)
                            .getWorldLocation().y);}
    }// End of update()

    private void draw() {

        if (holder.getSurface().isValid()) {
            //Lock area of mem being drawn to
            canvas = holder.lockCanvas();

            //Clean last frame
            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(paint.getColor()); //////////////////////////////////////////////////////////////////////// Remember to change this is it doesn't work

            // Draw all the GameObjects
            Rect toScreen2d = new Rect();
            // Draw a layer at a time
            for (int layer = -1; layer <= 1; layer++) {
                for (GameObject go : lm.gameObjects) {
                    //Only draw if visible and this layer
                    if (go.isVisible() && go.getWorldLocation().z
                            == layer) {
                        toScreen2d.set(vp.worldToScreen
                                (go.getWorldLocation().x,
                                        go.getWorldLocation().y,
                                        go.getWidth(),
                                        go.getHeight()));

                        if (go.isAnimated()) {
                            // Get the next frame of the bitmap
                            // Rotate if necessary
                            if (go.getFacing() == 1) {
                                // Rotate
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1, 1);
                                Rect r = go.getRectToDraw(System.currentTimeMillis());
                                Bitmap b = Bitmap.createBitmap(
                                        lm.bitmapsArray[lm.getBitmapIndex(go.getType())],
                                        r.left,
                                        r.top,
                                        r.width(),
                                        r.height(),
                                        flipper,
                                        true);
                                canvas.drawBitmap(b, toScreen2d.left, toScreen2d.top, paint);
                            } else {
                                // draw it the regular way round
                                canvas.drawBitmap(
                                        lm.bitmapsArray[lm.getBitmapIndex(go.getType())],
                                        go.getRectToDraw(System.currentTimeMillis()),
                                        toScreen2d, paint);
                            }
                        } else { // Just draw the whole bitmap
                            canvas.drawBitmap(
                                    lm.bitmapsArray[lm.getBitmapIndex(go.getType())],
                                    toScreen2d.left,
                                    toScreen2d.top, paint);
                        }
//                        // Draw the appropriate bitmap
//                        canvas.drawBitmap(
//                                lm.bitmapsArray
//                                        [lm.getBitmapIndex(go.getType())],
//                                toScreen2d.left,
//                                toScreen2d.top, paint);
                    }
                }

            }

            // Text for debugging
            if (debugging) {
                paint.setTextSize(16);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                canvas.drawText("fps:" + fps, 10, 60, paint);

                canvas.drawText("num objects:" +
                        lm.gameObjects.size(), 10, 80, paint);

                canvas.drawText("num clipped:" +
                        vp.getNumClipped(), 10, 100, paint);

                canvas.drawText("playerX:" +
                                lm.gameObjects.get(lm.playerIndex).
                                        getWorldLocation().x,
                        10, 120, paint);
                canvas.drawText("playerY:" +
                        lm.gameObjects.get(lm.playerIndex).getWorldLocation().y,
                        10, 140, paint);

                canvas.drawText("Gravity:" +
                        lm.gravity, 10, 160, paint);

                canvas.drawText("X velocity:" +
                                lm.gameObjects.get(lm.playerIndex).getxVelocity(),
                        10, 180, paint);

                canvas.drawText("Y velocity:" +
                                lm.gameObjects.get(lm.playerIndex).getyVelocity(),
                        10, 200, paint);
                //for reset the number of clipped objects each frame
                vp.resetNumClipped();
            }// End if(debugging)

            //draw buttons
            paint.setColor(Color.argb(80, 255, 255, 255));
            ArrayList<Rect> buttonsToDraw;
            buttonsToDraw = ic.getButtons();
            for (Rect rect : buttonsToDraw) {
                RectF rf = new RectF(rect.left, rect.top, rect.right, rect.bottom);
                canvas.drawRoundRect(rf, 15f, 15f, paint);
            }

            //draw paused text
            if (!this.lm.isPlaying()) {
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(120);
                canvas.drawText("Paused", vp.getScreenWidth() / 2,
                        vp.getScreenHeight() / 2, paint);
            }
            //Unlock and draw scene
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("error", "failed to pause thread");
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void loadLevel(String level, float px, float py) {


        lm = null;
        // Create a new LevelManager
        // Pass in a Context, screen details, level name
        // and player location
        lm = new LevelManager(context,
                vp.getPixelsPerMetreX(),
                vp.getScreenWidth(),
                ic, level, px, py);

        ic = new InputController(vp.getScreenWidth(),
                vp.getScreenHeight());
        // Set the players location as the world centre
        vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex)
                        .getWorldLocation().x,
                lm.gameObjects.get(lm.playerIndex)
                        .getWorldLocation().y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (lm != null) {
            ic.handleInput(motionEvent, lm, sm, vp);
        }
        //invalidate();
        return true;
    }

} //End of LAView class
