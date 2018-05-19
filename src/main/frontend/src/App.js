import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import {BrowserRouter as Router, Route, Link} from 'react-router-dom';

class App extends Component {
    render() {
        return (
            <Router>
                <div className="App">
                    <Route exact path="/" render={() => (
                        <div>
                            <header className="App-header">
                                <img src={logo} className="App-logo" alt="logo"/>
                                <h1 className="App-title">Welcome to React</h1>
                            </header>
                            <p className="App-intro">
                                To get started, edit <code>src/App.js</code> and save to reload.
                                <div>
                                    <Link to="/text/Hello">
                                        Say Hello
                                    </Link>
                                </div>
                            </p>
                        </div>
                    )}/>
                    <Route path="/text/:greeting" render={({match}) => (
                        <div>
                            <header className="App-header">
                                <h1 className="App-title">{match.params.greeting}</h1>
                            </header>
                        </div>
                    )}/>
                </div>
            </Router>
        );
    }
}

export default App;
