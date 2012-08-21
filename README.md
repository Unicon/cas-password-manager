# CAS Password Manager

* Drew Mazurek <dmazurek@unicon.net>
* Drew Wills <awills@unicon.net>
* Unicon, Inc.


Background
----------
This project is an extension of the CAS LPPE work which has been integrated
into CAS 3.5. The original password manager work was performed for Onondaga 
Community College (http://www.sunyocc.edu), who graciously allowed us to 
make this product available to the public. The CAS 3.5 upgrade was sponsored
by the University of Kansas (http://ku.edu) and by the Unicon CAS 
Cooperative Support program (http://www.unicon.net/support/cooperative).

Features
--------
The CAS PM extension provides basic password management tasks to users with
minimal disruption to their browsing process. If their password has expired,
they're asked to change it during the login flow and then they're sent
on to the application they were initially trying to access. The idea
was to make password management as simple and unobtrusive as possible for
the end user. Features include:

  * expired password reset
  * forgotten password reset using security questions based on prepopulated 
    (e.g. ID number, birthday, etc.) or custom security questions.
  * force users to configure their custom security question if one is not
    set. (Configurable.)
  * support for Active Directory and OpenLDAP. (Password expiration
    features aren't available for OpenLDAP.)
  * configurable password security level enforcement (E.g. 8 characters, 
    upper/lowercase, etc.)
  * password change lockout with too many incorrect security question attempts
  * ReCAPTCHA integration to aid against scripted attacks
    
Additionally, Active Directory users receive the benefits of the LPPE
integration that provides enhanced login failure messages in the following
situations:

  * account is disabled
  * account is expired
  * account is attempting to login at an unaccepted time
  * account is attempting to login from an unaccepted workstation
  * account must change password on login (handled by password manager)
  * account password will expire soon (handled by password manager)

Build & Installation
--------------------

Prerequisites
-------------

* Maven 3+ (http://maven.apache.org)
* Git client (http://git-scm.com)

* This source code:

`$ git clone https://github.com/unicon/cas-password-manager.git`

* Recaptcha4j version 0.0.8:

Unfortunately, Recaptcha4j 0.0.8 is still not available through Maven.
You'll have to download and install it in your local build machine's
Maven repository:

`$ wget 'http://recaptcha4j.googlecode.com/files/recaptcha4j-0.0.8.jar'`

```bash 
$ mvn install:install-file -Dfile=recaptcha4j-0.0.8.jar \
    -DgroupId=net.tanesha.recaptcha4j -DartifactId=recaptcha4j \
    -Dversion=0.0.8 -Dpackaging=jar
```

You will also need to set up an account at http://www.google.com/recaptcha.

Configuration
-------------

After you have obtained the prerequisites and checked out the source code,
there are a few files you will need to configure.

  * `src/main/webapp/WEB-INF/spring-configuration/passwordManagerContext.xml`:
  
  There are only two things you may need to configure in this file. One
  is the password security policy regular expression. By default passwords
  must be at least 8 characters and contain at least one uppercase letter,
  one lowercase letter, and one digit. If you want to change that, modify
  the passwordRegex property of the changePasswordBeanValidator.
  
  The other possible configuration option is the encryption algorithm if 
  you're using OpenLDAP. If you're using OpenLDAP and don't set this, 
  passwords will be stored unencrypted. (Active Directory automatically
  encrypts all passwords.) If you are running OpenLDAP, towards the bottom
  of the passwordManagerContext.xml, you'll see the section that you will 
  need to uncomment.
    
  * `src/main/webapp/WEB-INF/cas.properties.example`:
  
  This file contains all of the CAS options you will need to set as well
  as the LPPE and password manager options. If you have any questions
  about basic (non-password manager) configuration, I recommend 
  you look at CAS configuration documentation on the Jasig wiki
  (https://wiki.jasig.org/display/CASUM/Home).
    
  The first step is to copy or rename this file to "cas.properties". Then
  read through the file and set the properties to match your environment.
  All of the configuration options in this file are annotated, so please
  read through the file and set everything appropriately.

  * `src/main/webapp/WEB-INF/view/jsp/default/ui/*.jsp` and `src/main/webapp/WEB-INF/classes/messages_en.properties`:
  
  Edit the views to your liking. This is a CAS overlay project, so if you
  need to edit a JSP file that isn't here, you can download the CAS Server
  3.5 distribution and copy the file from there into this directory.

Building
--------

To build the CAS server, run the following from the root of the project:

`$ mvn clean package`

If the build was successful, the CAS webapp can be found under `target/cas.war`.
See the Jasig CAS documentation if you run into any issues building CAS.

Troubleshooting
---------------

Hopefully if you follow these instructions everything should work out fine.
If not, however, the first step is to check your cas.log. If there are no 
obvious errors, try turning on debug logging. Edit 
cas/WEB-INF/classes/log4j.xml and change the log level for the 
org.jasig.cas.pm appender from INFO to DEBUG. Then check cas.log to see 
if you can spot anything that looks wrong.

This is (still) a pretty new project, so I'm not sure what can go wrong 
yet. :) If you do run into an issue, try emailing the CAS community discussion 
list. You can subscribe by visiting http://www.jasig.org/cas/mailing-lists. I'm
on the list, so maybe I can help out!

To-Dos
------

There's no guaranteed timeline for any of the following, but the following
are possible future improvements this project could use:

* ability to change security questions
* ability to have more than one custom security question.  Hooks for this
  feature already exist, but it's not fully implemented.
* allow administrator to predefine a list of security questions
* add flags for enabling/disabling certain aspects of the password manager
* unit/integration tests would be nice
* add back-end security question stores other than LDAP (e.g. DBMS)

