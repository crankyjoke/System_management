package com.example.demo.functions;

import java.util.*;
import java.util.regex.*;

public class CourseCopier {
    private final String rawData;

    public CourseCopier(String data) {
        this.rawData = data;
    }

    public Map<String, String> convert() {
        String cleaned = rawData
                .replace("\\r", "")
                .replace("\\n", " ")
                .replace("\\t", " ")
                .replaceAll("\\s{2,}", " ");

        Pattern COURSE_PATTERN = Pattern.compile("(ECE|MATH|GENE)\\s\\d{3}");
        Pattern SECTION_PATTERN = Pattern.compile("(\\d{4})\\s+(\\d{3})");

        Map<String, String> courseMap = new LinkedHashMap<>();
        Matcher courseMatcher = COURSE_PATTERN.matcher(cleaned);

        List<Integer> starts = new ArrayList<>();
        List<String> codes  = new ArrayList<>();

        while (courseMatcher.find()) {
            codes.add(courseMatcher.group());
            starts.add(courseMatcher.start());
        }
        starts.add(cleaned.length());  // sentinel for the last block

        for (int i = 0; i < codes.size(); i++) {
            String block = cleaned.substring(starts.get(i), starts.get(i + 1));
            Matcher m = SECTION_PATTERN.matcher(block);
            if (m.find()) {
                courseMap.put(codes.get(i), m.group(2)); // 3‑digit section
            }
        }
        for (Map.Entry<String, String> entry : courseMap.entrySet()) {
            System.out.println("Course: " + entry.getKey() + ", Section: " + entry.getValue());
        }
        return courseMap;
    }
}
