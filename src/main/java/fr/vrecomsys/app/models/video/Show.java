package fr.vrecomsys.app.models.video;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Show
        extends Video implements Serializable {
    public Show(UUID id, String title, List<String> labels, Integer number_of_episodes) {
        super(id, title, labels);
        this.number_of_episodes = number_of_episodes;
    }

    private final Integer number_of_episodes;

    public Integer getNumber_of_episodes() {
        return number_of_episodes;
    }
}
