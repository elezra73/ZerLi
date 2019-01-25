# ZerLi
Semester A 2018 Software Engineering Project.


Project link: https://github.com/Romansko/ZerLi.git


Team name: Los Socios.

Team Members: 
- Avner (colombusAH)
- Daniel (elezra73)
- Eden (edeneden52)
- Linoy (linoyi)
- Roman (romansko)


# Server.jar instructions:

1. go with CMD to the sever jar location.

2. type in the following:

    java -jar serverFileName.jar 
    
    -this option will have default listening port "5555"
                            
    or 
    
    java -jar serverFileName.jar arg0
    
    -this option will have arg0 as port number.
			    

    
# SQL settings

In order to define SQL settings, you have to modify the file databaseSettings.zerli

which is in: src/server/

1st Row in the file is the SQL Host address.

2nd Row in the file is the SQL Username.

3rd Row in the file is the SQL Password.

if parsing goes wrong, default values will be used:

	private final static String SQL_HOST = "jdbc:mysql://localhost/zerli"; 
	
	private final static String SQL_USERNAME = "root";
	
	private final static String SQL_PASSWORD = "Braude";
                             
                             
       
