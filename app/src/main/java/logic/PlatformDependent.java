package logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlatformDependent {
    private static final String LIB_VOSK_PATH = "dynamic-resources/native/macOS/libvosk.dylib";
    private static final String DEVELOP_RESOURCES_PATH = "develop_resources/dbcreation.sql";
    private static final String APP_NAME = "Vstrecheslav";

    private static final String SAVE_DIR;

    private static final String PLATFORM_PREFIX;
    public static final Platform CURRENT_PLATFORM;

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            CURRENT_PLATFORM = Platform.WINDOWS;
        } else if (osName.contains("mac")) {
            CURRENT_PLATFORM = Platform.MACOS;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            CURRENT_PLATFORM = Platform.LINUX;
        } else {
            throw new RuntimeException("Unsupported OS: " + osName);
        }

        if (new File(DEVELOP_RESOURCES_PATH).exists()) {
            PLATFORM_PREFIX = "";
            SAVE_DIR = "dynamic-resources/saves/";
        } else if (CURRENT_PLATFORM == Platform.WINDOWS) {
            PLATFORM_PREFIX = getWindowsPrefix();
            SAVE_DIR = Paths.get(System.getenv("APPDATA"), APP_NAME).toAbsolutePath().toString() + "/";
        } else if (CURRENT_PLATFORM == Platform.MACOS) {
            PLATFORM_PREFIX = getMacOSPrefix();
            SAVE_DIR = Paths.get(System.getProperty("user.home"), "Library", "Application Support", APP_NAME).toAbsolutePath().toString() + "/";
        } else {
            PLATFORM_PREFIX = getLinuxPrefix();
            SAVE_DIR = Paths.get(System.getProperty("user.home"), ".local", "share", APP_NAME).toAbsolutePath().toString() + "/";
        }

        if (CURRENT_PLATFORM == Platform.MACOS) {
            System.load(new File(PLATFORM_PREFIX + LIB_VOSK_PATH).getAbsolutePath());
        }

        try {
            Files.createDirectories(Path.of(SAVE_DIR));
        } catch (IOException e) {
            System.err.println("Не удалось создать директорию: " + e.getMessage());
        }
    }

    public static String getPrefix() {
        return PLATFORM_PREFIX;
    }

    private static String getWindowsPrefix() {
        return "";
    }

    private static String getMacOSPrefix() {
        Path javaHome = Paths.get(System.getProperty("java.home"));
        Path appPath = javaHome.getParent().getParent().getParent();
        return appPath.toString() + "/";
    }

    private static String getLinuxPrefix() {
        return "";
    }

    public static String getPathToSaves() {
        return SAVE_DIR;
    }
}
