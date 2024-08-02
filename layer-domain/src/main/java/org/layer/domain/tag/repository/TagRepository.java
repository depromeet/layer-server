package org.layer.domain.tag.repository;


import java.util.List;

import org.layer.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
	List<Tag> findAllByFormId(Long formId);
}
