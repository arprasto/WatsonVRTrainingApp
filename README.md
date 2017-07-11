Introduction:

In this developer journey we will create Scavenger Hunt game based on Watson's below services:

1. Speech To Text(STT)
2. Text To Speech(TTS)
3. Visual Recognition(VR)
4. Watson IoT
5. Cloudant NoSQL storage service

This scavenger app consists of below two applications:
1. Training App(to create and train a new custom classifier using Watson's VR)
2. Scavenger App(to demonstrate the prediction results of each image through created custom classifier including default one.)

Scavenger Hunt game is standalone java based application built on top of Linux based platform which can be run independently on IoT based device such as RaspPi or MacBook or any linux based operating system.

After completing this journey you will understand how to:
a. Authorize a person through creating a custom classifier in IBM Watson visual recognition.
b. Recognize speech using Speech To Text service of Watson.
c. User friendly interaction by playing WAV stream received by calling Text to Speech.
d. Store Images on cloud using IBM bluemix Cloudant DB service. 


Dependency:

Note: this app has been tested on MacBook with below versions:
1. java version "1.8.0_121"
2. Apache Maven 3.2.5
3. curl 7.52.1
4. Zip 3.0

Pre Requisite:

1. goto bluemix catalog home page and under "Apps" select "Boilerplates".
2. click "Internet of Things Platform Starter".
3. Enter app name and other required inputs and click "create" button.
4. this will configure below three things for you to start with IoT stuff:
	* Node-Red JS sdk
	* IoT Platform service
	* Cloudant NoSQL DB service
5. get the credentials for below services:
	a. Speech to Text
	b. Text to Speech
	c. Visual Recognition
	d. IoT (created in previus step)
	e. Cloudant NoSQL(created in previus step)

Before you begin:

a. Register your IoT device on WatsonIoTPlatform:

	1. goto your Bluemix services catalog and click IOT-service created in previous step.
	2. On this page you will see Launch button, click this. This will open saperate IBM Watson IoT Platform home page for your service.
	3. Select DEVICES option in left menu panel.
	4. Click on "+Add Device" button. Now follow the steps mentioned <a href="https://console.bluemix.net/docs/services/IoT/iotplatform_task.html#iotplatform_task">here</a>
	5. Once you register your device update below to your properties file which we will create in next step:

		Organization ID xxxx
		Device Type xxxxx
		Device ID xxxxx
		Authentication xxxxxx
		Authentication Token xxxxxx

b. create Node-Red device to receive events/send notification back to IoT device.

	1. goto the home page of boilerplate app created in Pre Requisite steps.
	2. click "overview" menu option in left page panel.
	3. on curren page you will see "Visit App URL" hyper link. This will load your Node-Red editor in saperate page.
	4. On this Node Red editor page cllick menu on top right corner and select 'Import' -> 'Clipboard'.
	5. In pop up input dialog copy and paste from NodeRedJsIoTReciever.json file attached with this code base.
	6. Click on 'Deploy' button. 

How to run it:

1. Create an properties file similar to sample-properties.properties provided.
2. Update Watson bluemix credentials in for each service in above created property file.
3. Clone this application code into your local env using 'git clone https://github.com/arprasto/ScavengerHuntGame'
3. Run the maven build through 'mvn clean install'. This will create 2 jars(one with dependencies and one with app code binaries)
4. Main class to start any of above mentioned app is com.ibm.watson.scavenger.LaunchApp. 

You can run any of below application:

a. Running the Training app

	1. run the below java command on your shell prompt:
	java -cp scavengerHunt-0.0.1-SNAPSHOT-jar-with-dependencies.jar:scavengerHunt-0.0.1-SNAPSHOT.jar com.ibm.watson.scavenger.LaunchApp sample-properties.properties train
	2. Please listen the announce message from STT service and create custom image classifier using launched java web cam.  
	3. After creating the classifier this app will automatically exit.

b. Running the Scavenger app

	1. run the below java command on your shell prompt:
	java -cp scavengerHunt-0.0.1-SNAPSHOT-jar-with-dependencies.jar:scavengerHunt-0.0.1-SNAPSHOT.jar com.ibm.watson.scavenger.LaunchApp sample-properties.properties scavenger
	2. To start the game your voice will be recognized through Watson STT service to recognize any of key work like:'game' or 'scavenger hunt game' or 'hunt game'.
	2. This will connect your device to Watson IoT and captured images will be stored in configured CLOUDANT DB.
	3. To exit from this app your voice will be recognized through Watson STT service to recognize any of key work like: 'exit' or 'i am done' or 'i'm done'.