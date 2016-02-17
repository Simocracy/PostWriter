#-*- coding:utf-8 -*-
import urllib, re, urllib2, cookielib
"""
Skript zur Auslese der aktuellen Postwriterversion
12-05-02 Fluggs

Input: py_wikiusername, py_wikipassword
Output: 
    py_loginError -- 0 bei erfolgreichem Wikilogin, Errorstring bei Fehler
    py_pwrVersion -- Postwriter-Versionsstring aus dem Versionskommentar /
        False, wenn kein Versionskommentar gefunden
    py_pwrVersionError -- 0 bei erfolgreicher Versionsauslese, Errorstring bei Fehler
"""

def wikilogin(username, password):
    """
    wikilogin -- stellt einen ins Wiki eingeloggten urllib2.build_opener bereit
    Mit Normalnutzerdaten nur für Auslese verwenden, NICHT für Änderungen im Wiki!
    Chef sagt, wir brauchen dafür nen Botuser.
    
    Globale Variablen: wikiopener, headers
    Output: Fehlerfrei: 0; Fehler: Errorstring
    Fluggs 12-05-02
    """

    # Login
    loginurl = "http://simocracy.de/index.php?title=Spezial:Anmelden&action=submitlogin&type=login"
    
    # Request header
    global headers
    headers = {
        'User-agent' : 'Fluggs Sammelkraken',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
        'Accept-Language': 'de-de,de;q=0.8,en-us;q=0.5,en;q=0.3',
        'Accept-Encoding': 'gzip,deflate',
        'Accept-Charset': 'ISO-8859-1,utf-8;q=0.7,*;q=0.7',
        'Connection': 'keep-alive',
        'Referer': 'http://simocracy.de/index.php?title=Spezial:Anmelden',
        'Content-Type': 'application/x-www-form-urlencoded',
        'Content-Lenght': '205'
    }
    
    # wikiopener basteln
    jar = cookielib.CookieJar()
    global wikiopener
    wikiopener = urllib2.build_opener(urllib2.HTTPCookieProcessor(jar))
    wikiopener.addheader = headers
    
    #Auslese des Login Tokens
    gettokenurl = wikiopener.open('http://simocracy.de/index.php?title=Spezial:Anmelden')
    gettokenlines = gettokenurl.readlines()
    for entry in gettokenlines:
        if re.search('<input type="hidden" name="wpLoginToken"', entry, re.S) != None:
            logintoken = re.sub('<input type="hidden" name="wpLoginToken" value="', '', entry)
            logintoken = re.sub('" /></form>', '', logintoken)
            logintoken = re.sub("\n", '', logintoken)
    gettokenurl.close()
    
    # POST fürn Login
    loginname = urllib.urlencode({'wpName' : username})
    loginpass = urllib.urlencode({'wpPassword' : password})
    loginreme = urllib.urlencode({'wpRemember' : "1"})
    loginatte = urllib.urlencode({'wpLoginAttempt' : "Anmelden"})
    logintoke = urllib.urlencode({'wpLoginToken' : logintoken})
    loginpostraw = loginname + "&" + loginpass + "&" + loginreme + "&"
    loginpostraw = loginpostraw + loginatte + "&" + logintoke
    
    # Send Request
    response = wikiopener.open(loginurl, loginpostraw)
    loginResponseUrl = response.geturl()
    wikiopener.close()

    if loginResponseUrl == 'http://simocracy.de/index.php?title=Spezial:Anmelden&action=submitlogin&type=login':
        return "Ungültiger Login"
    elif loginResponseUrl == 'http://simocracy.de/index.php/Hauptseite':
        return 0
    else:
        return "Woah, irgendwas is ganz krass schiefgelaufen."

def pwr_vAuslese():
    versionssuchstring = '&lt;!--version='
    pwrEditurl = wikiopener.open('http://simocracy.de/index.php?title=PostWriter&action=edit')
    pwrEdithtml = pwrEditurl.readlines()
    pwrEditurl.close()
    
    boolVersion = False
    for entry in pwrEdithtml:
        if re.search(versionssuchstring, entry, re.S) != None:
            version = entry
            boolVersion = True
            break
    
    # Von Anhängseln und Schnipseln befreien
    if boolVersion:
        version = version.strip()
        version = re.sub('<!--version=', '', version)
        version = re.sub('&lt;!--version=', '', version)
        version = re.sub('-->', '', version)
        version = version.strip()
        return version
    else:
        return False

loginstatus = wikilogin(py_wikiusername, py_wikipassword)
if loginstatus == 0:
    pwrVersion = pwr_vAuslese()
    py_pwrVersion = pwrVersion
    py_loginError = 0
    if pwrVersion != False:
        py_pwrVersionError = 0
    else:
        py_pwrVersionError = "Konnte keinen Versionskommentar finden."
else:
    py_loginError = loginstatus
    py_pwrVersionError = "Ungültiger Login"
    py_pwrVersion = False
