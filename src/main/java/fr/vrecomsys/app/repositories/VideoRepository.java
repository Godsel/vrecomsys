package fr.vrecomsys.app.repositories;

import fr.vrecomsys.app.models.video.Movie;
import fr.vrecomsys.app.models.video.Show;
import fr.vrecomsys.app.models.video.Video;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VideoRepository {
    private final List<Video> videoList;

    public VideoRepository(List<Video> videoList) {
        this.videoList = videoList;
    }


    public List<Video> findAll() {
        return videoList;
    }

    public List<Video> findByTitleContaining(String word) {
        return videoList.stream()
                        .filter(video -> video.getTitle()
                                              .contains(word))
                        .toList();
    }

    public List<Video> findVideosWithLabelsContaining(String label) {
        return videoList.stream()
                        .filter(video -> video.getLabels()
                                              .contains(label))
                        .toList();
    }

    public List<Video> getVideosByIdIn(List<String> idList) {
        return videoList.stream()
                        .filter(video -> idList.contains(video.getId()
                                                              .toString()))
                        .toList();
    }

    public Video add(Video video) {
        videoList.add(video);
        return video;
    }

    public Optional<Video> findVideoById(String id) {
        return videoList.stream()
                        .filter(video -> video.getId()
                                              .toString()
                                              .equals(id))
                        .findFirst();
    }

    public boolean deleteVideoById(String id) {
        return videoList.removeIf(video -> video.getId()
                                                .toString()
                                                .equals(id));
    }

    public List<Movie> findMovies() {
        return videoList.stream()
                        .filter(Movie.class::isInstance)
                        .map(Movie.class::cast)
                        .toList();
    }

    public List<Show> findTvShows() {
        return videoList.stream()
                        .filter(Show.class::isInstance)
                        .map(Show.class::cast)
                        .toList();
    }
}
