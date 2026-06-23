package org.layer.domain.reaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmojiCode {
    LEC01("대단해"),
    LEC02("완벽해"),
    LEC03("최고야"),
    LEC04("역시"),
    LEC05("고생했어"),
    LEC06("기대중"),
    LEC07("괜찮아"),
    LEC08("성장했다"),
    LEC09("화이팅"),
    LEC10("할 수 있다");

    private final String description;
}
