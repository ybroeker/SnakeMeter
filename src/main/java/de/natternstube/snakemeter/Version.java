package de.natternstube.snakemeter;

import java.util.logging.Logger;

/**
 * @author ybroeker
 */
public class Version {
    private static final Logger LOG = Logger.getLogger(Version.class.getName());

    final int major;
    final int minor;
    final int patch;
    final String suffix;
    final String build;

    public Version(String version) {
        String[] builds = version.split("\\+");
        if (builds.length == 2) {
            this.build = builds[1];
        } else {
            this.build = "";
        }

        String[] suffixes = builds[0].split("-");
        if (suffixes.length == 2) {
            this.suffix = suffixes[1];
        } else {
            this.suffix = "";
        }

        String[] versions = suffixes[0].split("\\.");

        if (versions.length > 0) {
            major = Integer.parseInt(versions[0]);
        } else {
            major = 0;
        }

        if (versions.length > 1) {
            minor = Integer.parseInt(versions[1]);
        } else {
            minor = 0;
        }

        if (versions.length > 2) {
            patch = Integer.parseInt(versions[2]);
        } else {
            patch = 0;
        }
    }

    public String fullVersion() {
        String version = "";
        version += major;

        if (minor != 0 || patch != 0) {
            version += "." + minor;
        }

        if (patch != 0) {
            version += "." + minor;
        }

        if (suffix != null && !suffix.isEmpty()) {
            version += "-" + suffix;
        }
        if (build != null && !build.isEmpty()) {
            version += "-" + build;
        }
        return version;
    }

    public boolean isNewerThan(Version that) {
        return (this.major > that.major)
                || this.major == that.major && this.minor > that.minor
                || this.major == that.major && this.minor == that.minor && this.patch > that.patch;
    }
}
