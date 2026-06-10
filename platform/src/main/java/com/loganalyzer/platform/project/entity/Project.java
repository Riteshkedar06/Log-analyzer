package com.loganalyzer.platform.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(
        name = "projects",
        indexes = {
                @Index(name = "idx_project_created_by", columnList = "created_by")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_project_name_user",
                        columnNames = {"project_name", "created_by"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID projectId;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String projectName;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;


}