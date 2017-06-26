package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import java.util.ArrayList;
import java.util.List;

public class ENVCommand extends Command {
    private List<EnvKeyValue> envKeyValues = new ArrayList<>();

    public ENVCommand(String key, String value) {
        this.envKeyValues.add(new EnvKeyValue(key, value));
    }

    public ENVCommand(List<EnvKeyValue> envKeyValues) {
        this.envKeyValues = envKeyValues;
    }

    public List<EnvKeyValue> getEnvKeyValues() {
        return envKeyValues;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ENVCommand{");
        sb.append("envKeyValues=").append(envKeyValues);
        sb.append('}');
        return sb.toString();
    }
}
