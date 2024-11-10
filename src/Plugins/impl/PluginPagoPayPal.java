package Plugins.impl;

import Plugins.PluginPago;

public class PluginPagoPayPal implements PluginPago {
    @Override
    public boolean procesarPago(String cardNumber, String cardHolder, String expiryDate) {
        // Implement PayPal payment processing logic here
        System.out.println("Processing PayPal payment for account holder: " + cardHolder);
        return true;
    }
}
