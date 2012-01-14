/*
Copyright 2009-2010 Igor Polevoy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


package org.javalite.activejdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.javalite.activejdbc.cache.CacheManager;
import org.javalite.activejdbc.dialects.DefaultDialect;
import org.javalite.activejdbc.dialects.H2Dialect;
import org.javalite.activejdbc.dialects.MSSQLDialect;
import org.javalite.activejdbc.dialects.MySQLDialect;
import org.javalite.activejdbc.dialects.OracleDialect;
import org.javalite.activejdbc.dialects.PostgreSQLDialect;
import org.javalite.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Igor Polevoy
 */
public class Configuration {

    private List<String> modelsIndex = new ArrayList<String>();
    private Properties properties = new Properties();
    private static CacheManager cacheManager;
    final static Logger logger = LoggerFactory.getLogger(Configuration.class);

    private  Map<String, DefaultDialect> dialects = new HashMap<String, DefaultDialect>();

    protected Configuration(){
        try {
            Enumeration<URL> resources = getClass().getClassLoader().getResources("activejdbc_models.properties");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                LogFilter.log(logger, "Load models from: " + url.toExternalForm());
                InputStream inputStream = null;
                try {
                    inputStream = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line ;
                    while(  (line = reader.readLine()) != null ){
                        modelsIndex.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if( inputStream != null )inputStream.close();
                }
            }
        } catch (IOException e) {
            throw new InitException(e);
        }
        if(modelsIndex.isEmpty()){
            LogFilter.log(logger, "ActiveJDBC Warning: Cannot locate any models, assuming project without models.");
            return;
        }
        try{
            InputStream in = getClass().getResourceAsStream("/activejdbc.properties");
            if( in != null ) properties.load(in);
        }
        catch(Exception e){
            throw new InitException(e);
        }


        String cacheManagerClass = properties.getProperty("cache.manager");
        if(cacheManagerClass != null){

            try{
                Class cmc = Class.forName(cacheManagerClass);
                cacheManager = (CacheManager)cmc.newInstance();
            }catch(Exception e){
                throw new InitException("failed to initialize a CacheManager. Please, ensure that the property " +
                        "'cache.manager' points to correct class which extends 'activejdbc.cache.CacheManager' class and provides a default constructor.", e);
            }

        }
    }

    String[] getModelNames() throws IOException {
        return modelsIndex.toArray(new String[modelsIndex.size()]) ;
    }


    public boolean collectStatistics(){
        return properties.getProperty("collectStatistics", "false").equals("true");
    }

    public boolean cacheEnabled(){
        return cacheManager != null;
    }

    DefaultDialect getDialect(MetaModel mm){
        if(dialects.get(mm.getDbType()) == null){
            if(mm.getDbType().equalsIgnoreCase("Oracle")){
                dialects.put(mm.getDbType(), new OracleDialect());
            }
            else if(mm.getDbType().equalsIgnoreCase("MySQL")){
                dialects.put(mm.getDbType(), new MySQLDialect());
            }
            else if(mm.getDbType().equalsIgnoreCase("PostgreSQL")){
                dialects.put(mm.getDbType(), new PostgreSQLDialect());
            }
            else if(mm.getDbType().equalsIgnoreCase("h2")){
                dialects.put(mm.getDbType(), new H2Dialect());
            }
            else if(mm.getDbType().equalsIgnoreCase("Microsoft SQL Server")){
                dialects.put(mm.getDbType(), new MSSQLDialect());
            }
            else{
                dialects.put(mm.getDbType(), new DefaultDialect());
            }
        }

        return dialects.get(mm.getDbType());
    }


    public CacheManager getCacheManager(){
        return cacheManager;
    }
}