package org.layer.domain.form.repository;

import org.layer.domain.form.entity.Form;
import org.layer.domain.form.entity.FormType;
import org.layer.domain.form.enums.FormTag;
import org.layer.domain.form.exception.FormException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static org.layer.common.exception.FormExceptionType.NOT_FOUND_FORM;

public interface FormRepository extends JpaRepository<Form, Long> {
	default Form findByIdOrThrow(Long formId) {
		return findById(formId)
			.orElseThrow(() -> new FormException(NOT_FOUND_FORM));
	}

	List<Form> findByFormTypeOrderById(FormType formType);

	Page<Form> findAllByFormTypeAndSpaceIdOrderByIdDesc(Pageable pageable, FormType formType, Long spaceId);

	Optional<Form> findByFormTagAndFormType(FormTag formTag, FormType formType);

	default Form findByFormTagAndFormTypeOrThrow(FormTag formTag, FormType formType) {
		return findByFormTagAndFormType(formTag, formType)
			.orElseThrow(() -> new FormException(NOT_FOUND_FORM));
	}
}
