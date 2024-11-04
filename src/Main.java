import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Mapa f = new Mapa();
        f.setContentPane(new Mapa().panel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.pack();
    }
}
