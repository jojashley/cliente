package Plugins;

public interface PluginPago {
    boolean procesarPago(String cardNumber, String cardHolder, String expiryDate);
}

