package com.twilio.oai;

import com.twilio.oai.common.ApplicationConstants;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DirectoryStructureServiceTest {
    private static final String SEPARATOR = ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

    @Test
    public void testGetRelativeRoot() {
        final DirectoryStructureService dirService = new DirectoryStructureService(null, null, null);

        assertEquals("..", dirService.getRelativeRoot(""));
        assertEquals("..", dirService.getRelativeRoot("autopilot"));
        assertEquals("..", dirService.getRelativeRoot("autopilot" + SEPARATOR));
        assertEquals("../..", dirService.getRelativeRoot("autopilot" + SEPARATOR + "v1"));
        assertEquals("../../..", dirService.getRelativeRoot("autopilot" + SEPARATOR + "v1" + SEPARATOR + "services"));
    }

    private static Operation operationWithParams(final String operationId, final String... paramNames) {
        final Operation operation = new Operation().operationId(operationId);
        final List<Parameter> params = new ArrayList<>();
        for (final String name : paramNames) {
            params.add(new Parameter().name(name));
        }
        operation.setParameters(params);
        return operation;
    }

    private static List<String> paramNames(final Operation operation) {
        return operation.getParameters().stream().map(Parameter::getName).collect(Collectors.toList());
    }

    @Test
    public void testUpdatePaginationParamsStripsPageTokenFromListOperation() {
        final DirectoryStructureService dirService = new DirectoryStructureService(null, null, null);
        final Operation operation = operationWithParams("ListCredentialAws", "PageSize", "Page", "PageToken");

        dirService.updatePaginationParams(operation);

        // Page and PageToken are removed from list operations; PageSize is retained.
        assertEquals(Arrays.asList("PageSize"), paramNames(operation));
    }

    @Test
    public void testUpdatePaginationParamsKeepsPageTokenOnFetchOperation() {
        final DirectoryStructureService dirService = new DirectoryStructureService(null, null, null);
        final Operation operation = operationWithParams("FetchSemanticQueryResults", "Sid", "PageToken");

        dirService.updatePaginationParams(operation);

        // On a fetch, PageToken is a legitimate user-supplied query param and must be preserved.
        assertTrue(paramNames(operation).contains("PageToken"));
        assertEquals(Arrays.asList("Sid", "PageToken"), paramNames(operation));
    }
}
