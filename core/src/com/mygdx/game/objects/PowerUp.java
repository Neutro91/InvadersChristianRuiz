package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class PowerUp {

    Vector2 position;
    float stateTime;
    State state;
    float speed = 5;
    TextureRegion frame;
    int tipoPowerUp;


    enum State{
        OBTENIDO,FALLADO,SOLTADO
    }

    PowerUp(float x, float y){
        this.position = new Vector2( x,y);
    }

    public void getPowerUp(){
        this.state=State.OBTENIDO;
    }

    public void failPowerUp(){
        this.state=State.FALLADO;
    }

    public boolean failedPowerUp(){
        return state==State.FALLADO;
    }


    public boolean faillingPowerUp(){
        return state==State.SOLTADO;
    }


    public boolean powerUpObteined(){
        return state==State.OBTENIDO;
    }

    public void drop(float x, float y){
        this.state= State.SOLTADO;
        this.position=new Vector2(x,y);
    }

    void render(SpriteBatch batch){
        batch.draw(frame, position.x, position.y);
    }

    public void update(float delta, Assets assets) {
        stateTime += delta;
        frame = assets.powerup.getKeyFrame(stateTime, true);

        if(this.state == State.SOLTADO){
            position.y--;
        }
    }


}
