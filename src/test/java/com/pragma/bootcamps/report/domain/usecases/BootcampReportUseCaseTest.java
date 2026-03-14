package com.pragma.bootcamps.report.domain.usecases;

import com.pragma.bootcamps.report.domain.clients.CapabilityClientPort;
import com.pragma.bootcamps.report.domain.clients.PersonClientPort;
import com.pragma.bootcamps.report.domain.clients.TechnologyClientPort;
import com.pragma.bootcamps.report.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.report.domain.exceptions.NotFoundException;
import com.pragma.bootcamps.report.domain.models.*;
import com.pragma.bootcamps.report.domain.spi.BootcampReportPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootcampReportUseCaseTest {

    @Mock
    private BootcampReportPersistencePort bootcampReportPersistencePort;

    @Mock
    private CapabilityClientPort capabilityClientPort;

    @Mock
    private TechnologyClientPort technologyClientPort;

    @Mock
    private PersonClientPort personClientPort;

    @InjectMocks
    private BootcampReportUseCase bootcampReportUseCase;

    private BootcampCreated bootcampCreated;
    private CapabilityReport capability1;
    private CapabilityReport capability2;
    private TechnologyReport tech1;
    private TechnologyReport tech2;
    private TechnologyReport tech3;
    private StudentReport student;
    private BootcampReport bootcampReport;

    @BeforeEach
    void setUp() {
        bootcampCreated = new BootcampCreated(1L, "Bootcamp Java", "Descripción del bootcamp", LocalDate.of(2026, 6, 1), 12, 2);

        tech1 = TechnologyReport.builder().id(10L).name("Java").build();
        tech2 = TechnologyReport.builder().id(11L).name("Spring Boot").build();
        tech3 = TechnologyReport.builder().id(12L).name("MongoDB").build();

        capability1 = CapabilityReport.builder().id(100L).name("Backend Development").technologies(List.of(tech1, tech2)).build();
        capability2 = CapabilityReport.builder().id(101L).name("Database Management").technologies(List.of(tech3)).build();

        student = StudentReport.builder().id(200L).name("John Doe").email("john@example.com").age(25).build();

        bootcampReport = BootcampReport.builder()
                .id("abc123")
                .bootcampId(1L)
                .name("Bootcamp Java")
                .description("Descripción del bootcamp")
                .capabilityCount(2)
                .technologyCount(3)
                .enrolledStudentCount(0)
                .capabilities(List.of(capability1, capability2))
                .students(List.of())
                .build();
    }

    @Nested
    @DisplayName("Tests para handleBootcampCreation")
    class HandleBootcampCreationTests {

        @Test
        @DisplayName("Debe crear el reporte del bootcamp exitosamente con capabilities y tecnologías")
        void handleBootcampCreation_Success() {
            when(capabilityClientPort.getCapabilitiesByBootcampId(1L))
                    .thenReturn(Flux.just(capability1, capability2));
            when(technologyClientPort.getTechnologiesByCapabilityId(100L))
                    .thenReturn(Flux.just(tech1, tech2));
            when(technologyClientPort.getTechnologiesByCapabilityId(101L))
                    .thenReturn(Flux.just(tech3));
            when(bootcampReportPersistencePort.save(any(BootcampReport.class)))
                    .thenReturn(Mono.just(bootcampReport));

            StepVerifier.create(bootcampReportUseCase.handleBootcampCreation(bootcampCreated))
                    .verifyComplete();

            ArgumentCaptor<BootcampReport> captor = ArgumentCaptor.forClass(BootcampReport.class);
            verify(bootcampReportPersistencePort).save(captor.capture());

            BootcampReport savedReport = captor.getValue();
            assertEquals(1L, savedReport.getBootcampId());
            assertEquals("Bootcamp Java", savedReport.getName());
            assertEquals("Descripción del bootcamp", savedReport.getDescription());
            assertEquals(2, savedReport.getCapabilityCount());
            assertEquals(3, savedReport.getTechnologyCount());
            assertEquals(0, savedReport.getEnrolledStudentCount());
            assertEquals(2, savedReport.getCapabilities().size());
            assertTrue(savedReport.getStudents().isEmpty());

            verify(capabilityClientPort).getCapabilitiesByBootcampId(1L);
            verify(technologyClientPort).getTechnologiesByCapabilityId(100L);
            verify(technologyClientPort).getTechnologiesByCapabilityId(101L);
        }

        @Test
        @DisplayName("Debe completar correctamente cuando no hay capabilities para el bootcamp")
        void handleBootcampCreation_EmptyCapabilities() {
            when(capabilityClientPort.getCapabilitiesByBootcampId(1L))
                    .thenReturn(Flux.empty());
            when(bootcampReportPersistencePort.save(any(BootcampReport.class)))
                    .thenReturn(Mono.just(bootcampReport));

            StepVerifier.create(bootcampReportUseCase.handleBootcampCreation(bootcampCreated))
                    .verifyComplete();

            ArgumentCaptor<BootcampReport> captor = ArgumentCaptor.forClass(BootcampReport.class);
            verify(bootcampReportPersistencePort).save(captor.capture());

            BootcampReport savedReport = captor.getValue();
            assertEquals(0, savedReport.getCapabilityCount());
            assertEquals(0, savedReport.getTechnologyCount());
            assertTrue(savedReport.getCapabilities().isEmpty());

            verify(technologyClientPort, never()).getTechnologiesByCapabilityId(anyLong());
        }

        @Test
        @DisplayName("Debe propagar error cuando el cliente de capabilities falla")
        void handleBootcampCreation_CapabilityClientError() {
            RuntimeException capabilityError = new RuntimeException("Capability service unavailable");
            when(capabilityClientPort.getCapabilitiesByBootcampId(1L))
                    .thenReturn(Flux.error(capabilityError));

            StepVerifier.create(bootcampReportUseCase.handleBootcampCreation(bootcampCreated))
                    .expectErrorMatches(error -> error instanceof RuntimeException
                            && error.getMessage().equals("Capability service unavailable"))
                    .verify();

            verify(bootcampReportPersistencePort, never()).save(any());
            verify(technologyClientPort, never()).getTechnologiesByCapabilityId(anyLong());
        }

        @Test
        @DisplayName("Debe propagar error cuando el cliente de tecnologías falla")
        void handleBootcampCreation_TechnologyClientError() {
            when(capabilityClientPort.getCapabilitiesByBootcampId(1L))
                    .thenReturn(Flux.just(capability1));
            when(technologyClientPort.getTechnologiesByCapabilityId(100L))
                    .thenReturn(Flux.error(new RuntimeException("Technology service unavailable")));

            StepVerifier.create(bootcampReportUseCase.handleBootcampCreation(bootcampCreated))
                    .expectErrorMatches(error -> error instanceof RuntimeException
                            && error.getMessage().equals("Technology service unavailable"))
                    .verify();

            verify(bootcampReportPersistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Debe propagar error cuando la persistencia falla al guardar")
        void handleBootcampCreation_PersistenceError() {
            when(capabilityClientPort.getCapabilitiesByBootcampId(1L))
                    .thenReturn(Flux.just(capability1));
            when(technologyClientPort.getTechnologiesByCapabilityId(100L))
                    .thenReturn(Flux.just(tech1, tech2));
            when(bootcampReportPersistencePort.save(any(BootcampReport.class)))
                    .thenReturn(Mono.error(new RuntimeException("Database error")));

            StepVerifier.create(bootcampReportUseCase.handleBootcampCreation(bootcampCreated))
                    .expectErrorMatches(error -> error instanceof RuntimeException
                            && error.getMessage().equals("Database error"))
                    .verify();
        }

        @Test
        @DisplayName("Debe manejar una capability con lista vacía de tecnologías")
        void handleBootcampCreation_CapabilityWithNoTechnologies() {
            when(capabilityClientPort.getCapabilitiesByBootcampId(1L))
                    .thenReturn(Flux.just(capability1));
            when(technologyClientPort.getTechnologiesByCapabilityId(100L))
                    .thenReturn(Flux.empty());
            when(bootcampReportPersistencePort.save(any(BootcampReport.class)))
                    .thenReturn(Mono.just(bootcampReport));

            StepVerifier.create(bootcampReportUseCase.handleBootcampCreation(bootcampCreated))
                    .verifyComplete();

            ArgumentCaptor<BootcampReport> captor = ArgumentCaptor.forClass(BootcampReport.class);
            verify(bootcampReportPersistencePort).save(captor.capture());

            BootcampReport savedReport = captor.getValue();
            assertEquals(1, savedReport.getCapabilities().size());
            assertEquals(0, savedReport.getTechnologyCount());
            assertTrue(savedReport.getCapabilities().get(0).getTechnologies().isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests para addPersonToBootcampReport")
    class AddPersonToBootcampReportTests {

        @Test
        @DisplayName("Debe agregar un estudiante al reporte del bootcamp exitosamente")
        void addPersonToBootcampReport_Success() {
            Long bootcampId = 1L;
            Long personId = 200L;

            when(personClientPort.getPersonById(personId))
                    .thenReturn(Mono.just(student));
            when(bootcampReportPersistencePort.addStudentToReport(eq(bootcampId), any(StudentReport.class)))
                    .thenReturn(Mono.empty());

            StepVerifier.create(bootcampReportUseCase.addPersonToBootcampReport(bootcampId, personId))
                    .verifyComplete();

            verify(personClientPort).getPersonById(personId);
            verify(bootcampReportPersistencePort).addStudentToReport(eq(bootcampId), eq(student));
        }

        @Test
        @DisplayName("Debe propagar error cuando el cliente de personas falla")
        void addPersonToBootcampReport_PersonClientError() {
            Long bootcampId = 1L;
            Long personId = 200L;

            when(personClientPort.getPersonById(personId))
                    .thenReturn(Mono.error(new RuntimeException("Person service unavailable")));

            StepVerifier.create(bootcampReportUseCase.addPersonToBootcampReport(bootcampId, personId))
                    .expectErrorMatches(error -> error instanceof RuntimeException
                            && error.getMessage().equals("Person service unavailable"))
                    .verify();

            verify(bootcampReportPersistencePort, never()).addStudentToReport(anyLong(), any());
        }

        @Test
        @DisplayName("Debe propagar error cuando la persistencia falla al agregar estudiante")
        void addPersonToBootcampReport_PersistenceError() {
            Long bootcampId = 1L;
            Long personId = 200L;

            when(personClientPort.getPersonById(personId))
                    .thenReturn(Mono.just(student));
            when(bootcampReportPersistencePort.addStudentToReport(eq(bootcampId), any(StudentReport.class)))
                    .thenReturn(Mono.error(new RuntimeException("Database error")));

            StepVerifier.create(bootcampReportUseCase.addPersonToBootcampReport(bootcampId, personId))
                    .expectErrorMatches(error -> error instanceof RuntimeException
                            && error.getMessage().equals("Database error"))
                    .verify();
        }

        @Test
        @DisplayName("Debe completar sin agregar estudiante cuando la persona no existe")
        void addPersonToBootcampReport_PersonNotFound() {
            Long bootcampId = 1L;
            Long personId = 999L;

            when(personClientPort.getPersonById(personId))
                    .thenReturn(Mono.empty());

            StepVerifier.create(bootcampReportUseCase.addPersonToBootcampReport(bootcampId, personId))
                    .verifyComplete();

            verify(bootcampReportPersistencePort, never()).addStudentToReport(anyLong(), any());
        }
    }

    @Nested
    @DisplayName("Tests para getMostSuccessfulBootcamp")
    class GetMostSuccessfulBootcampTests {

        @Test
        @DisplayName("Debe retornar el bootcamp con más estudiantes inscritos")
        void getMostSuccessfulBootcamp_Success() {
            BootcampReport mostEnrolled = bootcampReport.toBuilder()
                    .enrolledStudentCount(50)
                    .students(List.of(student))
                    .build();

            when(bootcampReportPersistencePort.findMostEnrolledBootcamp())
                    .thenReturn(Mono.just(mostEnrolled));

            StepVerifier.create(bootcampReportUseCase.getMostSuccessfulBootcamp())
                    .assertNext(report -> {
                        assertEquals("abc123", report.getId());
                        assertEquals(1L, report.getBootcampId());
                        assertEquals("Bootcamp Java", report.getName());
                        assertEquals(50, report.getEnrolledStudentCount());
                        assertFalse(report.getStudents().isEmpty());
                    })
                    .verifyComplete();

            verify(bootcampReportPersistencePort).findMostEnrolledBootcamp();
        }

        @Test
        @DisplayName("Debe lanzar NotFoundException cuando no hay bootcamps registrados")
        void getMostSuccessfulBootcamp_NotFound() {
            when(bootcampReportPersistencePort.findMostEnrolledBootcamp())
                    .thenReturn(Mono.empty());

            StepVerifier.create(bootcampReportUseCase.getMostSuccessfulBootcamp())
                    .expectErrorMatches(error -> error instanceof NotFoundException
                            && error.getMessage().equals(ExceptionMessages.BOOTCAMP_NOT_FOUND.getMessage()))
                    .verify();

            verify(bootcampReportPersistencePort).findMostEnrolledBootcamp();
        }

        @Test
        @DisplayName("Debe propagar error cuando la persistencia falla al buscar")
        void getMostSuccessfulBootcamp_PersistenceError() {
            when(bootcampReportPersistencePort.findMostEnrolledBootcamp())
                    .thenReturn(Mono.error(new RuntimeException("Database connection lost")));

            StepVerifier.create(bootcampReportUseCase.getMostSuccessfulBootcamp())
                    .expectErrorMatches(error -> error instanceof RuntimeException
                            && error.getMessage().equals("Database connection lost"))
                    .verify();
        }
    }
}
