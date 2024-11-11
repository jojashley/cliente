import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static final String PLUGIN_FOLDER = "src/Plugins";

    public static void main(String[] args) {
        // Initialize and display the Mapa window
        Mapa f = new Mapa();
        f.setContentPane(new Mapa().panel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.pack();


    }


}
