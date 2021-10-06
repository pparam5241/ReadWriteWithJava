# ReadWriteWithJava
Read data from google sheet and write it to same sheet or same sheet with another tab or another sheet using Google API call of OAuth.

For Generation of OAuth Tocken, if you don't have your own url, use http://localhost:8080 as url and http://localhost:8080/Callback as redirected url.
I've kept the port number as 8888 so I've used 8888 as port number in my url.
Setup won't required the token folder so you can delete that for setting up your own email.
Provide your email grant using add user on console.cloud.google.com

Step's for generating OAuth Properly.
1) Go to https://console.cloud.google.com/
2) Create one project if you have no project
3) Left Panel > API & Services > Library > Search for Google Sheet
4) Add Google Sheet API to the project.
5) Left Panel > API & Services > Credentials > It will show you To add OAuth Consent Screen > Add all the information > Add scope as Google Sheet API > Done the setup.
6) Credential > API Keys > Generate API Keys
7) Credentials > OAuth 2.0 Client ID's > Give the project information and all > Give Authorized JavaScript Origin to http://localhost:8888 if you are using my code, else provide your url.
8) Authorized Redirect URIs > Put http://localhost:8888/Callback if you are using the same mine code. else you can generate or put your own callback uri's.
9) Generate token and rename it to credentials.json file and replace it to the resources folder credentials.json file
10) Open the project > Change the Google Sheet URL and cell number and whatever you want to change. Run the project That's it.
