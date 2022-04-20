package fr.vrecomsys.app.services;

import fr.vrecomsys.app.exceptions.UnvalidInputException;
import fr.vrecomsys.app.models.*;
import fr.vrecomsys.app.models.dto.VideoForm;
import fr.vrecomsys.app.models.video.Movie;
import fr.vrecomsys.app.models.video.Show;
import fr.vrecomsys.app.models.video.Video;
import fr.vrecomsys.app.repositories.VideoHistoryRepository;
import org.springframework.stereotype.Service;
import fr.vrecomsys.app.repositories.VideoRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoHistoryRepository videoHistoryRepository;

    private final VideoFactory videoFactory;

    public VideoService(VideoRepository videoRepository,
                        VideoHistoryRepository videoHistoryRepository, VideoFactory videoFactory) {
        this.videoRepository = videoRepository;
        this.videoHistoryRepository = videoHistoryRepository;
        this.videoFactory = videoFactory;
    }

    public Video addVideo(VideoForm videoForm) {
        Video video = videoFactory.createVideo(videoForm);
        return videoRepository.add(video);
    }

    public Video getVideoById(String id) {
        Optional<Video> video = videoRepository.findVideoById(id);
        return video.orElseGet(() -> new Video(null, null, null));
    }

    public List<Video> getVideosWithTitleContaining(String word) {
        if (word.length() < 3) {
            throw new UnvalidInputException("Le mot doit être supérieur à 3 caractères");
        }
        return videoRepository.findByTitleContaining(word);
    }


    public boolean deleteVideoById(String id) {
        if (videoRepository.deleteVideoById(id)) {
            videoHistoryRepository.add(id);
            return true;
        }
        return false;
    }

    public List<String> getDeletedIds() {
        return videoHistoryRepository.findAll();
    }

    public List<Movie> getMovieList() {
        return videoRepository.findMovies();
    }

    public List<Show> getTvShowList() {
        return videoRepository.findTvShows();
    }

    public List<Video> getSimilarVideos(String id, Integer labelsInCommon) {
        Optional<Video> videoById = videoRepository.findVideoById(id);
        return videoById.map(video -> getVideoWithCommonLabelsForVideo(video, labelsInCommon))
                        .orElse(null);
    }

    private List<Video> getVideoWithCommonLabelsForVideo(Video video,
                                                         Integer minimumAmountOfCommonLabels) {
        Map<String, Integer> labelsInCommonById = new HashMap<>();
        for (String label : video.getLabels()) {
            Map<String, Integer> temp = videoRepository.findVideosWithLabelsContaining(label)
                                                       .stream()
                                                       .map(Video::getId)
                                                       .map(UUID::toString)
                                                       .collect(Collectors.toMap(string -> string,
                                                                                 string -> 1));

            temp.forEach((key, value) ->
                                 labelsInCommonById.merge(key,
                                                          value,
                                                          Integer::sum)
                        );
        }

        labelsInCommonById.remove(video.getId()
                                       .toString());
        List<String> similarVideosIdList = labelsInCommonById.entrySet()
                                                             .stream()
                                                             .filter(stringIntegerEntry ->
                                                                             stringIntegerEntry.getValue() >=
                                                                             minimumAmountOfCommonLabels)
                                                             .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                                                             .map(Map.Entry::getKey)
                                                             .toList();

        return videoRepository.getVideosByIdIn(similarVideosIdList);
    }

    public List<Video> getVideos(String word) {
        return word != null ? getVideosWithTitleContaining(word) : videoRepository.findAll();
    }
}
