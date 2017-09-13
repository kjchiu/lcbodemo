import React from 'react';
import {
	Col,
	Panel,
	FormControl
} from 'react-bootstrap';

export default class App extends React.Component {

	render() {
		return (
			<div>
			<Col xs={4}>
				<FormControl placeholder={"Search..."} />
			</Col>
			<Col xs={4}>
				what
			</Col>
			</div>
		);
	}
}
