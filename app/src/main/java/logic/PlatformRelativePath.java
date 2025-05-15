package logic;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PlatformRelativePath {
    private static String PLATFORM_PREFIX = "";
    {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            PLATFORM_PREFIX = getWindowsPrefix();
        } else if (osName.contains("mac")) {
            PLATFORM_PREFIX = getMacOSPrefix();
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
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
