package gui.components.main.animations;

import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class RotateAnimation {
    public static void applyRotateAnimation(Node node, double durationMillis, double fromAngle, double toAngle) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(durationMillis), node);
        rotateTransition.setFromAngle(fromAngle);
        rotateTransition.setToAngle(toAngle);
        rotateTransition.play();
    }
}
