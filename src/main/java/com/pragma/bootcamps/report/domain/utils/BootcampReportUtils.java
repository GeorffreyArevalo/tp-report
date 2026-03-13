package com.pragma.bootcamps.report.domain.utils;


import com.pragma.bootcamps.report.domain.models.BootcampCreated;
import com.pragma.bootcamps.report.domain.models.BootcampReport;
import com.pragma.bootcamps.report.domain.models.CapabilityReport;
import com.pragma.bootcamps.report.domain.models.TechnologyReport;
import lombok.experimental.UtilityClass;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class BootcampReportUtils {

    public static CapabilityReport buildCapabilityWithTechnologies(CapabilityReport capability, List<TechnologyReport> technologies) {
        return CapabilityReport.builder()
                .id(capability.getId())
                .name(capability.getName())
                .technologies(List.copyOf(technologies))
                .build();
    }

    public static int calculateTotalTechnologies(List<CapabilityReport> capabilities) {
        return Optional.ofNullable(capabilities)
                .map(list -> list.stream()
                        .map(CapabilityReport::getTechnologies)
                        .filter(Objects::nonNull)
                        .mapToInt(List::size)
                        .sum())
                .orElse(0);
    }

    public static BootcampReport buildBootcampReport(BootcampCreated event, List<CapabilityReport> capabilities) {
        return BootcampReport.builder()
                .bootcampId(event.getBootcampId())
                .name(event.getName())
                .description(event.getDescription())
                .capabilities(List.copyOf(capabilities))
                .students(List.of())
                .capabilityCount(Optional.of(capabilities).map(List::size).orElse(0))
                .technologyCount(calculateTotalTechnologies(capabilities))
                .enrolledStudentCount(0)
                .build();
    }
}
