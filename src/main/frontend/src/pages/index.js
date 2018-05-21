import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogActions from '@material-ui/core/DialogActions';
import Typography from '@material-ui/core/Typography';
import {withStyles} from '@material-ui/core/styles';
import withRoot from '../withRoot';
import {BrowserRouter as Router, Route} from 'react-router-dom';
import repos from '../store';

const styles = theme => ({
    root: {
        textAlign: 'center',
        paddingTop: theme.spacing.unit * 20,
    },
});

class Login extends React.Component {

    componentDidMount() {
        const firebase = repos.firebase();
        if (firebase.auth().isSignInWithEmailLink(window.location.href)) {
            // Additional state parameters can also be passed via URL.
            // This can be used to continue the user's intended action before triggering
            // the sign-in operation.
            // Get the email if available. This should be available if the user completes
            // the flow on the same device where they started it.
            let email = window.localStorage.getItem('emailForSignIn');
            if (!email) {
                // User opened the link on a different device. To prevent session fixation
                // attacks, ask the user to provide the associated email again. For example:
                email = window.prompt('Please provide your email for confirmation');
            }
            // The client SDK will parse the code from the link for you.
            firebase.auth().signInWithEmailLink(email, window.location.href)
                .then(function (result) {
                    // Redirect to landing
                    window.location.replace('/');

                    // Clear email from storage.
                    //window.localStorage.removeItem('emailForSignIn');
                    // You can access the new user via result.user
                    // Additional user info profile not available via:
                    // result.additionalUserInfo.profile == null
                    // You can check if the user is new or existing:
                    // result.additionalUserInfo.isNewUser
                })
                .catch(function (error) {
                    // Some error occurred, you can inspect the code: error.code
                    // Common errors could be invalid email and invalid or expired OTPs.
                    console.error('Login failed', error);
                });
        }
    }

    render() {
        return <h1>Logging in...</h1>;
    }
}

class Index extends React.Component {
    state = {
        open: false,
    };

    handleClose = () => {
        this.setState({
            open: false,
        });
    };

    handleClick = () => {
        this.setState({
            open: true,
        });
    };

    render() {
        const { classes } = this.props;
        const { open } = this.state;

        return (
            <Router>
                <div className={classes.root}>
                    <Route exact path="/" render={() => (
                        <div>
                            <Dialog open={open} onClose={this.handleClose}>
                                <DialogTitle>Super Secret Password</DialogTitle>
                                <DialogContent>
                                    <DialogContentText>1-2-3-4-5</DialogContentText>
                                </DialogContent>
                                <DialogActions>
                                    <Button color="primary" onClick={this.handleClose}>
                                        OK
                                    </Button>
                                </DialogActions>
                            </Dialog>
                            <Typography variant="display1" gutterBottom>
                                Material-UI
                            </Typography>
                            <Typography variant="subheading" gutterBottom>
                                example project
                            </Typography>
                            <Button variant="raised" color="secondary" onClick={this.handleClick}>
                                Super Secret Password
                            </Button>
                        </div>
                    )}/>
                    <Route path="/parties/:partyId" render={props => (
                        <Login {...props}/>
                    )}/>
                </div>
            </Router>
        );
    }
}

Index.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withRoot(withStyles(styles)(Index));