package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Controls;

public class Ship {

    enum State {
        IDLE, LEFT, RIGHT, SHOOT,DIE;
    }

    Vector2 position;

    State state;
    float stateTime;
    float speed = 5;
    int vida;
    int puntuacion;
    TextureRegion frame;
    Weapon weapon;
    boolean muerte;

    Ship(int initialPosition){
        position = new Vector2(initialPosition, 10);
        state = State.IDLE;
        stateTime = 0;
        weapon = new Weapon();
        vida=3;
        puntuacion=0;
        muerte=false;
    }


    void setFrame(Assets assets){
        switch (state){
            case IDLE:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
            case LEFT:
                frame = assets.naveleft.getKeyFrame(stateTime, true);
                break;
            case RIGHT:
                frame = assets.naveright.getKeyFrame(stateTime, true);
                break;
            case SHOOT:
                frame = assets.naveshoot.getKeyFrame(stateTime, true);
                break;
            case DIE:
                frame = assets.shipdie.getKeyFrame(stateTime);
                break;
            default:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
        }
    }

    void render(SpriteBatch batch){
        batch.draw(frame, position.x, position.y);

        weapon.render(batch);
    }

    public void update(float delta, Assets assets) {
        stateTime += delta;

        if(Controls.isLeftPressed()){
            if(vida>0){
                moveLeft();
            }

        } else if(Controls.isRightPressed()){
            if(vida>0){
                moveRight();
            }
        } else if (state != State.DIE){
            if(vida>0){
                idle();
            }

        }else {
            if (assets.shipdie.isAnimationFinished(stateTime) && state==State.DIE){
                muerte=true;
            }
        }


        if(Controls.isShootPressed()) {
            shoot();
            assets.shootSound.play();
        }

        setFrame(assets);

        weapon.update(delta, assets);
    }

    void idle(){
        state = State.IDLE;
    }

    void moveLeft(){
        position.x -= speed;
        state = State.LEFT;
    }

    void moveRight(){
        position.x += speed;
        state = State.RIGHT;
    }

    void shoot(){
        state = State.SHOOT;
        weapon.shoot(position.x +16);
    }


    public void damage() {
        if (vida >0) {
            vida--;
        }

        if(vida==0){
            kill();
        }
    }

    private void kill() {
        stateTime = 0;
        state = State.DIE;
    }
}
