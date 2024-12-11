/*
 * Copyright (C) 2009-2024 SonarSource SA (mailto:info AT sonarsource DOT com), Michael Clarke
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
 */
package com.github.mc1arke.sonarqube.plugin.server.pullrequest.ws.pullrequest.action;

import static org.sonar.server.user.AbstractUserSession.insufficientPrivilegesException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.web.UserRole;
import org.sonar.db.DbClient;
import org.sonar.db.DbSession;
import org.sonar.db.permission.GlobalPermission;
import org.sonar.db.project.ProjectDto;
import org.sonar.server.component.ComponentFinder;
import org.sonar.server.user.UserSession;
import org.sonar.server.ws.WsUtils;
import org.sonarqube.ws.ProjectPullRequests;
import org.springframework.beans.factory.annotation.Autowired;

public class ListAction extends ProjectWsAction {

    private final UserSession userSession;
    private final ProtoBufWriter protoBufWriter;

    @Autowired
    public ListAction(DbClient dbClient, ComponentFinder componentFinder, UserSession userSession) {
        this(dbClient, componentFinder, userSession, WsUtils::writeProtobuf);
    }

    ListAction(DbClient dbClient, ComponentFinder componentFinder, UserSession userSession, ProtoBufWriter protoBufWriter) {
        super("list", dbClient, componentFinder);
        this.userSession = userSession;
        this.protoBufWriter = protoBufWriter;
    }

    @Override
    protected void configureAction(WebService.NewAction action) {
        //no-op
    }

    @Override
    public void handleProjectRequest(ProjectDto project, Request request, Response response, DbSession dbSession) {
         checkPermission(project, userSession);
        ProjectPullRequests.ListWsResponse.Builder protobufResponse = ProjectPullRequests.ListWsResponse.newBuilder();
        protoBufWriter.write(protobufResponse.build(), request, response);
    }

    private static void checkPermission(ProjectDto project, UserSession userSession) {
        if (userSession.hasEntityPermission(UserRole.USER, project) ||
            userSession.hasEntityPermission(UserRole.SCAN, project) ||
            userSession.hasPermission(GlobalPermission.SCAN)) {
            return;
        }
        throw insufficientPrivilegesException();
    }
}
