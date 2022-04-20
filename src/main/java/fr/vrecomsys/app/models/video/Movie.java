package fr.vrecomsys.app.models.video;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Movie extends Video implements Serializable {
    public Movie(UUID id, String title, List<String> labels, String director, Instant releaseDate) {
        super(id, title, labels);
        this.director = director;
        this.releaseDate = releaseDate;
    }
    private final String director;
    private final Instant releaseDate;

    public String getDirector() {
        return director;
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }
}
