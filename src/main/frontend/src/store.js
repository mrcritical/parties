import firebase from 'firebase';
import 'firebase/firestore';
import {initFirestorter, Collection} from 'firestorter';
import * as serviceAccount from './service-account';

firebase.initializeApp(serviceAccount);

initFirestorter({firebase: firebase});

const parties = new Collection('parties');

export {
    parties,
    firebase
};