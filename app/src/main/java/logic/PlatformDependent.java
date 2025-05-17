package logic;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlatformDependent {
    private static final String LIB_VOSK_PATH = "dynamic-resources/native/macOS/libvosk.dylib";
    private static final String DEVELOP_RESOURCES_PATH = "develop_resources/dbcreation.sql";

    private static String PLATFORM_PREFIX = "";
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
        } else if (CURRENT_PLATFORM == Platform.WINDOWS) {
            PLATFORM_PREFIX = getWindowsPrefix();
        } else if (CURRENT_PLATFORM == Platform.MACOS) {
            PLATFORM_PREFIX = getMacOSPrefix();
        } else {
            PLATFORM_PREFIX = getLinuxPrefix();
        }

        if (CURRENT_PLATFORM == Platform.MACOS) {
            System.load(PLATFORM_PREFIX + LIB_VOSK_PATH);
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
        System.out.println("Im in Linux");
        return Paths.get("").toAbsolutePath().getParent().resolve("lib").toString();
    }
}
