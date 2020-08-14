package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture snakehead;
    private Texture snakebody;

    private float MOVE_TIME = 0.25F;
    private float timer = MOVE_TIME;

    private static final int MOVEMENT = 30 ;
    private int snakeX = 0,snakeY = 0;

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private int snakeDirection = RIGHT;


    private Array<BodyPart> bodyParts = new Array<BodyPart>();
    private int snakeXBeforeUpdate = 0, snakeYBeforeUpdate = 0;

    private Texture apple, appleS, appleP;
    private boolean apple1Available = false, apple2Available = false, apple3Available = false, appleSAvailable = false, applePAvailable = false;
    private int appleX1, appleY1, appleX2, appleY2, appleX3, appleY3, appleSX, appleSY, applePX, applePY;


    private Texture enemy1, enemy2, enemy3;
    private boolean enemyAvailable1 = false, enemyAvailable2 = false, enemyAvailable3 = false;
    private int enemyX1, enemyY1, enemyX2, enemyY2, enemyX3, enemyY3;
    private static final float ENEMY_TIME = 1.0F;
    private float enemyTimer = ENEMY_TIME;

    private int enemyDirection1 = RIGHT, enemyDirection2 = RIGHT, enemyDirection3 = RIGHT;

    private float Timer = 0F;
    private boolean killenemy = false;

    private boolean hasHit = false;

    private int score = 0;
    private int scoreType = 0;

    private BitmapFont bitmapFont;

    private static final String GAME_OVER_TEXT = "Game Over. Tap Enter To Restart!";

    private static final float AppleS_TIME = 10.0F;
    private float appleS_timer = AppleS_TIME;

    private static final float AppleP_TIME = 18.0F;
    private float appleP_timer = AppleP_TIME;

    private static final float Speed_TIME = 5.0F;
    private float speed_timer = Speed_TIME;

    private static final float Disappear_TIME = 10.0F;
    private float disappear_timer = Disappear_TIME;

    private boolean Speedup = false;
    private boolean disappear = false;

    private int Counter = 180;
    private float Time = 1.0f;



    Music music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
    Sound deadsound = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
    Sound applesound = Gdx.audio.newSound(Gdx.files.internal("apple.mp3"));
    Sound killsound = Gdx.audio.newSound(Gdx.files.internal("kill.mp3"));

    private Texture background;

    public void show(){
        bitmapFont = new BitmapFont();
        batch = new SpriteBatch();
        snakehead = new Texture(Gdx.files.internal("snakehead3.png"));
        snakebody = new Texture(Gdx.files.internal("snakebody3.png"));
        apple = new Texture(Gdx.files.internal("apple.png"));
        appleS = new Texture(Gdx.files.internal("appleS.png"));
        appleP = new Texture(Gdx.files.internal("appleP.png"));
        enemy1 = new Texture(Gdx.files.internal("purple_ghost.png"));
        enemy2 = new Texture(Gdx.files.internal("green_ghost.png"));
        enemy3 = new Texture(Gdx.files.internal("orange_ghost.png"));

        background = new Texture(Gdx.files.internal("background.jpg"));
        bitmapFont.setColor(Color.BLACK);
        bitmapFont.getData().scale(0.5f);


    }

    public void render(float delta) {
        //MOVE_TIME = 0.5F;
        switch (state) {
            case PLAYING: {
                timer -= delta;
                if (timer <= 0) {
                    timer = MOVE_TIME;
                    snakeXBeforeUpdate = snakeX;
                    snakeYBeforeUpdate = snakeY;
                    movesnake();
                    checkForOutOfBound();
                    updateBodyPartsPosition();
                    checkSnakeBodyCollision();
                }
                music.play();

                enemyTimer -= delta;
                if (enemyTimer <= 0) {
                    enemyTimer = ENEMY_TIME;
                    moveEnemy1();
                    moveEnemy2();
                    moveEnemy3();
                    enemyX1 = XcheckForBound(enemyX1);
                    enemyY1 = YcheckForBound(enemyY1);
                    enemyX2 = XcheckForBound(enemyX2);
                    enemyY2 = YcheckForBound(enemyY2);
                    enemyX3 = XcheckForBound(enemyX3);
                    enemyY3 = YcheckForBound(enemyY3);
                }

                Time -= delta;
                if (Time <= 0) {
                    Time = 1;
                    Counter--;
                }

                checkAndPlaceEnemy1();
                checkAndPlaceEnemy2();
                checkAndPlaceEnemy3();

                appleS_timer -= delta;
                if ((appleS_timer <= 5)&&(appleS_timer > 0)) {
                    PlaceAppleS();
                }
                else if(appleS_timer <= 0){
                    appleSAvailable = false;
                    appleS_timer = AppleS_TIME;
                }

                appleP_timer -= delta;
                if ((appleP_timer <= 9)&&(appleP_timer > 0)) {
                    PlaceAppleP();
                }
                else if(appleP_timer <= 0){
                    applePAvailable = false;
                    appleP_timer = AppleP_TIME;
                }

                SpeedUp(delta);
                Disappear(delta);

                queryInput();

                enemyDirection1 = MathUtils.random(3);
                enemyDirection2 = MathUtils.random(3);
                enemyDirection3 = MathUtils.random(3);

                checkAppleCollision();
                checkAndPlaceApple();

                checkCollision();

            }
            break;

            case GAME_OVER: {
                music.stop();
                checkForRestart();
            }
            break;

        }
            Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            clearScreen();
            draw();


    }

    public void checkForOutOfBound(){
        if(snakeX >= Gdx.graphics.getWidth()){
            snakeX = 0;
        }
        if(snakeX < 0){
            snakeX = Gdx.graphics.getWidth() - MOVEMENT;
        }
        if(snakeY >= Gdx.graphics.getHeight()){
            snakeY = 0;
        }
        if(snakeY < 0){
            snakeY = Gdx.graphics.getHeight() - MOVEMENT;
        }
    }

    public int XcheckForBound(int X){
        if(X >= Gdx.graphics.getWidth()){
            X = 0;
        }
        if(X < 0){
            X = Gdx.graphics.getWidth() - MOVEMENT;;
        }
        return X;
    }
    public int YcheckForBound(int Y){
        if(Y >= Gdx.graphics.getHeight()){
            Y = 0;
        }
        if(Y < 0){
            Y = Gdx.graphics.getHeight() - MOVEMENT;
        }
        return Y;
    }

    public void movesnake(){
        switch (snakeDirection){
            case RIGHT:{
                snakeX += MOVEMENT;
                return;
            }

            case LEFT:{
                snakeX -= MOVEMENT;
                return;
            }

            case UP:{
                snakeY += MOVEMENT;
                return;
            }

            case DOWN:{
                snakeY -= MOVEMENT;
                return;
            }
        }
    }

    public void moveEnemy1(){
        switch (enemyDirection1){
            case RIGHT:{
                enemyX1 += MOVEMENT;
                return;
            }

            case LEFT:{
                enemyX1 -= MOVEMENT;
                return;
            }

            case UP:{
                enemyY1 += MOVEMENT;
                return;
            }

            case DOWN:{
                enemyY1 -= MOVEMENT;
                return;
            }
        }
    }

    public void moveEnemy2(){
        switch (enemyDirection2){
            case RIGHT:{
                enemyX2 += MOVEMENT;
                return;
            }

            case LEFT:{
                enemyX2 -= MOVEMENT;
                return;
            }

            case UP:{
                enemyY2 += MOVEMENT;
                return;
            }

            case DOWN:{
                enemyY2 -= MOVEMENT;
                return;
            }
        }
    }

    public void moveEnemy3(){
        switch (enemyDirection3){
            case RIGHT:{
                enemyX3 += MOVEMENT;
                return;
            }

            case LEFT:{
                enemyX3 -= MOVEMENT;
                return;
            }

            case UP:{
                enemyY3 += MOVEMENT;
                return;
            }

            case DOWN:{
                enemyY3 -= MOVEMENT;
                return;
            }
        }
    }


    private void queryInput(){
        boolean LPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean RPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean UPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean DPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if(LPressed) snakeDirection = LEFT;
        if(RPressed) snakeDirection = RIGHT;
        if(UPressed) snakeDirection = UP;
        if(DPressed) snakeDirection = DOWN;

    }

    public class BodyPart {
        private int x, y;
        private Texture texture;

        public BodyPart(Texture texture) {
            this.texture = texture;
        }

        public void updateBodyPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void draw(Batch batch) {
            if (!(x == snakeX && y == snakeY))
                batch.draw(texture, x, y);
        }
    }

    private void updateBodyPartsPosition() {
        if (bodyParts.size > 0) {
            BodyPart bodyPart = bodyParts.removeIndex(0);
            bodyPart.updateBodyPosition(snakeXBeforeUpdate, snakeYBeforeUpdate);
            bodyParts.add(bodyPart);
        }
    }

    private void checkAndPlaceApple(){
        if(!apple1Available){
            do {
                appleX1 = MathUtils.random(Gdx.graphics.getWidth() / MOVEMENT - 1) * MOVEMENT;
                appleY1 = MathUtils.random(Gdx.graphics.getHeight() / MOVEMENT - 1) * MOVEMENT;
                apple1Available = true;
            }while(appleX1 == snakeX && appleY1 == snakeY);
        }
        if(!apple2Available){
            do {
                appleX2 = MathUtils.random(Gdx.graphics.getWidth() / MOVEMENT - 1) * MOVEMENT;
                appleY2 = MathUtils.random(Gdx.graphics.getHeight() / MOVEMENT - 1) * MOVEMENT;
                apple2Available = true;
            }while(appleX2 == snakeX && appleY2 == snakeY);
        }
        if(!apple3Available){
            do {
                appleX3 = MathUtils.random(Gdx.graphics.getWidth() / MOVEMENT - 1) * MOVEMENT;
                appleY3 = MathUtils.random(Gdx.graphics.getHeight() / MOVEMENT - 1) * MOVEMENT;
                apple3Available = true;
            }while(appleX3 == snakeX && appleY3 == snakeY);
        }
    }

    private void PlaceAppleS() {
        if (!appleSAvailable) {
            do {
                appleSX = MathUtils.random(Gdx.graphics.getWidth() / MOVEMENT - 1) * MOVEMENT;
                appleSY = MathUtils.random(Gdx.graphics.getHeight() / MOVEMENT - 1) * MOVEMENT;
                appleSAvailable = true;
            } while (appleSX == snakeX && appleSY == snakeY);
        }
    }

    private void PlaceAppleP(){
        if(!applePAvailable){
            do {
                applePX = MathUtils.random(Gdx.graphics.getWidth() / MOVEMENT - 1) * MOVEMENT;
                applePY = MathUtils.random(Gdx.graphics.getHeight() / MOVEMENT - 1) * MOVEMENT;
                applePAvailable = true;
            }while(applePX == snakeX && applePY == snakeY);
        }

    }

    private void checkAndPlaceEnemy1() {
        if (!enemyAvailable1){
            enemyX1 = MathUtils.random(Gdx.graphics.getWidth() / MOVEMENT - 1) * MOVEMENT;
            enemyY1 = MathUtils.random(Gdx.graphics.getHeight() / MOVEMENT - 1) * MOVEMENT;
            enemyAvailable1 = true;
        }
    }

    private void checkAndPlaceEnemy2() {
        if (!enemyAvailable2){
            enemyX2 = MathUtils.random(Gdx.graphics.getWidth() / MOVEMENT - 1) * MOVEMENT;
            enemyY2 = MathUtils.random(Gdx.graphics.getHeight() / MOVEMENT - 1) * MOVEMENT;
            enemyAvailable2 = true;
        }
    }

    private void checkAndPlaceEnemy3() {
        if (!enemyAvailable3){
            enemyX3 = MathUtils.random(Gdx.graphics.getWidth() / MOVEMENT - 1) * MOVEMENT;
            enemyY3 = MathUtils.random(Gdx.graphics.getHeight() / MOVEMENT - 1) * MOVEMENT;
            enemyAvailable3 = true;
        }
    }

    private void clearScreen(){
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private GlyphLayout layout = new GlyphLayout();
    private GlyphLayout layoutscore = new GlyphLayout();
    private GlyphLayout layouttime = new GlyphLayout();


    private void draw(){
        batch.begin();
        batch.draw(background,0,0);
        batch.draw(snakehead, snakeX, snakeY);
        for (BodyPart bodyPart : bodyParts) {
            bodyPart.draw(batch);
        }
        if(apple1Available){
            batch.draw(apple, appleX1, appleY1);
        }
        if(apple2Available){
            batch.draw(apple, appleX2, appleY2);
        }
        if(apple3Available){
            batch.draw(apple, appleX3, appleY3);
        }
        if(appleSAvailable){
            batch.draw(appleS, appleSX, appleSY);
        }

        if(applePAvailable){
            batch.draw(appleP, applePX, applePY);
        }

        if(enemyAvailable1){
            batch.draw(enemy1, enemyX1, enemyY1);
        }
        if(enemyAvailable2){
            batch.draw(enemy2, enemyX2, enemyY2);
        }
        if(enemyAvailable3){
            batch.draw(enemy3, enemyX3, enemyY3);
        }

        if (state == STATE.GAME_OVER) {
            layout.setText(bitmapFont, GAME_OVER_TEXT);
            bitmapFont.draw(batch, GAME_OVER_TEXT,(Gdx.graphics.getWidth() - layout.width) / 2,(Gdx.graphics.getHeight() - layout.height) / 2);
        }


        drawScore();
        drawTime();
        batch.end();
    }

    private void checkAppleCollision(){
        if(apple1Available && appleX1 == snakeX && appleY1 == snakeY){
            scoreType = 1;
            addToScore();
            addToBody();
            apple1Available = false;
            applesound.play();
        }
        if(apple2Available && appleX2 == snakeX && appleY2 == snakeY){
            scoreType = 1;
            addToScore();
            addToBody();
            apple2Available = false;
            applesound.play();
        }
        if(apple3Available && appleX3 == snakeX && appleY3 == snakeY){
            scoreType = 1;
            addToScore();
            addToBody();
            apple3Available = false;
            applesound.play();
        }

        if(appleSAvailable && appleSX == snakeX && appleSY == snakeY){
            scoreType = 2;
            addToScore();
            addToBody();
            Speedup = true;
            appleSAvailable = false;
            applesound.play();
        }

        if(applePAvailable && applePX == snakeX && applePY == snakeY){
            scoreType = 2;
            addToScore();
            addToBody();
            disappear = true;
            applePAvailable = false;
            applesound.play();
        }
    }

    private void SpeedUp(float delta){
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)&&(bodyParts.size > 0)&&(score>=100)){
                MOVE_TIME = 0.1F;
                Timer += delta;
                if(Timer >= 0.5F) {
                    bodyParts.removeIndex(0);
                    score -= 100;
                    Timer = 0F;
                }
                killenemy = true;
            }
        else if((Speedup == true)&&(speed_timer >= 0)){
            MOVE_TIME = 0.1F;
            speed_timer -= delta;
            killenemy = true;
        }
        else{
            MOVE_TIME = 0.25F;
            killenemy = false;
            Speedup = false;
            speed_timer = Speed_TIME;
        }
    }

    private void Disappear(float delta){
        if((disappear == true)&&(disappear_timer >= 0)){
            disappear_timer -= delta;
            snakehead = new Texture(Gdx.files.internal("snakeheadw3.png"));
        }
        else {
            snakehead = new Texture(Gdx.files.internal("snakehead3.png"));
            disappear_timer = Disappear_TIME;
            disappear = false;
        }
    }

    private void checkCollision(){
        if(((hasHit||((enemyX1 == snakeX && enemyY1 == snakeY)||(enemyX2 == snakeX && enemyY2 == snakeY)||(enemyX3 == snakeX && enemyY3 == snakeY))&&!killenemy)&&!disappear)||Counter==0){
            state = STATE.GAME_OVER;
            deadsound.play();
        }
        else if((enemyX1 == snakeX && enemyY1 == snakeY) && killenemy) {
            enemyAvailable1 = false;
            scoreType = 3;
            addToScore();
            addToBody();
            killsound.play();
            checkAndPlaceEnemy1();
        }
        else if((enemyX2 == snakeX && enemyY2 == snakeY) && killenemy) {
            enemyAvailable2 = false;
            scoreType = 3;
            addToScore();
            addToBody();
            killsound.play();
            checkAndPlaceEnemy2();
        }
        else if((enemyX3 == snakeX && enemyY3 == snakeY) && killenemy) {
            enemyAvailable3 = false;
            scoreType = 3;
            addToScore();
            addToBody();
            killsound.play();
            checkAndPlaceEnemy3();
        }
    }

    private void checkSnakeBodyCollision() {
        for (BodyPart bodyPart : bodyParts) {
            if (((bodyPart.x == enemyX1 && bodyPart.y == enemyY1)||(bodyPart.x == enemyX2 && bodyPart.y == enemyY2)||(bodyPart.x == enemyX3 && bodyPart.y == enemyY3))&&!disappear&&!killenemy) {
                hasHit = true;
            }
            else if(((bodyPart.x == enemyX1 && bodyPart.y == enemyY1)&&killenemy)) {
                enemyAvailable1 = false;
                scoreType = 3;
                addToScore();
                addToBody();
                killsound.play();
                checkAndPlaceEnemy1();
            }
            else if(((bodyPart.x == enemyX2 && bodyPart.y == enemyY2)&&killenemy)) {
                enemyAvailable2 = false;
                scoreType = 3;
                addToScore();
                addToBody();
                killsound.play();
                checkAndPlaceEnemy2();
            }
            else if(((bodyPart.x == enemyX3 && bodyPart.y == enemyY3)&&killenemy)){
                enemyAvailable3 = false;
                scoreType = 3;
                addToScore();
                addToBody();
                killsound.play();
                checkAndPlaceEnemy3();
            }
        }
    }

    private void checkForRestart() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER))
            doRestart();
    }

    private void doRestart() {
        state = STATE.PLAYING;
        bodyParts.clear();
        snakeDirection = RIGHT;
        timer = MOVE_TIME;
        enemyTimer = ENEMY_TIME;
        appleS_timer = AppleS_TIME;
        appleP_timer = AppleP_TIME;
        speed_timer = Speed_TIME;
        Counter = 180;
        snakeX = 0;
        snakeY = 0;
        snakeXBeforeUpdate = 0;
        snakeYBeforeUpdate = 0;
        apple1Available = false;
        apple2Available = false;
        apple3Available = false;
        appleSAvailable = false;
        applePAvailable = false;
        enemyAvailable1 = false;
        enemyAvailable2 = false;
        enemyAvailable3 = false;
        Speedup = false;
        hasHit = false;
        score = 0;
    }

    private enum STATE {
        PLAYING, GAME_OVER
    }

    private STATE state = STATE.PLAYING;

    private void addToScore() {
        if(scoreType == 1)
            score += 100;
        if(scoreType == 2)
            score += 200;
        if(scoreType ==3)
            score += 300;
    }

    private void addToBody(){
        for(int i = 0; i < scoreType; i++){
            BodyPart bodyPart = new BodyPart(snakebody);
            bodyPart.updateBodyPosition(snakeX, snakeY);
            bodyParts.insert(0, bodyPart);
        }
        scoreType = 0;
    }

    private void drawScore() {
        String scoreAsString = "Score: " + Integer.toString(score);
        layoutscore.setText(bitmapFont, scoreAsString);
        bitmapFont.draw(batch, scoreAsString, layouttime.width+10, Gdx.graphics.getHeight()-10);
    }

    public void drawTime() {
        String timeAsString = "Time: " + Integer.toString(Counter)+" s";
        layouttime.setText(bitmapFont, timeAsString);
        bitmapFont.draw(batch, timeAsString, 0, Gdx.graphics.getHeight()-10);
    }
}


