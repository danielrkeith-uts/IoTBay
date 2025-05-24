package model.Enums;

public enum PaymentStatus {
    ACCEPTED(0),
    PENDING(1),
    REJECTED(2);

    private final int value;

    PaymentStatus(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public static PaymentStatus fromInt(int value) {
        switch (value) {
            case 0: return ACCEPTED;
            case 1: return PENDING;
            case 2: return REJECTED;
            default: throw new IllegalArgumentException("Invalid PaymentStatus value: " + value);
        }
    }
}
