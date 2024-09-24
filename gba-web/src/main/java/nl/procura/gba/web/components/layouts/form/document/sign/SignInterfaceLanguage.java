package nl.procura.gba.web.components.layouts.form.document.sign;

public enum SignInterfaceLanguage {
    NL("nl_NL", "Nederlands"),
    EN("en_US", "Engels");

    private String value;
    private String label;

    SignInterfaceLanguage(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
