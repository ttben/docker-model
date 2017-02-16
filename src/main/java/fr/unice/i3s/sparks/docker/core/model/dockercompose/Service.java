package fr.unice.i3s.sparks.docker.core.model.dockercompose;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private String name;
    private String imagePath;
    private String containerName;
    private List<Integer> exposedPortsOfTheService = new ArrayList<>();
    private List<Pair<Integer, Integer>>  exposedPortsOfTheApp = new ArrayList<>();
    private List<String> dependencies = new ArrayList<>();
    private List<Pair<String, String>> volumes = new ArrayList<>();
    private String command;
    private List<Pair<String, String>> environment = new ArrayList<>();

    public Service(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public Service(String name, String imagePath, String containerName, List<Integer> exposedPortsOfTheService, List<Pair<Integer, Integer>> exposedPortsOfTheApp, List<String> dependencies, List<Pair<String, String>> volumes, String command) {
        this.name = name;
        this.imagePath = imagePath;
        this.containerName = containerName;
        this.exposedPortsOfTheService = exposedPortsOfTheService;
        this.exposedPortsOfTheApp = exposedPortsOfTheApp;
        this.dependencies = dependencies;
        this.volumes = volumes;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getContainerName() {
        return containerName;
    }

    public List<Integer> getExposedPortsOfTheService() {
        return exposedPortsOfTheService;
    }

    public List<Pair<Integer, Integer>> getExposedPortsOfTheApp() {
        return exposedPortsOfTheApp;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public List<Pair<String, String>> getVolumes() {
        return volumes;
    }

    public String getCommand() {
        return command;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public void setExposedPortsOfTheService(List<Integer> exposedPortsOfTheService) {
        this.exposedPortsOfTheService = exposedPortsOfTheService;
    }

    public void setExposedPortsOfTheApp(List<Pair<Integer, Integer>> exposedPortsOfTheApp) {
        this.exposedPortsOfTheApp = exposedPortsOfTheApp;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public void setVolumes(List<Pair<String, String>> volumes) {
        this.volumes = volumes;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
