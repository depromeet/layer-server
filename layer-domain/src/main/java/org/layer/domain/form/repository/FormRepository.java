package org.layer.domain.form.repository;

import static org.layer.common.exception.FormExceptionType.*;

import org.layer.domain.form.entity.Form;
import org.layer.domain.form.exception.FormException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<Form, Long> {
	default Form findByIdOrThrow(Long formId) {
		return findById(formId)
			.orElseThrow(() -> new FormException(NOT_FOUND_FORM));
	}


}
