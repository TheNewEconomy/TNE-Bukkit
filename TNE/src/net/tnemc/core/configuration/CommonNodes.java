package net.tnemc.core.configuration;

public enum CommonNodes implements IConfigNode {
  VERSION_HEADER {
    @Override
    public String getNode() {
      return "version";
    }
  },

  VERSION {
    @Override
    public String getNode() {
      return "version.version";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "This is the current version of TheNewEconomy.  Please do not edit."
      };
    }
  },

  LAST_RUN_VERSION {
    @Override
    public String getNode() {
      return "version.last_run_version";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Please do not edit."
      };
    }
  },
}
