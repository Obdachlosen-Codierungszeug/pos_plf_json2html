package at.kaindorf.pojos;

import at.kaindorf.annotations.HtmlTagAttributes;
import at.kaindorf.annotations.HtmlTagChildren;
import at.kaindorf.annotations.HtmlTagContent;
import at.kaindorf.annotations.HtmlTagName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HtmlElement {
    @JsonProperty("tag")
    @HtmlTagName
    private String name;

    @JsonProperty("content")
    @HtmlTagContent
    private String content;

    @JsonProperty
    private HtmlType type = HtmlType.SELF_CLOSING;

    @JsonProperty("attr")
    @HtmlTagAttributes
    private List<HtmlAttribute> attributes;

    @JsonProperty("inner")
    @HtmlTagChildren
    private List<HtmlElement> children;
}
