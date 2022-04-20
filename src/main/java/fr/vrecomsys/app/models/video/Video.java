package fr.vrecomsys.app.models.video;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Video implements Serializable {

    private final UUID id;
    private final String title;
    private final List<String> labels;

    public Video(UUID id, String title, List<String> labels) {
        this.id = id;
        this.title = title;
        this.labels = labels;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLabels() {
        return labels;
    }
}
