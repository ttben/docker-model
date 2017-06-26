package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

public class EnvKeyValue extends Command {
    private String key;
    private String value;

    public EnvKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EnvKeyValue{");
        sb.append("key='").append(key).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}