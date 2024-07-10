package org.layer.domain.blockOption.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.layer.domain.BaseEntity;
import org.layer.domain.block.entity.Block;

@Entity
@Table(name= "block_option", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"block_id", "value"})
})
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockOption extends BaseEntity {

    private String label;

    @NotNull
    private String value;

    @ManyToOne
    @JoinColumn( name = "block_id")
    private Block block;
}