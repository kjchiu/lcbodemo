import React from 'react';
import {
	Button,
	Navbar,
} from 'react-bootstrap';

import PropTypes from 'prop-types';

const AuthenticatedUser = ({user, logout}) => (
	<div>
		<Navbar.Text>Welcome {user}</Navbar.Text>
		<Button onClick={logout}>Logout</Button>
	</div>
);

AuthenticatedUser.propTypes = {
	user: PropTypes.string.isRequired,
	logout: PropTypes.func.isRequired
}

export default AuthenticatedUser;
