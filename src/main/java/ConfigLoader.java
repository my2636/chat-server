import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

class ConfigLoader {

    public static Map<String, String> loadConfig(String filePath) throws IOException {
        Map<String, String> config = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.forEach(line -> {
                if (!line.startsWith("#") && line.contains("=")) { // Пропускаем комментарии и строки без "="
                    String[] parts = line.split("=", 2); // Разделяем на ключ и значение
                    String key = parts[0].trim(); // Убираем пробелы в начале и конце ключа
                    String value = parts[1].trim(); // Убираем пробелы в начале и конце значения
                    config.put(key, value);
                }
            });
        }
        return config;
    }
}