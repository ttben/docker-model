package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.EnvKeyValue;

import java.util.ArrayList;
import java.util.List;

public class ENVAutomata {

    private List<EnvKeyValue> envKeyValues = new ArrayList<>();

    private String currentKey = "";
    private String currentValue = "";

    public List<EnvKeyValue> getEnvKeyValues() {
        return envKeyValues;
    }

    public static void main(String[] args) {
        ENVAutomata a = new ENVAutomata();

        char[] characters = "a=f w=\"a\\ c\" w=2".toCharArray();
        a.handle(characters, 0);
    }

    public void handle(char[] characters, Integer index) {
        index = extractKey(characters, index);
        index++;    //  Skip '='
        index = extractValue(characters, index);
        envKeyValues.add(new EnvKeyValue(currentKey, currentValue));
        if (index < characters.length) {    // Fixme this test should be useless. Some path in the code leads to this addition.. (IOoB)
            if (characters[index] == ' ') {
                currentKey = "";
                currentValue = "";
                index++;
                handle(characters, index);
            }
        }
    }


    private Integer extractKey(char[] characters, Integer index) {
        while (characters[index] != '=' && (Character.isLetter(characters[index]) || Character.isDigit(characters[index]))) {
            currentKey += characters[index];
            index++;
        }

        return index;
    }

    private Integer extractValue(char[] characters, Integer index) {
        if (characters[index] == '"') {
            index = extractValueWithQuotes(characters, index);
            index++;
        } else {
            index = extractValueWithoutQuotes(characters, index);
        }

        return index;
    }

    private Integer extractValueWithQuotes(char[] characters, Integer index) {
        index++;    // Skip '"'

        while (characters[index] != '"') {
            currentValue += characters[index];
            if (index != characters.length - 1) {
                index++;
            } else {
                break;
            }
        }

        return index;
    }

    private Integer extractValueWithoutQuotes(char[] characters, Integer index) {
        Character lastCharacter = null;
        // while (characters[index] != '=' && (Character.isLetter(characters[index]) || Character.isDigit(characters[index]) || characters[index] == '\\' || characters[index] == ' ')) {
        while (true) {
            if (characters[index] == ' ' && lastCharacter != null && lastCharacter != '\\') {
                break;
            }
            lastCharacter = characters[index];

            currentValue += characters[index];
            if (index != characters.length - 1) {
                index++;
            } else {
                break;
            }
        }
        return index;
    }
}
