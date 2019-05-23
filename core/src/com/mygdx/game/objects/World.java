package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Controls;
import com.mygdx.game.Timer;

import java.util.TimerTask;

public class World {
    Space space;
    Ship ship;
    AlienArmy alienArmy;
    Alien alien;
    int WORLD_WIDTH, WORLD_HEIGHT;
    BitmapFont font;
    boolean gameOver;
    PowerUp powerUp;


    public World(int WORLD_WIDTH, int WORLD_HEIGHT) {
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;
        space = new Space();
        ship = new Ship(WORLD_WIDTH / 2);
        alienArmy = new AlienArmy(WORLD_WIDTH, WORLD_HEIGHT);
        font = new BitmapFont();
        powerUp = new PowerUp(WORLD_WIDTH, WORLD_HEIGHT);
        gameOver = false;

    }

    public void render(float delta, SpriteBatch batch, Assets assets) {
        update(delta, assets);
        batch.begin();
        space.render(batch);
        ship.render(batch);
        alienArmy.render(batch);
        font.draw(batch, "VIDA  " + ship.vida, 10, 254);
        font.draw(batch, "PUNTUACION  " + ship.puntuacion, 230, 254);

            if (ship.muerte) {
                font.draw(batch, "Has Perdido", 150, 150);
                font.draw(batch, "Pulsa Enter para volver a jugar", 95, 120);
                assets.isFinished();

            }else if(alienArmy.aliens.size==0){
                font.draw(batch, "Has Ganado!!!!!", 150, 150);
                font.draw(batch, "Pulsa Enter para volver a jugar", 95, 120);
                assets.isFinished();
            }


            if((ship.muerte || alienArmy.aliens.size==0) && Controls.isEnterPressed() ){
                this.alienArmy= new AlienArmy(WORLD_WIDTH,WORLD_HEIGHT);
                this.ship=new Ship(WORLD_WIDTH/2);
            }

        batch.end();
        System.out.println(alienArmy.aliens.size);

    }

    void update(float delta, Assets assets) {
//        if(ship.vida>0) {
//            space.update(delta, assets);
//            ship.update(delta, assets);
//            alienArmy.update(delta, assets);
//            checkCollisions(assets);
//        }

        if (!ship.muerte && alienArmy.aliens.size != 0) {
            space.update(delta, assets);
            ship.update(delta, assets);
            alienArmy.update(delta, assets);
            checkCollisions(assets);


        }


    }

    private void checkCollisions(Assets assets) {
        checkNaveInWorld();
        checkShootsInWorld();
        checkShootsToAlien(assets);
        checkShootsToShip();
        checkShipObteinPowerUp();
        checkPowerUpInWorld();

    }

    private void checkShootsToShip() {
        Rectangle shipRectangle = new Rectangle(ship.position.x, ship.position.y, ship.frame.getRegionWidth(), ship.frame.getRegionHeight());

        for (AlienShoot shoot : alienArmy.shoots) {
            Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, shoot.frame.getRegionWidth(), shoot.frame.getRegionHeight());

            if (Intersector.overlaps(shootRectangle, shipRectangle)) {
                ship.damage();
                shoot.remove();
            }
        }
    }

    private void checkShootsToAlien(Assets assets) {
        for (Shoot shoot : ship.weapon.shoots) {
            //Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, shoot.frame.getRegionWidth(), shoot.frame.getRegionHeight());
            Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, ship.weapon.widthBala, ship.weapon.heightBala);

            for (Alien alien : alienArmy.aliens) {
                if (alien.isAlive()) {
                    Rectangle alienRectangle = new Rectangle(alien.position.x, alien.position.y, alien.frame.getRegionWidth(), alien.frame.getRegionHeight());

                    if (Intersector.overlaps(shootRectangle, alienRectangle)) {
                        alien.kill();
                        shoot.remove();
                        assets.aliendieSound.play();
                        ship.puntuacion += 100;

                    }
                }
            }
        }
    }

    private void checkShootsInWorld() {
        for (Shoot shoot : ship.weapon.shoots) {
            if (shoot.position.y > WORLD_HEIGHT) {
                shoot.remove();
            }
        }

        for (AlienShoot shoot : alienArmy.shoots) {
            if (shoot.position.y < 0) {
                shoot.remove();
            }
        }
    }

    private void checkShipObteinPowerUp() {
        Rectangle shipRectangle = new Rectangle(ship.position.x, ship.position.y, ship.frame.getRegionWidth(), ship.frame.getRegionHeight());

        for (Alien alien : alienArmy.aliens) {

            if (alien.powerUp.faillingPowerUp()) {

                Rectangle powerUpRectangle = new Rectangle(alien.powerUp.position.x, alien.powerUp.position.y, alien.powerUp.frame.getRegionWidth(), alien.powerUp.frame.getRegionHeight());

                if (Intersector.overlaps(powerUpRectangle, shipRectangle)) {
                    alien.powerUp.getPowerUp();

                    powerUp.tipoPowerUp = (int) (Math.random() * 3) + 1;

                    if (powerUp.tipoPowerUp == 1) {
                        ship.vida += 1;
                    } else if (powerUp.tipoPowerUp == 2) {
                        ship.puntuacion += 300;
                    } else if (powerUp.tipoPowerUp == 3) {
                        System.out.println("Entra");
                        ship.weapon.heightBala += 5;
                        ship.weapon.widthBala += 5;

                    }
                }

            }
        }
    }

    private void checkPowerUpInWorld() {
        for (Alien alien : alienArmy.aliens) {
            if (alien.powerUp.faillingPowerUp()) {
                if (alien.powerUp.position.y < 100) {
                    alien.powerUp.failedPowerUp();
                }
            }

        }
    }


    private void checkNaveInWorld() {
        if (ship.position.x > WORLD_WIDTH - 32) {
            ship.position.x = WORLD_WIDTH - 32;
        } else if (ship.position.x < 0) {
            ship.position.x = 0;
        }
    }
}
