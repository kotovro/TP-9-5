package logic.utils;

import logic.general.Speaker;

import java.util.ArrayList;
import java.util.List;

public class Search {
    public static List<Integer> search(String text, List<Speaker> list) {
        List<String> speakerNames = list.stream()
                .map(Speaker::getName)
                .toList();
        
        List<Integer> matches = new ArrayList<>();
        for (int i = 0; i < speakerNames.size(); i++) {
            if (speakerNames.get(i).toLowerCase().contains(text.toLowerCase())) {
                matches.add(i);
            }
        }
        return matches;
    }
}
