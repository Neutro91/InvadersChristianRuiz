package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class Alien {

    enum State {
        LIVE, DYING, DEAD
    }

    Vector2 position;
    float stateTime;
    TextureRegion frame;
    State state;
    PowerUp powerUp;
    double powerUpIndicator;


    public Alien(int x, int y) {
        position = new Vector2(x, y);
        state = State.LIVE;
        powerUp = new PowerUp(x,y);
        powerUpIndicator = Math.random()*10;
    }

    public void render(SpriteBatch batch) {
        batch.draw(frame, position.x, position.y);

        if(state == State.DYING){
            if(powerUp.faillingPowerUp()){
                powerUp.render(batch);
            }
        }
    }

    void update(float delta, Assets assets){
        stateTime += delta;
        if(state == State.LIVE) {
            frame = assets.alien.getKeyFrame(stateTime, true);
        } else if(state == State.DYING){
            frame = assets.aliendie.getKeyFrame(stateTime, false);
        }

        if(state == State.DYING){

            if(powerUpIndicator >8.5 && !powerUp.faillingPowerUp() && !(powerUp.powerUpObteined() || powerUp.failedPowerUp())){
                powerUp.drop(position.x,position.y);
            }else if(assets.aliendie.isAnimationFinished(stateTime) && (powerUp.powerUpObteined() || powerUp.failedPowerUp())){
                state = State.DEAD;
            }else if (assets.aliendie.isAnimationFinished(stateTime) && powerUpIndicator < 8.5){
                state=State.DEAD;
            }
        }

        powerUp.update(delta,assets);
    }

    void shoot(){

    }

    public void kill() {
        state = State.DYING;
        stateTime = 0;
    }

    public boolean isAlive() {
        return state == State.LIVE;
    }

}
