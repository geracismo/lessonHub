# lessonHub
School repetition web-service, include backend, frontend and android application. <br>

Backend server communicate with clients in json (Google.Gson) format, it is based on jakarta servlet 5.0 and tomcat 10.0.17. <br>
Frontend is developed in Vue.js and Jquery. <br>
Android application use Android.volley to send http request to server, and SharedPreferences to keep user logged. <br>

Use case: <br>
  NORMAL USER <br>
- Book a repetition. <br>
- Manage own repetition: cancel it or mark as done.
- Check history section: it contains all repetition booked, canceled or done one.
- Login
- Logout
  
  ADMIN <br>
- All normal-user function except for "mark as done". <br>
- Can check history of all user. <br>
- Can add new Professor, Subject and Professor-Subject-Association to the platform. <br>
- Can delete active reservation of a user. <br>
