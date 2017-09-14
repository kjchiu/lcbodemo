import React from 'react';
import {
	Button,
	ControlLabel,
	Form,
	FormControl,
	FormGroup
} from 'react-bootstrap';
import {connect} from 'react-redux';

import {
	createUser,
	login,
	logout,
	queryProducts
} from '../Actions';

import UserLogin from '../components/UserLogin.jsx';
import AuthenticatedUser from '../components/AuthenticatedUser.jsx';

class User extends React.Component {

	render() {
		if (! this.props.isAuthed) {
			return (
				<UserLogin
					login={this.props.login}
					createUser={this.props.createUser}/>);
		} else {
			return (
				<AuthenticatedUser
					user={this.props.user}
					token={this.props.token}
					history={this.props.history}
					logout={this.props.logout}
					onQuery={this.props.onQuery} />);
		}
	}
}

const mapStateToProps = ({login}) => {
	return {
		user: login.user,
		isAuthed: !! login.token,
		token: login.token,
		history: login.history
	}
}

function mapDispatchToProps(dispatch) {
	return {
		login: e => {
			e.preventDefault();
			var form = e.target;
			var user = form.elements[0].value;
			var password = form.elements[1].value;
			dispatch(login(user, password));
			return false;
		},
		createUser: e => {
			e.preventDefault();
			// rip react style
			var form = document.getElementById('user-login');
			var user = form.elements[0].value;
			var password = form.elements[1].value;
			dispatch(createUser(user, password));
			return false;
		},
		logout: e => {
			e.preventDefault();
			dispatch(logout());
		},
		onQuery: (query, token, e) => {
			dispatch(queryProducts(query, 1, token));
		},
	}
}

const UserContainer = connect(
	mapStateToProps,
	mapDispatchToProps
)(User);

export default UserContainer;
