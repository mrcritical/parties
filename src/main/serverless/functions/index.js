const functions = require('firebase-functions');
const admin = require('firebase-admin');
const app = require('express')();

admin.initializeApp();

// We'll enable CORS support to allow the function to be invoked
// from our app client-side.
app.use(require('cors')({origin: true}));

app.get('/attendee/authorize', (req, res) => {
    const token = req.query.token;
    admin
        .firestore()
        .collection('attendees')
        .where('token', '==', token)
        .get()
        .then((attendee) => {
            return admin
                .auth()
                .createCustomToken(attendee.uid)
                .then((customToken) => {
                    return res.json({
                        token: customToken
                    });
                });
        })
        .catch((error) => {
            console.error('An invalid token was received', error);
            return res
                .status(403)
                .send('Unauthorized');
        });
});

exports.api = functions.https.onRequest(app);