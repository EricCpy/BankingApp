package de.eric.bankingapp.banking.model;

public enum Currency {
    EUR,
    USD,
    GBP;


    public static Currency fromCode(String code) {
        for (Currency currency : values()) {
            if (code.equalsIgnoreCase(currency.toString())) {
                return currency;
            }
        }
        throw new IllegalArgumentException("No enum constant for code: " + code);
    }
}
