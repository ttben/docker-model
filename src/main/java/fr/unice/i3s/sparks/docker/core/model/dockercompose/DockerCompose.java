package fr.unice.i3s.sparks.docker.core.model.dockercompose;

import java.util.ArrayList;
import java.util.List;

public class DockerCompose {
    private String version;
    private String appName;
    private List<Service> serviceList = new ArrayList<>();

    public DockerCompose(String version, String appName) {
        this.version = version;
        this.appName = appName;
    }

    public void addService(Service service) {
        this.serviceList.add(service);
    }

    public String getVersion() {
        return version;
    }

    public String getAppName() {
        return appName;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }
}
