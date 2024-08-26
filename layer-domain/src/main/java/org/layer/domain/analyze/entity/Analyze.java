package org.layer.domain.analyze.entity;

import java.util.ArrayList;
import java.util.List;

import org.layer.domain.analyze.enums.AnalyzeType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`analyze`")
public class Analyze {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private Long retrospectId;

	private Long memberId;

	private int satisfactionCount;

	private int normalCount;

	private int regretCount;

	private int goalCompletionRate;

	@Enumerated(EnumType.STRING)
	private AnalyzeType analyzeType;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "analyze_id")
	private List<AnalyzeDetail> analyzeDetails = new ArrayList<>();

	@Builder
	private Analyze(Long retrospectId, Long memberId, int satisfactionCount, int normalCount, int regretCount, int goalCompletionRate,
		AnalyzeType analyzeType, List<AnalyzeDetail> analyzeDetails) {
		this.retrospectId = retrospectId;
		this.memberId = memberId;
		this.satisfactionCount = satisfactionCount;
		this.normalCount = normalCount;
		this.regretCount = regretCount;
		this.goalCompletionRate = goalCompletionRate;
		this.analyzeType = analyzeType;
		this.analyzeDetails = analyzeDetails;
	}
}
