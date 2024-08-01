package org.layer.domain.form.controller;

import org.layer.common.annotation.MemberId;
import org.layer.domain.form.controller.dto.response.DefaultFormGetResponse;
import org.layer.domain.form.service.FormService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/form")
public class FormController implements FormApi {
	private final FormService formService;

	@Override
	@GetMapping("/{formId}")
	public ResponseEntity<DefaultFormGetResponse> getForm(@PathVariable Long formId, @MemberId Long memberId) {
		DefaultFormGetResponse dto = formService.getForm(formId, memberId);

		return ResponseEntity.ok().body(dto);
	}

}
