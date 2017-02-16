package fr.unice.i3s.sparks.docker.core.model.dockercompose;

import javafx.util.Pair;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DumpToDiskDockerCompose {
    public static void dumpToDisk(DockerCompose dockercomposeFile, Path pathToFile) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            Files.deleteIfExists(pathToFile);
            Path file = Files.createFile(pathToFile);

            stringBuilder.append("# Dockercompose file of the app ").append(dockercomposeFile.getAppName()).append("\n\n");

            stringBuilder.append("version: ").append(dockercomposeFile.getVersion()).append("\n");
            stringBuilder.append("services:\n");

            List<Service> serviceList = dockercomposeFile.getServiceList();
            for (Service service : serviceList) {
                stringBuilder.append("\t").append(service.getName()).append(":\n");
                stringBuilder.append("\t\timage: ").append(service.getImagePath()).append("\n");

                if (service.getContainerName() != null && !service.getContainerName().isEmpty()) {
                    stringBuilder.append("\t\tcontainer_name: ").append(service.getContainerName()).append("\n");
                }

                List<String> dependencies = service.getDependencies();
                if (dependencies != null && !dependencies.isEmpty()) {
                    stringBuilder.append("\t\tdepends_on:\n");

                    for (String dependency : dependencies) {
                        stringBuilder.append("\t\t\t- ").append(dependency);
                    }
                }

                List<Integer> exposedPortsOfTheService = service.getExposedPortsOfTheService();
                if (exposedPortsOfTheService != null && !exposedPortsOfTheService.isEmpty()) {
                    stringBuilder.append("\t\texpose:\n");

                    for (Integer exposedPort : exposedPortsOfTheService) {
                        stringBuilder.append("\t\t\t- \"").append(exposedPort).append("\"\n");
                    }
                }

                List<Pair<Integer, Integer>> exposedPortsOfTheApp = service.getExposedPortsOfTheApp();
                if (exposedPortsOfTheApp != null && !exposedPortsOfTheApp.isEmpty()) {
                    stringBuilder.append("\t\tports:\n");

                    for (Pair<Integer, Integer> exposedPort : exposedPortsOfTheApp) {
                        stringBuilder.append("\t\t\t- \"").append(exposedPort.getKey())
                                .append(":").append(exposedPort.getValue()).append("\"\n");
                    }
                }

                List<Pair<String, String>> volumes = service.getVolumes();
                if (volumes != null && !volumes.isEmpty()) {
                    stringBuilder.append("\t\tvolumes:\n");

                    for (Pair<String, String> exposedPort : volumes) {
                        stringBuilder.append("\t\t\t- ").append(exposedPort.getKey())
                                .append(":").append(exposedPort.getValue()).append("\n");
                    }
                }

                if (service.getCommand() != null && !service.getCommand().isEmpty()) {
                    stringBuilder.append("\t\tcommand: ").append(service.getContainerName()).append("\n");
                }
            }

            PrintWriter printWriter = new PrintWriter(file.toFile());
            printWriter.write(stringBuilder.toString());
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
