package com.nansha.largobjecttransclient.scheduler;

import static org.mockito.Mockito.*;

import com.nansha.largobjecttransclient.service.ReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReportGeneratorSchedulerTest {
  // Existing setup and tests
  @Mock private ReportGenerator reportGenerator;

  @InjectMocks private ReportGeneratorScheduler scheduler;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  //    @Test
  //    public void testGenerateReport_Success() {
  //        when(reportGenerator.fetchAndProcessPage(anyInt(), anyList()))
  //                .thenReturn(Mono.just(new UploadResult("report.csv", true)));
  //
  //        scheduler.generateReport();
  //
  //        // Verify that fetchAndProcessPage was called once
  //        verify(reportGenerator, times(1)).fetchAndProcessPage(anyInt(), anyList());
  //
  //        // Additional verification steps can be added here if needed
  //    }
  //
  //    @Test
  //    public void testGenerateReport_Error() {
  //        when(reportGenerator.fetchAndProcessPage(anyInt(), anyList()))
  //                .thenReturn(Mono.error(new RuntimeException("Test error")));
  //
  //        scheduler.generateReport();
  //
  //        // Verify that fetchAndProcessPage was called once even in error scenario
  //        verify(reportGenerator, times(1)).fetchAndProcessPage(anyInt(), anyList());
  //
  //        // Additional error handling verification can be added here
  //    }
}
