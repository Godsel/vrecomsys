package fr.vrecomsys.app.controllers;

import fr.vrecomsys.app.models.video.Movie;
import fr.vrecomsys.app.models.video.Show;
import fr.vrecomsys.app.models.video.Video;
import fr.vrecomsys.app.models.dto.VideoForm;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.vrecomsys.app.services.VideoService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/videos",
                produces = MediaType.APPLICATION_JSON_VALUE,
                consumes =
                        MediaType.APPLICATION_JSON_VALUE)
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }


    @PostMapping()
    public ResponseEntity<Void> addVideo(
            @RequestBody
            @Valid
            VideoForm videoForm) {
        Video createdVideo = videoService.addVideo(videoForm);
        return ResponseEntity.created(URI.create("/videos/" + createdVideo.getId()))
                             .build();
    }

    @GetMapping()
    public ResponseEntity<List<Video>> getAllVideos(
            @RequestParam(name = "titre",
                          required = false)
            String word) {
        List<Video> videos = videoService.getVideos(word);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(
            @PathVariable
            String id) {
        Video video = videoService.getVideoById(id);
        return video.getId() != null ? ResponseEntity.ok(video) : ResponseEntity.notFound()
                                                                                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideoById(
            @PathVariable
            String id) {
        return videoService.deleteVideoById(id) ? ResponseEntity.ok()
                                                                .build() :
               ResponseEntity.notFound()
                             .build();
    }

    @GetMapping("/historique-suppression")
    public ResponseEntity<List<String>> getDeletedVideoIds() {
        return ResponseEntity.ok(videoService.getDeletedIds());
    }

    @GetMapping("/films")
    public ResponseEntity<List<Movie>> getMovieList() {
        return ResponseEntity.ok(videoService.getMovieList());
    }

    @GetMapping("/series")
    public ResponseEntity<List<Show>> getTvShowList() {
        return ResponseEntity.ok(videoService.getTvShowList());
    }

    @GetMapping("/{id}/videos-similaires")
    public ResponseEntity<List<Video>> getSimilarVideos(
            @PathVariable
            String id,
            @RequestParam (name = "nombre_de_label_minimum_en_commun")
            Integer labelsAmountInCommon) {
        List<Video> videoList = videoService.getSimilarVideos(id, labelsAmountInCommon);
        return videoList != null ? ResponseEntity.ok(videoList) : ResponseEntity.notFound()
                                                                                .build();
    }
}
