package fr.vrecomsys.app.controllers;

import fr.vrecomsys.app.SpringTest;
import fr.vrecomsys.app.models.*;
import fr.vrecomsys.app.models.dto.VideoForm;
import fr.vrecomsys.app.models.video.Movie;
import fr.vrecomsys.app.models.video.Show;
import fr.vrecomsys.app.models.video.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class VideoControllerSpringTest {
    @Autowired
    VideoController videoController;
    @LocalServerPort
    Integer localPort;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    VideoFactory videoFactory;

    @Test
    void should_create_video_and_return_its_id_in_location_with_http_created() {
        // Given
        String url = "http://localhost:" + localPort + "/videos";
        Video video = new Video(null, "Test", Collections.singletonList("test"));

        HttpEntity<Video> requestEntity = new HttpEntity<>(video,
                                                           getHttpHeader());

        // When
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url,
                                                                          HttpMethod.POST,
                                                                          requestEntity,
                                                                          String.class);
        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders()
                                 .getLocation()
                                 .getPath())
                .contains("/videos/");
        assertThat(UUID.fromString(getIdFromHeader(responseEntity.getHeaders())))
                .isNotNull();
    }

    @Test
    void should_get_video_by_its_id_with_http_ok() {
        // Given
        VideoForm video = new VideoForm(null, "Test", Collections.singletonList("test"), null, null,
                                        null);
        ResponseEntity<Void> stringResponseEntity = videoController.addVideo(video);
        String url = "http://localhost:" + localPort + "/videos/" +
                     getIdFromHeader(stringResponseEntity.getHeaders());

        // When
        ResponseEntity<Video> responseEntity = testRestTemplate.exchange(url,
                                                                         HttpMethod.GET,
                                                                         new HttpEntity<>(null,
                                                                                          getHttpHeader()),
                                                                         Video.class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()
                                 .getTitle())
                .isEqualTo(video.getTitle());
        assertThat(responseEntity.getBody()
                                 .getLabels())
                .isEqualTo(video.getLabels());
    }

    @Test
    void should_return_404_when_getting_video_with_unknown_id() {
        // Given
        String url = "http://localhost:" + localPort + "/videos/1";


        // When
        ResponseEntity<Video> responseEntity = testRestTemplate.exchange(url,
                                                                         HttpMethod.GET,
                                                                         new HttpEntity<>(null,
                                                                                          getHttpHeader()),
                                                                         Video.class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void should_return_ok_and_2_videos_when_getting_videos_with_title_containing_ind() {
        // Given
        String url = "http://localhost:" + localPort + "/videos?titre=ind";
        VideoForm indianna = new VideoForm(null, "indianna", Collections.singletonList("adventure"),
                                           null
                , null, null);
        VideoForm indestructibles = new VideoForm(null, "les indestructibles",
                                                  Collections.singletonList("superhero"), null,
                                                  null, null);
        videoController.addVideo(indianna);
        videoController.addVideo(indestructibles);

        // When
        ResponseEntity<Video[]> responseEntity = testRestTemplate.exchange(url,
                                                                           HttpMethod.GET,
                                                                           new HttpEntity<>(null,
                                                                                            getHttpHeader()),
                                                                           Video[].class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
    }

    @Test
    void should_return_bad_request_when_title_param_is_lower_than_3_chars() {
        // Given
        String url = "http://localhost:" + localPort + "/videos?titre=in";
        // When
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url,
                                                                          HttpMethod.GET,
                                                                          new HttpEntity<>(null,
                                                                                           getHttpHeader()),
                                                                          String.class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void should_return_ok_when_video_is_deleted() {
        // Given
        VideoForm starwars = new VideoForm(null,
                                           "Star Wars",
                                           Collections.singletonList("space-opera"),
                                           null,
                                           null,
                                           null);
        ResponseEntity<Void> creationResponseEntity = videoController.addVideo(starwars);
        String url = "http://localhost:" + localPort + "/videos/" +
                     getIdFromHeader(creationResponseEntity.getHeaders());
        // When
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url,
                                                                          HttpMethod.DELETE,
                                                                          new HttpEntity<>(null,
                                                                                           getHttpHeader()),
                                                                          String.class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void should_return_ok_when_requesting_deleted_id_list() {
        // Given
        String url = "http://localhost:" + localPort + "/videos/historique-suppression";
        // When
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url,
                                                                          HttpMethod.GET,
                                                                          new HttpEntity<>(null,
                                                                                           getHttpHeader()),
                                                                          String.class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    void should_return_movies() {
        // Given
        VideoForm starwars = new VideoForm(null,
                                           "Star Wars I - La Menace Fantôme",
                                           Collections.singletonList("space-opera"),
                                           "George Lucas",
                                           Instant.ofEpochSecond(915148800), null);
        VideoForm got = new VideoForm(null,
                                      "Game Of Thrones",
                                      Collections.singletonList("fantasy"),
                                      null,
                                      null,
                                      73);
        videoController.addVideo(starwars);
        videoController.addVideo(got);
        String url = "http://localhost:" + localPort + "/videos/films";
        // When
        ResponseEntity<Movie[]> responseEntity = testRestTemplate.exchange(url,
                                                                           HttpMethod.GET,
                                                                           new HttpEntity<>(null,
                                                                                            getHttpHeader()),
                                                                           Movie[].class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                                                    "id")
                                            .isEqualTo(new Video[]{
                                                    videoFactory.createVideo(starwars)});
    }

    @Test
    void should_return_series() {
        // Given
        VideoForm starwars = new VideoForm(null,
                                           "Star Wars I - La Menace Fantôme",
                                           Collections.singletonList("space-opera"),
                                           "George Lucas",
                                           Instant.ofEpochSecond(915148800), null);
        VideoForm got = new VideoForm(null,
                                      "Game Of Thrones",
                                      Collections.singletonList("fantasy"),
                                      null,
                                      null,
                                      73);
        videoController.addVideo(starwars);
        videoController.addVideo(got);
        String url = "http://localhost:" + localPort + "/videos/series";
        // When
        ResponseEntity<Show[]> responseEntity = testRestTemplate.exchange(url,
                                                                          HttpMethod.GET,
                                                                          new HttpEntity<>(null,
                                                                                           getHttpHeader()),
                                                                          Show[].class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                                                    "id")
                                            .isEqualTo(new Video[]{
                                                    videoFactory.createVideo(got)});
    }

    @Test
    void should_return_bad_request_when_creating_video_with_bad_format() {
        // Given
        VideoForm badformat = new VideoForm(null,
                                            "pas bon",
                                            Collections.singletonList("space-opera"),
                                            "George Lucas",
                                            Instant.ofEpochSecond(915148800), 100);
        String url = "http://localhost:" + localPort + "/videos";
        // When
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url,
                                                                          HttpMethod.POST,
                                                                          new HttpEntity<>(badformat,
                                                                                           getHttpHeader()),
                                                                          String.class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shoukd_return_1_similar_video_when_1_label_in_common() {
        // Given
        Integer expectedMinimumLabelsInCommon = 1;
        VideoForm vikings = new VideoForm(null,
                                          "Vikings",
                                          Arrays.asList("war", "medieval", "history"), null, null,
                                          2);
        VideoForm thelastkingdom = new VideoForm(null,
                                          "The Last Kingdom",
                                          Arrays.asList("war", "medieval", "history"), null, null,
                                          2);
        VideoForm got = new VideoForm(null,
                                      "Game Of Thrones",
                                      Arrays.asList("medieval", "fantasy"),
                                      null,
                                      null,
                                      73);
        ResponseEntity<Void> voidResponseEntity = videoController.addVideo(vikings);
        videoController.addVideo(thelastkingdom);
        videoController.addVideo(got);

        String url =
                "http://localhost:" + localPort + "/videos/" + getIdFromHeader(voidResponseEntity.getHeaders()) + "/videos-similaires" + "?nombre_de_label_minimum_en_commun=" + expectedMinimumLabelsInCommon ;

        // When
        ResponseEntity<Video[]> responseEntity = testRestTemplate.exchange(url,
                                                                          HttpMethod.GET,
                                                                          new HttpEntity<>(null,
                                                                                           getHttpHeader()),
                                                                          Video[].class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(List.of(responseEntity.getBody())).hasSize(2);
    }

    @Test
    void shoukd_return_1_similar_video_when_2_label_in_common() {
        // Given
        Integer expectedMinimumLabelsInCommon = 2;
        VideoForm vikings = new VideoForm(null,
                                          "Vikings",
                                          Arrays.asList("war", "medieval", "history"), null, null,
                                          2);
        VideoForm thelastkingdom = new VideoForm(null,
                                                 "The Last Kingdom",
                                                 Arrays.asList("war", "medieval", "history"), null, null,
                                                 2);
        VideoForm got = new VideoForm(null,
                                      "Game Of Thrones",
                                      Arrays.asList("medieval", "fantasy"),
                                      null,
                                      null,
                                      73);
        ResponseEntity<Void> voidResponseEntity = videoController.addVideo(vikings);
        videoController.addVideo(thelastkingdom);
        videoController.addVideo(got);

        String url =
                "http://localhost:" + localPort + "/videos/" + getIdFromHeader(voidResponseEntity.getHeaders()) + "/videos-similaires" + "?nombre_de_label_minimum_en_commun=" + expectedMinimumLabelsInCommon ;

        // When
        ResponseEntity<Video[]> responseEntity = testRestTemplate.exchange(url,
                                                                          HttpMethod.GET,
                                                                          new HttpEntity<>(null,
                                                                                           getHttpHeader()),
                                                                          Video[].class);

        // Then
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(List.of(responseEntity.getBody())).hasSize(1);
    }


    private String getIdFromHeader(HttpHeaders creationResponseEntity) {
        return creationResponseEntity
                .getLocation()
                .getPath()
                .split("/")[2];
    }

    private HttpHeaders getHttpHeader() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        return requestHeaders;
    }
}
