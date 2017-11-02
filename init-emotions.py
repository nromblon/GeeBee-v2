#CALL doHappiness(characterName, 0 to 2)
#CALL doConcern(characterName, 0 to 2)
#CALL doHeadGesture(characterName)
#change saySomething

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
	
def doHeadGesture(characterName):
	from random import *
	gestureRandom = randrange(0, 6)
	shakeamount = nodamount = tossamount = 0
	
	if gestureRandom == 0:
		shakeamount = 0
		
	elif gestureRandom == 1:
		nodamount = uniform(0.00, 0.04)
	
	elif gestureRandom == 2:
		tossamount = uniform(0.00, 0.03)
		
	elif gestureRandom == 3: 
		shakeamount = uniform(0.00, 0.03)
		nodamount = uniform(0.00, 0.04)
	
	elif gestureRandom == 4:
		shakeamount = uniform(0.00, 0.03)
		tossamount = uniform(0.00, 0.03)
		
	elif gestureRandom == 5:
		nodamount = uniform(0.00, 0.04)
		tossamount = uniform(0.00, 0.03)
		
	bml.execBML(characterName, '<head type="SHAKE" velocity="0.5" amount="'+ str(shakeamount) +'" /><head type="NOD" velocity="0.5" amount="'+ str(nodamount) +'" /> <head type="TOSS" velocity="0.5" amount="' + str(tossamount) + '"/>')

def saySomething(actor, message):
	# bmlMsg = androidEngine.getNonverbalBehavior(message)
    # print bmlMsg
	bmlMsg = '<speech>' + message + '</speech>'
	stopTalking(actor)
	bmlID = bml.execBML(actor, bmlMsg)
	if emotion < 3:
		doHappiness(characterName, emotion)
	else:
		doConcern(characterName, emotion - 3)
	doHeadGesture(actor)