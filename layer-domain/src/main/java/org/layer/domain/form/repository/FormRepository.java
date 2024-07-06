package org.layer.domain.form.repository;

import org.layer.domain.form.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<Form, Long> {

}
