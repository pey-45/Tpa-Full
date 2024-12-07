package tpafull.data;

public enum TpaMode {
    TPA("Tpa"),
    TPAHERE("TpaHere");

    private final String string;

    TpaMode(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
