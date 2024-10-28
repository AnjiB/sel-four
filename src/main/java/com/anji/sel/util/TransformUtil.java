package com.anji.sel.util;

import com.anji.sel.pojo.light.LighthouseResponse;
import com.anji.sel.pojo.metric.MetricDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.openqa.selenium.devtools.v128.performance.model.Metric;

public class TransformUtil {

  public static List<MetricDto> transform(String hostBaseUrl, List<Metric> metricList) {

    return metricList.stream()
        .map(
            metric -> {
              return MetricDto.builder()
                  .document(hostBaseUrl)
                  .name(metric.getName())
                  .value(metric.getValue().doubleValue())
                  .build();
            })
        .toList();
  }

  public static List<MetricDto> transformFromLighthouse() throws IOException {

    String report = new String(Files.readAllBytes(Paths.get("target/lighthouse-report.json")));

    LighthouseResponse lightHouseResponse =
        new ObjectMapper().readValue(report, LighthouseResponse.class);

    List<MetricDto> lightHouseMetrics =
        List.of(
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getCategories().getPerformance().getId())
                .value(lightHouseResponse.getCategories().getPerformance().getScore())
                .build(),
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getCategories().getAccessibility().getId())
                .value(lightHouseResponse.getCategories().getAccessibility().getScore())
                .build(),
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getAudits().getCumulativeLayoutShift().getId())
                .value(lightHouseResponse.getAudits().getCumulativeLayoutShift().getNumericValue())
                .build(),
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getAudits().getFirstContentfulPaint().getId())
                .value(lightHouseResponse.getAudits().getFirstContentfulPaint().getNumericValue())
                .build(),
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getAudits().getLargestContentfulPaint().getId())
                .value(lightHouseResponse.getAudits().getLargestContentfulPaint().getNumericValue())
                .build(),
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getAudits().getFirstMeaningfulPaint().getId())
                .value(lightHouseResponse.getAudits().getFirstMeaningfulPaint().getNumericValue())
                .build(),
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getAudits().getSpeedIndex().getId())
                .value(lightHouseResponse.getAudits().getSpeedIndex().getNumericValue())
                .build(),
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getAudits().getTotalBlockingTime().getId())
                .value(lightHouseResponse.getAudits().getTotalBlockingTime().getNumericValue())
                .build(),
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getAudits().getServerResponseTime().getId())
                .value(lightHouseResponse.getAudits().getServerResponseTime().getNumericValue())
                .build(),
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getAudits().getUsesLongCacheTtl().getId())
                .value(lightHouseResponse.getAudits().getUsesLongCacheTtl().getNumericValue())
                .build(),
            MetricDto.builder()
                .document(lightHouseResponse.getRequestedUrl())
                .name(lightHouseResponse.getAudits().getTotalByteWeight().getId())
                .value(lightHouseResponse.getAudits().getTotalByteWeight().getNumericValue())
                .build());

    return lightHouseMetrics;
  }
}
