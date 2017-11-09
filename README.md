<h1> VK Google Sheets Planner </h1>

<p> help you to send some tasks or messages to your friends or colleagues using automatic distribution 
according to the schedule, which you specify in your Google table.</p>

<h3>Prepare your service:</h3> 

1.  <h4>Google Sheets</h4>
    Register an account in Google or if you already have one - please log in
    Create your application using 
    <a href="https://console.developers.google.com"> Google Developers Console. </a>
    
    API Key - register your application in Google by activating the Google Sheets API and 
    in your account - choose to get the API key.
   
    ![alt text](http://i12.pixs.ru/storage/5/2/3/apikeypng_3022399_28248523.png)

    Create a Google spreadsheet. Configure access settings - "anyone who has a link".
    
    ![alt text](http://i12.pixs.ru/storage/5/1/2/googleshee_8563663_28248512.png)
    
    SpreadSheetID - to access the table to get a list of data, for example:
    https://docs.google.com/spreadsheets/d/1n3H8D_AZmwt7snEHLWjgxvIU1yxpl2wnQN2lkhHvu6w/edit#gid=0
    in this case spreadsheetId will be: <i>1n3H8D_AZmwt7snEHLWjgxvIU1yxpl2wnQN2lkhHvu6w</i>
   
    Table Name - the name of our Sheet in the table is indicated on the tab at the bottom of the sheet.
    
    Summary we should have:
    <ul>
    <li> API key</li>
    <li> SpreadSheetID</li>
    <li> Table Name</li>
    </ul>
    
2.  <h4>VK application</h4>
    Register your VK application and read the instruction how to do it, using
    <a href="https://vk.com/dev/manuals"> VK API Documentation</a>
    
    ![alt text](http://i12.pixs.ru/storage/9/4/3/vkdevelope_6719632_28248943.png)
    
    Remember that we will use private message to send each task.
    In this case all users in your table need to be in your friend-list in VK,
    or have open Message Sending access for every people.
    
    Finally, we need to save in our service YML file these properties from your VK app:
    <ul>
    <li> application-id</li>
    <li> application-secret</li>
    <li> access-token</li>
    <li> api-version</li>
    </ul>
    
3.  <h4>Local Data Base</h4>

    Need to prepear some DB to create your users and control the correct schedule.
    ```mysql
    CREATE TABLE `users` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `date` varchar(255) DEFAULT NULL,
    `last_task_date` varchar(255) DEFAULT NULL,
    `name` varchar(255) NOT NULL,
    `task` varchar(255) DEFAULT NULL,
    `vk_domain` varchar(255) NOT NULL,
    PRIMARY KEY (`id`))
    ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8
    ```
4. <h4>Put all settings in the application.yml<h4>
  ```yaml
  spring:
      datasource:
        #put your database name instead of "userbase" and correct port for your localhost
        url: jdbc:mysql://localhost:3306/userbase?serverTimezone=Europe/Moscow&verifyServerCertificate=false&useSSL=true
        username: root #your login
        password: root #your password
     
  vk:
      application-id:  #application ID  (for example: 8547651)
      application-secret:  #application Secret (for example: phPut7JeX8DLfpKfGHiZ) 
      access-token: #access token (for example: dfg68dfg4848e1d8bf9a55718b21f4083da4r9y87rty846f5h935ae75ec0e3351ab654178f7y8)
      api-version: 5.68 # vk api version
   
  google:
      spreadsheetId: # table id in Google (for example: 1n3H8D_AZmwt7snEHLWjgxvIU1yxpl2wnQN2lkhHvu6w)
      tableName:  # list name of table (for example: Test1, or you can use Range like A1:E4)
      apiKey: # secret API key from your google project (for example: AIzaSyDzZjamreSxO6MbsoW57OEd5i8hWS75ErE)
   
  users:
      domainColumnNum: 0 # put the Num of column from your Google sheet, when you want to save the VK domain of each users
      nameColumnNum: 1 #num of column for users names
      dateColumnNum: 2 #num of column for date of task for users
      taskColumnNum: 3 #task-message for user
  ```
    
5.  <h4>Prepare your Scheduler periodicity<h4>

    Check the getUsersMessagesForVkPlanner() method in class GoogleServiceImpl and use one of Scheduled scheme annotation:

    <i>everyday start at 21:00 Moscow time:</i>
    ```java
    @Scheduled(cron = "0 0 21 * * *", zone = "Europe/Moscow")
    ```
    <i>start with fix delay using milliseconds const</i>
    ```java
    @Scheduled(fixedDelay = 10000)
    ```
