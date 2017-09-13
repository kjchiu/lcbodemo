import React from 'react';
import {
	Button,
	ControlLabel,
	Form,
	FormControl,
	FormGroup
} from 'react-bootstrap';

import PropTypes from 'prop-types';

const AuthenticatedUser = ({user, logout}) => (
	<ul className="inline">
		<li><h3>Welcome {user}</h3></li>
		<li><Button onClick={logout}>Logout</Button></li>
	</ul>
);

AuthenticatedUser.propTypes = {
	user: PropTypes.string.isRequired,
	logout: PropTypes.func.isRequired
}

export default AuthenticatedUser;
