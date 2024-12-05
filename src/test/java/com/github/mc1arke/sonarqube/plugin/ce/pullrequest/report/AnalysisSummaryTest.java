/*
 * Copyright (C) 2022 Michael Clarke
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package com.github.mc1arke.sonarqube.plugin.ce.pullrequest.report;

import com.github.mc1arke.sonarqube.plugin.ce.pullrequest.markup.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AnalysisSummaryTest {

    @Test
    void testCreateAnalysisSummary() {
        AnalysisSummary underTest = AnalysisSummary.builder()
                .withNewDuplications(BigDecimal.valueOf(199))
                .withSummaryImageUrl("summaryImageUrl")
                .withProjectKey("projectKey")
                .withBugCount(911)
                .withBugUrl("bugUrl")
                .withBugImageUrl("bugImageUrl")
                .withCodeSmellCount(1)
                .withCoverage(BigDecimal.valueOf(303))
                .withCodeSmellUrl("codeSmellUrl")
                .withCodeSmellImageUrl("codeSmellImageUrl")
                .withCoverageUrl("codeCoverageUrl")
                .withCoverageImageUrl("codeCoverageImageUrl")
                .withDashboardUrl("dashboardUrl")
                .withDuplications(BigDecimal.valueOf(66))
                .withDuplicationsUrl("duplicationsUrl")
                .withDuplicationsImageUrl("duplicationsImageUrl")
                .withFailedQualityGateConditions(java.util.List.of("issuea", "issueb", "issuec"))
                .withNewCoverage(BigDecimal.valueOf(99))
                .withSecurityHotspotCount(69)
                .withStatusDescription("status description")
                .withStatusImageUrl("statusImageUrl")
                .withTotalIssueCount(666)
                .withVulnerabilityCount(96)
                .withVulnerabilityUrl("vulnerabilityUrl")
                .withVulnerabilityImageUrl("vulnerabilityImageUrl")
                .build();

        Formatter<Document> formatter = mock();
        doReturn("formatted content").when(formatter).format(any());
        FormatterFactory formatterFactory = mock(FormatterFactory.class);
        doReturn(formatter).when(formatterFactory).documentFormatter();

        assertThat(underTest.format(formatterFactory)).isEqualTo("formatted content");

        ArgumentCaptor<Document> documentArgumentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(formatter).format(documentArgumentCaptor.capture());

        Document expectedDocument = new Document(
            new Paragraph(
                new Text("Analysis Details: "),
                new Link("dashboardUrl", new Text("666 Issues")),
                new Text("      "),
                new Text("911  bugs, "),
                new Text("165  vulnerabilities, "),
                new Text("1  code smell")
            ),new Heading(6, new Text("*sonarqube may show more due to slow master sync"))
        );

        assertThat(documentArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDocument);
    }

    @Test
    void shouldReturn0ForTotalDuplicationsWhereValueIsNull() {
        AnalysisSummary underTest = AnalysisSummary.builder()
            .withNewDuplications(BigDecimal.valueOf(199))
            .withSummaryImageUrl("summaryImageUrl")
            .withProjectKey("projectKey")
            .withBugCount(911)
            .withBugUrl("bugUrl")
            .withBugImageUrl("bugImageUrl")
            .withCodeSmellCount(1)
            .withCoverage(BigDecimal.valueOf(303))
            .withCodeSmellUrl("codeSmellUrl")
            .withCodeSmellImageUrl("codeSmellImageUrl")
            .withCoverageUrl("codeCoverageUrl")
            .withCoverageImageUrl("codeCoverageImageUrl")
            .withDashboardUrl("dashboardUrl")
            .withDuplications(null)
            .withDuplicationsUrl("duplicationsUrl")
            .withDuplicationsImageUrl("duplicationsImageUrl")
            .withFailedQualityGateConditions(java.util.List.of("issuea", "issueb", "issuec"))
            .withNewCoverage(BigDecimal.valueOf(99))
            .withSecurityHotspotCount(69)
            .withStatusDescription("status description")
            .withStatusImageUrl("statusImageUrl")
            .withTotalIssueCount(666)
            .withVulnerabilityCount(96)
            .withVulnerabilityUrl("vulnerabilityUrl")
            .withVulnerabilityImageUrl("vulnerabilityImageUrl")
            .build();

        Formatter<Document> formatter = mock();
        doReturn("formatted content").when(formatter).format(any());
        FormatterFactory formatterFactory = mock(FormatterFactory.class);
        doReturn(formatter).when(formatterFactory).documentFormatter();

        assertThat(underTest.format(formatterFactory)).isEqualTo("formatted content");

        ArgumentCaptor<Document> documentArgumentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(formatter).format(documentArgumentCaptor.capture());

        System.out.println(underTest.format(new MarkdownFormatterFactory()));

        Document expectedDocument = new Document(
            new Paragraph(
                new Text("Analysis Details: "),
                new Link("dashboardUrl", new Text("666 Issues")),
                new Text("      "),
                new Text("911  bugs, "),
                new Text("165  vulnerabilities, "),
                new Text("1  code smell")
            ),new Heading(6, new Text("*sonarqube may show more due to slow master sync"))
        );

        assertThat(documentArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedDocument);

    }

}
