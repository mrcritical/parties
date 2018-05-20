const functions = require('firebase-functions');
const admin = require('firebase-admin');
const app = require('express')();

admin.initializeApp();

// We'll enable CORS support to allow the function to be invoked
// from our app client-side.
app.use(require('cors')({origin: true}));

/**
 * Authorizes a link for a specific attendee. If authorized, returns a token for login to Firebase.
 * Links send to the attendees should go to
 * https://us-central1-parties-35d88.cloudfunctions.net/api/attendee/authorize?token={email-token}.
 */
app.get('/attendee/authorize', (req, res) => {
    const token = req.query.token;
    admin
        .firestore()
        .collection('attendees')
        .where('token', '==', token)
        .get()
        .then((attendee) => {
            if (attendee !== 'undefined' && attendee.uid && attendee.id !== '') {
                console.log('Attendee found=' + JSON.stringify(attendee));
                return res.json({
                    token: token,
                    attendee: attendee
                });
                /*return admin
                    .auth()
                    .createCustomToken(attendee.uid)
                    .then((customToken) => {
                        console.log('Firebase token created=' + customToken);
                        return res.json({
                            token: customToken
                        });
                    });*/
            } else {
                return Promise.reject(new Error('Failed to find the uid for attendee using token=' + token + ''));
            }
        })
        .catch((error) => {
            console.error('An invalid token=' + token + ' was received', error);
            return res
                .status(403)
                .send();
        });
});

exports.api = functions.https.onRequest(app);