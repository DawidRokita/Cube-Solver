import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class AssetTableLoader {
    private AssetTableLoader() {}

    public static List<String> readLines(Context ctx, String assetPath) throws IOException {
        try (InputStream is = ctx.getAssets().open(assetPath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
            return lines;
        }
    }

    public static String readAll(Context ctx, String assetPath) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String line : readLines(ctx, assetPath)) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }
}
