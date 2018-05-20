import firebase from 'firebase';
import 'firebase/firestore';
import {initFirestorter, Collection} from 'firestorter';

firebase.initializeApp({
    apiKey: "",
    authDomain: "parties-35d88.firebaseapp.com",
    databaseURL: "https://parties-35d88.firebaseio.com",
    projectId: "parties-35d88",
    storageBucket: "parties-35d88.appspot.com",
    messagingSenderId: "459543766271"
});

initFirestorter({firebase: firebase});

const parties = new Collection('parties');

export {
    parties,
    firebase
};