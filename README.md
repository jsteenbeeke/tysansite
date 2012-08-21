Tysan Clan Website
==================
This repository contains the website source for [The Tysan Clan](https://www.tysanclan.com/ "The Tysan Clan").

Code Conventions
----------------
Please import the formatting.xml and cleanup.xml from the settings folder in the repository root.

Running the site
----------------
First of all, you need access to a PostgreSQL database.

Second, you need to create a properties file containing the follow entries:

    database.driverclass=org.postgresql.Driver
    database.url=jdbc:postgresql://SERVER_IP/DATABASE_NAME
    database.username=DATABASE_USERNAME
    database.password=DATABASE_PASSWORD
    mail.server=SMTP_SERVER
    mail.username=SMTP_USERNAME
    mail.password=SMTP_PASSWORD (or gibberish if you don't want your local version to send e-mails
    # OPTIONAL
    twitter.username=TWITTER_USERNAME_THAT_MAY_SPAM_MESSAGES
    twitter.accessToken=TWITTER_ACCESS_TOKEN_FOR_YOUR_APP
    twitter.accessTokenSecret=TWITTER_ACCESS_TOKEN_SECRET_FOR_YOUR_APP
    twitter.consumerKey=TWITTER_CONSUMER_KEY_AFTER_AUTHORIZING_YOUR_APP
    twitter.consumerSecret=TWITTER_CONSUMER_SECRET_AFTER_AUTHORIZING_YOUR_APP
    # FOLLOWING ASSUMES YOU HAVE APACHE RUNNING ON SAME SERVER
    gallery.urlbase=http://127.0.0.1/gallery
    gallery.path=/var/www/gallery


Finally, you need to add a VM command line argument to your eclipse launch configuration:

    -Dewok.properties=file:/PATH/TO/YOUR/PROPERTIES/FILE

