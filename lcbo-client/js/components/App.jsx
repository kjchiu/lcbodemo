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

import Search from '../containers/Search.jsx';
import ProductGrid from '../components/ProductGrid.jsx';

export default class App extends React.Component {

	render() {
		return (
			<div>
				<Navbar>
					<Nav>
						<NavItem>Login</NavItem>
					</Nav>
				</Navbar>
				<Grid>
					<Row>
						<Col xs={4}>
							<Search/>
						</Col>
						<Col xs={4}>
							what
						</Col>
					</Row>
					<Row>
						<ProductGrid />
					</Row>
				</Grid>
			</div>
		);
	}
}
