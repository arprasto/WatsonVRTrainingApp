/**
 *****************************************************************************
 * Copyright (c) 2017 IBM Corporation and other Contributors.

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Arpit Rastogi - Initial Contribution
 *****************************************************************************
 */
/*
 * Main class to launch the Scavenger hunt game.
 */


package com.ibm.watson.scavenger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watson.scavenger.CloudantNoSQLDB.DBCommunicator;
import com.ibm.watson.scavenger.iot.util.IoTUtil;
import com.ibm.watson.scavenger.speechToText.SpeechToTextWebSocketMain;
import com.ibm.watson.scavenger.textToSpeech.TTSMain;
import com.ibm.watson.scavenger.util.ScavengerContants;
import com.ibm.watson.scavenger.util.images.WatchDir;

public class PredictionApp 
{
	Logger LOG = Logger.getLogger(PredictionApp.class.getName());

	
    public static void main( String[] args ) throws Exception
    {
    	    	PredictionApp.getInstance().startGame();
    }    
    
    static PredictionApp obj = null;
    public static PredictionApp getInstance(){
    	if(obj == null){
    		obj=new PredictionApp();
    	}
    	return obj;
    }
    
    public TTSMain tts = null;
    public SpeechToTextWebSocketMain stt = null;
    public DBCommunicator dbsvc = null;
    public IoTUtil iotObj = null;
    public int unique_app_id = 0;
    public String random_img_obj_str = null;
    
    public void loadServices() throws MalformedURLException
    {
    	/* startup all Below watson services:
    	 * 
    	 * a. IBM Watson Text to Speech
    	 * b. IBM Watson Speech to Text
    	 * c. IBM Watson Cloudant No SQL DB service
    	 * d. IBM Watson IoT connect
    	 */
		tts = new TTSMain(ScavengerContants.TTS_uname,ScavengerContants.TTS_pass);
		stt = new SpeechToTextWebSocketMain(ScavengerContants.STT_uname,ScavengerContants.STT_pass);
		dbsvc = new DBCommunicator(ScavengerContants.cloudant_uname,ScavengerContants.cloudant_pass,ScavengerContants.cloudant_url,ScavengerContants.cloudant_dbName);
		iotObj = new IoTUtil();
    }
    

    void startGame()
    {
    	try{
    		loadServices();
		tts.playTextToSpeech("welcome to IBM bluemix platform. To start the game you can say the keyword like. game. scavenger hunt game. hunt game. To end the game anytime you can say the keyword like. exit. i am done. please exit.");
		
		Thread hearingThread = new Thread() {
			
			public void run() {
				stt.startSTT();
			}
		};
		
		//start the IBM Watson STT service thread		
		hearingThread.start();
		
		final Path dir = Paths.get(ScavengerContants.tmp_image_dir.toURI());

		Thread dirWatchThread = new Thread(){

		@Override
		public void run() {
			try{
	        new WatchDir(dir, true).processEvents();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		};
				
		//start the Watch thread to look after IMG file creation/updation in tmp_image_dir_path directory path. 
		dirWatchThread.start();
		
		
    	}catch(RuntimeException e)
    	{
    		LOG.log(Level.SEVERE,"looks like internet/service connectivity issue");
    		e.printStackTrace();
    	} catch (MalformedURLException e1) {
    		LOG.log(Level.SEVERE,"looks like some connectivity issue with DB service please check it.");
			e1.printStackTrace();
		}
    }
}
