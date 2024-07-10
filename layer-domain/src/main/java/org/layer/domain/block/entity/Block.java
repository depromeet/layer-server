package org.layer.domain.block.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.layer.domain.BaseEntity;
import org.layer.domain.block.converter.BlockStyleConverter;
import org.layer.domain.block.enums.BlockType;
import org.layer.domain.blockOption.entity.BlockOption;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block extends BaseEntity {

    /**
     * Form RelationId
     */
    @NotNull
    private Long formId;

    private String label;

    @Column(length = 20)
    @NotNull
    @Convert(converter = BlockStyleConverter.class)
    private BlockType style;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "block", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BlockOption> options = new HashSet<>();
}
