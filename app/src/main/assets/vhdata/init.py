from VH import *
import math

import sys
sys.path.append("/sdcard/vhdata/pythonlibs")
print sys.modules.keys()

print "hola"

characterName = "ChrLindsay"
bmlID = 0

emotion = 2 #0-2 happy, 3-5 concern

def saySomething(actor, message):
	bmlMsg = androidEngine.getNonverbalBehavior(message)
	print "bmlMsg: " + bmlMsg
	stopTalking(actor)
	bmlID = bml.execXML(actor, bmlMsg)
	if emotion < 3:
		doHappiness(characterName, emotion)
	else:
		doConcern(characterName, emotion - 3)

def doHappiness(characterName, intensity):
	from random import *
	happiness = intensity
	
	mouth = cheek = eyebrow = 0
	if happiness == 0:
		mouth = 0.4
		cheek = 0.45
		eyebrow = 0.05
		
	elif happiness == 1:
		mouth = 0.65
		cheek = 0.65
		eyebrow = 0.15
	
	elif happiness == 2:
		mouth = 0.95
		cheek = 0.95
		eyebrow = 0.25
	
	mouthRandom = uniform(mouth, mouth + 0.05)
	cheekRandom = uniform(cheek, cheek + 0.05)
	eyebrow2Random = uniform(eyebrow, eyebrow + 0.15)
	#the inner should always be higher or else it will look angry / fearful / worried
	eyebrow1Random = uniform(eyebrow2Random, eyebrow + 0.1)
	lidRandom = uniform(0.35, 0.65)
	
	bmlExec = '<face type="FACS" stroke="3" au="1" velocity="0.5" amount="' + str(eyebrow1Random) + '" /><face type="FACS" stroke="3" velocity="0.5" au="2" amount="'+ str(eyebrow2Random) + '" /><face type="FACS" stroke="3" velocity="0.5" au="5" amount="'+ str(lidRandom) +'" /><face type="FACS" stroke="3" velocity="0.5" au="6" amount="' + str(cheekRandom) +'" /><face type="FACS" stroke="3" velocity="0.5" au="12" amount="' + str(mouthRandom) + '" />'
	bml.execBML(characterName, bmlExec)

def doConcern(characterName, intensity):
	from random import *
	concern = intensity
	
	lipRaise = cheek = lipGap = lowerbrow = eyebrow1 = eyebrow2 = 0.0
	
	if concern == 0:
		eyebrow2 = 0.3
		cheek = 0.05
		eyebrow1 = 0.90
		lowerbrow = 0.05
		lipGap = 0.15
		lipRaise = 0.35
		
	elif concern == 1:
		eyebrow2 = 0.15
		cheek = 0.1
		eyebrow1 = 0.90
		lowerbrow = 0.25
		lipGap = 0.45
		lipRaise = 0.55
	
	elif concern == 2:
		eyebrow2 = 0
		cheek = 0.15
		eyebrow1 = 0.90
		lowerbrow = 0.55
		lipGap = 0.85
		lipRaise = 0.85
		
	eyebrow1Random = uniform(eyebrow1, eyebrow1 + 0.1)
	eyebrow2Random = uniform(eyebrow2, eyebrow2 + 0.1)
	lowerbrowRandom = uniform(lowerbrow, lowerbrow + 0.1)
	lipGapRandom = uniform(lipGap, lipGap + 0.2)
	cheekRandom = uniform(cheek, cheek + 0.05)
	lipRaiseRandom = uniform(lipRaise, lipRaise + 0.15)
	
	lidRandom = uniform(0.07, 0.24)
	mouthRandom = uniform(0, 0.27)
	
	bmlExec = '<face type="FACS" au="1" amount="'+ str(eyebrow1Random) +'" /><face type="FACS" au="2" amount="' + str(eyebrow2Random) + '" /><face type="FACS" au="4" amount="'+ str(lowerbrowRandom) + '" /><face type="FACS" au="6" amount="'+ str(cheekRandom) +'" /><face type="FACS" au="7" amount="' + str(lidRandom) + '" /><face type="FACS" au="10" amount="' + str(lipRaiseRandom) + '" /><face type="FACS" au="12" amount="'+ str(mouthRandom) +'" /><face type="FACS" au="25" amount="'+ str(lipGapRandom) +'" />'
	bml.execBML(characterName, bmlExec)

def doBehavior(actor, message):
	bmlMsg = androidEngine.getBehavior(message, 0, 5)
	bml.execBML(actor, bmlMsg)
	print bmlMsg

def stopTalking(actor):
	bml.interruptCharacter(actor, bmlID)
	print "interrupt talking"
	bml.execBML(characterName, '<body posture="ChrBrad@Idle01"/>')
	bml.execBML(characterName, '<saccade mode="listen"/>')

def setToHappy(characterName, intensity):
	emotion = intensity

def setToConcern(characterName, intensity):
	emotion = intensity - 3

class MyVHEngine(VHEngine):
	def eventInit(self):
		print "Starting VH engine..."		
		self.initTextToSpeechEngine('cerevoice') # initialize tts engine for speech synthesis
	
	def eventInitUI(self):	
		self.setBackgroundImage('/sdcard/vhdata/office2.png')
				
androidEngine = MyVHEngine()
setVHEngine(androidEngine)		
	
scene.addAssetPath("script", "scripts")
scene.addAssetPath("motion", "characters")
scene.setBoolAttribute("internalAudio", True)
scene.run("camera.py")
scene.run("light.py")
scene.run("setupCharacter.py")
setupCharacter(characterName, characterName, "", "")
character = scene.getCharacter(characterName)
bml.execBML(characterName, '<body posture="ChrBrad@Idle01"/>')
bml.execBML(characterName, '<saccade mode="listen"/>')
sim.start()

# start the first utterance
#scene.command('bml char ChrRachel file ./Sounds/intro_age_1.xml')
