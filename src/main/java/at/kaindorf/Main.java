package at.kaindorf;

import at.kaindorf.io.Mapper;
import at.kaindorf.pojos.HtmlFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Mapper mapper = new Mapper();
        Path resourcePath = Path.of("src", "main", "resources");

        try {
            HtmlFile file = mapper.readJson(Files.readString(resourcePath.resolve("html.json")), HtmlFile.class);
            mapper.writeHtml(file);
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}