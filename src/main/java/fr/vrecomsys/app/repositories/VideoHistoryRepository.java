package fr.vrecomsys.app.repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VideoHistoryRepository {
    private final List<String> deletedIdList;

    public VideoHistoryRepository(List<String> deletedIdList) {
        this.deletedIdList = deletedIdList;
    }

    public void add(String id) {
        deletedIdList.add(id);
    }

    public List<String> findAll() {
        return deletedIdList;
    }
}
