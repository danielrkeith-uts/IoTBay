package model.Enums;

public enum ApplicationAction {
    LOGIN(0),
    LOGOUT(1),
    REGISTER(2),
    ADD_TO_CART(3),
    CHANGED_PASSWORD(4);

    private int value;

    private ApplicationAction(int value) {
        this.value = value;
    }

    public int toInt() {
        return this.value;
    }

    public static ApplicationAction fromInt(int value) {
        switch (value) {
            case 0: return LOGIN;
            case 1: return LOGOUT;
            case 2: return REGISTER;
            case 3: return ADD_TO_CART;
            case 4: return CHANGED_PASSWORD;
            default: return null;
        }
    }
}
