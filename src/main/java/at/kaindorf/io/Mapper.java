package at.kaindorf.io;

import at.kaindorf.annotations.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Mapper {
    public <T> T readJson(String path, Class<T> clazz) throws JsonProcessingException {
        JsonMapper mapper = new JsonMapper();
        return mapper.readValue(path, clazz);
    }

    public void writeHtml(Object object) throws IOException, IllegalAccessException {
        Class<?> objectClass = object.getClass();
        HtmlRootFile rootFile = objectClass.getAnnotation(HtmlRootFile.class);

        if (rootFile == null) {
            throw new IllegalArgumentException("Class is not annotated with @HtmlRootFile");
        }

        StringBuilder sb = new StringBuilder();
        String newLine = rootFile.pretty() ? "\n" : "";
        String indent = rootFile.pretty() ? "\t" : "";

        sb.append("<!DOCTYPE html>").append(newLine);

        for (var field : objectClass.getDeclaredFields()) {
            field.setAccessible(true);

            if (!field.getType().equals(List.class)) continue;
            if (field.get(object) == null) continue;

            String tagName = field.isAnnotationPresent(HtmlHead.class) ? "head"
                    : field.isAnnotationPresent(HtmlBody.class) ? "body"
                    : "root";

            sb.append("<").append(tagName).append(">").append(newLine);
            sb.append(getHtmlElements((List<?>) field.get(object), rootFile.pretty())
                    .lines()
                    .map(s -> indent + s + newLine)
                    .collect(Collectors.joining()));
            sb.append("</").append(tagName).append(">").append(newLine);
        }

        System.out.println(sb.toString());
        Files.writeString(
                Path.of("src", "main", "resources", rootFile.name() + ".html"),
                sb.toString());
    }

    private String getHtmlElements(List<?> elements, boolean pretty) {
        return elements.stream()
                .map(element -> {
                    try {
                        return getHtmlElement(element, pretty);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining());
    }

    private String getHtmlElement(Object element, boolean pretty) throws IllegalAccessException {
        Class<?> elementClass = element.getClass();

        String tagName = "";
        String tagContent = "";
        Map<String, String> tagAttributes = new HashMap<>();
        String tagChildren = "";

        for (var field : elementClass.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.get(element) == null) continue;

            if (field.isAnnotationPresent(HtmlTagName.class)) {
                tagName = field.getAnnotation(HtmlTagName.class).upper()
                        ? ((String) field.get(element)).toUpperCase()
                        : ((String) field.get(element)).toLowerCase();
            } else if (field.isAnnotationPresent(HtmlTagContent.class)) {
                tagContent = (String) field.get(element);
            } else if (field.isAnnotationPresent(HtmlTagAttributes.class)) {
                tagAttributes = getHtmlElementAttributes((List<?>) field.get(element));
            } else if (field.isAnnotationPresent(HtmlTagChildren.class)) {
                tagChildren = getHtmlElements((List<?>) field.get(element), pretty);
            }
        }

        StringBuilder sb = new StringBuilder();
        String newLine = pretty ? "\n" : "";
        String indent = pretty ? "\t" : "";

        sb.append("<").append(tagName);

        if (!tagAttributes.isEmpty()) {
            tagAttributes.entrySet()
                    .stream()
                    .map(entry -> " " + entry.getKey() + "=\"" + entry.getValue() + "\"")
                    .forEach(sb::append);
        }

        sb.append(">");

        if (!tagContent.isBlank()) {
            sb.append(tagContent).append("</").append(tagName).append(">");
        } else if (!tagChildren.isBlank()) {
            sb.append(newLine).append(tagChildren
                    .lines()
                    .map(s -> indent + s + newLine)
                    .collect(Collectors.joining()));
            sb.append("</").append(tagName).append(">");
        }

        return sb.append(newLine).toString();
    }

    private Map<String, String> getHtmlElementAttributes(List<?> element) throws IllegalAccessException {
        Map<String, String> attributes = new LinkedHashMap<>();

        for (Object attribute : element) {
            Class<?> attributeClass = attribute.getClass();

            String attributeName = null;
            String attributeValue = null;

            for (var field : attributeClass.getDeclaredFields()) {
                field.setAccessible(true);

                if (field.isAnnotationPresent(HtmlAttributeName.class)) {
                    attributeName = (String) field.get(attribute);
                } else if (field.isAnnotationPresent(HtmlAttributeValue.class)) {
                    attributeValue = (String) field.get(attribute);
                }
            }

            attributes.put(attributeName, attributeValue);
        }

        return attributes;
    }
}
