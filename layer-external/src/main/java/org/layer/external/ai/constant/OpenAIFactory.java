package org.layer.external.ai.constant;

import java.util.List;

import org.layer.external.ai.dto.request.ArrayProperty;
import org.layer.external.ai.dto.request.ResponseFormat;
import org.layer.external.ai.dto.request.ItemProperties;
import org.layer.external.ai.dto.request.Items;
import org.layer.external.ai.dto.request.Properties;
import org.layer.external.ai.dto.request.Property;
import org.layer.external.ai.dto.request.JsonSchema;
import org.layer.external.ai.dto.request.Schema;

public class OpenAIFactory {

	public static ResponseFormat createJsonSchema(){
		Property answerProperty = new Property("string", "Frequency of responses based on people");
		Property countProperty = new Property("number", "Number of people who mentioned this content");

		// ItemProperties object for the items in the array
		ItemProperties itemProperties = new ItemProperties(answerProperty, countProperty);

		// Items object for the array property
		Items items = new Items("object", itemProperties, List.of("point", "count"), false);

		// ArrayProperty for the "responses" field
		ArrayProperty goodProperty = new ArrayProperty("array", "Top 3 good points", items);
		ArrayProperty badProperty = new ArrayProperty("array", "Top 3 bad points", items);
		ArrayProperty improvementProperty = new ArrayProperty("array", "Top 3 improvement points", items);
		ArrayProperty highFrequencyWordsProperty = new ArrayProperty("array", "Top 5 most mentioned words in all documents", items);

		// Properties object containing the "responses" property
		Properties properties = new Properties(goodProperty, badProperty, improvementProperty, highFrequencyWordsProperty);

		// Schema object
		Schema schema = new Schema("object", properties, List.of("good_points", "bad_points", "improvement_points", "high_frequency_words"), false);

		// Final ResponseFormat object
		JsonSchema jsonSchema = new JsonSchema("retrospect_analyze", true, schema);

		return new ResponseFormat("json_schema", jsonSchema);
	}
}
