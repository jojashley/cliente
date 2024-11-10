package Plugins.impl;

import Plugins.PluginPago;

public class PluginPagoVisa implements PluginPago {
    @Override
    public boolean procesarPago(String cardNumber, String cardHolder, String expiryDate) {
        // Implement Visa payment processing logic here
        System.out.println("Processing Visa payment for cardholder: " + cardHolder);
        return true;
    }
}
