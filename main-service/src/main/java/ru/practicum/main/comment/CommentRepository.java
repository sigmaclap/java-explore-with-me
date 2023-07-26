package ru.practicum.main.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.comment.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByEvent_Id(Long eventId);

    List<Comment> findAllByEvent_IdIn(List<Long> eventIds);
}