package ru.itis.fpvhub.util;

import java.text.Normalizer;
import java.util.Locale;

public final class SlugGenerator {

    private SlugGenerator() {
    }

    public static String fromTitle(String source) {
        if (source == null || source.isBlank()) {
            return "article";
        }

        String normalized = Normalizer.normalize(source, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replace("ё", "e")
                .replace("а", "a")
                .replace("б", "b")
                .replace("в", "v")
                .replace("г", "g")
                .replace("д", "d")
                .replace("е", "e")
                .replace("ж", "zh")
                .replace("з", "z")
                .replace("и", "i")
                .replace("й", "y")
                .replace("к", "k")
                .replace("л", "l")
                .replace("м", "m")
                .replace("н", "n")
                .replace("о", "o")
                .replace("п", "p")
                .replace("р", "r")
                .replace("с", "s")
                .replace("т", "t")
                .replace("у", "u")
                .replace("ф", "f")
                .replace("х", "h")
                .replace("ц", "c")
                .replace("ч", "ch")
                .replace("ш", "sh")
                .replace("щ", "sch")
                .replace("ъ", "")
                .replace("ы", "y")
                .replace("ь", "")
                .replace("э", "e")
                .replace("ю", "yu")
                .replace("я", "ya")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        if (normalized.isBlank()) {
            return "article";
        }
        return normalized.length() > 180 ? normalized.substring(0, 180).replaceAll("-$", "") : normalized;
    }
}
