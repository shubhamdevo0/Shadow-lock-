import Views.Welcome;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Run the GUI safely inside the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            Welcome w = new Welcome();
            w.welcomeScreen(); // Called exactly ONCE. No loops needed!
        });
    }
}