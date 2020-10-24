package shorturl.demo.mapper;

import org.springframework.stereotype.Repository;
import shorturl.demo.entity.Surl;

@Repository
public interface Surlmapper {
    Boolean createShortUrl(Surl surl);

    Surl findByShortId(String shortUrlId);

    void updateShortUrl(String shortUrlId,String lastView);
}
