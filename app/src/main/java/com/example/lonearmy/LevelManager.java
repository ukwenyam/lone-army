package com.example.lonearmy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

public class LevelManager {

    private String level;
    int mapWidth;
    int mapHeight;

    Player player;
    int playerIndex;

    private boolean playing;
    float gravity;

    LevelData levelData;
    ArrayList<GameObject> gameObjects;

    ArrayList<Rect> currentButtons;
    Bitmap[] bitmapsArray;

    public LevelManager(Context context, int pixelsPerMetre, int screenWidth, InputController ic, String level, float px, float py) {

        this.level = level;

        switch (level) {
            case "LevelGround":
                levelData = new LevelGround();
                break;
        }

        // To hold all our GameObjects
        gameObjects = new ArrayList<>();

        // To hold 1 of every Bitmap
        bitmapsArray = new Bitmap[25];

        // Load all the GameObjects and Bitmaps
        loadMapdata(context, pixelsPerMetre, px, py);


        // Ready to play
        // playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    public Bitmap getBitmap(char type) {
        int index;
        switch (type) {
            case '.':
                index = 0;
                break;

            case '1':
                index = 1;
                break;

            case 'p':
                index = 2;
                break;

            case 'j':
                index = 3;
                break;

            case 'd':
                index = 4;
                break;

            default:
                index = 0;
                break;
        }// End switch

        return bitmapsArray[index];
    }

    public int getBitmapIndex(char blockType) {
        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            case 'j':
                index = 3;
                break;
            case 'd':
                index = 4;
                break;
            default:
                index = 0;
                break;

        }// End switch
        return index;
    }// End getBitmapIndex()

    private void loadMapdata(Context context, int pixelsPerMetre, float px, float py) {

        char c;

        // keep track of where we load out game objects
        int currentIndex = -1;

        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();

        for (int i = 0; i < levelData.tiles.size(); i++) {
            for (int j = 0; j <
                    levelData.tiles.get(i).length(); j++) {
                c = levelData.tiles.get(i).charAt(j);
                // Don't want to load the empty spaces
                if (c != '.'){
                    currentIndex++;
                    switch (c) {
                        case '1':
                            // Add grass to the gameObjects
                            gameObjects.add(new Floor(j, i, c));
                            break;
                        case 'p':
                            // Add a player to the gameObjects
                            gameObjects.add(new Player
                                    (context, px, py,
                                            pixelsPerMetre));
                            // We want the index of the player
                            playerIndex = currentIndex;
                            // We want a reference to the player
                            player = (Player) gameObjects.get(playerIndex);
                            break;

                    }// End switch

                    // If the bitmap isn't prepared yet
                    if (bitmapsArray[getBitmapIndex(c)] == null) {

                        // Prepare it now and put it in the bitmapsArrayList
                        bitmapsArray[getBitmapIndex(c)] = gameObjects.get(currentIndex).prepareBitmap(context,
                                gameObjects.get(currentIndex).getBitmapName(),
                                pixelsPerMetre);
                    }// End if

                }// End if (c != '.'){

            }// End for j

        }// End for i

        //Prepare bitmaps for jumping and dying
        Player temp = new Player(context, 0,0, pixelsPerMetre);
        temp.setType('j');
        bitmapsArray[getBitmapIndex(temp.getType())] = temp.prepareBitmap(context,
                temp.getBitmapName(),
                pixelsPerMetre);
        temp.setType('d');
        bitmapsArray[getBitmapIndex(temp.getType())] = temp.prepareBitmap(context,
                temp.getBitmapName(),
                pixelsPerMetre);




    }// End loadMapData()

    public void switchPlayingStatus() {
        playing = !playing;
        if (playing) {
            gravity = 6;
        } else {
            gravity = 0;
        }
    }
}// End LevelManager


