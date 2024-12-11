/*
 * Copyright (C) 2022-2024 Michael Clarke
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.db.DbClient;
import org.sonar.server.component.ComponentFinder;
import org.sonar.server.exceptions.ForbiddenException;
import org.sonar.server.user.UserSession;

class ListActionTest {

    private final DbClient dbClient = mock(DbClient.class);
    private final UserSession userSession = mock(UserSession.class);
    private final ComponentFinder componentFinder = mock(ComponentFinder.class);
    private final ProtoBufWriter protoBufWriter = mock(ProtoBufWriter.class);
    private final ListAction underTest = new ListAction(dbClient, componentFinder, userSession, protoBufWriter);

    @Test
    void shouldDefineEndpointWithProjectParameter() {
        WebService.NewController newController = mock(WebService.NewController.class);
        WebService.NewAction newAction = mock(WebService.NewAction.class);
        when(newAction.setHandler(any())).thenReturn(newAction);
        when(newController.createAction(any())).thenReturn(newAction);
        WebService.NewParam projectParam = mock(WebService.NewParam.class);
        when(newAction.createParam(any())).thenReturn(projectParam);

        underTest.define(newController);

        verify(newController).createAction("list");
        verify(newAction).setHandler(underTest);
        verify(newAction).createParam("project");
        verifyNoMoreInteractions(newAction);
        verify(projectParam).setRequired(true);
        verifyNoMoreInteractions(projectParam);

        verifyNoMoreInteractions(newController);
    }

    @Test
    void shouldNotExecuteRequestIfUserDoesNotHaveAnyPermissions() {
        Request request = mock(Request.class);
        when(request.mandatoryParam("project")).thenReturn("project");

        Response response = mock(Response.class);

        assertThatThrownBy(() -> underTest.handle(request, response)).isInstanceOf(ForbiddenException.class);

        verifyNoMoreInteractions(protoBufWriter);
    }

}