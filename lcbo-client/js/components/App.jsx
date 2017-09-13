import React from 'react';
import {
	Col,
	FormControl,
	Grid,
	Nav,
	Navbar,
	NavItem,
	Panel,
	Row
} from 'react-bootstrap';

export default class App extends React.Component {

	render() {
		return (
			<div>
				<Navbar>
					<Nav>
						<NavItem>login</NavItem>
						<NavItem>hello</NavItem>
					</Nav>
				</Navbar>
				<Grid>
					<Row>
						<Col xs={4}>
							<FormControl placeholder={"Search..."} />
						</Col>
						<Col xs={4}>
							what
						</Col>
					</Row>
				</Grid>
			</div>
		);
	}
}
