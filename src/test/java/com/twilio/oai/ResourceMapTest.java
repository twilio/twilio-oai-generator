package com.twilio.oai;

import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.resource.ResourceMap;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ResourceMapTest {

    @Test
    public void testAddAndRetrieveResources() {
        final IResourceTree resourceTree = new ResourceMap(new Inflector());

        final String listPath = "/v1/Path";
        final String instancePath = "/v1/Path/{WithParam}.json";
        final String nestedPath = "/v1/Path/{WithParam}/Nested.json";
        final PathItem listPathItem = new PathItem();
        final PathItem instancePathItem = new PathItem();
        final PathItem nestedPathItem = new PathItem();

        // Something to distinguish the path items.
        listPathItem.setDescription(listPath);
        instancePathItem.setDescription(instancePath);
        nestedPathItem.setDescription(nestedPath);

        nestedPathItem.addExtension("x-twilio", Map.of("parent", "/Path/{WithParam}.json"));

        final String listTag = resourceTree.addResource(listPath, listPathItem);
        final String instanceTag = resourceTree.addResource(instancePath, instancePathItem);
        final String nestedTag = resourceTree.addResource(nestedPath, nestedPathItem);

        assertNotEquals("Different paths should yield different tags", listTag, instanceTag);

        final Resource listResource = resourceTree.getResourceByTag(listTag).orElseThrow();
        final Resource instanceResource = resourceTree.getResourceByTag(instanceTag).orElseThrow();
        final Resource nestedResource = resourceTree.getResourceByTag(nestedTag).orElseThrow();

        assertEquals(listPathItem, listResource.getPathItem());
        assertEquals(instancePathItem, instanceResource.getPathItem());
        assertEquals(nestedPathItem, nestedResource.getPathItem());

        assertEquals(listResource, resourceTree.findResource(listPath).orElseThrow());
        assertEquals(instanceResource, resourceTree.findResource(instancePath).orElseThrow());
        assertEquals(nestedResource, resourceTree.findResource(nestedPath).orElseThrow());

        assertEquals(List.of("Path"), resourceTree.ancestors(listPath, new Operation()));
        assertEquals(List.of("Path"), resourceTree.ancestors(instancePath, new Operation()));
        assertEquals(List.of("Path", "Nested"), resourceTree.ancestors(nestedPath, new Operation()));
    }
}
