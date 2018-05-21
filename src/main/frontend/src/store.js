import firebase from 'firebase/app';
import 'firebase/firestore';
import 'firebase/auth';
import {Collection, Document, initFirestorter} from 'firestorter';
import * as serviceAccount from './firebase-account';

firebase.initializeApp(serviceAccount);
firebase
    .firestore()
    .settings({
        timestampsInSnapshots: true
    });

initFirestorter({firebase: firebase});

let currentUser = null;

class CurrentUser {
    constructor(accountId, profile) {
        this.accountId = accountId;
        this.profile = profile;
    }
}

// Detect changes to the logged in user. Keep the profile in local storage while logged in.
firebase.auth().onAuthStateChanged((authUser) => {
    if (authUser) {
        // User logged in
        firebase
            .firestore()
            .doc('users/' + authUser.uid)
            .get()
            .then(user => {
                if (user && user.exists) {
                    return Promise.all([
                        user,
                        user.data().profile.get()
                    ]);
                }
                return Promise.reject(new Error('User=' + authUser.uid + ' not found'));
            })
            .then(results => {
                currentUser = new CurrentUser(
                    results[0].accountId,
                    results[1]
                );
            })
            .catch(err => {
                console.error('User was not found', err);
            });
    } else {
        // User logged out
        currentUser = null;
    }
});

const repository = {
    currentUser: currentUser,

    // Get, an listen to updates, on the party
    party: (accountId, partyId) => new Document('accounts/' + accountId + '/parties/' + partyId),
    // Get, and listen, to all attendees in the party
    attendees: (accountId, partyId) => new Collection('accounts/' + accountId + '/parties/' + partyId + '/attendees'),
    // Get, and listen to updates, to all posts in the party
    posts: (accountId, partyId) => new Collection('accounts/' + accountId + '/parties/' + partyId + '/posts'),
    // Get, and listen to updates, to all plans for the party
    plans: (accountId, partyId) => new Collection('accounts/' + accountId + '/parties/' + partyId + '/plans'),

    // Admin functions
    parties: (accountId) => new Collection('accounts/' + accountId + '/parties'),
    assets: (accountId) => new Collection('accounts/' + accountId + '/assets'),
    profiles: (accountId) => new Collection('accounts/' + accountId + '/profiles'),

    accounts: () => new Collection('accounts'),
    users: () => new Collection('users'),

    // Expose Firebase itself
    firebase: () => firebase
};

export default repository;