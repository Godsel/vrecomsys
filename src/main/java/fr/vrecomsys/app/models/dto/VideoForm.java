package fr.vrecomsys.app.models.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class VideoForm implements Serializable {
    private final String id;
    @NotNull
    private final String title;
    @NotEmpty
    private final List<String> labels;
    private final String director;
    private final Instant release_date;
    private final Integer number_of_episodes;

    public VideoForm(String id, String title, List<String> labels, String director,
                     Instant release_date, Integer number_of_episodes) {
        this.id = id;
        this.title = title;
        this.labels = labels;
        this.director = director;
        this.release_date = release_date;
        this.number_of_episodes = number_of_episodes;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getDirector() {
        return director;
    }

    public Instant getRelease_date() {
        return release_date;
    }

    public Integer getNumber_of_episodes() {
        return number_of_episodes;
    }
}
