package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class Shoot {
    enum State {
        SHOOTING, TO_REMOVE
    }

    Vector2 position;
    float stateTime;
    State state;
    float speed = 5;
    float width;
    float height;
    TextureRegion frame;

    Shoot(float position,float widthBala, float heightBala){
        this.position = new Vector2(position, 16);
        state = State.SHOOTING;
        width=widthBala;
        height=heightBala;
    }

    void render(SpriteBatch batch){
        batch.draw(frame, position.x, position.y,width,height);
    }

    public void update(float delta, Assets assets) {
        stateTime += delta;
        position.y += speed;

        frame = assets.shoot.getKeyFrame(stateTime, true);
    }

    public void remove(){
        state = State.TO_REMOVE;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
