package app.service;

import app.annotation.ToLog;
import app.model.Comment;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CommentService {
    private Logger logger = Logger.getLogger(CommentService.class.getName());

    public void publishComment(Comment comment) {
        logger.info("Publishing comment: " + comment);
    }

    @ToLog
    public String editComment(Comment comment){
        logger.info("Editing comment: " + comment);
        return "Success";
    }
}
