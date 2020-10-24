package shorturl.demo.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.DateUtils;
import shorturl.demo.entity.Surl;
import shorturl.demo.mapper.Surlmapper;

import java.util.Date;
import java.util.Locale;

@Service
public class surlServer {
    @Autowired
    Surlmapper smapper;

    public Boolean createurl(Surl surl){
        return smapper.createShortUrl(surl);
    }

    public Surl findByShortId(String shortUrlId){
        return smapper.findByShortId(shortUrlId);
    }

    public void updateShortUrl(String shortUrlId){
        String newDate= DateUtils.format(new Date(),"yyyy-mm-dd hh-mm-ss", Locale.SIMPLIFIED_CHINESE);
        smapper.updateShortUrl(shortUrlId,newDate);
    }

}
