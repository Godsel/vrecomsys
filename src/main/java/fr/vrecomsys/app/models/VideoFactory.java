package fr.vrecomsys.app.models;

import fr.vrecomsys.app.exceptions.UnvalidInputException;
import fr.vrecomsys.app.models.dto.VideoForm;
import fr.vrecomsys.app.models.video.Movie;
import fr.vrecomsys.app.models.video.Show;
import fr.vrecomsys.app.models.video.Video;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VideoFactory {
    public Video createVideo(VideoForm videoForm) {
        return switch (determineVideoType(videoForm)) {
            case "movie" -> new Movie(UUID.randomUUID(), videoForm.getTitle(),
                                      videoForm.getLabels(), videoForm.getDirector(),
                                      videoForm.getRelease_date());
            case "tvshow" -> new Show(UUID.randomUUID(), videoForm.getTitle(),
                                      videoForm.getLabels(), videoForm.getNumber_of_episodes());

            default -> new Video(UUID.randomUUID(), videoForm.getTitle(),
                                 videoForm.getLabels());
        };
    }

    private String determineVideoType(VideoForm videoForm) {
        if (isAMovie(videoForm) && isAShow(videoForm)) {
            throw new UnvalidInputException("L'entrée ne peut pas être à la fois un film et une " +
                                            "série");
        } else if (isAMovie(videoForm)) {
            return "movie";
        } else if (isAShow(videoForm)) {
            return "tvshow";
        }
        return "video";
    }

    private boolean isAShow(VideoForm videoForm) {
        return videoForm.getNumber_of_episodes() != null;
    }

    private boolean isAMovie(VideoForm videoForm) {
        return videoForm.getDirector() != null && videoForm.getRelease_date() != null;
    }
}
