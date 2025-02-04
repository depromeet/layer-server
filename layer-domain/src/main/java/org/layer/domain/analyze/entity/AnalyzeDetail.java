package org.layer.domain.analyze.entity;

import org.layer.domain.analyze.enums.AnalyzeDetailType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalyzeDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;

	private int count;

	@Column(name = "analyze_detail_rank")
	private int rank;

	@Enumerated(EnumType.STRING)
	private AnalyzeDetailType analyzeDetailType;

	@Builder
	private AnalyzeDetail(String content, int count, int rank, AnalyzeDetailType analyzeDetailType) {
		this.content = content;
		this.count = count;
		this.rank = rank;
		this.analyzeDetailType = analyzeDetailType;
	}

}
