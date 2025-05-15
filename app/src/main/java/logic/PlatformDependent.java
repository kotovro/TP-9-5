package logic;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PlatformDependent {
    private static final String LIB_VOSK_PATH = "dynamic-resources/native/macOS/libvosk.dylib";
    public static final Platform CURRENT_PLATFORM;
    private static final String PLATFORM_PREFIX;
    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            CURRENT_PLATFORM = Platform.WINDOWS;
            PLATFORM_PREFIX = getWindowsPrefix();
        } else if (osName.contains("mac")) {
            CURRENT_PLATFORM = Platform.MACOS;
            PLATFORM_PREFIX = getMacOSPrefix();
            System.load(PLATFORM_PREFIX + LIB_VOSK_PATH);
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            CURRENT_PLATFORM = Platform.LINUX;
            PLATFORM_PREFIX = getLinuxPrefix();
        } else {
            throw new RuntimeException("Unsupported OS: " + osName);
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
}
