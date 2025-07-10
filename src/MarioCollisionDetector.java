import bagel.util.Rectangle;

/**
 * Handles all collision detection for Mario with game objects.
 */
public class MarioCollisionDetector {
    private final Mario mario;

    public MarioCollisionDetector(Mario mario) {
        this.mario = mario;
    }

    /**
     * Checks if Mario is touching a ladder.
     */
    public boolean isTouchingLadder(Ladder ladder) {
        Rectangle marioBounds = mario.getBoundingBox();
        return marioBounds.intersects(ladder.getBoundingBox());
    }

    /**
     * Checks if Mario is touching a hammer.
     */
    public boolean isTouchingHammer(Hammer hammer) {
        Rectangle marioBounds = mario.getBoundingBox();
        return marioBounds.intersects(hammer.getBoundingBox());
    }

    /**
     * Checks if Mario is touching a barrel.
     */
    public boolean isTouchingBarrel(Barrel barrel) {
        Rectangle marioBounds = mario.getBoundingBox();
        return marioBounds.intersects(barrel.getBoundingBox());
    }

    /**
     * Checks if Mario is touching a normal monkey.
     */
    public boolean isTouchingMonkey(NormalMonkey monkey) {
        if (monkey.isDestroyed()) return false;
        Rectangle marioBounds = mario.getBoundingBox();
        return marioBounds.intersects(monkey.getBoundingBox());
    }

    /**
     * Checks if Mario is touching an intelligent monkey.
     */
    public boolean isTouchingMonkey(IntelligentMonkey monkey) {
        if (monkey.isDestroyed()) return false;
        Rectangle marioBounds = mario.getBoundingBox();
        return marioBounds.intersects(monkey.getBoundingBox());
    }

    /**
     * Checks if Mario is touching a banana.
     */
    public boolean isTouchingBanana(Banana banana) {
        if (banana.isDestroyed()) return false;
        Rectangle marioBounds = mario.getBoundingBox();
        return marioBounds.intersects(banana.getBoundingBox());
    }

    /**
     * Checks if Mario is touching a blaster.
     */
    public boolean isTouchingBlaster(Blaster blaster) {
        if (blaster.isCollected()) return false;
        Rectangle marioBounds = mario.getBoundingBox();
        return marioBounds.intersects(blaster.getBoundingBox());
    }

    /**
     * Checks if Mario has reached Donkey Kong.
     */
    public boolean hasReached(Donkey donkey) {
        Rectangle marioBounds = mario.getBoundingBox();
        return marioBounds.intersects(donkey.getBoundingBox());
    }

    /**
     * Determines if Mario successfully jumps over a barrel.
     */
    public boolean jumpOver(Barrel barrel) {
        return !barrel.isDestroyed() &&
                mario.isJumping() &&
                Math.abs(mario.getX() - barrel.getX()) <= 1 &&
                (mario.getY() < barrel.getY()) &&
                ((mario.getY() + mario.getHeight() / 2) >=
                        (barrel.getY() + barrel.getBarrelImage().getHeight() / 2 -
                                (25) / (2 * Physics.MARIO_GRAVITY) - mario.getHeight() / 2));
    }

    /**
     * Handles platform collision detection and snapping.
     */
    public boolean handlePlatforms(Platform[] platforms) {
        boolean onPlatform = false;

        // Only snap to platform if moving downward
        if (mario.getVelocityY() >= 0) {
            for (Platform platform : platforms) {
                if (platform == null) continue;

                Rectangle marioBounds = mario.getBoundingBox();
                Rectangle platformBounds = platform.getBoundingBox();

                if (marioBounds.intersects(platformBounds)) {
                    double marioBottom = marioBounds.bottom();
                    double platformTop = platformBounds.top();

                    // Check if Mario should snap to platform
                    if (marioBottom <= platformTop + mario.getVelocityY()) {
                        // Snap Mario to platform top
                        mario.setY(platformTop - (mario.getHeight() / 2));
                        mario.setVelocityY(0);
                        mario.setJumping(false);
                        onPlatform = true;
                        break;
                    }
                }
            }
        }

        return onPlatform;
    }
}