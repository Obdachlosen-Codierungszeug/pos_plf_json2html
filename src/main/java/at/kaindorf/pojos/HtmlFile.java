package at.kaindorf.pojos;

import at.kaindorf.annotations.HtmlBody;
import at.kaindorf.annotations.HtmlHead;
import at.kaindorf.annotations.HtmlRootFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@HtmlRootFile(name = "index", pretty = true)
public class HtmlFile {
    @JsonProperty("root")
    private List<HtmlElement> rootElements;

    @JsonProperty("head")
    @HtmlHead
    private List<HtmlElement> headElements;

    @JsonProperty("body")
    @HtmlBody
    private List<HtmlElement> bodyElements;
}
