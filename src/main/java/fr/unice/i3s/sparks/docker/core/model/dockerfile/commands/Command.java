package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import fr.uca.i3s.sparks.composition.metamodel.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends Action {
    protected List<Tag> tags = new ArrayList<>();

    public void addTag(Tag... tag) {
        this.tags.addAll(Arrays.asList(tag));
    }

    public List<Tag> getTags() {
        return tags;
    }

    public boolean containsTag(Class<? extends Tag> tagClass) {
        for (Tag tag : tags) {
            if (tag.getClass().equals(tagClass)) {
                return true;
            }
        }
        return false;
    }
}
