package org.layer.domain.reaction.repository;

import org.layer.domain.reaction.entity.EmojiCode;
import org.layer.domain.reaction.entity.Reaction;
import org.layer.domain.reaction.exception.ReactionException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.layer.global.exception.ReactionExceptionType.NOT_FOUND_REACTION;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    default Reaction findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ReactionException(NOT_FOUND_REACTION));
    }

    Optional<Reaction> findByEmojiCode(EmojiCode emojiCode);

    default Reaction findByEmojiCodeOrThrow(EmojiCode emojiCode) {
        return findByEmojiCode(emojiCode).orElseThrow(() -> new ReactionException(NOT_FOUND_REACTION));
    }
}
