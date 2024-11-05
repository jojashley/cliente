import jdk.jfr.Category;

import javax.swing.*;

public class Compra extends JFrame{
    public JPanel panelB;
    private JLabel tituloText;

    private String Respuesta;
    private String RespuestaZona;
    private String RespuestaCategoria;

    public Compra(String opcionSeleccionada, String zonaSeleccionada, String Categoria) {
        this.Respuesta = opcionSeleccionada;
        this.RespuestaZona = zonaSeleccionada;
        this.RespuestaCategoria = Categoria;
        System.out.println(Respuesta);
        System.out.println(RespuestaZona);
        System.out.println(RespuestaCategoria);
    }
}
