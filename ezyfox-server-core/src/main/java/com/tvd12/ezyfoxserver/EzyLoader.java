/**
 * 
 */
package com.tvd12.ezyfoxserver;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzySettings;
import com.tvd12.ezyfoxserver.service.EzyJsonMapping;
import com.tvd12.ezyfoxserver.service.EzyXmlReading;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyControllersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyManagersImpl;

/**
 * @author tavandung12
 *
 */
public class EzyLoader {
    
    private Logger logger;
    private EzyConfig config;
    private ClassLoader classLoader;
    private EzyXmlReading xmlReading;
    private EzyJsonMapping jsonMapping;
    
    private EzyLoader() {
        this.logger = LoggerFactory.getLogger(getClass());
    }
    
    public static EzyLoader newInstance() {
    	return new EzyLoader();
    }
    
    public EzyServer load() {
    	EzyServer answer = new EzyServer();
    	answer.setConfig(config);
    	answer.setXmlReading(xmlReading);
    	answer.setManagers(newManagers());
    	answer.setJsonMapping(jsonMapping);
    	answer.setClassLoader(classLoader);
    	answer.setSettings(readSettings());
    	answer.setControllers(newControllers());
    	answer.setAppClassLoaders(newAppClassLoaders());
    	return answer;
    }
    
    public EzyManagers newManagers() {
    	return EzyManagersImpl.builder().build();
    }
    
    public EzyControllers newControllers() {
    	return EzyControllersImpl.builder().build();
    }
    
    private EzySettings readSettings() {
    	logger.info("read setting file: " + getSettingsFilePath());
    	return xmlReading.read(getSettingsFilePath(), EzySettings.class);
    }
    
    private Map<String, EzyAppClassLoader> newAppClassLoaders() {
        Map<String, EzyAppClassLoader> answer = new ConcurrentHashMap<>();
        for(File dir : getEntryFolders())
        	answer.put(dir.getName(), newAppClassLoader(dir));
        return answer;
    }
    
    private EzyAppClassLoader newAppClassLoader(File dir) {
    	logger.info("load " + dir);
        return new EzyAppClassLoader(dir, classLoader);
    }
    
    private File[] getEntryFolders() {
        File entries = getEntriesFolder();
        return entries.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
    }
    
    private File getEntriesFolder() {
        String entriesPath = getEntriesPath();
        File entries = new File(entriesPath);
        if(!entries.exists() || entries.isFile())
            throw new IllegalStateException("entries path " + 
                    entriesPath + " is not exists or is not a directory");
        return entries;
    }
    
    private String getEntriesPath() {
        return getPath(getAppsPath(), "entries");
    }
    
    private String getAppsPath() {
    	return getPath(getHomePath(), "apps");
    }
    
    private String getSettingsPath() {
    	return getPath(getHomePath(), "settings");
    }
    
    private String getSettingsFilePath() {
    	return getPath(getSettingsPath(), "ezy-settings.xml");
    }
    
    private String getPath(String first, String... more) {
        return Paths.get(first, more).toString();
    }
    
    private String getHomePath() {
    	return config.getEzyfoxHome();
    }
    
    @SuppressWarnings("unused")
    private String getVersion() {
    	return config.getEzyfoxVersion();
    }
    
    public EzyLoader classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }
    
    public EzyLoader config(EzyConfig config) {
    	this.config = config;
    	return this;
    }
    
    public EzyLoader xmlReading(EzyXmlReading xmlReading) {
    	this.xmlReading = xmlReading;
    	return this;
    }
    
    public EzyLoader jsonMapping(EzyJsonMapping jsonMapping) {
    	this.jsonMapping = jsonMapping;
    	return this;
    }
    
}
