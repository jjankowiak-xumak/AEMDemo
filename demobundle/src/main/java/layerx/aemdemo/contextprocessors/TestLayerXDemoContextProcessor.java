package layerx.aemdemo.contextprocessors;

import com.google.common.collect.Sets;
import layerx.core.contextprocessors.AbstractCheckComponentCategoryContextProcessor;
import layerx.api.ContentModel;
import layerx.api.ExecutionContext;
import layerx.api.exceptions.ProcessException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;

import java.util.Set;

import static layerx.Constants.LOW_PRIORITY;
import static layerx.aem.Constants.SLING_HTTP_REQUEST;


@Component
@Service
public class TestLayerXDemoContextProcessor extends
        AbstractCheckComponentCategoryContextProcessor<ContentModel> {

    @Override
    public Set<String> anyOf() {
        return Sets.newHashSet("testlayerx");
    }

    @Override
    public int priority() {
        return LOW_PRIORITY;
    }

    @Override
    public void process(ExecutionContext executionContext, ContentModel contentModel)
            throws ProcessException {

        try {
            final SlingHttpServletRequest request = (SlingHttpServletRequest) executionContext.get(SLING_HTTP_REQUEST);
            JSONArray list = new JSONArray();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", "ID 1");
            jsonObject.put("title", "value 1");
            list.add(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("id", "ID 2");
            jsonObject.put("title", "value 2");
            list.add(jsonObject);

            contentModel.set("content.list", list);

            ValueMap valueMap = request.getResource().getValueMap();
            if (valueMap.containsKey("style")) {
                contentModel.set("content."+valueMap.get("style", String.class), true);
            }
            if ("test me".equals(valueMap.get("heading", String.class))) {
                contentModel.set("content.heading", "My test me heading: "+valueMap.get("heading", String.class) + " ...");
            }

        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
