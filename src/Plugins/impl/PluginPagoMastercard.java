package Plugins.impl;

import Plugins.PluginPago;

public class PluginPagoMastercard implements PluginPago {
    @Override
    public boolean procesarPago(String cardNumber, String cardHolder, String expiryDate) {
        // Implement Mastercard payment processing logic here
        System.out.println("Processing Mastercard payment for cardholder: " + cardHolder);
        // Assume the payment is successful for demonstration
        return true;
    }
}