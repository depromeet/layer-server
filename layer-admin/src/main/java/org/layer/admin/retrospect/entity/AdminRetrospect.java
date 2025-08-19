package org.layer.admin.retrospect.entity;

import java.time.LocalDateTime;

import org.layer.admin.retrospect.enums.AdminAnalysisStatus;
import org.layer.admin.retrospect.enums.AdminRetrospectStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "retrospect")
public class AdminRetrospect {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@NotNull
	private Long spaceId;

	@NotNull
	private String title;

	@NotNull
	private String introduction;

	@NotNull
	@Enumerated(EnumType.STRING)
	private AdminRetrospectStatus retrospectStatus;

	@NotNull
	@Enumerated(EnumType.STRING)
	private AdminAnalysisStatus analysisStatus;

	private LocalDateTime deadline;

	@Builder
	public AdminRetrospect(Long spaceId, String title, String introduction, AdminRetrospectStatus retrospectStatus,
		AdminAnalysisStatus analysisStatus, LocalDateTime deadline) {
		this.spaceId = spaceId;
		this.title = title;
		this.introduction = introduction;
		this.retrospectStatus = retrospectStatus;
		this.analysisStatus = analysisStatus;
		this.deadline = deadline;
	}
}
