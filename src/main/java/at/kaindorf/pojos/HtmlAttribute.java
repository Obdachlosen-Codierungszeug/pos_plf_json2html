package at.kaindorf.pojos;

import at.kaindorf.annotations.HtmlAttributeName;
import at.kaindorf.annotations.HtmlAttributeValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HtmlAttribute {
    @JsonProperty
    @HtmlAttributeName
    private String description;

    @JsonProperty
    @HtmlAttributeValue
    private String value;
}
