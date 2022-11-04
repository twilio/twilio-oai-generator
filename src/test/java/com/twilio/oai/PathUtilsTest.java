package com.twilio.oai;

import java.util.Map;

import io.swagger.v3.oas.models.PathItem;
import org.junit.Test;
import org.openapitools.codegen.CodegenOperation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathUtilsTest {

    @Test
    public void testGetTwilioExtension() {
        final String extensionKey = "that-one";
        final PathItem pathItem = new PathItem();

        assertFalse(PathUtils.getTwilioExtension(pathItem, extensionKey).isPresent());

        pathItem.addExtension("x-twilio", null);
        assertFalse(PathUtils.getTwilioExtension(pathItem, extensionKey).isPresent());

        pathItem.addExtension("x-twilio", Map.of("other-one", "other-value"));
        assertFalse(PathUtils.getTwilioExtension(pathItem, extensionKey).isPresent());

        pathItem.addExtension("x-twilio", Map.of("that-one", "that-value"));
        assertTrue(PathUtils.getTwilioExtension(pathItem, extensionKey).isPresent());
        assertEquals("that-value", PathUtils.getTwilioExtension(pathItem, extensionKey).orElseThrow());
    }

    @Test
    public void testGetFirstPathPart(){
        final String path = "/version-v1";
        assertEquals("version-v1", PathUtils.getFirstPathPart(path));

        final String pathWithParts = "first/second/{pathVar}";
        assertEquals("first", PathUtils.getFirstPathPart(pathWithParts));

        final String emptyPath = "";
        assertEquals("", PathUtils.getFirstPathPart(emptyPath));
    }

    @Test
    public void testCleanPath(){
        final String pathWithParams = "some/path/{pathParam1}/withPathParams/{pathParam2}.json";
        assertEquals("some/path/withPathParams.json", PathUtils.cleanPath(pathWithParams));

        final String pathWithoutParams = "some/other/path/noParams";
        assertEquals("some/other/path/noParams", PathUtils.cleanPath(pathWithoutParams));
    }

    @Test
    public void testRemoveExtension(){
        final String pathWithExtension = "some/path/{param1}/with/extension.ext";
        assertEquals("some/path/{param1}/with/extension", PathUtils.removeExtension(pathWithExtension));

        final String pathWithoutExtension = "some/path";
        assertEquals("some/path", PathUtils.removeExtension(pathWithoutExtension));
    }
    @Test
    public void testRemoveFirstPart(){
        final String path = "/some/path/{param}";
        assertEquals("/path/{param}", PathUtils.removeFirstPart(path));
    }

    @Test
    public void testCleanPathAndRemoveFirstElement(){
        final String path = "/some/path/with/{params}/and/extension.ext";
        assertEquals("/path/with/and/extension", PathUtils.cleanPathAndRemoveFirstElement(path));
    }

    @Test
    public void testRemoveBraces(){
        final String path = "path/with/{braces}/{braces2}/";
        assertEquals("path/with/braces/braces2/", PathUtils.removeBraces(path));
    }

    @Test
    public void testRemoveTrailingPathParam(){
        final String pathWithTrailingParam = "some/path/with/{multiple}/trailing/{param}";
        assertEquals("some/path/with/{multiple}/trailing", PathUtils.removeTrailingPathParam(pathWithTrailingParam));
    }

    @Test
    public void testFetchLastElement(){
        final String pathWithDelimiter = "this:is:a:path:wow";
        assertEquals("wow", PathUtils.fetchLastElement(pathWithDelimiter, ":"));

        final String pathWithoutDelimiter = "something";
        assertEquals("something", PathUtils.fetchLastElement(pathWithoutDelimiter, ";"));
    }

    @Test
    public void testIsInstanceOperation(){
        CodegenOperation instanceCo = new CodegenOperation();
        instanceCo.vendorExtensions.put("x-path-type", "instance");
        assertTrue(PathUtils.isInstanceOperation(instanceCo));

        CodegenOperation nonInstanceCo = new CodegenOperation();
        assertFalse(PathUtils.isInstanceOperation(nonInstanceCo));
    }

    @Test
    public void testIsInstancePath(){
        final String instancePath = "some/instance/path/{param}.json";
        assertTrue(PathUtils.isInstancePath(instancePath));

        final String nonInstancePath = "some/non/instance/path.json";
        assertFalse(PathUtils.isInstancePath(nonInstancePath));

    }
}
