import bagel.*;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * Streamlined Mario class that coordinates between three specialized components.
 * This class maintains Mario's state and delegates complex functionality.
 */
public class Mario {
    // Core state
    private double x, y;
    private double velocityY = 0;
    private boolean isJumping = false;
    private boolean hasHammer = false;
    private boolean hasBlaster = false;
    private boolean isFacingRight = true;
    private double width, height;

    // Specialized components
    private MarioCollisionDetector collisionDetector;
    private MarioInputHandler inputHandler;
    private MarioSpriteManager spriteManager;

    /**
     * Constructs Mario at the specified position.
     */
    public Mario(double startX, double startY) {
        this.x = startX;
        this.y = startY;

        // Initialize components
        this.collisionDetector = new MarioCollisionDetector(this);
        this.inputHandler = new MarioInputHandler(this, collisionDetector);
        this.spriteManager = new MarioSpriteManager(this);
    }

    /**
     * Main update method for Level 2 (with blaster support).
     */
    public void update(Input input, Ladder[] ladders, Platform[] platforms,
                       Hammer[] hammers, Blaster[] blasters, int[] bulletCounter) {

        // Update sprite first
        spriteManager.updateSprite();

        // Handle all input and game logic
        inputHandler.handleInput(input, ladders, platforms, hammers, blasters, bulletCounter);

        // Update sprite again in case items were collected
        spriteManager.updateSprite();

        // Draw Mario
        spriteManager.draw();
    }

    /**
     * Overloaded update method for Level 1 (no blaster support).
     */
    public void update(Input input, Ladder[] ladders, Platform[] platforms, Hammer[] hammers) {
        update(input, ladders, platforms, hammers, null, null);
    }

    // Getters and setters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getVelocityY() { return velocityY; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }

    public boolean isJumping() { return isJumping; }
    public void setJumping(boolean jumping) { this.isJumping = jumping; }

    public boolean hasHammer() { return hasHammer; }
    public void setHasHammer(boolean hasHammer) { this.hasHammer = hasHammer; }

    public boolean hasBlaster() { return hasBlaster; }
    public void setHasBlaster(boolean hasBlaster) { this.hasBlaster = hasBlaster; }

    public boolean isFacingRight() { return isFacingRight; }
    public void setFacingRight(boolean facingRight) { this.isFacingRight = facingRight; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    // Public API methods (legacy compatibility)
    public boolean holdHammer() { return hasHammer; }
    public boolean holdBlaster() { return hasBlaster; }

    public Rectangle getBoundingBox() {
        return new Rectangle(x - (width / 2), y - (height / 2), width, height);
    }

    public ArrayList<Bullet> getBullets() {
        return inputHandler.getBullets();
    }

    // Collision detection methods (delegated to collision detector)
    public boolean isTouchingLadder(Ladder ladder) {
        return collisionDetector.isTouchingLadder(ladder);
    }

    public boolean isTouchingHammer(Hammer hammer) {
        return collisionDetector.isTouchingHammer(hammer);
    }

    public boolean isTouchingBarrel(Barrel barrel) {
        return collisionDetector.isTouchingBarrel(barrel);
    }

    public boolean isTouchingMonkey(NormalMonkey monkey) {
        return collisionDetector.isTouchingMonkey(monkey);
    }

    public boolean isTouchingMonkey(IntelligentMonkey monkey) {
        return collisionDetector.isTouchingMonkey(monkey);
    }

    public boolean isTouchingBanana(Banana banana) {
        return collisionDetector.isTouchingBanana(banana);
    }

    public boolean isTouchingBlaster(Blaster blaster) {
        return collisionDetector.isTouchingBlaster(blaster);
    }

    public boolean hasReached(Donkey donkey) {
        return collisionDetector.hasReached(donkey);
    }

    public boolean jumpOver(Barrel barrel) {
        return collisionDetector.jumpOver(barrel);
    }
}