Register-Login ,Android app that includes activities for registration, login, and home. The app uses Firebase Authentication for handling user authentication 
and Firebase Firestore for storing user data.
The MainActivity class is the launcher activity and serves as a splash screen. It navigates to the Home activity after 2 seconds.
The Register class allows users to register by providing their name, email, phone number, and password. The entered data is validated, and if valid, the user is
registered using Firebase Authentication. The user details are also saved in the Firebase Firestore.
The Login class allows users to log in using their email and password. The entered email is validated using a regular expression. If the email and password are 
correct, the user is logged in, and their details are retrieved from Firebase Firestore and displayed in the Home activity.
The Home class displays the user's name, email, and phone number. The data is retrieved from Firebase Authentication and Firestore.
The app also uses SharedPreferences to store the login status of the user. If the user is already logged in, the app directly navigates to the Home activity.

Overall, this code provides a simple implementation of a registration, login, and home activity for an Android app using Firebase Authentication and Firestore.
