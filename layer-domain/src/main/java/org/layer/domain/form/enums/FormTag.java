package org.layer.domain.form.enums;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	public static FormTag getRecommandFormTag(List<RetrospectPurpose> retrospectPurposes) {
		Map<FormTag, Integer> formTagIntegerMap = EnumSet.allOf(FormTag.class).stream()
			.collect(Collectors.toMap(tag -> tag, tag -> 0));

		retrospectPurposes.forEach(purpose -> {
			formTagIntegerMap.put(KPT, formTagIntegerMap.get(KPT) + purpose.getKtpPoint());
			formTagIntegerMap.put(FIVE_F, formTagIntegerMap.get(FIVE_F) + purpose.getFiveFPoint());
			formTagIntegerMap.put(MAD_SAD_GLAD, formTagIntegerMap.get(MAD_SAD_GLAD) + purpose.getMadSadGladPoint());
			formTagIntegerMap.put(SSC, formTagIntegerMap.get(SSC) +purpose.getSscPoint());
			formTagIntegerMap.put(PMI, formTagIntegerMap.get(PMI) +purpose.getPmiPont());
		});

		return formTagIntegerMap.entrySet()
			.stream()
			.max(Map.Entry.comparingByValue())
			.map(Map.Entry::getKey)
			.orElse(FormTag.UNTITLED);
	}
}
