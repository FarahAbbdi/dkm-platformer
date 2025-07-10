import bagel.*;
import bagel.util.Rectangle;

/**
 * Represents a normal monkey enemy that moves back and forth on platforms.
 */
public class NormalMonkey extends Monkey {
    // Sprite images
    private static final Image NORMAL_MONKEY_RIGHT_IMAGE = new Image("res/normal_monkey_right.png");
    private static final Image NORMAL_MONKEY_LEFT_IMAGE = new Image("res/normal_monkey_left.png");

    // Current sprite
    private Image currentImage;

    // Velocity for gravity
    private double velocityY = 0;

    /**
     * Creates a new normal monkey at the specified position.
     *
     * @param x The initial x-coordinate
     * @param y The initial y-coordinate
     * @param direction Initial facing direction ("left" or "right")
     * @param waypoints Array of waypoint distances for monkey movement
     */
    public NormalMonkey(double x, double y, String direction, int[] waypoints) {
        // Call super constructor with the waypoints array
        super(x, y, direction, waypoints);

        this.width = NORMAL_MONKEY_RIGHT_IMAGE.getWidth();
        this.height = NORMAL_MONKEY_RIGHT_IMAGE.getHeight();

        changeSprite();
    }

    /**
     * Updates the monkey's position and handles movement based on waypoints and platform edges.
     *
     * @param platforms The platforms to check for collisions
     */
    @Override
    public void update(Platform[] platforms) {
        if (isDestroyed) return;

        // Apply gravity
        velocityY += Physics.NORMAL_MONKEY_GRAVITY;
        velocityY = Math.min(Physics.NORMAL_MONKEY_TERMINAL_VELOCITY, velocityY);

        // Move vertically based on gravity
        y += velocityY;

        // Check platform collisions
        boolean onPlatform = false;
        Platform currentPlatform = null;

        for (Platform platform : platforms) {
            if (platform == null) continue;

            double monkeyBottom = y + height/2;
            double platformTop = platform.getY() - platform.getHeight()/2;

            // Check if monkey's feet are on the platform
            if (Math.abs(monkeyBottom - platformTop) < 5 &&
                    x + width/2 >= platform.getX() - platform.getWidth()/2 &&
                    x - width/2 <= platform.getX() + platform.getWidth()/2) {
                // Snap to platform
                y = platformTop - height/2;
                velocityY = 0;
                onPlatform = true;
                currentPlatform = platform;
                break;
            }
        }

        // Only move horizontally if on a platform
        if (onPlatform) {
            // Move horizontally
            x += velocity;

            // Update distance traveled
            distanceTravelled += Math.abs(velocity);

            // Check if reached current waypoint
            boolean reachedWaypoint = false;
            if (waypoints != null && waypoints.length > 0 &&
                    distanceTravelled >= waypoints[currentRouteIndex]) {
                reachedWaypoint = true;
            }

            // Check platform edges
            boolean atPlatformEdge = false;
            if (currentPlatform != null) {
                double platformLeft = currentPlatform.getX() - currentPlatform.getWidth()/2;
                double platformRight = currentPlatform.getX() + currentPlatform.getWidth()/2;

                // Check if the monkey would walk off the platform
                if ((isFacingRight && x + width/2 + velocity > platformRight) ||
                        (!isFacingRight && x - width/2 + velocity < platformLeft)) {
                    atPlatformEdge = true;
                }
            }

            // Check if should turn around
            if (reachedWaypoint || atPlatformEdge) {
                // Turn around
                isFacingRight = !isFacingRight;
                velocity = isFacingRight ? MONKEY_SPEED : -MONKEY_SPEED;
                changeSprite();

                // Reset distance and update waypoint index if reached waypoint
                if (reachedWaypoint && waypoints != null && waypoints.length > 0) {
                    distanceTravelled = 0;
                    currentRouteIndex = (currentRouteIndex + 1) % waypoints.length;
                }
            }
        }
    }

    /**
     * Draws the monkey on screen.
     */
    @Override
    public void draw() {
        if (!isDestroyed) {
            currentImage.draw(x, y);
        }
    }

    /**
     * Changes the monkey's sprite based on direction.
     */
    @Override
    public void changeSprite() {
        currentImage = isFacingRight ? NORMAL_MONKEY_RIGHT_IMAGE : NORMAL_MONKEY_LEFT_IMAGE;
    }
}