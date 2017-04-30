Configurations
==================
The purpose of this document is to outline TNE's upcoming replacement for the default Bukkit configuration system that lacks some basic functionality, and
some quality of life fixes for it.

Example Syntax
----------------
This is just some of the changed syntax/features.
``` java
// We remove the various repetitive initialization tasks with custom configuration files.
// The first argument is the File instance for the configuration, or a string containing the file's location.
// The second argument is the File instance for the default configurations, or a string containing the file's 
   location in the jar.(optional, default null)
// The third argument is a boolean value that determines if we should copy defaults(optional, default true)
// The fourth argument is a boolean value that determines if we should remove configuration nodes that are present in 
   the existing configuration file, but not in the default.(optional, default true)
ConfigurationFile configuration = ConfigurationFile.load(File instance, "config.yml", true, true);

// The following will allow you to perform various actions using a node in a configuration file.
ConfigurationNode parentNode = configuration.node("Test.Configuration.Node");

// This returns a String List of the node keys immediately under the parentNode.
List<String> keys = parentNode.keys();

// This returns a String List of the node keys within the specified depth under the parentNode.
// The first argument is the max depth you wish to be checked for node keys.
List<String> keys = parentNode.keys(int depth);
```

Structure
---------------
The class structure for our configuration system.

### ConfigurationNode
 - Set<String> getKeys()
 - Set<String> getComments()
 - setComments(Set<String> comments)
 - ConfigurationNode get(String path)
 - setValue(String path, Object value)
 - Object getValue(String path)  
### ConfigurationFile extends ConfigurationNode  
 - static ConfigurationFile load(File file)
 - save()