package org.layer.domain.form.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.layer.domain.common.random.CustomRandom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FormTag {
	KPT("KPT"),
	FIVE_F("5F"),
	MAD_SAD_GLAD("Mad Sad Glad"),
	SSC("SSC"),
	PMI("PMI"),
	UNTITLED("무제"),
	CUSTOM("CUSTOM");

	private final String tag;

	public static FormTag getRecommandFormTag(List<RetrospectPurpose> retrospectPurposes, CustomRandom customRandom) {
		Map<FormTag, Integer> formTagIntegerMap = EnumSet.allOf(FormTag.class).stream()
			.collect(Collectors.toMap(tag -> tag, tag -> 0));

		retrospectPurposes.forEach(purpose -> {
			formTagIntegerMap.put(KPT, formTagIntegerMap.get(KPT) + purpose.getKtpPoint());
			formTagIntegerMap.put(FIVE_F, formTagIntegerMap.get(FIVE_F) + purpose.getFiveFPoint());
			formTagIntegerMap.put(MAD_SAD_GLAD, formTagIntegerMap.get(MAD_SAD_GLAD) + purpose.getMadSadGladPoint());
			formTagIntegerMap.put(SSC, formTagIntegerMap.get(SSC) +purpose.getSscPoint());
			formTagIntegerMap.put(PMI, formTagIntegerMap.get(PMI) +purpose.getPmiPont());
		});

		int maxValue = Collections.max(formTagIntegerMap.values());

		List<FormTag> maxTags = formTagIntegerMap.entrySet().stream()
			.filter(entry -> entry.getValue() == maxValue)
			.map(Map.Entry::getKey)
			.toList();

		// 최댓값을 가진 것 중 랜덤으로 하나 선택
		return maxTags.get(customRandom.nextInt(maxTags.size()));
	}
}
