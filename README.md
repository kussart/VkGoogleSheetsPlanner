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
   
    ![alt text](https://3.downloader.disk.yandex.ru/disk/cc1a321abc8960c5a7f4effcb255b5915e7a20b10424b5c5b972e711fb662855/59fb7803/PcVKaV0E5tQ2noQ5gYGlGBDVxGCWL2J6vT5HYWj2w5eAjH-p4AIZuShZPOkcWdx-yYpQr3ompQLHP5tCYlL8Mg%3D%3D?uid=0&filename=api%20key.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&fsize=46462&hid=8c430edc7c2961dc76929ff89343ff67&media_type=image&tknv=v2&etag=c68f8e07390b9fce1daeb7aa0e8d1db4)

    Create a Google spreadsheet. Configure access settings - "anyone who has a link".
    
    ![alt text](https://3.downloader.disk.yandex.ru/disk/e8153e1b9b8c7964c6dd8d5ef31b421cc77676a7993413f5aae64672f2767efc/59fb77cf/PcVKaV0E5tQ2noQ5gYGlGGoMkOYDwOp9DK1ZJe8SMgWTm3jPtu8pMcfpRG6UpH4QUd8ENCQ4ANqy_9234IYxJw%3D%3D?uid=0&filename=google%20sheet%20for%20git.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&fsize=62554&hid=e9df44c3e78d8f5fff294d299b5614b8&media_type=image&tknv=v2&etag=e0e6aa26c7c9b28eb3e2053fcba5cabc)
    
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
    
    ![alt text](https://4.downloader.disk.yandex.ru/disk/d6f01e78584efdcfc8cd637aa40f14d77c9f04b22186ea68662ae018bbf87080/59fb7ff8/PcVKaV0E5tQ2noQ5gYGlGNShcZPI61i1HnkyYnNzFl_luHRjGasGaKrbtmyB7wvmSv4w3u-r-SvDekprx6DgjQ%3D%3D?uid=0&filename=vk%20developers_19-28-09.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&fsize=66235&hid=122b9944a6a67c194720d896f625e642&media_type=image&tknv=v2&etag=5584d5d11a77e0e25cfce8dd766ef6fe)
    
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
