import React from 'react';
import {
	Button,
	Col,
	Navbar,
	NavDropdown,
	MenuItem
} from 'react-bootstrap';

import PropTypes from 'prop-types';

const AuthenticatedUser = ({
	user,
	token,
	history,
	logout,
	onQuery
}) => {

	var items = history.map((query, idx) => (
		<MenuItem
			key={"history-" + idx}
			eventKey={"1." + idx}
			onClick={onQuery.bind(this, query, token)}>{query}</MenuItem>
	));

	return (
		<Col>
			<Navbar.Text>Welcome {user}</Navbar.Text>
			<Button onClick={logout}>Logout</Button>
			<NavDropdown eventKey={1} title="History" id="basic-nav-dropdown">
				{items}
			</NavDropdown>
		</Col>
	);
};
AuthenticatedUser.propTypes = {
	user: PropTypes.string.isRequired,
	token: PropTypes.string.isRequired,
	history: PropTypes.arrayOf(PropTypes.string).isRequired,
	logout: PropTypes.func.isRequired,
	onQuery: PropTypes.func.isRequired
}

export default AuthenticatedUser;
