package com.dotool.util;

import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;


/**
 * Created by IntelliJ IDEA.
 * User: chenqueming
 * Date: 2008-12-2
 * Time: 11:15:24
 * To change this template use File | Settings | File Templates.
 */
public class VelocityPubTool {

    /**
	 *
	 */
	private static HashMap dataMap = new HashMap();

    //ÓÊ¼þÄ£°åµØÖ·
    public static final String MailTemplateDir = "D:\\programing\\learn-velocity\\do-tool\\";

    public static VelocityEngine getVelocityEngine()
	{
        VelocityEngine ve = null;

        try
        {

            ve = (VelocityEngine)dataMap.get("VelocityEngine");

            if(ve == null)
            {
               
                    String tmpMailDir = MailTemplateDir;
                    Properties props = parse(tmpMailDir + "/velocity.properties");

                    props.setProperty("file.resource.loader.path" , tmpMailDir);


                    ve = new VelocityEngine();
                    ve.init(props);

                    dataMap.put("VelocityEngine",ve);
                }
            

        }
	    catch(Exception ce)
	    {
	        ce.printStackTrace();
	    }

	    return ve;
	}
    
    public static VelocityEngine getVelocityEngineByUtf8()
	{
        VelocityEngine ve = null;

        try
        {

            ve = (VelocityEngine)dataMap.get("VelocityEngine");

            if(ve == null)
            {
               
                    String tmpMailDir = MailTemplateDir;
                    Properties props = parse(tmpMailDir + "/velocity_utf8.properties");

                    props.setProperty("file.resource.loader.path" , tmpMailDir);


                    ve = new VelocityEngine();
                    ve.init(props);

                    dataMap.put("VelocityEngine",ve);
                }
            

        }
	    catch(Exception ce)
	    {
	        ce.printStackTrace();
	    }

	    return ve;
	}
	
	
	
	private static Properties parse(String cfgFilename)
		{
			
			System.out.println("cfgFilename="+cfgFilename);
			Properties props = new Properties();

			try {
			InputStream is = new FileInputStream(new File(cfgFilename));
			
			
				props.load(is);
				
				
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			return props;
			
		}
}
