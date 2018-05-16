package net.tnemc.core.configuration.impl;


import net.tnemc.core.configuration.IConfigNode;

public enum CoreConfigNodes implements IConfigNode {

  MAIN_HEADER {
    @Override
    public String getNode() {
      return "main";
    }
  },

  MAIN_METRICS {
    @Override
    public String getNode() {
      return "main.metrics";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not to enable plugin metrics. This lets us track plugin usage."
      };
    }
  },
  
  DATABASE_HEADER {
    @Override
    public String getNode() {
      return "database";
    }
  },
  
  DATABASE_TYPE {
    @Override
    public String getNode() {
      return "database.type";
    }

    @Override
    public String getDefaultValue() {
      return "MySQL";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "This is the database type you wish to use(Current options: H2, MySQL, Oracle, PostgreSQL)"
      };
    }
  },
  
  DATABASE_FILE {
    @Override
    public String getNode() {
      return "database.file";
    }

    @Override
    public String getDefaultValue() {
      return "TheNewEconomyDB";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "File name, minus extension, for any DB type which requires a file."
      };
    }
  },
  
  DATABASE_HOST {
    @Override
    public String getNode() {
      return "database.host";
    }

    @Override
    public String getDefaultValue() {
      return "localhost";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The host for the database."
      };
    }
  },
  
  DATABASE_PORT {
    @Override
    public String getNode() {
      return "database.port";
    }

    @Override
    public String getDefaultValue() {
      return "3306";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The port for the database."
      };
    }
  },
  
  DATABASE_DB {
    @Override
    public String getNode() {
      return "database.db";
    }

    @Override
    public String getDefaultValue() {
      return "TheNewBasics";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The name of the database to use for storage"
      };
    }
  },
  
  DATABASE_USER {
    @Override
    public String getNode() {
      return "database.user";
    }

    @Override
    public String getDefaultValue() {
      return "user";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The name of the user for the database"
      };
    }
  },
  
  DATABASE_PASSWORD {
    @Override
    public String getNode() {
      return "database.password";
    }

    @Override
    public String getDefaultValue() {
      return "password";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The password for the database user"
      };
    }
  }
}
