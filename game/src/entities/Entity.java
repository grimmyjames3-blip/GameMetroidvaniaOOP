package entities;

import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Graphics;

public abstract class Entity {
    
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initHitbox();
    }

    protected void drawHitbox(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void initHitbox() {
        hitbox = new Rectangle2D.Float(x, y, this.width, this.height);
    }

    public void updateHitbox(float x, float y, float width, float height) {
        hitbox.x = (int) x;
        hitbox.y = (int) y;
        hitbox.width = (int) width;
        hitbox.height = (int) height;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void initHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }
}
