import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Collision manager that handles both Level 1 and Level 2.
 * Uses the level number to determine which collision checks to perform.
 */
public class CollisionManager {
    // Score constants
    private static final int BARREL_SCORE = 100;
    private static final int BARREL_CROSS_SCORE = 30;
    private static final int MONKEY_SCORE = 100;
    private static final int INTEL_MONKEY_SCORE = 100;

    private final GameObjectManager gameObjects;
    private final ScoreManager scoreManager;

    public CollisionManager(GameObjectManager gameObjects, ScoreManager scoreManager) {
        this.gameObjects = gameObjects;
        this.scoreManager = scoreManager;
    }

    /**
     * Checks all collisions based on the current level.
     *
     * @return GameCollisionResult containing collision outcomes
     */
    public GameCollisionResult checkAllCollisions() {
        GameCollisionResult result = new GameCollisionResult();

        // Common collisions for both levels
        checkMarioBarrelCollisions(result);
        checkMarioDonkeyCollision(result);

        // Level 2 specific collisions
        if (gameObjects.isLevel2()) {
            checkMarioMonkeyCollisions(result);
            checkMarioBananaCollisions(result);
            checkBulletCollisions(result);
        }

        return result;
    }

    /**
     * Checks collisions between Mario and barrels.
     */
    private void checkMarioBarrelCollisions(GameCollisionResult result) {
        Mario mario = gameObjects.getMario();

        for (Barrel barrel : gameObjects.getBarrels()) {
            if (barrel == null) continue;

            // Check jump over
            if (mario.jumpOver(barrel)) {
                scoreManager.addScore(BARREL_CROSS_SCORE);
            }

            // Check collision
            if (!barrel.isDestroyed() && mario.isTouchingBarrel(barrel)) {
                if (!mario.holdHammer()) {
                    result.setGameOver(true);
                } else {
                    barrel.destroy();
                    scoreManager.addScore(BARREL_SCORE);
                }
            }
        }
    }

    /**
     * Checks collision between Mario and Donkey.
     */
    private void checkMarioDonkeyCollision(GameCollisionResult result) {
        Mario mario = gameObjects.getMario();
        Donkey donkey = gameObjects.getDonkey();

        // Level 1: Game over if Mario reaches Donkey without hammer
        if (gameObjects.getLevelNumber() == 1) {
            if (mario.hasReached(donkey) && !mario.holdHammer()) {
                result.setGameOver(true);
            }
        }
        // Level 2: Only game over if Mario reaches Donkey WITHOUT hammer (and Donkey not defeated)
        else if (gameObjects.getLevelNumber() == 2) {
            if (mario.hasReached(donkey) && !mario.holdHammer() && !donkey.isDefeated()) {
                result.setGameOver(true);
            }
            // Note: Mario reaching Donkey WITH hammer is handled as level completion in Level2Screen
        }
    }

    /**
     * Checks collisions between Mario and monkeys (Level 2 only).
     */
    private void checkMarioMonkeyCollisions(GameCollisionResult result) {
        Mario mario = gameObjects.getMario();

        // Check normal monkeys
        NormalMonkey[] normalMonkeys = gameObjects.getNormalMonkeys();
        if (normalMonkeys != null) {
            for (NormalMonkey monkey : normalMonkeys) {
                if (monkey != null && !monkey.isDestroyed()) {
                    if (mario.isTouchingMonkey(monkey)) {
                        if (mario.holdHammer()) {
                            monkey.destroy();
                            scoreManager.addScore(MONKEY_SCORE);
                        } else {
                            result.setGameOver(true);
                        }
                    }
                }
            }
        }

        // Check intelligent monkeys
        IntelligentMonkey[] intelligentMonkeys = gameObjects.getIntelligentMonkeys();
        if (intelligentMonkeys != null) {
            for (IntelligentMonkey monkey : intelligentMonkeys) {
                if (monkey != null && !monkey.isDestroyed()) {
                    if (mario.isTouchingMonkey(monkey)) {
                        if (mario.holdHammer()) {
                            monkey.destroy();
                            scoreManager.addScore(INTEL_MONKEY_SCORE);
                        } else {
                            result.setGameOver(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks collisions between Mario and bananas (Level 2 only).
     */
    private void checkMarioBananaCollisions(GameCollisionResult result) {
        Mario mario = gameObjects.getMario();
        ArrayList<Banana> bananas = gameObjects.getBananas();

        if (bananas != null) {
            Iterator<Banana> bananaIterator = bananas.iterator();
            while (bananaIterator.hasNext()) {
                Banana banana = bananaIterator.next();

                if (mario.isTouchingBanana(banana)) {
                    result.setGameOver(true);
                    banana.destroy();
                    bananaIterator.remove();
                }
            }
        }
    }

    /**
     * Checks all bullet collisions (Level 2 only).
     */
    private void checkBulletCollisions(GameCollisionResult result) {
        Mario mario = gameObjects.getMario();

        if (mario.getBullets() != null && !mario.getBullets().isEmpty()) {
            Iterator<Bullet> bulletIterator = mario.getBullets().iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();

                // Skip destroyed bullets
                if (bullet.isDestroyed()) {
                    bulletIterator.remove();
                    continue;
                }

                // Check Donkey collision
                if (checkBulletDonkeyCollision(bullet, result)) {
                    bulletIterator.remove();
                    continue;
                }

                // Check monkey collisions
                if (checkBulletMonkeyCollisions(bullet)) {
                    bulletIterator.remove();
                    continue;
                }
            }
        }
    }

    /**
     * Checks collision between bullet and Donkey.
     */
    private boolean checkBulletDonkeyCollision(Bullet bullet, GameCollisionResult result) {
        Donkey donkey = gameObjects.getDonkey();

        if (!donkey.isDefeated() && checkBulletCollision(bullet, donkey.getBoundingBox())) {
            boolean donkeyDefeated = donkey.takeDamage();
            bullet.destroy();

            if (donkeyDefeated) {
                result.setLevelCompleted(true);
            }
            return true;
        }
        return false;
    }

    /**
     * Checks collisions between bullet and monkeys.
     */
    private boolean checkBulletMonkeyCollisions(Bullet bullet) {
        // Check normal monkeys
        NormalMonkey[] normalMonkeys = gameObjects.getNormalMonkeys();
        if (normalMonkeys != null) {
            for (NormalMonkey monkey : normalMonkeys) {
                if (monkey != null && !monkey.isDestroyed()) {
                    if (checkBulletCollision(bullet, monkey.getBoundingBox())) {
                        monkey.destroy();
                        bullet.destroy();
                        scoreManager.addScore(MONKEY_SCORE);
                        return true;
                    }
                }
            }
        }

        // Check intelligent monkeys
        IntelligentMonkey[] intelligentMonkeys = gameObjects.getIntelligentMonkeys();
        if (intelligentMonkeys != null) {
            for (IntelligentMonkey monkey : intelligentMonkeys) {
                if (monkey != null && !monkey.isDestroyed()) {
                    if (checkBulletCollision(bullet, monkey.getBoundingBox())) {
                        monkey.destroy();
                        bullet.destroy();
                        scoreManager.addScore(INTEL_MONKEY_SCORE);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Helper method for bullet collision detection.
     */
    private boolean checkBulletCollision(Bullet bullet, Rectangle targetBox) {
        if (bullet.isDestroyed()) return false;
        return bullet.getBoundingBox().intersects(targetBox);
    }

    /**
     * Result class for collision detection outcomes.
     */
    public static class GameCollisionResult {
        private boolean isGameOver = false;
        private boolean isLevelCompleted = false;

        public boolean isGameOver() { return isGameOver; }
        public void setGameOver(boolean gameOver) { this.isGameOver = gameOver; }

        public boolean isLevelCompleted() { return isLevelCompleted; }
        public void setLevelCompleted(boolean levelCompleted) { this.isLevelCompleted = levelCompleted; }
    }
}