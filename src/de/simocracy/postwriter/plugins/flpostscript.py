#-*- coding:utf-8 -*-
"""
Skript zum automatischen Posten eines Posts in den Poli
12-05-09 Fluggs

Inputvars: py_username, py_password, py_sep, py_postpath
Outputvars: py_polipost_status, py_polipost_output
py_polipost_status: 0 bei erfolgreicher Durchführung, 1 bei Fehlern
py_polipost_output: Outputstring; Post-URL bei erf.D., Fehlermeldung bei Fehlern
py_sep: Line Separator, OS-abhängig
"""

def polipost(username, password, sep, postpath):
    import urllib, urllib2, cookielib, hashlib, sys, re
   
    threadid = "166677" #poli
   
    # Login
    loginurl = "http://www.simforum.de/login.php?do=login"
   
    md5 = hashlib.md5(password);md5 = md5.hexdigest()
   
    # POST fürn Login
    loginpost = {
        'do': 'login',
        'vb_login_md5password': md5,
        'vb_login_md5password_utf': md5,
        's': '',
        'vb_login_username': username,
        'security_token': 'guest',
    }
    loginpostraw = urllib.urlencode(loginpost)
   
    # Request header
   
    global headers
    headers = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
        'Accept-Language': 'es-es,es;q=0.8,en-us;q=0.5,en;q=0.3',
        'Accept-Encoding': 'gzip,deflate',
        'Accept-Charset': 'iso-8859-15,utf-8;q=0.7,*;q=0.7',
        'Connection': 'keep-alive',
        'Referer': loginurl,
        'Content-Type': 'application/x-www-form-urlencoded',
        'Content-Lenght': '205'
    }
   
    # Cookiemonster
    jar = cookielib.CookieJar()
    opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(jar))
   
    # Send Request
    opener.addheader = headers
    opener.open(loginurl, loginpostraw)
   
    postpageopen = opener.open('http://www.simforum.de/newreply.php?do=newreply&noquote=1&p=4273980') #poli
    postpagehtml = postpageopen.readlines()
   
    ### POST zusammenstellen
    postvalues = {}
   
    # message
    try:
        postfile = open(postpath, "r")
    except IOError:
        return [1, 'Ehh, ich find keinen Post.']
       
    postread = postfile.readlines()
    postlines = []
    for i in range(len(postread)):
        entry = postread[i]
        #a = entry.decode("utf-8")
        #a = a.encode("iso-8859-15")
        a = entry
        #a = re.sub(sep, "%3Cbr%3E", a)
        a = re.sub("\r\n", "%3Cbr%3E", a)
        a = re.sub("\n", "%3Cbr%3E", a)
        a = re.sub("\r", "%3Cbr%3E", a)
        a = re.sub('\s{2,}', ' ', a)
        a = re.sub("\s", "+", a)
        postlines.append(a)
    value_message = "".join(postlines)
   
    postvalues['message'] = value_message
    postvalues['wysiwyg'] = "1"
    postvalues['iconid'] = "0"
    postvalues['s'] = ""
   
    # aus html zu extrahieren; securitytoken, posthash, poststarttime, loggedinuser
    token = False
    posthash = False
    starttime = False
    loggedinuser = False
    bool_p = False
    for entry in postpagehtml:
        if re.search('<input type="hidden" name="securitytoken"', entry, re.S) != None:
            value_securitytoken = re.sub('<input type="hidden" name="securitytoken" value="', '', entry)
            value_securitytoken = re.sub('" />', '', value_securitytoken)
            value_securitytoken = re.sub("\n", '', value_securitytoken)
            value_securitytoken = value_securitytoken.strip()
            token = True
        elif re.search('<input type="hidden" name="posthash"', entry, re.S) != None:
            value_posthash = re.sub('        <input type="hidden" name="posthash" value="', '', entry)
            value_posthash = re.sub('" />', '', value_posthash)
            value_posthash = re.sub("\n", '', value_posthash)
            posthash = True
        elif re.search('<input type="hidden" name="poststarttime"', entry, re.S) != None:
            value_poststarttime = re.sub('        <input type="hidden" name="poststarttime" value="', '', entry)
            value_poststarttime = re.sub('" />', '', value_poststarttime)
            value_poststarttime = re.sub("\n", '', value_poststarttime)
            starttime = True
        elif re.search('<input type="hidden" name="loggedinuser"', entry, re.S) != None:
            value_loggedinuser = re.sub('        <input type="hidden" name="loggedinuser" value="', '', entry)
            value_loggedinuser = re.sub('" />', '', value_loggedinuser)
            value_loggedinuser = re.sub("\n", '', value_loggedinuser)
            loggedinuser = True
        elif re.search('<input type="hidden" name="p"', entry, re.S) != None:
            value_p = re.sub('        <input type="hidden" name="p" value="', '', entry)
            value_p = re.sub('" />', '', value_p)
            value_p = re.sub("\n", '', value_p)
            bool_p = True
   
    if value_securitytoken.strip() == "guest":
        return [1, "Login war nicht erfolgreich. Vermutlich stimmen Username oder Passwort nicht."]
    if token == False:
        return [1, "Security Token nicht gefunden."]
    if posthash == False:
        return [1, "posthash nicht gefunden."]
    if starttime == False:
        return [1, "poststarttime nicht gefunden."]
    if loggedinuser == False:
        return [1, "loggedinuser nicht gefunden."]
    if bool_p == False:
        return [1, "p nicht gefunden."]
    value_t = threadid
    postvalues['securitytoken'] = value_securitytoken
    postvalues['do'] = "postreply"
    postvalues['t'] = value_t
    postvalues['p'] = value_p
    postvalues['specifiedpost'] = "0"
    postvalues['posthash'] = value_posthash
    postvalues['poststarttime'] = value_poststarttime
    postvalues['loggedinuser'] = value_loggedinuser
    postvalues['multiquoteempty'] = ""
    postvalues['sbutton'] = "Antworten"
    postvalues['signature'] = "1"
    postvalues['parseurl'] = "1"
    postvalues['emailupdate'] = "9999"
   
    postraw = "title="
    for key in postvalues:
        postraw = postraw + "&" + key + "=" + postvalues[key]
   
    # Abschicken des Posts
    try:
        posturl = "http://www.simforum.de/newreply.php?do=postreply&t=" + threadid
        response = opener.open(posturl, postraw)
        the_page = response.read()
        posturl = response.geturl()
        if posturl == "http://www.simforum.de/newreply.php?do=postreply&t=166677":
            return [1, "Das Posten schlug fehl. Ist der Post zu kurz?"]
        return [0, posturl]
    except Exception, e:
        return [1, 'Err ' + str(e)]
       
py_polipost = polipost(py_username, py_password, py_sep, py_postpath)
py_polipost_status = py_polipost[0]
py_polipost_output = py_polipost[1]