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
import org.apache.commons.lang.StringUtils;

public final class AnalysisIssueSummary {

    private String uniqKey;
    private String rule;
    private final String typeImageUrl;
    private final String severityImageUrl;
    private final String issueUrl;
    private final String issueKey;
    private final String projectKey;
    private final Long effortInMinutes;
    private final String type;
    private final String message;
    private final String severity;
    private final String resolution;

    private AnalysisIssueSummary(Builder builder) {
        this.typeImageUrl = builder.typeImageUrl;
        this.severityImageUrl = builder.severityImageUrl;
        this.issueUrl = builder.issueUrl;
        this.issueKey = builder.issueKey;
        this.projectKey = builder.projectKey;
        this.effortInMinutes = builder.effortInMinutes;
        this.type = builder.type;
        this.message = builder.message;
        this.severity = builder.severity;
        this.resolution = builder.resolution;
    }

    public void setRule(String rule) { this.rule = rule; }

    public void setUniqKey(String key) { this.uniqKey = key; }

    public String getUniqKey() { return this.uniqKey; }

    public String getTypeImageUrl() {
        return typeImageUrl;
    }

    public String getSeverityImageUrl() {
        return severityImageUrl;
    }

    public String getIssueUrl() {
        return issueUrl;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public Long getEffortInMinutes() {
        return effortInMinutes;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getSeverity() {
        return severity;
    }

    public String getResolution() {
        return resolution;
    }

    private String getEmojiType(String type) {
        switch (type) {
            case "CODE_SMELL":
                return "🦨";
            case "BUG":
                return "🪳";
            case "VULNERABILITY":
                return "🔓";
            case "SECURITY_HOTSPOT":
                return "🔥";
            default:
                return  "";
        }
    }

    private String getEmojiSeverity(String severity) {
        switch(severity) {
            case "INFO": return "⚠";
            case "MINOR": return "⚠️";
            case "MAJOR": return "🟡";
            case "CRITICAL": return "🔴";
            case "BLOCKER": return "⛔";
            default: return "";
        }
    }

    public String format(FormatterFactory formatterFactory) {
        Long effort = getEffortInMinutes();
        Node effortNode = (null == effort ? new Text("") : new Paragraph(new Text(String.format("**Duration (min):** %s", effort))));

        Node resolutionNode = (StringUtils.isBlank(getResolution()) ? new Text("") : new Paragraph(new Text(String.format("**Resolution:** %s", getResolution()))));
        String type = getEmojiType(getType());
        String severity = getEmojiSeverity(getSeverity());

        Document document = new Document(
            new Paragraph(
                    new Text(String.format("%s %s %s", type, severity, getMessage()))
            ),
            new Paragraph(
                    new Link(getIssueUrl(), new Text("View in SonarQube")),
                    new Text("      "),
                    new Link("https://link.orangelogic.com/Tasks/262A92", new Text("Report false positive"))
            ),
            new Heading(6, new Text(rule == null ? "" : "to ignore comment: surround bad code line with:")),
            new Paragraph(new Text(rule == null ? "" : ("```\n#pragma warning disable "+ rule +"\nLINE WITH BAD CODE HERE \n#pragma warning restore "+ rule + "\n```")))
        );

        return formatterFactory.documentFormatter().format(document);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String typeImageUrl;
        private String severityImageUrl;
        private String issueUrl;
        private String issueKey;
        private String projectKey;
        private Long effortInMinutes;
        private String type;
        private String message;
        private String severity;
        private String resolution;

        private Builder() {
            super();
        }

        public Builder withTypeImageUrl(String typeImageUrl) {
            this.typeImageUrl = typeImageUrl;
            return this;
        }

        public Builder withSeverityImageUrl(String severityImageUrl) {
            this.severityImageUrl = severityImageUrl;
            return this;
        }

        public Builder withIssueUrl(String issueUrl) {
            this.issueUrl = issueUrl;
            return this;
        }

        public Builder withIssueKey(String issueKey) {
            this.issueKey = issueKey;
            return this;
        }

        public Builder withProjectKey(String projectKey) {
            this.projectKey = projectKey;
            return this;
        }

        public Builder withEffortInMinutes(Long effortInMinutes) {
            this.effortInMinutes = effortInMinutes;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withSeverity(String severity) {
            this.severity = severity;
            return this;
        }

        public Builder withResolution(String resolution) {
            this.resolution = resolution;
            return this;
        }

        public AnalysisIssueSummary build() {
            return new AnalysisIssueSummary(this);
        }
    }
}
