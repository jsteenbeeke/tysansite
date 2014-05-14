Tysan Clan Website
==================
This repository contains the website source for [The Tysan Clan](https://www.tysanclan.com/ "The Tysan Clan").

Dependencies
------------
Most of this project's dependencies are available from Maven Central. The only project you need to manually install is
[Hyperion Events](https://github.com/jsteenbeeke/hyperion-events), which is also hosted on Github.

Code Conventions
----------------
Please import the ```formatting.xml``` and ```cleanup.xml``` from the settings folder in the repository root.

Running the site
----------------
The Tysan Clan website requires Java 7 or higher.

For testing purposes, the site will launch an in-memory [H2 database](http://www.h2database.com) instance. All you need to
do is run the ```com.tysanclan.site.projectewok.util.StartTysan``` class in the ```src/main/test``` folder.

To run a production version of the site, you need to have a [PostgreSQL database](http://www.postgresql.org) and a servlet container 
such as [Jetty](http://www.eclipse.org/jetty/) or [Tomcat](http://tomcat.apache.org/).

Second, you need to create a properties file containing the follow entries:

	database.dialect=org.hibernate.dialect.PostgreSQLDialect
    database.driverclass=org.postgresql.Driver
    database.url=jdbc:postgresql://SERVER_IP/DATABASE_NAME
    database.username=DATABASE_USERNAME
    database.password=DATABASE_PASSWORD
    mail.server=SMTP_SERVER
    mail.username=SMTP_USERNAME
    mail.password=SMTP_PASSWORD (or gibberish if you don't want your local version to send e-mails

Finally, you need to add two VM command line argument to your Java invocation. The first is to tell the site where its properties are located, the second is to instruct Wicket to run in Deployment mode

    -Dewok.properties=file:/PATH/TO/YOUR/PROPERTIES/FILE -Dwicket.configuration=deployment

