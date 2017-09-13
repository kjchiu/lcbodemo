import React from 'react';
import {
	Button,
	ControlLabel,
	Form,
	FormControl,
	FormGroup
} from 'react-bootstrap';

import PropTypes from 'prop-types';

const UserLogin = ({login, createUser}) => (
	<Form id="user-login" inline onSubmit={login}>
		<FormGroup>
			<ControlLabel>User</ControlLabel>
			{' '}
			<FormControl type='text' placeholder={'user'} />
		</FormGroup>
		{' '}
		<FormGroup>
			<ControlLabel>Password</ControlLabel>
			{' '}
			<FormControl type='password' placeholder={"password"} />
		</FormGroup>
		{' '}
		<Button type="submit">Login</Button>
		<Button onClick={createUser}>Sign Up</Button>
	</Form>
);

UserLogin.propTypes = {
	login: PropTypes.func.isRequired
}

export default UserLogin;
