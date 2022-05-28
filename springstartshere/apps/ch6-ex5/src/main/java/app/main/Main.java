package app.main;


import app.config.ProjectConfig;
import app.model.Comment;
import app.service.CommentService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.logging.Logger;

public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        var service = context.getBean(CommentService.class);

        Comment comment = new Comment();
        comment.setText("San Guo Zhi");
        comment.setAuthor("Chen Shou");

        service.publishComment(comment);

        logger.info("Main Method------------now to start editing---------------------");
        service.editComment(comment);

    }
}
