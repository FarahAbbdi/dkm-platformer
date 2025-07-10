import bagel.*;
import java.util.ArrayList;

/**
 * Handles all input processing and game logic for Mario including movement, 
 * jumping, climbing, weapon collection, and shooting.
 */
public class MarioInputHandler {
    // Movement constants
    private static final double JUMP_STRENGTH = -5;
    private static final double MOVE_SPEED = 3.5;
    private static final double CLIMB_SPEED = 2;

    // Weapon constants
    private static final int BULLETS_PER_BLASTER = 5;
    private static final int COOLDOWN_FRAMES = 10;
    private static final double BULLET_OFFSET_X = 20.0;
    private static final double BULLET_OFFSET_Y = -5.0;

    private final Mario mario;
    private final MarioCollisionDetector collisionDetector;
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private int shootCooldown = 0;

    public MarioInputHandler(Mario mario, MarioCollisionDetector collisionDetector) {
        this.mario = mario;
        this.collisionDetector = collisionDetector;
    }

    /**
     * Processes all input and updates Mario's state accordingly.
     */
    public void handleInput(Input input, Ladder[] ladders, Platform[] platforms,
                            Hammer[] hammers, Blaster[] blasters, int[] bulletCounter) {

        // 1. Handle horizontal movement
        handleHorizontalMovement(input);

        // 2. Handle weapon collection
        handleHammerCollection(hammers);
        if (blasters != null) {
            handleBlasterCollection(blasters, bulletCounter);
        }

        // 3. Handle shooting
        if (bulletCounter != null) {
            handleShooting(input, bulletCounter);
        }

        // 4. Handle ladder climbing
        boolean isOnLadder = handleLadders(input, ladders);

        // 5. Apply physics
        applyPhysics(isOnLadder);

        // 6. Handle platform collisions
        boolean onPlatform = collisionDetector.handlePlatforms(platforms);

        // 7. Handle jumping
        boolean wantsToJump = input.wasPressed(Keys.SPACE);
        handleJumping(onPlatform, wantsToJump);

        // 8. Enforce boundaries
        enforceBoundaries();

        // 9. Update bullets
        updateBullets();
    }

    /**
     * Handles horizontal movement based on player input.
     */
    private void handleHorizontalMovement(Input input) {
        if (input.isDown(Keys.LEFT)) {
            mario.setX(mario.getX() - MOVE_SPEED);
            mario.setFacingRight(false);
        } else if (input.isDown(Keys.RIGHT)) {
            mario.setX(mario.getX() + MOVE_SPEED);
            mario.setFacingRight(true);
        }
    }

    /**
     * Handles Mario's interaction with ladders, allowing him to climb up or down
     * based on user input and position relative to the ladder.
     *
     * Mario can only climb if he is within the horizontal boundaries of the ladder.
     * He stops sliding unintentionally when not pressing movement keys.
     *
     * @param input   The {@link Input} object that checks for user key presses.
     * @param ladders An array of {@link Ladder} objects representing ladders in the game.
     * @return {@code true} if Mario is on a ladder, {@code false} otherwise.
     */
    private boolean handleLadders(Input input, Ladder[] ladders) {
        boolean isOnLadder = false;
        for (Ladder ladder : ladders) {
            double ladderLeft = ladder.getX() - (ladder.getWidth() / 2);
            double ladderRight = ladder.getX() + (ladder.getWidth() / 2);
            double marioRight = mario.getX() + (mario.getWidth() / 2);
            double marioBottom = mario.getY() + (mario.getHeight() / 2);
            double ladderTop = ladder.getY() - (ladder.getHeight() / 2);
            double ladderBottom = ladder.getY() + (ladder.getHeight() / 2);

            if (collisionDetector.isTouchingLadder(ladder)) {
                // Check horizontal overlap so Mario is truly on the ladder
                if (marioRight - mario.getWidth() / 2 > ladderLeft && marioRight - mario.getWidth() / 2 < ladderRight) {
                    isOnLadder = true;

                    // Stop Mario from sliding up when not moving
                    if (!input.isDown(Keys.UP) && !input.isDown(Keys.DOWN)) {
                        mario.setVelocityY(0);  // Prevent sliding inertia effect
                    }

                    // ----------- Climb UP -----------
                    if (input.isDown(Keys.UP)) {
                        mario.setY(mario.getY() - CLIMB_SPEED);
                        mario.setVelocityY(0);
                    }

                    // ----------- Climb DOWN -----------
                    if (input.isDown(Keys.DOWN)) {
                        double nextY = mario.getY() + CLIMB_SPEED;
                        double nextBottom = nextY + (mario.getHeight() / 2);

                        if (marioBottom > ladderTop && nextBottom <= ladderBottom) {
                            mario.setY(nextY);
                            mario.setVelocityY(0);
                        } else if (marioBottom == ladderBottom) {
                            mario.setVelocityY(0);
                        } else if (ladderBottom - marioBottom < CLIMB_SPEED) {
                            mario.setY(mario.getY() + ladderBottom - marioBottom);
                            mario.setVelocityY(0);
                        }
                    }
                }
            } else if (marioBottom == ladderTop && input.isDown(Keys.DOWN) && (marioRight - mario.getWidth() / 2 > ladderLeft && marioRight - mario.getWidth() / 2 < ladderRight)) {
                double nextY = mario.getY() + CLIMB_SPEED;
                mario.setY(nextY);
                mario.setVelocityY(0); // ignore gravity
                isOnLadder = true;
            } else if (marioBottom == ladderBottom && input.isDown(Keys.DOWN) && (marioRight - mario.getWidth() / 2 > ladderLeft && marioRight - mario.getWidth() / 2 < ladderRight)) {
                mario.setVelocityY(0); // ignore gravity
                isOnLadder = true;
            }
        }
        return isOnLadder;
    }

    /**
     * Applies gravity and vertical movement.
     */
    private void applyPhysics(boolean isOnLadder) {
        if (!isOnLadder) {
            mario.setVelocityY(mario.getVelocityY() + Physics.MARIO_GRAVITY);
            mario.setVelocityY(Math.min(Physics.MARIO_TERMINAL_VELOCITY, mario.getVelocityY()));
        }

        // Apply vertical movement
        mario.setY(mario.getY() + mario.getVelocityY());
    }

    /**
     * Handles jumping when Mario is on a platform and jump is requested.
     */
    private void handleJumping(boolean onPlatform, boolean wantsToJump) {
        if (onPlatform && wantsToJump) {
            mario.setVelocityY(JUMP_STRENGTH);
            mario.setJumping(true);
        }

        // Prevent falling below screen
        double bottomOfMario = mario.getY() + (mario.getHeight() / 2);
        if (bottomOfMario > ShadowDonkeyKong.getScreenHeight()) {
            mario.setY(ShadowDonkeyKong.getScreenHeight() - (mario.getHeight() / 2));
            mario.setVelocityY(0);
            mario.setJumping(false);
        }
    }

    /**
     * Enforces screen boundaries.
     */
    private void enforceBoundaries() {
        double halfW = mario.getWidth() / 2;

        // Left and right boundaries
        if (mario.getX() < halfW) {
            mario.setX(halfW);
        }

        double maxX = ShadowDonkeyKong.getScreenWidth() - halfW;
        if (mario.getX() > maxX) {
            mario.setX(maxX);
        }

        // Bottom boundary
        double bottomOfMario = mario.getY() + (mario.getHeight() / 2);
        if (bottomOfMario > ShadowDonkeyKong.getScreenHeight()) {
            mario.setY(ShadowDonkeyKong.getScreenHeight() - (mario.getHeight() / 2));
            mario.setVelocityY(0);
            mario.setJumping(false);
        }
    }

    /**
     * Handles hammer collection.
     */
    private void handleHammerCollection(Hammer[] hammers) {
        for (Hammer hammer : hammers) {
            if (hammer != null && !hammer.isCollected() && collisionDetector.isTouchingHammer(hammer)) {
                if (mario.hasBlaster()) {
                    mario.setHasBlaster(false);
                }
                mario.setHasHammer(true);
                hammer.collect();
                break;
            }
        }
    }

    /**
     * Handles blaster collection.
     */
    private void handleBlasterCollection(Blaster[] blasters, int[] bulletCounter) {
        for (Blaster blaster : blasters) {
            if (blaster != null && !blaster.isCollected() && collisionDetector.isTouchingBlaster(blaster)) {
                if (mario.hasHammer()) {
                    mario.setHasHammer(false);
                }
                mario.setHasBlaster(true);
                blaster.collect();

                if (bulletCounter != null && bulletCounter.length > 0) {
                    bulletCounter[0] += BULLETS_PER_BLASTER;
                }
                break;
            }
        }
    }

    /**
     * Handles shooting logic.
     */
    private void handleShooting(Input input, int[] bulletCounter) {
        if (shootCooldown > 0) {
            shootCooldown--;
        }

        if (mario.hasBlaster() && input.wasPressed(Keys.S) &&
                shootCooldown == 0 && bulletCounter[0] > 0) {

            double bulletX = mario.getX() + (mario.isFacingRight() ? BULLET_OFFSET_X : -BULLET_OFFSET_X);
            double bulletY = mario.getY() + BULLET_OFFSET_Y;

            bullets.add(new Bullet(bulletX, bulletY, mario.isFacingRight()));
            bulletCounter[0]--;
            shootCooldown = COOLDOWN_FRAMES;
        }
    }

    /**
     * Updates all bullets.
     */
    private void updateBullets() {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            if (bullet.update()) {
                bulletsToRemove.add(bullet);
            }
        }

        bullets.removeAll(bulletsToRemove);
    }

    /**
     * Gets all active bullets.
     */
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
}